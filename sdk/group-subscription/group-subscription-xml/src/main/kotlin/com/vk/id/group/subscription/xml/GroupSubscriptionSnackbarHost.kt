@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.xml

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost

public class GroupSubscriptionSnackbarHost @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val composeView = ComposeView(context)

    @InternalVKIDApi
    public val snackbarHostState: SnackbarHostState = SnackbarHostState()

    init {
        composeView.setContent { Content() }
        addView(composeView)
    }

    @Composable
    private fun Content() {
        GroupSubscriptionSnackbarHost(
            snackbarHostState = snackbarHostState
        )
    }
}
