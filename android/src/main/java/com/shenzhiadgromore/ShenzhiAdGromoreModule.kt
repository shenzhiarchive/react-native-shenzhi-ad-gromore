package com.shenzhiadgromore

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.Callback
import com.facebook.react.module.annotations.ReactModule
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTCustomController
import com.bytedance.sdk.openadsdk.mediation.MediationPrivacyConfig

@ReactModule(name = ShenzhiAdGromoreModule.NAME)
class ShenzhiAdGromoreModule(reactContext: ReactApplicationContext) :
  NativeShenzhiAdGromoreSpec(reactContext) {

  private val TAG = "ShenzhiAdGromore"
  private val context = reactContext

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }

  /**
   * 初始化穿山甲广告SDK
   * @param config 初始化配置
   * @param callback 回调
   */
  override fun initSDK(config: ReadableMap, callback: ReadableMap) {
    Log.d(TAG, "initSDK called with config: $config")
    
    try {
      // 从config中提取appId，必须参数
      val appId = config.getString("appId")
      if (appId.isNullOrEmpty()) {
        handleInitFail(callback, 1001, "AppId cannot be empty")
        return
      }
      
      // 构建TTAdConfig
      val builder = TTAdConfig.Builder()
        .appId(appId)
      
      // 可选参数设置
      if (config.hasKey("appName")) {
        config.getString("appName")?.let { builder.appName(it) }
      }
      
      if (config.hasKey("paid")) {
        builder.paid(config.getBoolean("paid"))
      }
      
      if (config.hasKey("keywords")) {
        config.getString("keywords")?.let { builder.keywords(it) }
      }
      
      if (config.hasKey("debug")) {
        builder.debug(config.getBoolean("debug"))
      }
      
      if (config.hasKey("supportMultiProcess")) {
        builder.supportMultiProcess(config.getBoolean("supportMultiProcess"))
      }
      
      if (config.hasKey("useMediation")) {
        builder.useMediation(config.getBoolean("useMediation"))
      }
      
      // 设置隐私控制
      if (config.hasKey("privacyConfig")) {
        val privacyConfig = config.getMap("privacyConfig")
        if (privacyConfig != null) {
          builder.customController(createTTCustomController(privacyConfig))
        }
      }
      
      // 初始化SDK
      TTAdSdk.init(context, builder.build())
      
      // 启动SDK
      TTAdSdk.start(object : TTAdSdk.Callback {
        override fun success() {
          // 初始化成功
          Log.d(TAG, "TTAdSdk init success")
          if (callback.hasKey("onSuccess")) {
            context.runOnUiQueueThread {
              val successCallback = callback.getMap("onSuccess")?.getDouble("__callbackID")?.toLong()
              if (successCallback != null) {
                context.catalystInstance.invokeCallback(successCallback, null)
              }
            }
          }
        }
        
        override fun fail(code: Int, msg: String) {
          // 初始化失败
          Log.e(TAG, "TTAdSdk init failed: code=$code, msg=$msg")
          handleInitFail(callback, code, msg)
        }
      })
    } catch (e: Exception) {
      Log.e(TAG, "Error during SDK initialization", e)
      handleInitFail(callback, 1000, "Error during SDK initialization: ${e.message}")
    }
  }
  
  /**
   * 处理初始化失败的情况
   */
  private fun handleInitFail(callback: ReadableMap, code: Int, msg: String) {
    if (callback.hasKey("onFail")) {
      context.runOnUiQueueThread {
        val failCallback = callback.getMap("onFail")?.getDouble("__callbackID")?.toLong()
        if (failCallback != null) {
          val args = listOf(code, msg)
          context.catalystInstance.invokeCallback(failCallback, args)
        }
      }
    }
  }
  
  /**
   * 创建隐私控制器
   */
  private fun createTTCustomController(privacyConfig: ReadableMap): TTCustomController {
    return object : TTCustomController() {
      override fun isCanUseLocation(): Boolean {
        return if (privacyConfig.hasKey("canUseLocation")) {
          privacyConfig.getBoolean("canUseLocation")
        } else {
          super.isCanUseLocation()
        }
      }
      
      override fun isCanUseAppList(): Boolean {
        return if (privacyConfig.hasKey("canUseAppList")) {
          privacyConfig.getBoolean("canUseAppList")
        } else {
          super.isCanUseAppList()
        }
      }
      
      override fun isCanUsePhoneState(): Boolean {
        return if (privacyConfig.hasKey("canUsePhoneState")) {
          privacyConfig.getBoolean("canUsePhoneState")
        } else {
          super.isCanUsePhoneState()
        }
      }
      
      override fun getDevImei(): String {
        return if (privacyConfig.hasKey("devImei")) {
          privacyConfig.getString("devImei") ?: super.getDevImei()
        } else {
          super.getDevImei()
        }
      }
      
      override fun isCanUseWriteExternal(): Boolean {
        return if (privacyConfig.hasKey("canUseWriteExternal")) {
          privacyConfig.getBoolean("canUseWriteExternal")
        } else {
          super.isCanUseWriteExternal()
        }
      }
      
      override fun getDevOaid(): String {
        return if (privacyConfig.hasKey("devOaid")) {
          privacyConfig.getString("devOaid") ?: super.getDevOaid()
        } else {
          super.getDevOaid()
        }
      }
      
      override fun isCanUseAndroidId(): Boolean {
        return if (privacyConfig.hasKey("canUseAndroidId")) {
          privacyConfig.getBoolean("canUseAndroidId")
        } else {
          super.isCanUseAndroidId()
        }
      }
      
      override fun getAndroidId(): String {
        return if (privacyConfig.hasKey("androidId")) {
          privacyConfig.getString("androidId") ?: super.getAndroidId()
        } else {
          super.getAndroidId()
        }
      }
      
      override fun getMediationPrivacyConfig(): MediationPrivacyConfig {
        return object : MediationPrivacyConfig() {
          override fun isCanUseOaid(): Boolean {
            return if (privacyConfig.hasKey("canUseOaid")) {
              privacyConfig.getBoolean("canUseOaid")
            } else {
              super.isCanUseOaid()
            }
          }
          
          override fun isLimitPersonalAds(): Boolean {
            return if (privacyConfig.hasKey("limitPersonalAds")) {
              privacyConfig.getBoolean("limitPersonalAds")
            } else {
              super.isLimitPersonalAds()
            }
          }
          
          override fun isProgrammaticRecommend(): Boolean {
            return if (privacyConfig.hasKey("programmaticRecommend")) {
              privacyConfig.getBoolean("programmaticRecommend")
            } else {
              super.isProgrammaticRecommend()
            }
          }
        }
      }
    }
  }

  companion object {
    const val NAME = "ShenzhiAdGromore"
  }
}
