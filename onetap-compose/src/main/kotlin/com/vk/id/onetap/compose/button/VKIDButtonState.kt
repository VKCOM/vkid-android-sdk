package com.vk.id.onetap.compose.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.vk.id.onetap.common.R

@Composable
public fun rememberVKIDButtonState(
    text: String = stringResource(R.string.vkid_log_in_with_vkid),
    inProgress: Boolean = false,
): VKIDButtonState = remember {
    VKIDButtonState(inProgress, text)
}

public open class VKIDButtonState(
    inProgress: Boolean,
    text: String,
    userIconUrl: String? = null,
    textVisible: Boolean = true,
    rightIconVisible: Boolean = false,
    userLoadFailed: Boolean = false
) {
    private var _inProgress: Boolean by mutableStateOf(inProgress)
    private var _text: String by mutableStateOf(text)
    private var _userIconUrl: String? by mutableStateOf(userIconUrl)
    private var _textVisible: Boolean by mutableStateOf(textVisible)
    private var _rightIconVisible: Boolean by mutableStateOf(rightIconVisible)
    private var _userLoadFailed: Boolean by mutableStateOf(userLoadFailed)

    internal var inProgress: Boolean
        get() = _inProgress
        internal set(value) {
            if (value != _inProgress) {
                _inProgress = value
            }
        }

    internal var text: String
        get() = _text
        internal set(value) {
            if (value != _text) {
                _text = value
            }
        }

    internal var userIconUrl: String?
        get() = _userIconUrl
        internal set(value) {
            if (value != _userIconUrl) {
                _userIconUrl = value
            }
        }

    internal var textVisible: Boolean
        get() = _textVisible
        internal set(value) {
            if (value != _textVisible) {
                _textVisible = value
            }
        }

    internal var rightIconVisible: Boolean
        get() = _rightIconVisible
        internal set(value) {
            if (value != _rightIconVisible) {
                _rightIconVisible = value
            }
        }

    internal var userLoadFailed: Boolean
        get() = _userLoadFailed
        internal set(value) {
            if (value != _userLoadFailed) {
                _userLoadFailed = value
            }
        }
}

public class VKIDSmallButtonState(inProgress: Boolean, userIconLoaded: Boolean) : VKIDButtonState(inProgress, text = "") {
    private var _userIconLoaded: Boolean by mutableStateOf(userIconLoaded)
    private var _userIconLoading: Boolean by mutableStateOf(false)

    internal var userIconLoaded: Boolean
        get() = _userIconLoaded
        internal set(value) {
            if (value != _userIconLoaded) {
                _userIconLoaded = value
            }
        }

    internal var userIconLoading: Boolean
        get() = _userIconLoading
        internal set(value) {
            if (value != _userIconLoading) {
                _userIconLoading = value
            }
        }
}
