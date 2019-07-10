#这里是热更新分包问题
#可参考https://blog.csdn.net/wangyun522/article/details/83017143
#tinker multidex keep patterns:
-keep public class * implements com.tencent.tinker.entry.ApplicationLifeCycle {
    <init>();
    void onBaseContextAttached(android.content.Context);
}

-keep public class * extends com.tencent.tinker.loader.TinkerLoader {
    <init>();
}

-keep public class * extends android.app.Application {
     <init>();
     void attachBaseContext(android.content.Context);
}

-keep class com.tencent.tinker.loader.TinkerTestAndroidNClassLoader {
    <init>();
}

#your dex.loader patterns here
-keep class tinker.sample.android.app.SampleApplication {
    <init>();
}

-keep class com.tencent.tinker.loader.** {
    <init>();
}