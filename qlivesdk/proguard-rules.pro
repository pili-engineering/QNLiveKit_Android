# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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


#保留行号
-keepattributes SourceFile,LineNumberTable
#保持泛型
-keepattributes Signature


# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep interface com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

#fastjosn
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }

-keepclasseswithmembernames class * {
    native <methods>;
}


#信令
-keep class com.qlive.rtm.**{*;}
-keep class com.qlive.rtminvitation.**{*;}

#coreimpl层 been不混淆
-keep class com.qlive.coreimpl.model.**{*;}
#core层不混淆
-keep class com.qlive.core.**{*;}
#QLiveService
-keep class * implements com.qlive.core.QLiveService {
#匹配所有构造器
  public <init>();
}
-keep class * implements com.qlive.coreimpl.BaseService {
  #匹配所有构造器
  public <init>();
}

#im
-keep class im.floo.floolib.**{*;}
#播放器
-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}
#rtc
-keep class org.webrtc.** {*;}
-dontwarn org.webrtc.**
-keep class com.qiniu.droid.rtc.**{*;}
-keep interface com.qiniu.droid.rtc.**{*;}

