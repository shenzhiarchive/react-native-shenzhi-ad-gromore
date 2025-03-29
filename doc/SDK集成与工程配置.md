接入须知
特别注意事项：

68版本起需同时更新SDK和Adapter，即68 SDK版本不再兼容历史Adapter版本
68版本起，CSJ SDK仅支持minSdkVersion为24，即兼容的最小手机系统版本为7.0，7.0以下手机运行CSJ SDK将会初始化失败、广告加载不成功，另外minSdkVersion相关工程编译问题可以参考如下如开发者主项目minSdkVersion >= 24，可正常使用，忽略该项如开发者主项目minSdkVersion < 24，编译过程遇到 Manifest merger failed : uses-sdk:minSdkVersion xx cannot be smaller than version 24 declared in library csj相关库 /Users/xxx/AndroidManifest.xml问题，可参考以下2种解决方案，选择其一即可，也可参考官方解决。
    (1) 修改主项目的最低版本minSdkVersion为24即可解决
    (2) 主项目AndroidManifest.xml调整tools:overrideLibrary标签解决，如果开发者主项目清单已经有tools.overrideLibrary标签，则在已有包名后，增加csj相关库名，并以逗号隔开；如果主项目清单没有该标签，则新增该标签再添加相关库名，举例如下：
聚合媒体需添加库名：
com.bytedance.gromore,com.bytedance.sdk.openadsdk,com.bytedance.msdk.adapter.admob,com.bytedance.msdk.adapter.BaiduMobAds,com.bytedance.msdk.adapter.gdt,com.bytedance.msdk.adapter.ks,com.bytedance.msdk.adapter.mintegral,com.bytedance.msdk.adapter.sigmob,com.bytedance.msdk.adapter.unity,com.bytedance.msdk.adapter.xiaomi,com.bytedance.tools

示例代码：

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    
    ...
    //媒体之前已有tools:overrideLibrary
    <uses-sdk tools:overrideLibrary="com.aaa.bbb，com.ccc.ddd" /> 
    //调整后的，新增了com.bytedance.gromore,com.bytedance.sdk.openadsdk,com.bytedance.tools等相关库
    <uses-sdk tools:overrideLibrary="com.aaa.bbb，com.ccc.ddd， com.bytedance.gromore,com.bytedance.sdk.openadsdk,com.bytedance.msdk.adapter.admob,com.bytedance.msdk.adapter.BaiduMobAds,com.bytedance.msdk.adapter.gdt,com.bytedance.msdk.adapter.ks,com.bytedance.msdk.adapter.mintegral,com.bytedance.msdk.adapter.sigmob,com.bytedance.msdk.adapter.unity,com.bytedance.msdk.adapter.xiaomi,com.bytedance.tools"/> 
    
    //媒体之前无tools:overrideLibrary，则新建
    <uses-sdk tools:overrideLibrary="com.aaa.bbb，com.ccc.ddd， com.bytedance.gromore,com.bytedance.sdk.openadsdk,com.bytedance.msdk.adapter.admob,com.bytedance.msdk.adapter.BaiduMobAds,com.bytedance.msdk.adapter.gdt,com.bytedance.msdk.adapter.ks,com.bytedance.msdk.adapter.mintegral,com.bytedance.msdk.adapter.sigmob,com.bytedance.msdk.adapter.unity,com.bytedance.msdk.adapter.xiaomi,com.bytedance.tools"/> 
    
    ...
    
