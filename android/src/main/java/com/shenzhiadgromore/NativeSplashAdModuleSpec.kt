package com.shenzhiadgromore

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReadableMap

abstract class NativeSplashAdModuleSpec(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {
  /**
   * 加载开屏广告
   * @param config 配置参数
   * @param callback 回调
   */
  abstract fun loadSplashAd(config: ReadableMap, callback: ReadableMap)
  
  /**
   * 展示开屏广告
   * @param callback 回调
   */
  abstract fun showSplashAd(callback: ReadableMap)
  
  /**
   * 检查缓存广告是否可用
   * @param callback 回调
   */
  abstract fun isSplashAdCacheAvailable(callback: ReadableMap)
  
  /**
   * 销毁开屏广告
   */
  abstract fun destroySplashAd()
  
  /**
   * 获取广告的eCPM信息
   * @param callback 回调
   */
  abstract fun getEcpmInfo(callback: ReadableMap)
  
  /**
   * 获取广告的详细信息
   * @param callback 回调
   */
  abstract fun getAdInfo(callback: ReadableMap)
  
  /**
   * 检查广告是否准备好展示
   * @param callback 回调
   */
  abstract fun isAdReady(callback: ReadableMap)
} 