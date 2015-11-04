# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/minggong/sdk/tools/proguard/proguard-android.txt
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
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-dontwarn rx.internal.util.unsafe.**

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.analytics.** {*;}
-dontwarn com.umeng.analytics.**

-keep class org.apache.http.**{*;}
-dontwarn org.apache.http.**

-keep class com.baidu.autoupdatesdk.**{*;}
-dontwarn com.baidu.autoupdatesdk