</manifest>
```
① 接入注意事项：https://bytedance.feishu.cn/docx/EAeid081toKzlmx3gCJc41cJnFg

② 为了提升开发者日常接入效率及降低线上稳定性等异常问题发生频率，融合SDK5361及以上版本支持在编译期内自动更新adapter功能，避免由于融合SDK版本与adapter版本接入不匹配带来的某些异常场景。

具体的接入流程可参考：Android自动拉取adapter说明文档     iOS自动拉取adapter说明文档

SDK版本说明列表
📢标黄为本次更新的ADN版本，可重点关注

ADN

SDK版本

adapter版本

穿山甲 SDK

open_ad_sdk_v6.7.0.6

/

admob SDK

<= com.google.android.gms:play-services-ads:17.2.0(暂不支持Androidx版本)

admob:17.2.0.65

gdt SDK

>=com.qq.e.union:union:4.611.1481, 当前支持的最新版本：com.qq.e.union:union:4.611.1481

gdt:4.611.1481.0

Unity SDK

== unity-ads-4.3.0

unity:4.3.0.32

baidu SDK

Baidu_MobAds_SDK_v9.17~v9.37, 当前支持的最新版本：Baidu_MobAds_SDK_v9.37

baidu:9.37.3



注意：sdk版本与adapter版本需匹配使用，不然可能存在兼容性问题

ks SDK

kssdk-ad-3.3.17 ~kssdk-ad-3.3.71.3 , 当前支持的最新版本：kssdk-ad-3.3.71.3

ks:3.3.71.3.0

Sigmob SDK

== windAd-4.19.5

sigmob:4.19.5.1

sigmob_common:1.7.2



注意：wind-sdk 和 common版本必须匹配使用，否则可能出现兼容性问题。

Mintegral SDK

== MAL_16.6.57

mintegral:16.6.57.8



aar集成
下载SDK的压缩包，解压后穿山甲Android_sdk_x.x.x.x_双架构的文件夹里面会有以下内容：

目录/文件

说明

open_ad_sdk_xxxx.aar

融合SDK aar（包含穿山甲以及聚合请求广告功能，如果只接入穿山甲广告，那么只需要此包即可），放置开发工程的libs目录下



融合SDK的基础包，必须引入；

xxx_adapter_xxx_.aar









ADN对应的adapter，将aar放置开发工程的libs目录下；



//unity的adapter

unity_adapter_x.x.x.xx.aar  



//sigmob的adapter

sigmob_adapter_x.xx.x.x.aar 



//mintegral的adapter

mintegral_adapter_xx.x.xx.x.aar



//ks的adapter  

ks_adapter_x.x.xx.x.aar



//klevin的adapter

klevin_adapter_x.xx.x.x.x.aar



//gdt的adapter

gdt_adapter_x.xxx.xxxx.x.aar 



//baidu的adapter

baiduMobAds_adapter_x.xx.x.aar 



//admob的adapter

admob_adapter_xx.x.x.xx.aar 

demo

demo项目源码



mediation目录下为融合demo事例源码

demo_xxxxx.apk

可直接安装的demo

mediation_sdk_test_tools.aar

融合SDK测试工具，可单独测试各家ADN的广告，放置开发工程的libs目录下



接入过程中可辅助开发者提前发现问题，不可带到线上



V>=5.3.6.1，测试工具合并，仅提供tools-release.aar包含聚合和CSJ测试功能，mediation_sdk_test_tools.aar不再单独提供且不能与tools-release.aar共用；



tools-release.aar

穿山甲测试工具，可单独测试代码位广告，放置开发工程的libs目录下



接入过程中可辅助开发者提前发现问题，不可带到线上

whiteList.txt

资源白名单，不可混淆

备注：融合SDK支持接入多家ADN,开发者可以按需接入对应的ADN的SDK和adapter

```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    
    //融合基础包，必须引入
     implementation(name: "open_ad_sdk_6.7.0.6", ext: 'aar')
      
    //ks
    implementation(name: "mediation_ks_adapter_3.3.71.3.0", ext: 'aar')
    implementation(name: "kssdk-ad-3.3.71.3", ext: 'aar')
      
    //admob 
    implementation(name: "mediation_admob_adapter_17.2.0.65", ext: 'aar')
    implementation("com.google.android.gms:play-services-ads:17.2.0") {
        exclude group: 'com.android.support'
    }
      
    //baidu
    implementation(name: "mediation_baidu_adapter_9.37.3", ext: 'aar')
    implementation(name: "Baidu_MobAds_SDK_v9.37", ext: 'aar')
      
    //gdt
    implementation(name: "mediation_gdt_adapter_4.611.1481.0", ext: 'aar')
    implementation(name: "GDTSDK.unionNormal.4.611.1481", ext: 'aar')
      
    //mintegral
    implementation "mediation_mintegral_adapter_16.6.57.8", ext: 'aar')
    implementation(name: "mbridge_videojs_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbjscommon_16.6.57", ext: 'aar')
    implementation(name: "mbridge_playercommon_16.6.57", ext: 'aar')
    implementation(name: "mbridge_reward_16.6.57", ext: 'aar')
    implementation(name: "mbridge_videocommon_16.6.57", ext: 'aar')
    implementation(name: "mbridge_chinasame_16.6.57", ext: 'aar')
    implementation(name: "mbridge_interstitialvideo_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbnative_16.6.57", ext: 'aar')
    implementation(name: "mbridge_nativeex_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbnativeadvanced_16.6.57", ext: 'aar')
    implementation(name: "mbridge_interstitial_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbbanner_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbsplash_16.6.57", ext: 'aar')
    implementation(name: "mbridge_mbbid_16.6.57", ext: 'aar')
    implementation(name: "mbridge_newinterstitial_16.6.57", ext: 'aar')
      
    //sigmob
    implementation(name: "mediation_sigmob_adapter_4.19.5.1", ext: 'aar')
    implementation(name: "windAd-4.19.5", ext: 'aar')
    implementation(name: "windAd-common-1.7.2", ext: 'aar') // wind-sdk 和 common版本必须匹配使用，不然可能存在兼容性问题
      
    //unity
    implementation(name: "mediation_unity_adapter_4.3.0.32", ext: 'aar')
    implementation(name: "unity-ads-4.3.0", ext: 'aar')
    
