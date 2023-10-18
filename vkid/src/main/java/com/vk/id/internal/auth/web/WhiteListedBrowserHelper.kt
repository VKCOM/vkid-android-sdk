package com.vk.id.internal.auth.web

import android.content.Context
import android.util.Log

internal object WhiteListedBrowserHelper {
    private val WHITE_LIST = listOf(
        VersionedBrowserMatcher.CHROME_BROWSER,
        VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
        VersionedBrowserMatcher.FIREFOX_CUSTOM_TAB,
        VersionedBrowserMatcher.FIREFOX_BROWSER,
        VersionedBrowserMatcher.SAMSUNG_BROWSER,
        VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
    )

    @Suppress("TooGenericExceptionCaught")
    fun selectBestBrowser(context: Context): BrowserDescriptor? {
        return try {
            val allBrowsers: List<BrowserDescriptor> = BrowserSelector.getAllBrowsers(context)
            for (descriptor in allBrowsers) {
                for (versionedBrowserMatcher in WHITE_LIST) {
                    if (versionedBrowserMatcher.matches(descriptor)) {
                        return descriptor
                    }
                }
            }
            if (allBrowsers.isEmpty()) null else allBrowsers[0]
        } catch (e: Exception) {
            Log.e("BrowserSelector", "Exception in select browser", e)
            null
        }
    }
}
