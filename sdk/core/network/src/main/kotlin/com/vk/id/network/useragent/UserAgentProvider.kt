package com.vk.id.network.useragent

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.core.content.pm.PackageInfoCompat
import com.vk.id.network.BuildConfig
import java.util.Locale

internal class UserAgentProvider(private val context: Context) {
    internal val userAgent: String by lazy {
        toHumanReadableAscii(
            String.format(
                Locale.US,
                "%s/%s-%s (Android %s; SDK %d; %s; %s %s; %s; %dx%d)",
                "VKID_${BuildConfig.VKID_VERSION_NAME}($packageName)",
                appVersion,
                appBuild,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                Build.SUPPORTED_ABIS[0],
                Build.MANUFACTURER,
                Build.MODEL,
                System.getProperty("user.language"),
                maxOf(displaySize.x, displaySize.y),
                minOf(displaySize.x, displaySize.y)
            )
        )
    }

    private val appBuild get() = packageInfo?.let(PackageInfoCompat::getLongVersionCode)?.toString().orEmpty()

    private val appVersion get() = packageInfo?.versionName.orEmpty()

    private val packageName get() = context.packageName

    private val packageInfo by lazy {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (ignored: Exception) {
            null
        }
    }

    @Suppress("DEPRECATION")
    private val displaySize by lazy {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        Point().also { point -> windowManager.defaultDisplay.getRealSize(point) }
    }

    @Suppress("MagicNumber")
    private fun toHumanReadableAscii(string: String): String {
        val result = StringBuilder()
        var i = 0
        while (i < string.length) {
            val codePoint = string.codePointAt(i)
            if (codePoint in 0x20..0x7e) {
                result.appendCodePoint(codePoint)
            } else {
                result.append('?')
            }
            i += Character.charCount(codePoint)
        }
        return result.toString()
    }
}