​
    //测试工具,不可带到线上
    implementation(name: 'tools-release', ext: 'aar')
​
}
```
添加权限
融合SDK所需权限
```
<!-- 所需权限 -->   
<uses-permission android:name="android.permission.INTERNET" /> 
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
```
申请以下权限用于防作弊功能以及有助于广告平台投放广告
```
<!--可选权限，申请后用于防作弊功能以及有助于广告平台投放广告-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />   
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" /> 
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<permission 
    android:name="${applicationId}.openadsdk.permission.TT_PANGOLIN"   
    android:protectionLevel="signature" /> 
<uses-permission android:name="${applicationId}.openadsdk.permission.TT_PANGOLIN" />  
  
 <!--建议添加“query_all_package”权限，穿山甲将通过此权限在Android R系统上判定广告对应的应用是否在用户的app上安装，避免投放错误的广告，以此提高用户的广告体验。若添加此权限，需要在您的用户隐私文档中声明！ -->
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" /> 
```
聚合三方ADN可选权限
```
<!-- 可选权限 -->  
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />    
  
<!--suppress DeprecatedClassUsageInspection -->    
<uses-permission android:name="android.permission.GET_TASKS" />    
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />  
```
Demo可选权限
```
    <!--demo场景用到的权限，不是必须的-->
    <uses-permission android:name="android.permission.RECEIVE_USER_PRESENT" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />
```
AndroidManifest及资源文件配置
📢根据项目实际依赖各家ADN情况配置AndroidManifest文件

CSJ广告

AndroidManifest配置
```
        <!-- 穿山甲 start================== -->
        <provider
            android:name="com.bytedance.sdk.openadsdk.TTFileProvider"
            android:authorities="${applicationId}.TTFileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/pangle_file_paths" />
        </provider>
​
        <provider
            android:name="com.bytedance.sdk.openadsdk.multipro.TTMultiProvider"
            android:authorities="${applicationId}.TTMultiProvider"
            android:exported="false" />
        <!-- 穿山甲 end================== -->
