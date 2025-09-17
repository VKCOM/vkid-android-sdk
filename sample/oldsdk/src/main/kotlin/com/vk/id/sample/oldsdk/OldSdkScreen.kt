package com.vk.id.sample.oldsdk

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.VKDefaultValidationHandler
import com.vk.api.sdk.utils.log.DefaultApiLogger
import com.vk.api.sdk.utils.log.Logger
import com.vk.id.sample.xml.uikit.common.showToast
import com.vk.id.vksdksupport.VKIDRefreshTokenFailException
import com.vk.id.vksdksupport.withVKIDToken
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponseDto
import com.vk.sdk.api.users.dto.UsersFieldsDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
public fun OldSdkScreen() {
    val context = LocalContext.current
    var friends by remember { mutableStateOf<FriendsGetFieldsResponseDto?>(null) }
    LaunchedEffect(Unit) {
        friends = withContext(Dispatchers.IO) {
            VK.setConfig(
                VKApiConfig(
                    context = context,
                    appId = 51867225,
                    validationHandler = VKDefaultValidationHandler(context),
                    apiHostProvider = { "api.vk.ru" },
                    logger = DefaultApiLogger(lazy { Logger.LogLevel.VERBOSE }, "VKSdkApi"),
                )
            )
            try {
                VK.executeSync(
                    FriendsService()
                        .friendsGet(fields = listOf(UsersFieldsDto.FIRST_NAME_NOM, UsersFieldsDto.LAST_NAME_NOM))
                        .withVKIDToken()
                )
            } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                Log.e("LOG", "error", t)
                withContext(Dispatchers.Main) {
                    @Suppress("InstanceOfCheckForException")
                    showToast(context, if (t is VKIDRefreshTokenFailException) t.fail.description else t.message.orEmpty())
                }
                null
            }
        }
    }
    Box(contentAlignment = Alignment.Center) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            items(friends?.items ?: emptyList()) {
                Text(it.firstName.orEmpty() + "" + it.lastName.orEmpty())
            }
        }
        if (friends == null) {
            CircularProgressIndicator()
        }
    }
}
