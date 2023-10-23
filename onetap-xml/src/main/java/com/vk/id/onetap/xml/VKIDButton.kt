package com.vk.id.onetap.xml

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates.observable
import com.vk.id.onetap.common.R as commonR

// TODO: Small button
// TODO: Sample
public class VKIDButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val vkId = VKID(context)
    private var onAuth: (AccessToken) -> Unit = {}
    private var onFail: (VKIDAuthFail) -> Unit = {}
    private var state by observable(
        VKIDButtonState(
            inProgress = false,
            text = context.getString(commonR.string.vkid_log_in_with_vkid)
        )
    ) { _, _, newState -> render(newState) }

    init {
        LayoutInflater.from(context).inflate(R.layout.vkid_button_layout, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val coroutineScope = findViewTreeLifecycleOwner()!!.lifecycleScope
        setOnClickListener { startAuth(coroutineScope, { state = it(state) }, vkId, { onAuth(it) }, { onFail(it) }) }
        fetchUserData(this, vkId,
            object : OnFetchingProgress {
                override suspend fun onPreFetch() {
                    if (state.userIconUrl == null) {
                        state = state.copy(inProgress = true)
                    }
                }

                override suspend fun onFetched(newText: String, newIconUrl: String?) {
                    if (state.text != newText || state.userIconUrl != newIconUrl) {
                        state = state.copy(inProgress = false)
                        state = state.copy(text = newText)
                        state = state.copy(userIconUrl = newIconUrl)
                    } else {
                        state = state.copy(inProgress = false)
                    }
                }

                override fun onDispose() {
                    state = state.copy(inProgress = false)
                }
            }
        )
    }

    public fun setCallbacks(
        onAuth: (AccessToken) -> Unit = {},
        onFail: (VKIDAuthFail) -> Unit = {},
    ) {
        this.onAuth = onAuth
        this.onFail = onFail
    }

    private fun render(state: VKIDButtonState) { // TODO: View binding
        findViewById<View>(R.id.progress).isVisible = state.inProgress
        findViewById<View>(R.id.userImage).isVisible = state.userIconUrl != null
        findViewById<ImageView>(R.id.userImage).load(state.userIconUrl) {
            scale(Scale.FIT)
            transformations(CircleCropTransformation())
        }
        findViewById<TextView>(R.id.vkidButtonText).text = state.text
    }
}

private data class VKIDButtonState(
    val inProgress: Boolean,
    val text: String,
    val userIconUrl: String? = null,
)

// TODO: Unify. Presenter?
private fun fetchUserData(
    view: View,
    vkid: VKID,
    onFetchingProgress: OnFetchingProgress,
) {
    val lifecycleOwner = view.findViewTreeLifecycleOwner()!!
    val resources = view.resources
    var fetchUserJob: Job? = null
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            fetchUserJob = lifecycleOwner.lifecycleScope.launch {
                onFetchingProgress.onPreFetch()
                val user = vkid.fetchUserData().getOrNull()
                val newText: String
                val newIconUrl: String?
                if (user != null) {
                    newText = resources.getString(commonR.string.vkid_log_in_as, user.firstName)
                    newIconUrl = user.photo200
                } else {
                    newText = resources.getString(commonR.string.vkid_log_in_with_vkid)
                    newIconUrl = null
                }
                onFetchingProgress.onFetched(newText, newIconUrl)
            }
        }

        override fun onPause(owner: LifecycleOwner) {
            onFetchingProgress.onDispose()
            fetchUserJob?.cancel()
        }
    })
}

private fun startAuth(
    coroutineScope: CoroutineScope,
    updateState: ((VKIDButtonState) -> VKIDButtonState) -> Unit,
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
) {
    coroutineScope.launch {
        updateState { it.copy(inProgress = true) }
        vkid.authorize(object : VKID.AuthCallback {
            override fun onSuccess(accessToken: AccessToken) {
                updateState { it.copy(inProgress = false) }
                onAuth(accessToken)
            }

            override fun onFail(fail: VKIDAuthFail) {
                updateState { it.copy(inProgress = false) }
                onFail(fail)
            }
        })
    }
}

private interface OnFetchingProgress {
    suspend fun onPreFetch()
    suspend fun onFetched(newText: String, newIconUrl: String?)
    fun onDispose()
}