```
在res/xml目录下，添加文件pangle_file_paths.xml
```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="tt_external_root" path="." />
    <external-path name="tt_external_download" path="Download" />
    <external-files-path name="tt_external_files_download" path="Download" />
    <files-path name="tt_internal_file_download" path="Download" />
    <cache-path name="tt_internal_cache_download" path="Download" />
</paths>
​```
Admob广告

AndroidManifest配置
value值需配置admob后台创建的应用的ID
```
        <!-- admob end================== -->
        <provider
            android:name="com.google.android.gms.ads.MobileAdsInitProvider"
            android:authorities="${applicationId}.mobileadsinitprovider"
            tools:replace="android:authorities" />
​
        <!-- Sample AdMob App ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-3940256099942544~3347511713" />
        <!--This meta-data tag is required to use Google Play Services.-->
        <!-- admob end================== -->
```
baidu广告

AndroidManifest配置
```
        <!-- baidu start================== -->
        <!-- 声明打开落地页的Activity（不建议修改主题配置）-->
        <activity
            android:name="com.baidu.mobads.sdk.api.AppActivity"
            android:configChanges="screenSize|keyboard|keyboardHidden|orientation"
            android:theme="@android:style/Theme.NoTitleBar" />
        <!-- 声明打开显示激励视频/全屏视频的Activity-->
        <activity
            android:name="com.baidu.mobads.sdk.api.MobRewardVideoActivity"
            android:configChanges="screenSize|orientation|keyboardHidden"
            android:launchMode="singleTask"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
​
        <!-- 如果targetSdkVersion设置值>=24，则强烈建议添加以下provider，否则会影响app变现 -->
        <!-- android:authorities="${packageName}.bd.provider" authorities中${packageName}部分必须替换成app自己的包名 -->
        <!-- 原来的FileProvider在新版本中改为BdFileProvider,继承自v4的FileProvider,需要在应用内引用support-v4包 -->
        <provider
            android:name="com.baidu.mobads.sdk.api.BdFileProvider"
            android:authorities="${applicationId}.bd.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/bd_file_paths" />
        </provider>
        <!-- baidu end================== -->
​```
在res/xml目录下，添加文件 bd_file_paths.xml

```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
     <external-path name="bd_lv_path" path="/" />
     <external-files-path name="bdpath" path="bddownload/" />
     <external-path name="bdpathsd" path="bddownload/" />
     <files-path name="bd_files_path" path="bddownload/" />
     <cache-path name="bd_cache_path" path="bddownload/" />
</paths>
​```
​
gdt广告

AndroidManifest配置
```
        <!-- GDT start================== -->
        <!-- targetSDKVersion >= 24时才需要添加这个provider。provider的authorities属性的值为${applicationId}.fileprovider，请开发者根据自己的${applicationId}来设置这个值，例如本例中applicationId为"com.qq.e.union.demo"。 -->
        <provider
            android:name="com.qq.e.comm.GDTFileProvider"
            android:authorities="${applicationId}.gdt.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/gdt_file_path" />
        </provider>
​
        <activity
            android:name="com.qq.e.ads.PortraitADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
            android:screenOrientation="portrait" />
        <activity
            android:name="com.qq.e.ads.LandscapeADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
            android:screenOrientation="landscape"
            tools:replace="android:screenOrientation" />
​
        <!-- 声明SDK所需要的组件 -->
        <service
            android:name="com.qq.e.comm.DownloadService"
            android:exported="false" />
        <!-- 请开发者注意字母的大小写，ADActivity，而不是AdActivity -->
​
        <activity
            android:name="com.qq.e.ads.ADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />
        <!-- GDT end================== -->
​```
在res/xml目录下，添加文件 gdt_file_path.xml
```
<paths>
    <!-- 这个下载路径也不可以修改，必须为com_qq_e_download -->
    <external-cache-path
        name="gdt_sdk_download_path1"
        path="com_qq_e_download" />
    <cache-path
        name="gdt_sdk_download_path2"
        path="com_qq_e_download" />
