/**
 * Copyright (c) 2020 - present, LLC “V Kontakte”
 *
 * 1. Permission is hereby granted to any person obtaining a copy of this Software to
 * use the Software without charge.
 *
 * 2. Restrictions
 * You may not modify, merge, publish, distribute, sublicense, and/or sell copies,
 * create derivative works based upon the Software or any part thereof.
 *
 * 3. Termination
 * This License is effective until terminated. LLC “V Kontakte” may terminate this
 * License at any time without any negative consequences to our rights.
 * You may terminate this License at any time by deleting the Software and all copies
 * thereof. Upon termination of this license for any reason, you shall continue to be
 * bound by the provisions of Section 2 above.
 * Termination will be without prejudice to any rights LLC “V Kontakte” may have as
 * a result of this agreement.
 *
 * 4. Disclaimer of warranty and liability
 * THE SOFTWARE IS MADE AVAILABLE ON THE “AS IS” BASIS. LLC “V KONTAKTE” DISCLAIMS
 * ALL WARRANTIES THAT THE SOFTWARE MAY BE SUITABLE OR UNSUITABLE FOR ANY SPECIFIC
 * PURPOSES OF USE. LLC “V KONTAKTE” CAN NOT GUARANTEE AND DOES NOT PROMISE ANY
 * SPECIFIC RESULTS OF USE OF THE SOFTWARE.
 * UNDER NO CIRCUMSTANCES LLC “V KONTAKTE” BEAR LIABILITY TO THE LICENSEE OR ANY
 * THIRD PARTIES FOR ANY DAMAGE IN CONNECTION WITH USE OF THE SOFTWARE.
*/

package com.vk.silentauth

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.vk.dto.common.id.UserId
import com.vk.dto.common.id.isReal
import com.vk.silentauth.SilentAuthInfo.Companion.KEY_IS_EXCHANGE_USER
import com.vk.silentauth.SilentAuthInfo.Companion.KEY_SERVICE
import com.vk.silentauth.SilentAuthInfo.Companion.KEY_SERVICE_NAME
import org.json.JSONObject
import kotlin.math.absoluteValue

/**
 * @param userId user id
 * @param uuid silent token uuid
 * @param token silent token
 * @param expireTime silent token expiration
 * @param firstName user first name
 * @param photo50 50x50 avatar url
 * @param photo100 100x100 avatar url
 * @param photo200 200x200 avatar url
 * @param lastName first letter of lastName (e.g. "A.")
 * @param phone phone mask of user
 * @param serviceInfo extra data for service in json format
 * @param extras extra data for service in bundle {@see [KEY_IS_EXCHANGE_USER], [KEY_SERVICE], [KEY_SERVICE_NAME]}
 * @param weight silent token priority
 * @param userHash user hash
 * @param applicationProviderPackage package name of the application that provided silent token
 * @param providerInfoItems simplified information about this user's other providers
 */
