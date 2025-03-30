/**
 * 这是NativeSpec文件，定义了与原生模块的接口规范
 */
import type { TurboModule } from 'react-native';
import type { Int32 } from 'react-native/Libraries/Types/CodegenTypes';

// 定义了ShenzhiAdGromore原生模块的接口
export interface Spec extends TurboModule {
  // 测试方法
  multiply(a: number, b: number): Promise<number>;
  
  // 初始化穿山甲SDK
  initSDK(config: Object, callback: Object): void;
}

// 定义了SplashAdModule原生模块的接口
export interface SplashSpec extends TurboModule {
  // 加载开屏广告
  loadSplashAd(config: Object, callback: Object): void;
  
  // 展示开屏广告
  showSplashAd(callback: Object): void;
  
  // 检查开屏广告缓存是否可用
  isSplashAdCacheAvailable(callback: Object): void;
  
  // 销毁开屏广告
  destroySplashAd(): void;
  
  // 获取广告的eCPM信息
  getEcpmInfo(callback: Object): void;
  
  // 获取广告的详细信息
  getAdInfo(callback: Object): void;
  
  // 检查广告是否准备好展示
  isAdReady(callback: Object): void;
} 