package com.vk.id

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public class AccessToken(
    public val token: String,
    public val userID: Long,
    public val expireTime: Long,
    public val userData: VKIDUser,
) : Parcelable
