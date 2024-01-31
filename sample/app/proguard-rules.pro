# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.vk.dto.common.id.UserId { *; }
-keep class * extends com.vk.id.sample.app.util.carrying.CarryingCallable { *; }
-keep class android.content.Context { *; }

# OneTapStyle.Companion methods's arguments and return types
-keep class com.vk.id.onetap.common.OneTapStyle { *; }
-keep class com.vk.id.onetap.common.OneTapStyle$* { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle { *; }
# OAuthListWidgetStyle.Companion methods's arguments and return types
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetStyle { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetStyle$* { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle { *; }
-keep class com.vk.id.multibranding.common.style.OAuthListWidgetSizeStyle { *; }
# OneTapBottomSheetStyle.Companion methods's arguments and return types
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle { *; }
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle$* { *; }
-keep class com.vk.id.onetap.compose.onetap.sheet.style.OneTapSheetCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle { *; }
-keep class com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle { *; }
