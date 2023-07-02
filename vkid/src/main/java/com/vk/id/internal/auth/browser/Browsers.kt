package com.vk.id.internal.auth.browser

import com.vk.id.internal.auth.browser.DelimitedVersion.Companion.parse

/*
 * Copyright 2016 The AppAuth for Android Authors. All Rights Reserved.
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
 */ /**
 * Defines the attributes of some commonly-used browsers on Android, for use in browser matchers.
 */
internal class Browsers private constructor() {
    /**
     * Constants related to Google Chrome.
     */
    object Chrome {
        /**
         * The package name for Chrome.
         */
        const val PACKAGE_NAME = "com.android.chrome"

        /**
         * The SHA-512 hash (Base64 url-safe encoded) of the public key for Chrome.
         */
        const val SIGNATURE = ("7fmduHKTdHHrlMvldlEqAIlSfii1tl35bxj1OXN5Ve8c4lU6URVu4xtSHc3BVZxS"
            + "6WWJnxMDhIfQN0N0K2NDJg==")

        /**
         * The set of signature hashes for Chrome.
         */
        @JvmField
        val SIGNATURE_SET = setOf(SIGNATURE)

        /**
         * The version in which Custom Tabs were introduced in Chrome.
         */
        @JvmField
        val MINIMUM_VERSION_FOR_CUSTOM_TAB = parse("45")

        /**
         * Creates a browser descriptor for the specified version of Chrome, when used as a
         * standalone browser.
         */
        fun standaloneBrowser(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, false)
        }

        /**
         * Creates a browser descriptor for the specified version of Chrome, when used as
         * a custom tab.
         */
        fun customTab(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, true)
        }
    }

    /**
     * Constants related to Mozilla Firefox.
     */
    object Firefox {
        /**
         * The package name for Firefox.
         */
        const val PACKAGE_NAME = "org.mozilla.firefox"

        /**
         * The SHA-512 hash (Base64 url-safe encoded) of the public key for Firefox.
         */
        const val SIGNATURE_HASH = ("2gCe6pR_AO_Q2Vu8Iep-4AsiKNnUHQxu0FaDHO_qa178GByKybdT_BuE8_dYk99G"
            + "5Uvx_gdONXAOO2EaXidpVQ==")

        /**
         * The set of signature hashes for Firefox.
         */
        @JvmField
        val SIGNATURE_SET = setOf(SIGNATURE_HASH)

        /**
         * The version in which Custom Tabs were introduced in Firefox.
         */
        @JvmField
        val MINIMUM_VERSION_FOR_CUSTOM_TAB = parse("57")

        /**
         * Creates a browser descriptor for the specified version of Firefox, when used
         * as a standalone browser.
         */
        fun standaloneBrowser(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, false)
        }

        /**
         * Creates a browser descriptor for the specified version of Firefox, when used as
         * a custom tab.
         */
        fun customTab(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, true)
        }
    }

    /**
     * Constants related to
     * [SBrowser](https://play.google.com/store/apps/details?id=com.sec.android.app.sbrowser),
     * the default browser on Samsung devices.
     */
    object SBrowser {
        /**
         * The package name for SBrowser.
         */
        const val PACKAGE_NAME = "com.sec.android.app.sbrowser"

        /**
         * The SHA-512 hash (Base64 url-safe encoded) of the public key for SBrowser.
         */
        const val SIGNATURE_HASH = ("ABi2fbt8vkzj7SJ8aD5jc4xJFTDFntdkMrYXL3itsvqY1QIw-dZozdop5rgKNxjb"
            + "rQAd5nntAGpgh9w84O1Xgg==")

        /**
         * The set of signature hashes for SBrowser.
         */
        @JvmField
        val SIGNATURE_SET = setOf(SIGNATURE_HASH)

        /**
         * The version in which Custom Tabs were introduced in Samsung Internet.
         */
        @JvmField
        val MINIMUM_VERSION_FOR_CUSTOM_TAB = parse("4.0")

        /**
         * Creates a browser descriptor for the specified version of SBrowser, when
         * used as a standalone browser.
         */
        fun standaloneBrowser(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, false)
        }

        /**
         * Creates a browser descriptor for the specified version of SBrowser, when
         * used as a custom tab.
         */
        fun customTab(version: String): BrowserDescriptor {
            return BrowserDescriptor(PACKAGE_NAME, SIGNATURE_SET, version, true)
        }
    }
}