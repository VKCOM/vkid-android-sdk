package com.vk.id.onetap.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource

@Composable
public fun rememberVKIDButtonState(
    text: String = stringResource(R.string.vkid_log_in_with_vkid),
    inProgress: Boolean = false,
): VKIDButtonState = remember {
    VKIDButtonState(inProgress, text)
}

public class VKIDButtonState(inProgress: Boolean, text: String, userIconUrl: String? = null) {

    private var _inProgress: Boolean by mutableStateOf(inProgress)
    private var _text: String by mutableStateOf(text)
    private var _userIconUrl: String? by mutableStateOf(userIconUrl)

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
            if (value !== _text) {
                _text = value
            }
        }

    internal var userIconUrl: String?
        get() = _userIconUrl
        internal set(value) {
            if (value !== _userIconUrl) {
                _userIconUrl = value
            }
        }
}
