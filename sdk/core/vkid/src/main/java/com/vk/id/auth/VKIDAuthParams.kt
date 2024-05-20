package com.vk.id.auth

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.vk.id.OAuth
import com.vk.id.common.InternalVKIDApi

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
 * @property extraParams Key-value pairs of extra params that client want to send to auth provider. Optional.
 */
@Suppress("LongParameterList")
public class VKIDAuthParams private constructor(
    internal val locale: Locale?,
    internal val theme: Theme?,
    internal val useOAuthProviderIfPossible: Boolean,
    internal val oAuth: OAuth?,
    internal val prompt: Prompt,
    internal val state: String?,
    internal val codeChallenge: String?,
    internal val scopes: Set<String>,
    internal val extraParams: Map<String, String>? = null,
    internal val internalUse: Boolean = false
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
     * Builder for [VKIDAuthParams].
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
         * A [Prompt] parameter to be passed to /authorize.
         * Note: Changing it only works for auth view browser (not auth provider). Add [useOAuthProviderIfPossible] = false for this to work.
         */
        public var prompt: Prompt = Prompt.BLANK

        /**
         * A token to be passes /authorize.
         */
        @InternalVKIDApi
        public var token: String? = null

        /**
         * An optional state to be passed to auth.
         */
        public var state: String? = null

        /**
         * An optional code challenge to be passed to auth.
         * See https://datatracker.ietf.org/doc/html/rfc7636#section-4.2 for more information.
         */
        public var codeChallenge: String? = null

        /**
         * A required parameter with a list of requested scopes for the access token.
         * You have to specify a subset a scopes that you request for your app in Self Service.
         * If you keep the scopes empty, only the default scope will be requested from user.
         * You can view the list of available scopes here: https://dev.vk.com/ru/reference/access-rights.
         * The user will see a screen where he may grant some of this scopes during authorization process.
         */
        public var scopes: Set<String> = emptySet()

        /**
         * Extra params that should be send to auth provider
         */
        @InternalVKIDApi
        public var extraParams: Map<String, String>? = null

        /**
         * Mark that auth started from internal VK ID module
         */
        @InternalVKIDApi
        public var internalUse: Boolean = false

        /**
         * Constructs [VKIDAuthParams] object with provided values.
         */
        @OptIn(InternalVKIDApi::class)
        public fun build(): VKIDAuthParams = VKIDAuthParams(
            locale = locale,
            theme = theme,
            useOAuthProviderIfPossible = useOAuthProviderIfPossible,
            oAuth = oAuth,
            prompt = prompt,
            state = state,
            codeChallenge = codeChallenge,
            scopes = scopes,
            extraParams = extraParams,
            internalUse = internalUse,
        )
    }
}
