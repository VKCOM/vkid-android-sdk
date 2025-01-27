@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionButtonsCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionSheetCornersStyle
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost

/**
 * The host for Group Subscription snackbars.
 *
 * Must be placed where you want snackbars to appear, respecting screen insets.
 */
public class GroupSubscriptionSnackbarHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    @InternalVKIDApi
    public val snackbarHostState: SnackbarHostState = SnackbarHostState()

    /**
     * The widget style, can change appearance.
     *
     * Should be the same as for [GroupSubscriptionSheet].
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
        var style by remember { mutableStateOf(style) }
        onStyleChange = { style = it }
        GroupSubscriptionSnackbarHost(
            snackbarHostState = snackbarHostState,
            style = style,
        )
    }
}
