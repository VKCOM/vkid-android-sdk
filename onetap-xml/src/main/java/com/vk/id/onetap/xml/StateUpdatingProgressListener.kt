package com.vk.id.onetap.xml

internal class StateUpdatingProgressListener(
    private val updateState: ((VKIDButtonState) -> VKIDButtonState) -> Unit
) {
    fun onPreFetch() = updateState {
        if (it.userIconUrl == null) {
            it.copy(inProgress = true)
        } else {
            it
        }
    }

    fun onFetched(newText: String, newIconUrl: String?) = updateState {
        if (it.text != newText || it.userIconUrl != newIconUrl) {
            it.copy(
                inProgress = false,
                text = newText,
                userIconUrl = newIconUrl,
            )
        } else {
            it.copy(inProgress = false)
        }
    }

    fun onDispose() = updateState {
        it.copy(inProgress = false)
    }
}
