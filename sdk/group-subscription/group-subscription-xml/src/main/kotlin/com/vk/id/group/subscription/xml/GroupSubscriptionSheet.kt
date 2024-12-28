package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vk.id.group.subscription.common.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.compose.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.GroupSubscriptionSheetState
import com.vk.id.group.subscription.compose.rememberGroupSubscriptionSheetState

public class GroupSubscriptionSheet @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)
    private var state: GroupSubscriptionSheetState? = null

    public var accessToken: String? = null
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
            accessToken = accessToken ?: error("accessToken is not specified"),
            groupId = groupId ?: error("groupId is not specified"),
            onSuccess = onSuccess ?: error("setCallbacks was not called"),
            onFail = onFail ?: error("setCallbacks was not called"),
        )
    }

    public fun setCallbacks(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onFail = onFail
    }

    public fun show() {
        state?.show() ?: error("View is not initialized")
    }

    public fun hide() {
        state?.hide() ?: error("View is not initialized")
    }
}
