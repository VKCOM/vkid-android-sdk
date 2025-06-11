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
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheet
import com.vk.id.onetap.compose.onetap.sheet.OneTapBottomSheetState
import com.vk.id.onetap.compose.onetap.sheet.rememberOneTapBottomSheetState

/**
 * OneTap is a modal bottom sheet that provides VKID One Tap login interface.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * You should [setCallbacks] on init view to get token after successful auth.
 *
 * To show or hide bottom sheet call [show] and [hide] methods.
 *
 * @since 0.0.1
 */
public class OneTapBottomSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    /**
     * Adds support multibranding auth. By default this widget is not displayed.
     * This property represents a set of [OneTapOAuth] to be displayed.
     *
     * @since 1.2.0
     */
    public var oAuths: Set<OneTapOAuth> = emptySet()
        set(value) {
            field = value
            onOAuthsChange(value)
        }
    private var onOAuthsChange: (Set<OneTapOAuth>) -> Unit = {}

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

    private var state: OneTapBottomSheetState? = null

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

    /**
     * Delay in millis after which sheet will be automatically shown.
     * Examples:
     * - null: not shown automatically
     * - 0: shown automatically immediately
     * - 1000: show automatically after 1 second
     *
     * @since 2.3.1
     */
    public var autoShowDelayMillis: Long? = null
        set(value) {
            field = value
            onAutoShowDelayMillisChange(value)
        }
    private var onAutoShowDelayMillisChange: (Long?) -> Unit = {}

    init {
        val params = parseOneTapBottomSheetAttrs(context, attrs)
        this.oAuths = params.oAuths
        this.authParams = authParams.newBuilder { this.scopes = params.scopes }
        this.fastAuthEnabled = params.fastAuthEnabled
        this.groupId = params.groupId
        this.groupSubscriptionStyle = params.groupSubscriptionStyle
        this.autoShowDelayMillis = params.autoShowDelayMillis
        composeView.setContent {
            Content(params)
        }
        addView(composeView)
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content(sheetSettings: OneTapBottomSheetAttributeSettings) {
        var oAuths by remember { mutableStateOf(oAuths) }
        onOAuthsChange = { oAuths = it }
        var authParams by remember { mutableStateOf(authParams) }
        onAuthParamsChange = { authParams = it }
        var groupId by remember { mutableStateOf(groupId) }
        onGroupIdChange = { groupId = it }
        var snackbarHost by remember { mutableStateOf(snackbarHost) }
        onSnackbarHostChange = { snackbarHost = it }
        var groupSubscriptionStyle by remember { mutableStateOf(groupSubscriptionStyle) }
        onGroupSubscriptionStyleChange = { groupSubscriptionStyle = it }
        var autoShowDelayMillis by remember { mutableStateOf(autoShowDelayMillis) }
        onAutoShowDelayMillisChange = { autoShowDelayMillis = it }
        if (groupId != null) {
            OneTapBottomSheet(
                state = rememberOneTapBottomSheetState().also {
                    state = it
                },
                style = sheetSettings.style,
                serviceName = sheetSettings.serviceName,
                scenario = sheetSettings.scenario,
                onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
                onFail = { oAuth, fail -> onFail(oAuth, fail) },
                autoHideOnSuccess = sheetSettings.autoHideOnSuccess,
                oAuths = oAuths,
                authParams = authParams,
                fastAuthEnabled = fastAuthEnabled,
                subscribeToGroupId = groupId!!,
                onSuccessSubscribingToGroup = { onSuccessSubscribingToGroup() },
                onFailSubscribingToGroup = { onFailSubscribingToGroup(it) },
                groupSubscriptionSnackbarHostState = snackbarHost?.snackbarHostState,
                groupSubscriptionStyle = groupSubscriptionStyle,
                autoShowSheetDelayMillis = autoShowDelayMillis,
            )
        } else {
            OneTapBottomSheet(
                state = rememberOneTapBottomSheetState().also {
                    state = it
                },
                style = sheetSettings.style,
                serviceName = sheetSettings.serviceName,
                scenario = sheetSettings.scenario,
                onAuth = { oAuth, accessToken -> onAuth(oAuth, accessToken) },
                onAuthCode = { data, isCompletion -> onAuthCode(data, isCompletion) },
                onFail = { oAuth, fail -> onFail(oAuth, fail) },
                autoHideOnSuccess = sheetSettings.autoHideOnSuccess,
                oAuths = oAuths,
                authParams = authParams,
                fastAuthEnabled = fastAuthEnabled,
                autoShowSheetDelayMillis = autoShowDelayMillis,
            )
        }
    }

    /**
     * Callbacks that provide auth result for version with multibranding.
     *
     * @param onAuth A callback to be invoked upon a successful auth.
     * @param onAuthCode A callback to be invoked upon successful first step of auth - receiving auth code
     * which can later be exchanged to access token.
     * @param onFail A callback to be invoked upon an error during auth.
     *
     * @since 0.0.1
     */
    public fun setCallbacks(
        onAuth: (oAuth: OneTapOAuth?, accessToken: AccessToken) -> Unit,
        onAuthCode: (data: AuthCodeData, isCompletion: Boolean) -> Unit = { _, _ -> },
        onFail: (oAuth: OneTapOAuth?, fail: VKIDAuthFail) -> Unit = { _, _ -> },
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

    /**
     * Expand bottom sheet with animation
     *
     * @since 0.0.1
     */
    public fun show() {
        state?.show()
    }

    /**
     * Hide bottom sheet with animation
     *
     * @since 0.0.1
     */
    public fun hide() {
        state?.hide()
    }

    /**
     * Whether the bottom sheet is visible.
     *
     * @since 0.0.1
     */
    public fun isVisible(): Boolean = state?.isVisible ?: false
}
