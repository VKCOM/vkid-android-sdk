package com.vk.id.auth

import android.content.Context
import android.content.res.Configuration
import android.os.Build

public inline fun VKIDAuthParams(initializer: VKIDAuthParams.Builder.() -> Unit): VKIDAuthParams {
    return VKIDAuthParams.Builder().apply(initializer).build()
}

public class VKIDAuthParams private constructor(
    public val locale: Locale? = null,
    public val theme: Theme? = null
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
        public fun build(): VKIDAuthParams = VKIDAuthParams(
            locale, theme
        )
    }
}