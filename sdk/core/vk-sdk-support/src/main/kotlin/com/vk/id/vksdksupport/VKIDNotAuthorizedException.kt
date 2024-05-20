package com.vk.id.vksdksupport

/**
 * An exception which is thrown during VK Android SDK Request while using [withVKIDToken] if the user didn't pass auth yet.
 */
public class VKIDNotAuthorizedException : IllegalStateException("Not authorized")
