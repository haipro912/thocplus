# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Luan Nguyen\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn org.webrtc.voiceengine.WebRtcAudioTrack
-dontwarn org.webrtc.Logging
-keep class com.stringee.StringeeConstant { *; }
-keep class com.stringee.StringeeCall { *; }
-keep class com.stringee.StringeeCallListener { *; }
-keep class com.stringee.StringeeIceCandidate { *; }
-keep class com.stringee.StringeeIceServer { *; }
-keep class com.stringee.StringeeSessionDescription { *; }
-keep class com.stringee.StringeeStream { *; }
-keep class com.stringee.StringeeSessionDescription$Type { *; }
-keep class com.stringee.StringeeCallListener$StringeeConnectionState { *; }
-keep class com.stringee.AudioStatsListener { *; }
-keep class com.stringee.StringeeStream$StringeeAudioStats { *; }
-keep class org.webrtc.** { *; }

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
    public static *** e(...);
}
-assumenosideeffects class org.webrtc.Logging {
    public static *** log(...);
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
    public static *** e(...);
}