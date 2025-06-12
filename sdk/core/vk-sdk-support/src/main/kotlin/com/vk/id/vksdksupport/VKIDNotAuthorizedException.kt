package com.vk.id.vksdksupport

/**
 * An exception which is thrown during VK Android SDK Request while using [withVKIDToken] if the user didn't pass auth yet.
 *
 * @since 1.3.2
 */
public class VKIDNotAuthorizedException : IllegalStateException("Not authorized")
