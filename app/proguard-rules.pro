-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别
-dontusemixedcaseclassnames         # 是否使用大小写混合
-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
-printmapping proguardMapping.txt
#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法
# 保留了继承自Activity、Application这些类的子类
# 因为这些子类有可能被外部调用
# 比如第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.widget.BaseAdapter {*;}
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
#AndroidX
-dontwarn androidx.**
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-keep class androidx.** {*;}
-keep class com.google.android.material.** {*;}
-keep interface androidx.** {*;}
-keep public class * extends androidx.**
#support
-dontwarn android.support.**
-dontwarn android.support.design.**
-keep class android.support.** {*;}
-keep class android.support.design.** { *; }
-keep interface android.support.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep public class * extends android.support.annotation.**
#support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
#support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.v7.**
# 保留Activity中的方法参数是view的方法，
# 从而我们在layout里面编写onClick就不会影响
-keepclassmembers class * extends android.app.Activity {
public void * (android.view.View);
}
# 枚举类不能被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(***);
*** get*();
void set*(***);
}
# 保留Parcelable序列化的类不能被混淆
-keep class * implements android.os.Parcelable{
public static final android.os.Parcelable$Creator *;
}
# 保留Serializable 序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
      private static final java.io.ObjectStreamField[] serialPersistentFields;
      !static !transient <fields>;
      !private <fields>;
      !private <methods>;
      private void writeObject(java.io.ObjectOutputStream);
      private void readObject(java.io.ObjectInputStream);
      java.lang.Object writeReplace();
      java.lang.Object readResolve();
}
# 对R文件下的所有类及其方法，都不能被混淆
-keep public class com.foresee.R$*{
public static final int *;
}
#避免资源混淆
-keep class **.R$* {*;}
# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * {
void *(**On*Event);
}
-keepclassmembers class * {
    void *(*Event);
}
#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keepattributes Signature# 避免混淆泛型
-keepattributes *Annotation*,InnerClasses
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable #运行抛出异常时保留代码行号
-keepattributes Exceptions # 解决AGPBI警告
-keepattributes JavascriptInterface #js
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment
# natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
#----------------------------- WebView(项目中没有可以忽略) -----------------------------
#webView需要进行特殊处理
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
#我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
-keepclassmembers class com.ljd.example.JSInterface {
    <methods>;
}
#回调函数
-keepclassmembers class * {
    void *(**On*Listener);
}
# 不混淆第三方引用的库
-dontskipnonpubliclibraryclasses
#---------------------------------业务组件实体类---------------------------------
#主体部分
-keep class com.ogh.support.databinding.** {*;}
-keep class com.ogh.support.bean.** {*;}
-keep class com.ogh.support.service.** {*;}
-keep class com.ogh.support.receiver.** {*;}
-keep class com.ogh.support.util.** {*;}
-keep class com.ogh.support.widget.** {*;}
#Frame框架部分
-keep class com.frame.bean.** {*;}
-keep class com.frame.util.** {*;}
-keep class com.frame.widget.** {*;}
-keep class com.frame.base.** {*;}
#---------------------------------第三方库及jar包-------------------------------
#Stream
-keep class java9.lang.** {*;}
-keep class java9.util.** {*;}
#com.goole
-keep class com.goole.gson.** {*;}
# Gson && protobuf
-dontwarn com.google.**
-keepattributes *Annotation*
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.qiancheng.carsmangersystem.**{*;}
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
# RxJava RxAndroid
-dontwarn rx.*
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQuene*Field*{
long producerIndex;
long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
rx.internal.util.atomic.LinkedQueueNode producerNode;
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.**
-keep class retrofit2.** { *; }
# OkHttp3
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.okhttp3.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-keep class com.squareup.okhttp.** { *; }
-keep class com.squareup.okhttp3.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
#AndroidUtilCode
-dontwarn com.blankj.utilcode.**
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
#immersionbar
-dontwarn com.gyf.immersionbar.**
-keep class com.gyf.immersionbar.* {*;}
#Aria
-dontwarn com.arialyy.aria.**
-keep class com.arialyy.aria.**{*;}
-keep class **$$DownloadListenerProxy{ *; }
-keep class **$$UploadListenerProxy{ *; }
-keep class **$$DownloadGroupListenerProxy{ *; }
-keep class **$$DGSubListenerProxy{ *; }
-keepclasseswithmembernames class * {
    @Download.* <methods>;
    @Upload.* <methods>;
    @DownloadGroup.* <methods>;
}
#PictureSelector
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep class com.luck.picture.lib.** { *; }
-keep interface com.yalantis.ucrop** { *; }
#支付宝支付
-dontwarn com.alipay.**
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-dontwarn HttpUtils.HttpFetcher
-keep class com.ut.*
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
#微信支付
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.stat.**
-keep class com.tencent.** { *;}
-keep class com.tencent.wxop.stat.**{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
#Butterknife
-dontwarn butterknife.internal.**
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#EventBus
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
# Bugly
-dontwarn com.tencent.bugly.**
-keep class com.tencent.bugly.** {*;}
#其他
-dontwarn org.xmlpull.v1.XmlPullParser
-dontwarn org.xmlpull.v1.XmlSerializer
-keep class org.xmlpull.v1.* {*;}
#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除
-keepclasseswithmembers class * {
    ... *JNI*(...);
}
-keepclasseswithmembernames class * {
	... *JRI*(...);
}
-keep class **JNI* {*;} 