internal data class SilentAuthInfo(
    internal val userId: UserId,
    val uuid: String,
    val token: String,
    val expireTime: Long,
    val firstName: String = "",
    val photo50: String? = null,
    val photo100: String? = null,
    val photo200: String? = null,
    val lastName: String = "",
    val phone: String? = null,
    val serviceInfo: String? = null,
    val extras: Bundle? = null,
    val weight: Int = 0,
    val userHash: String = EMPTY_USER_HASH,
    val applicationProviderPackage: String? = null,
    val providerInfoItems: List<SilentTokenProviderInfo> = emptyList(),
    val providerAppId: Int = 0
) : Parcelable {

    @Deprecated("Use constructor with [UserId], current constructor will be hidden in future")
    constructor(
        userIdOld: Int,
        uuid: String,
        token: String,
        expireTime: Long,
        firstName: String = "",
        photo50: String? = null,
        photo100: String? = null,
        photo200: String? = null,
        lastName: String = "",
        phone: String? = null,
        serviceInfo: String? = null,
        extras: Bundle? = null,
        weight: Int = 0,
        userHash: String = EMPTY_USER_HASH,
        applicationProviderPackage: String? = null,
        providerInfoItems: List<SilentTokenProviderInfo> = emptyList(),
        userId: UserId? = null,
        providerAppId: Int = 0
    ) : this(
        userId ?: UserId(userIdOld.toLong()),
        uuid,
        token,
        expireTime,
        firstName,
        photo50,
        photo100,
        photo200,
        lastName,
        phone,
        serviceInfo,
        extras,
        weight,
        userHash,
        applicationProviderPackage,
        providerInfoItems,
        providerAppId
    )

    val oauthServiceName: String?
        get() = extras?.getString(KEY_SERVICE_NAME)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Bundle::class.java.classLoader),
        parcel.readInt(),
        parcel.readString() ?: EMPTY_USER_HASH,
        parcel.readString(),
        ArrayList<SilentTokenProviderInfo>().also {
            parcel.readList(
                it as List<*>,
                SilentTokenProviderInfo::class.java.classLoader
            )
        },
        parcel.readParcelable(UserId::class.java.classLoader),
        parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(userId.value.toInt())
        dest.writeString(uuid)
        dest.writeString(token)
        dest.writeLong(expireTime)
        dest.writeString(firstName)
        dest.writeString(photo50)
        dest.writeString(photo100)
        dest.writeString(photo200)
        dest.writeString(lastName)
        dest.writeString(phone)
        dest.writeString(serviceInfo)
        dest.writeParcelable(extras, 0)
        dest.writeInt(weight)
        dest.writeString(userHash)
        dest.writeString(applicationProviderPackage)
        dest.writeList(providerInfoItems)
        dest.writeParcelable(userId, 0)
        dest.writeInt(providerAppId)
    }

    override fun describeContents() = 0

    fun isExchangeUser() = extras?.getBoolean(KEY_IS_EXCHANGE_USER) ?: false

    fun getDistinctId(): Long {
        if (userId.isReal()) return userId.value
        // С большой вероятностью связка имя+фото+маска номера -- уникальна для пользователя
        val distinctString = "$firstName$lastName$photo200$phone"
        return distinctString.hashCode().absoluteValue.unaryMinus().toLong()
    }

    companion object {
        const val EMPTY_USER_HASH = ""

        // Fallback for exchange users in VK/VK Me
        private const val KEY_IS_EXCHANGE_USER = "isExchangeUser"

        // По этому ключу хранится name enum-а VkOAuthService. Используется для определения клика по
        // юзеру другого сервиса, для которого доступна быстрая авторизация.
        // Например, MailSilentAuthInfoProvider.
        const val KEY_SERVICE = "key_service"

        // По этому ключу хранится VkOAuthService.serviceName. Используется для определения внешнего
        // oauth сервиса, для которого происходит обмен ST на AT. Заполняется в момент, когда бэк VK
        // ответил связкой (silentToken,silentTokenUuid,silentTokenTtl) на авторизацию
        // с grant_type=external_service.
        const val KEY_SERVICE_NAME = "key_service_name"

        const val KEY_USER = "user"
        const val KEY_ID = "id"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_AVATAR = "avatar"
        const val KEY_PHONE = "phone"
        const val KEY_TOKEN = "token"
        const val KEY_UUID = "uuid"
        const val KEY_TTL = "ttl"

        fun createFromJSON(
            json: JSONObject
        ): SilentAuthInfo {
            val user = json.getJSONObject(KEY_USER)
            val avatar = user.optString(KEY_AVATAR)
            return SilentAuthInfo(
                userId = UserId(user.getLong(KEY_ID)),
                uuid = json.getString(KEY_UUID),
                token = json.getString(KEY_TOKEN),
                expireTime = json.optLong(KEY_TTL),
                phone = user.optString(KEY_PHONE),
                firstName = user.optString(KEY_FIRST_NAME),
                lastName = user.optString(KEY_LAST_NAME),
                photo50 = avatar,
                photo100 = avatar,
                photo200 = avatar,
            )
        }

        fun createForExchangeUser(
            userId: UserId,
            exchangeToken: String,
            name: String,
            avatarUrl: String?
        ): SilentAuthInfo {
            return SilentAuthInfo(
                userId = userId,
                uuid = "",
                token = exchangeToken,
                expireTime = 0,
                firstName = name,
                photo50 = avatarUrl,
                photo100 = avatarUrl,
                photo200 = avatarUrl,
                extras = Bundle(1).apply { putBoolean(KEY_IS_EXCHANGE_USER, true) }
            )
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<SilentAuthInfo> {
            override fun createFromParcel(parcel: Parcel): SilentAuthInfo {
                return SilentAuthInfo(parcel)
            }

            override fun newArray(size: Int): Array<SilentAuthInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}
