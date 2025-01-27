package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
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
    private var onSuccess: (() -> Unit)? = null
    private var onFail: ((VKIDGroupSubscriptionFail) -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.vkid_GroupSubscription,
            0,
            0
        ).apply {
            groupId = getString(R.styleable.vkid_GroupSubscription_vkid_groupId)
        }
        composeView.setContent { Content() }
        addView(composeView)
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun Content() {
        GroupSubscriptionSheet(
            state = rememberGroupSubscriptionSheetState().also { state = it },
            accessTokenProvider = accessTokenProvider,
            groupId = groupId ?: error("groupId is not specified"),
            onSuccess = onSuccess ?: error("setCallbacks was not called"),
            onFail = onFail ?: error("setCallbacks was not called"),
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
