package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.R
import com.vk.id.onetap.xml.databinding.VkidButtonSmallLayoutBinding
import kotlin.properties.Delegates

public class VKIDButtonSmall @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val vkId = VKID(context)
    private var onAuth: (AccessToken) -> Unit = {}
    private var onFail: (VKIDAuthFail) -> Unit = {}
    private var state by Delegates.observable(
        VKIDButtonState(
            inProgress = false,
            text = context.getString(R.string.vkid_log_in_with_vkid)
        )
    ) { _, _, newState -> render(newState) }

    private val binding = VkidButtonSmallLayoutBinding.inflate(LayoutInflater.from(context), this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val coroutineScope = findViewTreeLifecycleOwner()!!.lifecycleScope
        setOnClickListener { startAuth(coroutineScope, { state = it(state) }, vkId, { onAuth(it) }, { onFail(it) }) }
        fetchUserData(this, vkId, StateUpdatingProgressListener { state = it(state) })
    }

    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit = {},
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }
    private fun render(state: VKIDButtonState) {
        binding.progress.isVisible = state.inProgress
        binding.vkidIcon.isVisible = state.userIconUrl == null && !state.inProgress
        binding.userImage.isVisible = state.userIconUrl != null && !state.inProgress
        binding.userImage.load(state.userIconUrl) {
            scale(Scale.FIT)
            transformations(CircleCropTransformation())
        }
    }
}
