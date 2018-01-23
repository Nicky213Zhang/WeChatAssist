# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\studio-sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**
#3D 地图 V5.0.0之前：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.amap.api.trace.**{*;}

#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#拼音
-keep class com.hp.hpl.location.**{*;}
-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.fo
#选择城市
-keep class com.zaaach.citypicker.**{*;}
#侧滑
-keep class com.jeremyfeinstein.**{*;}
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
#xpose 混淆
-keep class vicmob.micropowder.hook.** {*;}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
#xposed
-keep class de.robv.android.xposed.** { *; }
-keep class android.* { *; }
#picasso
-keep class com.squareup.picasso.* {*;}
#====okhttp====
-keep class okhttp3.** {*;}
-keep class com.zhy.http.okhttp.** {*;}
-keep  class okio.* {*;}
-keep  class maven.com.squareup.okhttp3.okhttp.* {*;}
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**
#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class vicmob.earn.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep  class com.umeng.** {*;}
#动画
-keep  class android.support.** {*;}
-keep  class org.hamcrest.** {*;}
-keep  class com.j256.ormlite.** {*;}
-dontwarn org.apache.log4j.**
-keep  class org.apache.log4j.** {*;}
-keep  class jxl.** {*;}
-keep  class junit.** {*;}
-keep  class org.junit.** {*;}
-keep  class javax.annotation.** {*;}
-keep  class javax.inject.** {*;}
-keep  class com.squareup.javawriter.** {*;}
#自己
-keep  class vicmob.micropowder.daoman.bean.** {*;}

-keep  class java.** {*;}
-keep  class javax.** {*;}
-keep  class org.slf4j.** {*;}
-keep  class com.squareup.okhttp.** {*;}
-keep  class demo.** {*;}
-keep  class codehaus {*;}
-ignorewarnings
-keep class javax.ws.rs.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends javax.**
-keep class javax.lang.mode.** { *; }
-keep class javax.annotation.processing.** { *; }
-keep class javax.tools.** { *; }
-keep class javax.persistence.** { *; }
-keep class java.nio.file.** { *; }
-keep class jorg.codehaus.mojo.** { *; }
-keep class javax.annotation.processing.Processor
#OrmLite uses reflection

-keep class com.j256.**

-keepclassmembers  class com.j256.** { *; }

-keep  enum com.j256.**

-keepclassmembers  enum com.j256.** { *; }

-keep interface com.j256.**

-keepclassmembers  interface com.j256.** { *; }



-keepattributes  *Annotation*
-keepclassmembers class com.package.bo.** { *; }
-keepclassmembers class * { @com.j256.ormlite.field.DatabaseField *;}
-keepattributes Signature
-keep public class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keep public class * extends com.j256.ormlite.android.apptools.OpenHelperManager
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }
-dontwarn com.j256.ormlite.**
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.field.**
-dontwarn com.j256.ormlite.stmt.**
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}