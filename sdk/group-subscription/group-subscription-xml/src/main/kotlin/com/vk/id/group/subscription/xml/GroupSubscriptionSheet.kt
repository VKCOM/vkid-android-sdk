@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheetState
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState

/**
 * A bottomsheet that provides Group Subscription functionality.
 *
 * You should [setCallbacks] on initialized view to get flow result.
 */
public class GroupSubscriptionSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)
    private var state: GroupSubscriptionSheetState? = null

    /**
     * The function that provides an access token that will be used for retrieving group information and subscribing the user.
     * NOTE: The token must have "groups" scope, otherwise you'll get an error.
     * NOTE: The token won't be automatically refreshed, in case it's outdated you'll get an error.
     * NOTE: In case you will pass null, the last token you received with the SDK will be used.
     */
    public var accessTokenProvider: (() -> String)? = null

    /**
     * The id of the group the user will be subscribed to.
     */
    public var groupId: String? = null
        set(value) {
            field = value
            onGroupIdChange(value)
        }
    private var onGroupIdChange: (String?) -> Unit = {}
    private var onSuccess: () -> Unit = {
        error("setCallbacks was not called")
    }
    private var onFail: (VKIDGroupSubscriptionFail) -> Unit = {
        error("setCallbacks was not called")
    }

    /**
     * The host for snackbars. Pass the view after placing it on screen.
     */
    public var snackbarHost: GroupSubscriptionSnackbarHost? = null
        set(value) {
            field = value
            onSnackbarHostChange(value)
        }
    private var onSnackbarHostChange: (GroupSubscriptionSnackbarHost?) -> Unit = {}

    /**
     * The widget style, can change appearance.
     */
    public var style: GroupSubscriptionStyle = GroupSubscriptionStyle.Light()
        set(value) {
            field = value
            onStyleChange(value)
        }
    private var onStyleChange: (GroupSubscriptionStyle) -> Unit = {}

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.vkid_GroupSubscription,
            0,
            0
        ).apply {
            try {
                groupId = vkidInternalGetGroupId()
                style = vkidInternalGetGroupSubscriptionStyleConstructor(context)(
                    GroupSubscriptionSheetCornersStyle.Custom(
                        context.pixelsToDp(vkidInternalGetGroupSubscriptionCornerRadius(context))
                    ),
                    GroupSubscriptionButtonsCornersStyle.Custom(
                        context.pixelsToDp(vkidInternalGetGroupSubscriptionButtonCornerRadius(context))
                    ),
                    vkidInternalGetGroupSubscriptionButtonSize(),
                )
            } finally {
                recycle()
            }
        }
        composeView.setContent { Content() }
        addView(composeView)
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content() {
        var groupId by remember { mutableStateOf(groupId) }
        onGroupIdChange = { groupId = it }
        var snackbarHost by remember { mutableStateOf(snackbarHost) }
        onSnackbarHostChange = { snackbarHost = it }
        var style by remember { mutableStateOf(style) }
        onStyleChange = { style = it }
        GroupSubscriptionSheet(
            state = rememberGroupSubscriptionSheetState().also { state = it },
            accessTokenProvider = accessTokenProvider,
            groupId = groupId ?: error("groupId is not specified"),
            onSuccess = { onSuccess() },
            onFail = { onFail(it) },
            snackbarHostState = snackbarHost?.snackbarHostState,
            style = style,
        )
    }

    /**
     * Callbacks that provide Group Subscription result.
     *
     * @param onSuccess Will be called upon successful subscription.
     * @param onFail Will be called upon any unsuccessful flow completion along with an description of the specific encountered error.
     */
    public fun setCallbacks(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit = {}
    ) {
        this.onSuccess = onSuccess
        this.onFail = onFail
    }

    /**
     * Show the bottom sheet.
     */
    public fun show() {
        state?.show() ?: error("View is not initialized")
    }

    /**
     * Hides the bottom sheet.
     */
    public fun hide() {
        state?.hide() ?: error("View is not initialized")
    }
}
