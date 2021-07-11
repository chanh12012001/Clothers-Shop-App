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
-keep class dmax.dialog.** {
    *;
}
-keep class javax.** {*;}
#   -keep class com.sun.** {*;}
#   -keep class myjava.** {*;}
#   -keep class org.apache.harmony.** {*;}
#   -keep class java.awt.datatransfer.Transferable
#   -keep class java.awt.datatransfer.Clipboard
#

-keepclassmembers class com.paytm.pgsdk.paytmWebView$PaytmJavaScriptInterface {
   public *;
}
   -keep class javax.** {*;}
   -keep class com.sun.** {*;}
   -keep class myjava.** {*;}
   -keep class org.apache.harmony.** {*;}
   -keep public class Mail {*;}
   -dontwarn java.awt.**
   -dontwarn java.beans.Beans
   -dontwarn javax.security.**
   -dontwarn javax.activation.**

#-libraryjars libs/mail.jar
#-libraryjars libs/activation.jar
#-libraryjars libs/additionnal.jar

#-keep public class Mail {*;}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes *JavascriptInterface*
-keepattributes *Annotation*


# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}
