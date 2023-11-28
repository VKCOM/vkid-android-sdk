package com.vk.id

public sealed class VKIDAuthFail(public val description: String) {
    public data class Canceled(
        private val errorDescription: String
    ) : VKIDAuthFail(errorDescription)

    public data class FailedApiCall(
        private val errorDescription: String,
        public val throwable: Throwable
    ) : VKIDAuthFail(errorDescription)

    public data class FailedOAuthState(
        private val errorDescription: String
    ) : VKIDAuthFail(errorDescription)

    public data class FailedRedirectActivity(
        private val errorDescription: String,
        public val throwable: Throwable?
    ) : VKIDAuthFail(errorDescription)

    public data class NoBrowserAvailable(
        private val errorDescription: String,
        public val throwable: Throwable?
    ) : VKIDAuthFail(errorDescription)
}
