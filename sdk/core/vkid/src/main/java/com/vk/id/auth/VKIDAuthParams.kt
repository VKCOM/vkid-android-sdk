@file:OptIn(InternalVKIDApi::class)

package com.vk.id.auth

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.vk.id.OAuth
import com.vk.id.auth.VKIDAuthParams.Locale
import com.vk.id.auth.VKIDAuthParams.Theme
import com.vk.id.common.InternalVKIDApi

/**
 * Create [VKIDAuthParams].
 *
 * @param initializer params' initialization.
 *
 * @since 0.0.1
 */
public inline fun VKIDAuthParams(initializer: VKIDAuthParams.Builder.() -> Unit): VKIDAuthParams {
    return VKIDAuthParams.Builder().apply(initializer).build()
}

/**
 * VKIDAuthParams encapsulates parameters for VK ID authentication.
 *
 * @since 0.0.1
 */
@Suppress("LongParameterList")
public class VKIDAuthParams private constructor(
    /**
     * The [Locale] setting for the authentication UI. Optional.
     */
    internal val locale: Locale?,
    /**
     * The [Theme] setting for the authentication UI (Light or Dark). Optional.
     */
    internal val theme: Theme?,
    /**
     * Flag to use OAuth provider installed on device if possible. Defaults to true.
     */
    internal val useOAuthProviderIfPossible: Boolean,
    /**
     * The [OAuth] provider to be used for authentication. Optional.
     * Note: If you specify this provider auth will happen in WebView
     */
    internal val oAuth: OAuth?,
    /**
     * A [Prompt] parameter to be passed to /authorize.
     * Note: Changing it only works for auth view browser (not auth provider). Add [useOAuthProviderIfPossible] = false for this to work.
     */
    internal val prompt: Prompt,
    /**
     * An optional state to be passed to auth.
     */
    internal val state: String?,
    /**
     * An optional code challenge to be passed to auth.
     * See https://datatracker.ietf.org/doc/html/rfc7636#section-4.2 for more information.
     */
    internal val codeChallenge: String?,
    /**
     * A required parameter with a list of requested scopes for the access token.
     * You have to specify a subset a scopes that you request for your app in Self Service.
     * If you keep the scopes empty, only the default scope will be requested from user.
     * You can view the list of available scopes here: https://dev.vk.ru/ru/reference/access-rights.
     * The user will see a screen where he may grant some of this scopes during authorization process.
     */
    internal val scopes: Set<String>,
    /**
     * Key-value pairs of extra params that client want to send to auth provider. Optional.
     */
    internal val extraParams: Map<String, String>? = null,
    /**
     * Marks internal launch of auth.
     */
    internal val internalUse: Boolean = false
) {
    /**
     * Represents a locale that user prefers during authorization.
     *
     * @since 0.0.1
     */
    public enum class Locale {
        /**
         * Russian locale.
         *
         * @since 0.0.1
         */
        RUS,

        /**
         * Ukrainian locale.
         *
         * @since 0.0.1
         */
        UKR,

        /**
         * English locale.
         *
         * @since 0.0.1
         */
        ENG,

        /**
         * Spanish locale.
         *
         * @since 0.0.1
         */
        SPA,

        /**
         * German locale.
         *
         * @since 0.0.1
         */
        GERMAN,

        /**
         * Polish locale.
         *
         * @since 0.0.1
         */
        POL,

        /**
         * French locale.
         *
         * @since 0.0.1
         */
        FRA,

        /**
         * Turkish locale.
         *
         * @since 0.0.1
         */
        TURKEY;

        @InternalVKIDApi
        public companion object {
            /**
             * Returns the current system locale.
             *
             * @since 0.0.1
             */
            internal fun systemLocale(context: Context): Locale? {
                val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales.get(0)
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale
                }
                return fromLocale(systemLocale)
            }

            @InternalVKIDApi
            public fun fromLocale(locale: java.util.Locale?): Locale? {
                return when (locale?.language) {
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
     *
     * @since 0.0.1
     */
    public enum class Theme {
        /**
         * Represents light theme.
         *
         * @since 0.0.1
         */
        Light,

        /**
         * Represents dark theme.
         *
         * @since 0.0.1
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
     *
     * @since 0.0.1
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public class Builder {
        /**
         * Locale that should be used during authorization.
         *
         * @since 0.0.1
         */
        public var locale: Locale? = null

        /**
         * Theme that should be used during authorization.
         *
         * @since 0.0.1
         */
        public var theme: Theme? = null

        /**
         * Whether to use existing auth provider.
         *
         * @since 1.0.0
         */
        public var useOAuthProviderIfPossible: Boolean = true

        /**
         * An [OAuth] that should be used for authorization.
         *
         * @since 1.0.0
         */
        public var oAuth: OAuth? = null

        /**
         * A [Prompt] parameter to be passed to /authorize.
         * Note: Changing it only works for auth view browser (not auth provider). Add [useOAuthProviderIfPossible] = false for this to work.
         *
         * @since 2.0.0-alpha
         */
        public var prompt: Prompt = Prompt.BLANK

        /**
         * An optional state to be passed to auth.
         *
         * @since 2.0.0-alpha
         */
        public var state: String? = null

        /**
         * An optional code challenge to be passed to auth.
         * See https://datatracker.ietf.org/doc/html/rfc7636#section-4.2 for more information.
         *
         * @since 2.0.0-alpha
         */
        public var codeChallenge: String? = null

        /**
         * A required parameter with a list of requested scopes for the access token.
         * You have to specify a subset a scopes that you request for your app in Self Service.
         * If you keep the scopes empty, only the default scope will be requested from user.
         * You can view the list of available scopes here: https://dev.vk.ru/ru/reference/access-rights.
         * The user will see a screen where he may grant some of this scopes during authorization process.
         *
         * @since 2.0.0-alpha
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
         *
         * @since 0.0.1
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

    @InternalVKIDApi
    public fun newBuilder(initializer: Builder.() -> Unit): VKIDAuthParams {
        val params = this
        return VKIDAuthParams {
            locale = params.locale
            theme = params.theme
            useOAuthProviderIfPossible = params.useOAuthProviderIfPossible
            oAuth = params.oAuth
            prompt = params.prompt
            state = params.state
            codeChallenge = params.codeChallenge
            scopes = params.scopes
            extraParams = params.extraParams
            internalUse = params.internalUse
            initializer()
        }
    }

    /** @suppress */
    override fun toString(): String {
        return "VKIDAuthParams(" +
            "locale=$locale, " +
            "theme=$theme, " +
            "useOAuthProviderIfPossible=$useOAuthProviderIfPossible, " +
            "oAuth=$oAuth, " +
            "prompt=$prompt, " +
            "state=$state, " +
            "codeChallenge=$codeChallenge, " +
            "scopes=$scopes, " +
            "extraParams=$extraParams, " +
            "internalUse=$internalUse" +
            ")"
    }

    /** @suppress */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VKIDAuthParams

        if (locale != other.locale) return false
        if (theme != other.theme) return false
        if (useOAuthProviderIfPossible != other.useOAuthProviderIfPossible) return false
        if (oAuth != other.oAuth) return false
        if (prompt != other.prompt) return false
        if (state != other.state) return false
        if (codeChallenge != other.codeChallenge) return false
        if (scopes != other.scopes) return false
        if (extraParams != other.extraParams) return false
        if (internalUse != other.internalUse) return false

        return true
    }

    /** @suppress */
    override fun hashCode(): Int {
        var result = locale?.hashCode() ?: 0
        result = 31 * result + (theme?.hashCode() ?: 0)
        result = 31 * result + useOAuthProviderIfPossible.hashCode()
        result = 31 * result + (oAuth?.hashCode() ?: 0)
        result = 31 * result + prompt.hashCode()
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (codeChallenge?.hashCode() ?: 0)
        result = 31 * result + scopes.hashCode()
        result = 31 * result + (extraParams?.hashCode() ?: 0)
        result = 31 * result + internalUse.hashCode()
        return result
    }
}
