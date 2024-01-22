package com.vk.id.auth

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.vk.id.OAuth

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
    public enum class Locale {
        RUS,
        UKR,
        ENG,
        SPA,
        GERMAN,
        POL,
        FRA,
        TURKEY;

        internal companion object {
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

    public enum class Theme {
        Light, Dark;

        internal companion object {
            fun systemTheme(context: Context): Theme? =
                when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> Light
                    Configuration.UI_MODE_NIGHT_YES -> Dark
                    else -> null
                }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public class Builder {
        public var locale: Locale? = null
        public var theme: Theme? = null
        public var useOAuthProviderIfPossible: Boolean = true
        public var oAuth: OAuth? = null
        public fun build(): VKIDAuthParams = VKIDAuthParams(
            locale,
            theme,
            useOAuthProviderIfPossible,
            oAuth,
        )
    }
}
