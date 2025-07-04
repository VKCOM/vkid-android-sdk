@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.xml.GroupSubscriptionSnackbarHost
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

/**
 * OneTap is a view that provides VKID One Tap login interface.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * You should [setCallbacks] on init view to get token after successful auth.
 *
 * @since 1.0.0
 */
public class OneTap @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    /**
     * The styling for the One Tap interface, default is [OneTapStyle.Light]
     *
     * @since 1.0.0
     */
    public var style: OneTapStyle = OneTapStyle.Light()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (OneTapStyle) -> Unit = {}

    /**
     * Flag to enable a button for signing into another account.
     * Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
     *
     * @since 1.0.0
     */
    public var isSignInToAnotherAccountEnabled: Boolean = false
        set(value) {
            field = value
            onSignInToAnotherAccountEnabledChange(value)
        }
    private var onSignInToAnotherAccountEnabledChange: (Boolean) -> Unit = {}

    /**
     * Optional params to be passed to auth. See [VKIDAuthUiParams.Builder] for more info.
     *
     * @since 2.0.0-alpha
     */
    public var authParams: VKIDAuthUiParams = VKIDAuthUiParams { }
        set(value) {
            field = value
            onAuthParamsChange(value)
        }
    private var onAuthParamsChange: (VKIDAuthUiParams) -> Unit = {}
    private var onAuth: (OneTapOAuth?, AccessToken) -> Unit = { _, _ ->
        throw IllegalStateException("No onAuth callback for VKID OneTap Button. Set it with setCallbacks method.")
    }
    private var onAuthCode: (AuthCodeData, Boolean) -> Unit = { _, _ -> }
    private var onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> }

    /**
     * Whether to fetch user. Defaults to true.
     * In case this parameter is set to false the user data won't be fetched and user will have to confirm authorization on click.
     * Note: this parameter doesn't support changes in runtime and must be initialized when the view is constructed.
     * Note: This parameter will hide "change account" button because in this case OneTap will have the same behaviour.
     *
     * @since 2.0.0-alpha03
     */
    public var fastAuthEnabled: Boolean = true

    /**
     * Adds support multibranding auth. By default this widget is not displayed.
     * This property represents a set of [OneTapOAuth] to be displayed.
     *
     * @since 1.0.0
     */
    public var oAuths: Set<OneTapOAuth> = emptySet()
        set(value) {
            field = value
            onOAuthsChange(value)
        }
    private var onOAuthsChange: (Set<OneTapOAuth>) -> Unit = {}

    /**
     * Scenario for which the OneTap is used. Changes title accordingly.
     *
     * @since 2.1.0
     */
    public var scenario: OneTapTitleScenario = OneTapTitleScenario.SignIn
        set(value) {
            field = value
            onScenarioChange(value)
        }
    private var onScenarioChange: (OneTapTitleScenario) -> Unit = {}

    /**
     * The id of the group the user will be subscribed to.
     *
     * @since 2.5.0
     */
    public var groupId: String? = null
        set(value) {
            field = value
            onGroupIdChange(value)
        }
    private var onGroupIdChange: (String?) -> Unit = {}
    private var onSuccessSubscribingToGroup: () -> Unit = {
        error("setGroupSubscriptionCallbacks was not called")
    }
    private var onFailSubscribingToGroup: (VKIDGroupSubscriptionFail) -> Unit = {
        error("setGroupSubscriptionCallbacks was not called")
    }

    /**
     * The host for snackbars. Pass the view after placing it on screen.
     *
     * @since 2.5.0
     */
    public var snackbarHost: GroupSubscriptionSnackbarHost? = null
        set(value) {
            field = value
            onSnackbarHostChange(value)
        }
    private var onSnackbarHostChange: (GroupSubscriptionSnackbarHost?) -> Unit = {}

    /**
     * The widget style, can change appearance.
     *
     * @since 2.5.0
     */
    public var groupSubscriptionStyle: GroupSubscriptionStyle = GroupSubscriptionStyle.Light()
        set(value) {
            field = value
            onGroupSubscriptionStyleChange(value)
        }
    private var onGroupSubscriptionStyleChange: (GroupSubscriptionStyle) -> Unit = {}

    init {
        val params = parseOneTapAttrs(context, attrs)
        this.style = params.style
        this.isSignInToAnotherAccountEnabled = params.isSignInToAnotherAccountEnabled
        this.oAuths = params.oAuths
        this.authParams = authParams.newBuilder { scopes = params.scopes }
        this.fastAuthEnabled = params.fastAuthEnabled
        this.scenario = params.scenario
        this.groupId = params.groupId
        this.groupSubscriptionStyle = params.groupSubscriptionStyle
        addView(composeView)
        composeView.setContent { Content() }
        clipChildren = false
        clipToPadding = false
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content() {
        var style by remember { mutableStateOf(style) }
        onStyleChange = { style = it }
        var isSignInToAnotherAccountEnabled by remember { mutableStateOf(isSignInToAnotherAccountEnabled) }
        onSignInToAnotherAccountEnabledChange = { isSignInToAnotherAccountEnabled = it }
        var authParams by remember { mutableStateOf(authParams) }
        onAuthParamsChange = { authParams = it }
        var oAuths by remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths = it }
        var scenario by remember { mutableStateOf(scenario) }
        onScenarioChange = { scenario = it }
        var groupId by remember { mutableStateOf(groupId) }
        onGroupIdChange = { groupId = it }
        var snackbarHost by remember { mutableStateOf(snackbarHost) }
        onSnackbarHostChange = { snackbarHost = it }
        var groupSubscriptionStyle by remember { mutableStateOf(groupSubscriptionStyle) }
        onGroupSubscriptionStyleChange = { groupSubscriptionStyle = it }

        if (groupId != null) {
            OneTap(
                modifier = Modifier,
                style = style,
                onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
                onFail = { oAuth, fail -> onFail(oAuth, fail) },
                oAuths = oAuths,
                signInAnotherAccountButtonEnabled = isSignInToAnotherAccountEnabled,
                authParams = authParams,
                fastAuthEnabled = fastAuthEnabled,
                scenario = scenario,
                subscribeToGroupId = groupId!!,
                onSuccessSubscribingToGroup = { onSuccessSubscribingToGroup() },
                onFailSubscribingToGroup = { onFailSubscribingToGroup(it) },
                groupSubscriptionSnackbarHostState = snackbarHost?.snackbarHostState ?: error("snackbarHostState is not provided"),
                groupSubscriptionStyle = groupSubscriptionStyle,
            )
        } else {
            OneTap(
                modifier = Modifier,
                style = style,
                onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
                onFail = { oAuth, fail -> onFail(oAuth, fail) },
                oAuths = oAuths,
                signInAnotherAccountButtonEnabled = isSignInToAnotherAccountEnabled,
                authParams = authParams,
                fastAuthEnabled = fastAuthEnabled,
                scenario = scenario,
            )
        }
    }

    /**
     * Callbacks that provide auth result for version with multibranding.
     *
     * @param onAuth A callback to be invoked upon a successful auth.
     * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
     * which can later be exchanged to access token.
     * isCompletion is true if [onSuccess] won't be called.
     * This will happen if you passed auth parameters and implement their validation yourself.
     * In that case we can't exchange auth code for access token and you should do this yourself.
     * @param onFail A callback to be invoked upon an error during auth.
     *
     * @since 1.0.0
     */
    public fun setCallbacks(
        onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
        onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
        onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
    ) {
        this.onAuth = onAuth
        this.onAuthCode = onAuthCode
        this.onFail = onFail
    }

    /**
     * Callbacks that provide Group Subscription result.
     *
     * @param onSuccess Will be called upon successful subscription.
     * @param onFail Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
     *
     * @since 2.5.0
     */
    public fun setGroupSubscriptionCallbacks(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit = {}
    ) {
        this.onSuccessSubscribingToGroup = onSuccess
        this.onFailSubscribingToGroup = onFail
    }
}
