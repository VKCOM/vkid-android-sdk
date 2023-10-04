package com.vk.id

public sealed class VKIDAuthFail(public val description: String) {
    public class Canceled(description: String) : VKIDAuthFail(description)
    public class FailedApiCall(description: String, public val throwable: Throwable) :
        VKIDAuthFail(description)

    public class FailedOAuthState(description: String) : VKIDAuthFail(description)
    public class FailedRedirectActivity(description: String, public val throwable: Throwable?) :
        VKIDAuthFail(
            description
        )

    public class NoBrowserAvailable(description: String, public val throwable: Throwable?) :
        VKIDAuthFail(description)
}
