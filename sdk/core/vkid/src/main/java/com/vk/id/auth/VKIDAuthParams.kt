package com.vk.id.auth

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.vk.id.OAuth

/**
 * Create [VKIDAuthParams].
 *
 * @param initializer params' initialization.
 */
public inline fun VKIDAuthParams(initializer: VKIDAuthParams.Builder.() -> Unit): VKIDAuthParams {
    return VKIDAuthParams.Builder().apply(initializer).build()
}

/**
 * VKIDAuthParams encapsulates parameters for VK ID authentication.
 *
 * @property locale The [Locale] setting for the authentication UI. Optional.
 * @property theme The [Theme] setting for the authentication UI (Light or Dark). Optional.
 * @property useOAuthProviderIfPossible Flag to use OAuth provider installed on device if possible. Defaults to true.
 * @property oAuth The [OAuth] provider to be used for authentication. Optional.
 */
public class VKIDAuthParams private constructor(
    public val locale: Locale? = null,
    public val theme: Theme? = null,
    public val useOAuthProviderIfPossible: Boolean = true,
    public val oAuth: OAuth? = null,
) {
    /**
     * Represents a locale that user prefers during authorization.
     */
    public enum class Locale {
        /**
         * Russian locale.
         */
        RUS,

        /**
         * Ukrainian locale.
         */
        UKR,

        /**
         * English locale.
         */
        ENG,

        /**
         * Spanish locale.
         */
        SPA,

        /**
         * German locale.
         */
        GERMAN,

        /**
         * Polish locale.
         */
        POL,

        /**
         * French locale.
         */
        FRA,

        /**
         * Turkish locale.
         */
        TURKEY;

        internal companion object {
            /**
             * Returns the current system locale.
             */
            fun systemLocale(context: Context): Locale? {
                val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales.get(0)
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale
                }
                return when (systemLocale.language) {
                    "ru" -> RUS
                    "uk" -> UKR
                    "en" -> ENG
                    "es" -> SPA
                    "de" -> GERMAN
                    "pl" -> POL
                    "fr" -> FRA
                    "tr" -> TURKEY
                    else -> null
                }
            }
        }
    }

    /**
     * Represents a theme that should be used to display data during authorization.
     */
    public enum class Theme {
        /**
         * Represents light theme.
         */
        Light,

        /**
         * Represents dark theme.
         */
        Dark;

        internal companion object {
            fun systemTheme(context: Context): Theme? =
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> Light
                    Configuration.UI_MODE_NIGHT_YES -> Dark
                    else -> null
                }
        }
    }

    /**
     * Builder for [VKIDAuthParams]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public class Builder {
        /**
         * Locale that should be used during authorization.
         */
        public var locale: Locale? = null

        /**
         * Theme that should be used during authorization.
         */
        public var theme: Theme? = null

        /**
         * Whether to use existing auth provider.
         */
        public var useOAuthProviderIfPossible: Boolean = true

        /**
         * An [OAuth] that shoud be used for authorization.
         */
        public var oAuth: OAuth? = null

        /**
         * Constructs [VKIDAuthParams] object with provided values.
         */
        public fun build(): VKIDAuthParams = VKIDAuthParams(
            locale,
            theme,
            useOAuthProviderIfPossible,
            oAuth,
        )
    }
}
