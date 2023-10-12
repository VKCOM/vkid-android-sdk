package com.vk.id.onetap.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
public fun rememberVKIDButtonState(
    inProgress: Boolean = false
): VKIDButtonState = remember {
    VKIDButtonState(inProgress)
}

public class VKIDButtonState(inProgress: Boolean) {

    private var _inProgress: Boolean by mutableStateOf(inProgress)

    internal var inProgress: Boolean
        get() = _inProgress
        internal set(value) {
            if (value != _inProgress) {
                _inProgress = value
            }
        }
}