</paths>
​```
​
​
Mintegral广告

AndroidManifest配置
```
        <!-- mintegral start================== -->
        <provider
            android:name="com.mbridge.msdk.foundation.tools.MBFileProvider"
            android:authorities="${applicationId}.mbFileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/mb_provider_paths" />
        </provider>
        <!-- mintegral end================== -->
​
在res/xml目录下，添加文件 mb_provider_paths.xml
```
    <?xml version="1.0" encoding="utf-8"?>
    <paths>
        <external-path
            name="external_files"
            path="." />
    </paths>
```
Sigmob广告

AndroidManifest配置
如仅支持Android Support V4环境, 请将一下代码添加到AndroidManifest.xml中
```
<manifest>
​
    <application>
      
        <provider
            android:name="com.sigmob.sdk.SigmobFileV4Provider"
            android:authorities="${applicationId}.sigprovider"
            android:exported="false"
            android:initOrder="200"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/sigmob_provider_paths"/>
        </provider>
​
    </application>
​
​
</manifest>
```
如不需要支持Android Support V4环境
```
        <!-- sigmob start================== -->
        <activity
            android:name="com.sigmob.sdk.base.common.AdActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
            android:theme="@style/sig_transparent_style" />
​
        <provider
            android:name="com.sigmob.sdk.SigmobFileProvider"
            android:authorities="${applicationId}.sigprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/sigmob_provider_paths" />
        </provider>
        <!-- sigmob end================== -->
​```
在res/xml目录下，添加文件 sigmob_provider_paths.xml
```
<paths>
    <!-- 这个下载路径不可以修改，SigDownload -->
    <external-cache-path
        name="SigMob_root"
        path="SigDownload" />
    <external-path
        name="SigMob_root_external"
        path="." />
</paths>
 ```
KS广告

截止到目前不需要单独配置，实际配置情况建议参照ADN官方文档说明为准
添加混淆
如果您需要使用proguard混淆代码，需确保不要混淆SDK的代码。 请在proguard-rules.pro文件(或其他混淆文件)尾部添加如下配置:
SDK包中whiteList.txt 白名单上的资源不支持混淆
```
//聚合混淆
-keep class bykvm*.**
-keep class com.bytedance.msdk.adapter.**{ public *; }
-keep class com.bytedance.msdk.api.** {
 public *;
}
-keep class com.bytedance.msdk.base.TTBaseAd{*;}
-keep class com.bytedance.msdk.adapter.TTAbsAdLoaderAdapter{
    public *;
    protected <fields>;
}
​
# baidu sdk 不接入baidu sdk可以不引入
-ignorewarnings
-dontwarn com.baidu.mobads.sdk.api.**
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
​
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
​
-keep class com.baidu.mobads.** { *; }
-keep class com.style.widget.** {*;}
-keep class com.component.** {*;}
-keep class com.baidu.ad.magic.flute.** {*;}
-keep class com.baidu.mobstat.forbes.** {*;}
​
#ks  不接入ks sdk可以不引入
-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**
​
# Admob 不接入admob sdk可以不引入
-keep class com.google.android.gms.ads.MobileAds {
 public *;
}
​
#sigmob  不接入sigmob sdk可以不引入
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
​
-keep class sun.misc.Unsafe { *; }
-dontwarn com.sigmob.**
-keep class com.sigmob.**.**{*;}
​
#oaid 不同的版本混淆代码不太一致，你注意你接入的oaid版本 ，不接入oaid可以不添加
-dontwarn com.bun.**
-keep class com.bun.** {*;}
-keep class a.**{*;}
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class com.zui.opendeviceidlibrary.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
​
​
#Mintegral 不接入Mintegral sdk，可以不引入
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }
```
支持架构
SDK默认支持armeabi-v7a,arm64-v8a两种架构，如果有其他架构（armeabi架构）需求，请联系技术支持同学；

您可以在应用中的build.gradle中使用abiFilters选择支持的架构。如下所示：

```
ndk { // 设置支持的 SO 库构架，注意这里要根据你的实际情况来设置 
   abiFilters  armeabi-v7a ,  arm64-v8a
}
```