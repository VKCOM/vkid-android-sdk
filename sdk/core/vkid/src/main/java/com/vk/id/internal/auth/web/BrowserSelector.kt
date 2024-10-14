/*
 * Copyright 2015 The AppAuth for Android Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.logger.internalVKIDCreateLoggerForClass

/**
 * Utility class to obtain the browser package name to be used for
 * OAuth calls. It prioritizes browsers which support
 * [custom tabs](https://developer.chrome.com/multidevice/android/customtabs). To mitigate
 * man-in-the-middle attacks by malicious apps pretending to be browsers for the specific URI we
 * query, only those which are registered as a handler for _all_ HTTP and HTTPS URIs will be
 * used.
 */
internal object BrowserSelector {
    private const val SCHEME_HTTP = "http"
    private const val SCHEME_HTTPS = "https"

    private val logger = internalVKIDCreateLoggerForClass()

    /**
     * The service we expect to find on a web browser that indicates it supports custom tabs.
     */
    // HACK: Using a StringBuilder prevents Jetifier from tempering with our constants.
    private val ACTION_CUSTOM_TABS_CONNECTION = StringBuilder("android")
        .append(".support.customtabs.action.CustomTabsService").toString()

    /**
     * An arbitrary (but unregistrable, per
     * [IANA rules](https://www.iana.org/domains/reserved)) web intent used to query
     * for installed web browsers on the system.
     */
    private val BROWSER_INTENT = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        .addCategory(Intent.CATEGORY_BROWSABLE)

    /**
     * Retrieves the full list of browsers installed on the device. Two entries will exist
     * for each browser that supports custom tabs, with the [BrowserDescriptor.useCustomTab]
     * flag set to `true` in one and `false` in the other. The list is in the
     * order returned by the package manager, so indirectly reflects the user's preferences
     * (i.e. their default browser, if set, should be the first entry in the list).
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun getAllBrowsers(pm: InternalVKIDPackageManager): List<BrowserDescriptor> {
        val browsers: MutableList<BrowserDescriptor> = ArrayList()
        var defaultBrowserPackage: String? = null
        var queryFlag = PackageManager.GET_RESOLVED_FILTER
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            queryFlag = queryFlag or PackageManager.MATCH_ALL
        }
        // When requesting all matching activities for an intent from the package manager,
        // the user's preferred browser is not guaranteed to be at the head of this list.
        // Therefore, the preferred browser must be separately determined and the resultant
        // list of browsers reordered to restored this desired property.
        val resolvedDefaultActivity = pm.resolveActivity(BROWSER_INTENT, 0)
        if (resolvedDefaultActivity != null) {
            defaultBrowserPackage = resolvedDefaultActivity.activityInfo.packageName
        }
        val resolvedActivityList = pm.queryIntentActivities(BROWSER_INTENT, queryFlag)
        for (info in resolvedActivityList) {
            // ignore handlers which are not browsers
            if (!isFullBrowser(info)) {
                continue
            }
            try {
                var defaultBrowserIndex = 0
                val packageInfo = pm.getPackageInfo(
                    info.activityInfo.packageName,
                    PackageManager.GET_SIGNATURES
                )
                if (hasWarmupService(pm, info.activityInfo.packageName)) {
                    val customTabBrowserDescriptor = BrowserDescriptor(packageInfo, true)
                    if (info.activityInfo.packageName == defaultBrowserPackage) {
                        // If the default browser is having a WarmupService,
                        // will it be added to the beginning of the list.
                        browsers.add(defaultBrowserIndex, customTabBrowserDescriptor)
                        defaultBrowserIndex++
                    } else {
                        browsers.add(customTabBrowserDescriptor)
                    }
                }
                val fullBrowserDescriptor = BrowserDescriptor(packageInfo, false)
                if (info.activityInfo.packageName == defaultBrowserPackage) {
                    // The default browser is added to the beginning of the list.
                    // If there is support for Custom Tabs, will the one disabling Custom Tabs
                    // be added as the second entry.
                    browsers.add(defaultBrowserIndex, fullBrowserDescriptor)
                } else {
                    browsers.add(fullBrowserDescriptor)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                logger.error("Can't generate browser descriptor without the package info", e)
            }
        }
        return browsers
    }

    /**
     * Searches through all browsers for the best match based on the supplied browser matcher.
     * Custom tab supporting browsers are preferred, if the matcher permits them, and browsers
     * are evaluated in the order returned by the package manager, which should indirectly match
     * the user's preferences.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun select(pm: InternalVKIDPackageManager, browserMatcher: BrowserMatcher): BrowserDescriptor? {
        val allBrowsers = getAllBrowsers(pm)
        var bestMatch: BrowserDescriptor? = null
        for (browser in allBrowsers) {
            if (!browserMatcher.matches(browser)) {
                continue
            }
            if (browser.useCustomTab) {
                // directly return the first custom tab supporting browser that is matched
                return browser
            }
            if (bestMatch == null) {
                // store this as the best match for use if we don't find any matching
                // custom tab supporting browsers
                bestMatch = browser
            }
        }
        return bestMatch
    }

    private fun hasWarmupService(pm: InternalVKIDPackageManager, packageName: String): Boolean {
        val serviceIntent = Intent()
        serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.setPackage(packageName)
        return pm.resolveService(serviceIntent, 0) != null
    }

    @Suppress("ReturnCount")
    private fun isFullBrowser(resolveInfo: ResolveInfo): Boolean {
        // The filter must match ACTION_VIEW, CATEGORY_BROWSEABLE, and at least one scheme,
        if ((
                !resolveInfo.filter.hasAction(Intent.ACTION_VIEW) ||
                    !resolveInfo.filter.hasCategory(Intent.CATEGORY_BROWSABLE)
                ) || resolveInfo.filter.schemesIterator() == null
        ) {
            return false
        }

        // The filter must not be restricted to any particular set of authorities
        if (resolveInfo.filter.authoritiesIterator() != null) {
            return false
        }

        // The filter must support both HTTP and HTTPS.
        var supportsHttp = false
        var supportsHttps = false
        val schemeIter = resolveInfo.filter.schemesIterator()
        while (schemeIter.hasNext()) {
            val scheme = schemeIter.next()
            supportsHttp = supportsHttp or (SCHEME_HTTP == scheme)
            supportsHttps = supportsHttps or (SCHEME_HTTPS == scheme)
            if (supportsHttp && supportsHttps) {
                return true
            }
        }

        // at least one of HTTP or HTTPS is not supported
        return false
    }
}
