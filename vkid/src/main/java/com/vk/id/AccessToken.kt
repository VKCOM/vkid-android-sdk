package com.vk.id

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public data class AccessToken(
    val token: String,
    val userID: Long,
    val expireTime: Long
): Parcelable
