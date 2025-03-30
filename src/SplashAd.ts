import { NativeModules, Platform } from 'react-native';

const { ShenzhiAdGromore } = NativeModules;

// 开屏广告配置
export interface SplashAdConfig {
  // 聚合广告位ID
  codeId: string;
  // 是否使用缓存
  useCache?: boolean;
  // 广告超时时间(ms)，默认3500ms
  timeOut?: number;
  // 宽高(px)
  width?: number;
  height?: number;
  // 是否开启预加载广告
  preLoad?: boolean;
}

// 开屏广告事件回调
export interface SplashAdCallback {
  // 广告加载成功回调
  onSplashLoadSuccess?: () => void;
  // 广告加载失败回调
  onSplashLoadFail?: (code: number, message: string) => void;
  // 广告渲染成功回调
  onSplashRenderSuccess?: () => void;
  // 广告渲染失败回调
  onSplashRenderFail?: (code: number, message: string) => void;
  // 广告展示回调
  onSplashAdShow?: () => void;
  // 广告点击回调
  onSplashAdClick?: () => void;
  // 广告关闭回调
  onSplashAdClose?: () => void;
}

/**
 * 开屏广告类
 */
class SplashAdManager {
  private static instance: SplashAdManager;
  private isLoading: boolean = false;
  
  /**
   * 获取单例
   */
  public static getInstance(): SplashAdManager {
    if (!SplashAdManager.instance) {
      SplashAdManager.instance = new SplashAdManager();
    }
    return SplashAdManager.instance;
  }
  
  /**
   * 加载并展示开屏广告
   * @param config 广告配置
   * @param callback 广告回调
   */
  public loadAndShowSplashAd(config: SplashAdConfig, callback?: SplashAdCallback): void {
    if (this.isLoading) {
      console.warn('SplashAd is already loading, please wait');
      return;
    }
    
    this.isLoading = true;
    
    // 处理回调
    const callbackWithCleanup: SplashAdCallback = {
      ...callback,
      onSplashRenderSuccess: () => {
        // 广告渲染成功后自动展示
        this._showSplashAd();
        callback?.onSplashRenderSuccess?.();
      },
      onSplashAdClose: () => {
        // 广告关闭后自动销毁并重置状态
        this._destroySplashAd();
        this.isLoading = false;
        callback?.onSplashAdClose?.();
        
        // 如果启用缓存，在广告关闭后预加载下一个广告
        if (config.useCache) {
          setTimeout(() => {
            this._preloadNextAd(config);
          }, 1000);
        }
      },
      onSplashLoadFail: (code, message) => {
        this.isLoading = false;
        callback?.onSplashLoadFail?.(code, message);
      },
      onSplashRenderFail: (code, message) => {
        this.isLoading = false;
        callback?.onSplashRenderFail?.(code, message);
      }
    };
    
    // 调用原生方法加载广告
    ShenzhiAdGromore.loadSplashAd(
      config,
      callbackWithCleanup || {}
    );
  }
  
  /**
   * 内部方法：显示开屏广告
   * @private
   */
  private _showSplashAd(): boolean {
    if (Platform.OS !== 'android') {
      console.warn('SplashAd is only supported on Android');
      return false;
    }
    return ShenzhiAdGromore.showSplashAd();
  }
  
  /**
   * 内部方法：销毁开屏广告
   * @private
   */
  private _destroySplashAd(): void {
    if (Platform.OS !== 'android') {
      return;
    }
    ShenzhiAdGromore.destroySplashAd();
  }
  
  /**
   * 内部方法：预加载下一个广告
   * @param config 广告配置
   * @private
   */
  private _preloadNextAd(config: SplashAdConfig): void {
    if (Platform.OS !== 'android' || this.isLoading) {
      return;
    }
    
    // 创建预加载配置，复用原有配置但不展示
    const preloadConfig: SplashAdConfig = {
      ...config,
      useCache: true,
      preLoad: true
    };
    
    // 简单的预加载回调
    const preloadCallback: SplashAdCallback = {
      onSplashLoadFail: (code, message) => {
        console.debug(`SplashAd preload failed: ${code}, ${message}`);
      }
    };
    
    // 调用原生方法预加载广告
    ShenzhiAdGromore.loadSplashAd(preloadConfig, preloadCallback);
  }
}

export default SplashAdManager.getInstance(); 