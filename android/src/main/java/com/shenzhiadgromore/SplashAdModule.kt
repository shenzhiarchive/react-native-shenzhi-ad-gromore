package com.shenzhiadgromore

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.CSJAdError
import com.bytedance.sdk.openadsdk.CSJSplashAd
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot
import com.facebook.react.bridge.UiThreadUtil
import java.util.*

@ReactModule(name = SplashAdModule.NAME)
class SplashAdModule(reactContext: ReactApplicationContext) : NativeSplashAdModuleSpec(reactContext) {

  private val TAG = "SplashAdModule"
  private val context = reactContext
  
  // 当前正在使用的开屏广告
  private var mSplashAd: CSJSplashAd? = null
  // 开屏广告回调
  private var mSplashAdCallback: ReadableMap? = null
  // 主线程Handler
  private val mHandler = Handler(Looper.getMainLooper())
  // 广告是否加载中
  private var mIsLoading = false
  // 广告是否已准备好
  private var mIsAdReady = false

  override fun getName(): String {
    return NAME
  }

  /**
   * 加载开屏广告，如果有预加载数据则自动展示
   */
  @ReactMethod
  override fun loadSplashAd(config: ReadableMap, callback: ReadableMap) {
    Log.d(TAG, "loadSplashAd called with config: $config")
    
    // 防止重复加载
    if (mIsLoading) {
      Log.w(TAG, "Ad is already loading, skipping this request")
      if (callback.hasKey("onSplashLoadFail")) {
        context.runOnUiQueueThread {
          val failCallback = callback.getMap("onSplashLoadFail")?.getDouble("__callbackID")?.toLong()
          if (failCallback != null) {
            val args = listOf(2002, "Ad is already loading")
            context.catalystInstance.invokeCallback(failCallback, args)
          }
        }
      }
      return
    }
    
    mIsLoading = true
    
    // 销毁旧的广告实例
    if (mSplashAd != null) {
      destroySplashAd()
    }
    
    // 保存回调
    mSplashAdCallback = callback
    
    try {
      // 检查广告位ID
      val codeId = config.getString("codeId")
      if (codeId.isNullOrEmpty()) {
        handleSplashAdFail(2001, "CodeId cannot be empty")
        mIsLoading = false
        return
      }
      
      // 获取超时时间
      val timeOut = if (config.hasKey("timeOut")) config.getInt("timeOut") else 3500
      
      // 获取屏幕尺寸
      val displayMetrics = DisplayMetrics()
      val activity = currentActivity ?: reactApplicationContext.currentActivity
      activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
      
      // 广告宽高
      val width = if (config.hasKey("width")) config.getInt("width") else displayMetrics.widthPixels
      val height = if (config.hasKey("height")) config.getInt("height") else displayMetrics.heightPixels
      
      // 始终使用预加载
      val forceUsePreload = true
      
      // 请求新广告
      UiThreadUtil.runOnUiThread {
        // 创建广告请求对象
        val ttAdNative = TTAdSdk.getAdManager().createAdNative(context)
        
        // 创建广告位配置
        val adSlotBuilder = AdSlot.Builder()
          .setCodeId(codeId)
          .setImageAcceptedSize(width, height)
        
        // 启用预加载
        adSlotBuilder.setMediationAdSlot(
          MediationAdSlot.Builder()
            .setSplashPreLoad(forceUsePreload)
            .build()
        )
        
        // 创建广告位
        val adSlot = adSlotBuilder.build()
        
        // 加载开屏广告
        ttAdNative.loadSplashAd(adSlot, object : TTAdNative.CSJSplashAdListener {
          override fun onSplashRenderSuccess(ad: CSJSplashAd) {
            Log.d(TAG, "Splash ad render success")
            mSplashAd = ad
            mIsAdReady = true
            mIsLoading = false
            
            // 处理渲染成功
            handleSplashAdRenderSuccess()
            
            // 广告渲染成功后自动展示
            showSplashAdInternal()
          }
          
          override fun onSplashLoadSuccess() {
            Log.d(TAG, "Splash ad load success")
            handleSplashAdLoadSuccess()
          }
          
          override fun onSplashLoadFail(error: CSJAdError) {
            Log.e(TAG, "Splash ad load fail: ${error.code}, ${error.msg}")
            mIsLoading = false
            mIsAdReady = false
            handleSplashAdFail(error.code, error.msg)
          }
          
          override fun onSplashRenderFail(ad: CSJSplashAd, error: CSJAdError) {
            Log.e(TAG, "Splash ad render fail: ${error.code}, ${error.msg}")
            mIsLoading = false
            mIsAdReady = false
            handleSplashAdRenderFail(error.code, error.msg)
          }
        }, timeOut)
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error during load splash ad", e)
      mIsLoading = false
      mIsAdReady = false
      handleSplashAdFail(2000, "Error during load splash ad: ${e.message}")
    }
  }
  
  /**
   * 内部方法：展示开屏广告
   */
  private fun showSplashAdInternal() {
    if (mSplashAd == null || !mIsAdReady) {
      Log.e(TAG, "Cannot show splash ad, ad not loaded yet or not ready")
      return
    }
    
    val activity = currentActivity ?: context.currentActivity
    if (activity == null) {
      Log.e(TAG, "Cannot show splash ad, activity is null")
      return
    }
    
    try {
      UiThreadUtil.runOnUiThread {
        // 获取内容容器
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        if (contentView == null) {
          Log.e(TAG, "Cannot show splash ad, content view is null")
          return@runOnUiThread
        }
        
        // 创建开屏广告容器
        val splashContainer = FrameLayout(activity)
        val params = FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        splashContainer.layoutParams = params
        
        // 添加广告容器到内容视图
        contentView.addView(splashContainer)
        
        // 设置广告监听器
        mSplashAd?.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
          override fun onSplashAdShow(ad: CSJSplashAd) {
            Log.d(TAG, "Splash ad show")
            handleSplashAdShow()
          }
          
          override fun onSplashAdClick(ad: CSJSplashAd) {
            Log.d(TAG, "Splash ad click")
            handleSplashAdClick()
          }
          
          override fun onSplashAdClose(ad: CSJSplashAd, closeType: Int) {
            Log.d(TAG, "Splash ad close: $closeType")
            
            // 从内容视图中移除广告容器
            contentView.removeView(splashContainer)
            
            // 处理广告关闭事件
            handleSplashAdClose()
            
            // 广告展示完成后，将广告标记为未就绪并销毁
            mIsAdReady = false
            destroySplashAd()
          }
        })
        
        // 获取开屏广告视图
        val splashView = mSplashAd?.getSplashView()
        if (splashView != null) {
          // 移除已有父视图
          val parent = splashView.parent as? ViewGroup
          parent?.removeView(splashView)
          
          // 添加到开屏容器
          splashContainer.removeAllViews()
          splashContainer.addView(splashView)
        } else {
          Log.e(TAG, "Splash view is null")
          contentView.removeView(splashContainer)
          return@runOnUiThread
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error showing splash ad", e)
    }
  }
  
  /**
   * 检查广告是否准备好展示
   * @param callback 回调
   */
  @ReactMethod
  override fun isAdReady(callback: ReadableMap) {
    Log.d(TAG, "isAdReady called")
    
    if (callback.hasKey("onComplete")) {
      context.runOnUiQueueThread {
        val completeCallback = callback.getMap("onComplete")?.getDouble("__callbackID")?.toLong()
        if (completeCallback != null) {
          val isReady = mSplashAd != null && mIsAdReady
          val args = listOf(isReady)
          context.catalystInstance.invokeCallback(completeCallback, args)
        }
      }
    }
  }
  
  /**
   * 销毁开屏广告
   */
  @ReactMethod
  override fun destroySplashAd() {
    Log.d(TAG, "destroySplashAd called")
    
    try {
      if (mSplashAd != null && mSplashAd?.getMediationManager() != null) {
        mSplashAd?.getMediationManager()?.destroy()
        mSplashAd = null
      }
      mIsAdReady = false
    } catch (e: Exception) {
      Log.e(TAG, "Error destroying splash ad", e)
    }
  }
  
  /**
   * 获取广告的eCPM信息
   * @param callback 回调
   */
  @ReactMethod
  override fun getEcpmInfo(callback: ReadableMap) {
    Log.d(TAG, "getEcpmInfo called")
    
    if (mSplashAd == null) {
      Log.e(TAG, "Cannot get eCPM info, ad not loaded yet")
      if (callback.hasKey("onFail")) {
        context.runOnUiQueueThread {
          val failCallback = callback.getMap("onFail")?.getDouble("__callbackID")?.toLong()
          if (failCallback != null) {
            val args = listOf(4001, "Ad not loaded yet")
            context.catalystInstance.invokeCallback(failCallback, args)
          }
        }
      }
      return
    }
    
    try {
      // 获取eCPM信息
      val ecpmLevel = mSplashAd?.getECPMLevel() ?: ""
      val multiPrice = mSplashAd?.getMultiBiddingECPM() ?: 0.0
      val cpm = mSplashAd?.getMediaECPM() ?: 0.0
      
      // 创建返回的eCPM信息对象
      val ecpmInfo = mapOf(
        "ecpmLevel" to ecpmLevel,
        "multiPrice" to multiPrice,
        "cpm" to cpm
      )
      
      // 返回eCPM信息
      if (callback.hasKey("onSuccess")) {
        context.runOnUiQueueThread {
          val successCallback = callback.getMap("onSuccess")?.getDouble("__callbackID")?.toLong()
          if (successCallback != null) {
            val args = listOf(ecpmInfo)
            context.catalystInstance.invokeCallback(successCallback, args)
          }
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error getting eCPM info", e)
      if (callback.hasKey("onFail")) {
        context.runOnUiQueueThread {
          val failCallback = callback.getMap("onFail")?.getDouble("__callbackID")?.toLong()
          if (failCallback != null) {
            val args = listOf(4000, "Error getting eCPM info: ${e.message}")
            context.catalystInstance.invokeCallback(failCallback, args)
          }
        }
      }
    }
  }
  
  /**
   * 获取广告的详细信息
   * @param callback 回调
   */
  @ReactMethod
  override fun getAdInfo(callback: ReadableMap) {
    Log.d(TAG, "getAdInfo called")
    
    if (mSplashAd == null) {
      Log.e(TAG, "Cannot get ad info, ad not loaded yet")
      if (callback.hasKey("onFail")) {
        context.runOnUiQueueThread {
          val failCallback = callback.getMap("onFail")?.getDouble("__callbackID")?.toLong()
          if (failCallback != null) {
            val args = listOf(5001, "Ad not loaded yet")
            context.catalystInstance.invokeCallback(failCallback, args)
          }
        }
      }
      return
    }
    
    try {
      // 获取广告的详细信息
      val adInfo = mutableMapOf<String, Any?>()
      
      // 基本信息
      adInfo["adId"] = mSplashAd?.getAdId() ?: ""
      adInfo["adNetworkName"] = mSplashAd?.getAdNetworkName() ?: ""
      adInfo["adRitId"] = mSplashAd?.getAdRitInfoAdnName() ?: ""
      adInfo["adSource"] = mSplashAd?.getAdSource() ?: ""
      adInfo["adType"] = mSplashAd?.getAdType() ?: 0
      
      // 互动信息
      adInfo["interactionType"] = mSplashAd?.getInteractionType() ?: 0
      adInfo["isExpress"] = mSplashAd?.isExpressAd() ?: false
      
      // eCPM信息
      adInfo["ecpmLevel"] = mSplashAd?.getECPMLevel() ?: ""
      adInfo["multiPrice"] = mSplashAd?.getMultiBiddingECPM() ?: 0.0
      adInfo["cpm"] = mSplashAd?.getMediaECPM() ?: 0.0
      
      // 素材类型
      adInfo["materialType"] = mSplashAd?.getMaterialType() ?: 0
      
      // 交互行为信息
      adInfo["hasCallToAction"] = mSplashAd?.hasCallToAction() ?: false
      adInfo["callToActionText"] = mSplashAd?.getCallToActionText() ?: ""
      
      // 广告规格尺寸
      adInfo["adSlotWidth"] = mSplashAd?.getAdSlotWidth() ?: 0
      adInfo["adSlotHeight"] = mSplashAd?.getAdSlotHeight() ?: 0
      
      // 返回广告信息
      if (callback.hasKey("onSuccess")) {
        context.runOnUiQueueThread {
          val successCallback = callback.getMap("onSuccess")?.getDouble("__callbackID")?.toLong()
          if (successCallback != null) {
            val args = listOf(adInfo)
            context.catalystInstance.invokeCallback(successCallback, args)
          }
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error getting ad info", e)
      if (callback.hasKey("onFail")) {
        context.runOnUiQueueThread {
          val failCallback = callback.getMap("onFail")?.getDouble("__callbackID")?.toLong()
          if (failCallback != null) {
            val args = listOf(5000, "Error getting ad info: ${e.message}")
            context.catalystInstance.invokeCallback(failCallback, args)
          }
        }
      }
    }
  }
  
  /**
   * 处理广告加载成功回调
   */
  private fun handleSplashAdLoadSuccess() {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashLoadSuccess")) {
      context.runOnUiQueueThread {
        val successCallback = mSplashAdCallback!!.getMap("onSplashLoadSuccess")?.getDouble("__callbackID")?.toLong()
        if (successCallback != null) {
          context.catalystInstance.invokeCallback(successCallback, null)
        }
      }
    }
  }
  
  /**
   * 处理广告加载失败回调
   */
  private fun handleSplashAdFail(code: Int, message: String) {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashLoadFail")) {
      context.runOnUiQueueThread {
        val failCallback = mSplashAdCallback!!.getMap("onSplashLoadFail")?.getDouble("__callbackID")?.toLong()
        if (failCallback != null) {
          val args = listOf(code, message)
          context.catalystInstance.invokeCallback(failCallback, args)
        }
      }
    }
  }
  
  /**
   * 处理广告渲染成功回调
   */
  private fun handleSplashAdRenderSuccess() {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashRenderSuccess")) {
      context.runOnUiQueueThread {
        val successCallback = mSplashAdCallback!!.getMap("onSplashRenderSuccess")?.getDouble("__callbackID")?.toLong()
        if (successCallback != null) {
          context.catalystInstance.invokeCallback(successCallback, null)
        }
      }
    }
  }
  
  /**
   * 处理广告渲染失败回调
   */
  private fun handleSplashAdRenderFail(code: Int, message: String) {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashRenderFail")) {
      context.runOnUiQueueThread {
        val failCallback = mSplashAdCallback!!.getMap("onSplashRenderFail")?.getDouble("__callbackID")?.toLong()
        if (failCallback != null) {
          val args = listOf(code, message)
          context.catalystInstance.invokeCallback(failCallback, args)
        }
      }
    }
  }
  
  /**
   * 处理广告展示回调
   */
  private fun handleSplashAdShow() {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashAdShow")) {
      context.runOnUiQueueThread {
        val callback = mSplashAdCallback!!.getMap("onSplashAdShow")?.getDouble("__callbackID")?.toLong()
        if (callback != null) {
          context.catalystInstance.invokeCallback(callback, null)
        }
      }
    }
  }
  
  /**
   * 处理广告点击回调
   */
  private fun handleSplashAdClick() {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashAdClick")) {
      context.runOnUiQueueThread {
        val callback = mSplashAdCallback!!.getMap("onSplashAdClick")?.getDouble("__callbackID")?.toLong()
        if (callback != null) {
          context.catalystInstance.invokeCallback(callback, null)
        }
      }
    }
  }
  
  /**
   * 处理广告关闭回调
   */
  private fun handleSplashAdClose() {
    if (mSplashAdCallback != null && mSplashAdCallback!!.hasKey("onSplashAdClose")) {
      context.runOnUiQueueThread {
        val callback = mSplashAdCallback!!.getMap("onSplashAdClose")?.getDouble("__callbackID")?.toLong()
        if (callback != null) {
          context.catalystInstance.invokeCallback(callback, null)
        }
      }
    }
  }

  companion object {
    const val NAME = "SplashAdModule"
  }
} 