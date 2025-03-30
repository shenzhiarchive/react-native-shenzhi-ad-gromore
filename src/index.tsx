import { NativeModules, Platform } from 'react-native';

// 定义类型
export interface SDKConfig {
  // 应用ID，必填
  appId: string;
  // 应用名称，可选
  appName?: string;
  // 是否是付费用户，默认false
  paid?: boolean;
  // 关键词，可选
  keywords?: string;
  // 是否开启调试模式，默认false
  debug?: boolean;
  // 是否支持多进程，默认false
  supportMultiProcess?: boolean;
  // 是否使用聚合SDK，默认true
  useMediation?: boolean;
  // 隐私合规配置
  privacyConfig?: PrivacyConfig;
}

// 隐私合规配置
export interface PrivacyConfig {
  // 是否允许获取位置信息
  canUseLocation?: boolean;
  // 是否允许获取应用列表
  canUseAppList?: boolean;
  // 是否允许获取手机状态
  canUsePhoneState?: boolean;
  // 设备IMEI
  devImei?: string;
  // 是否允许使用外部存储
  canUseWriteExternal?: boolean;
  // 设备OAID
  devOaid?: string;
  // 是否允许使用AndroidId
  canUseAndroidId?: boolean;
  // AndroidId
  androidId?: string;
  // 是否允许使用OAID
  canUseOaid?: boolean;
  // 是否限制个性化广告
  limitPersonalAds?: boolean;
  // 是否允许程序化推荐
  programmaticRecommend?: boolean;
}

// SDK回调
export interface SDKCallback {
  // 初始化成功回调
  onSuccess?: () => void;
  // 初始化失败回调
  onFail?: (code: number, message: string) => void;
}

/**
 * 开屏广告配置
 */
export interface SplashAdConfig {
  // 广告位ID
  codeId: string;
  // 超时时间，默认3500ms
  timeOut?: number;
  // 宽度，默认屏幕宽度
  width?: number;
  // 高度，默认屏幕高度
  height?: number;
  // 是否使用缓存
  useCache?: boolean;
  // 是否预加载
  preLoad?: boolean;
}

/**
 * 开屏广告回调
 */
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
 * eCPM信息
 */
export interface EcpmInfo {
  // eCPM等级
  ecpmLevel: string;
  // 多竞价eCPM（单位：分）
  multiPrice: number;
  // 媒体eCPM（单位：分）
  cpm: number;
}

/**
 * 广告详细信息
 */
export interface AdInfo {
  // 广告ID
  adId: string;
  // 广告平台名称
  adNetworkName: string;
  // 广告位ID
  adRitId: string;
  // 广告来源
  adSource: string;
  // 广告类型
  adType: number;
  // 交互类型
  interactionType: number;
  // 是否为Express广告
  isExpress: boolean;
  // eCPM等级
  ecpmLevel: string;
  // 多竞价eCPM（单位：分）
  multiPrice: number;
  // 媒体eCPM（单位：分）
  cpm: number;
  // 素材类型
  materialType: number;
  // 是否有行动按钮
  hasCallToAction: boolean;
  // 行动按钮文字
  callToActionText: string;
  // 广告位宽度
  adSlotWidth: number;
  // 广告位高度
  adSlotHeight: number;
}

// 原生模块接口
interface ShenzhiAdGromoreInterface {
  // 初始化SDK
  initSDK(config: SDKConfig, callback: Object): void;
  // 原始方法
  multiply(a: number, b: number): Promise<number>;
}

// 开屏广告模块接口
interface SplashAdModuleInterface {
  // 加载开屏广告
  loadSplashAd(config: SplashAdConfig, callback: Object): void;
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

// 获取原生模块
const NativeShenzhiAdGromore: ShenzhiAdGromoreInterface = NativeModules.ShenzhiAdGromore;
const NativeSplashAdModule: SplashAdModuleInterface = NativeModules.SplashAdModule;

// 检查模块是否存在
if (!NativeShenzhiAdGromore) {
  throw new Error(`穿山甲广告SDK模块未找到，请检查原生模块是否正确安装。
  Platform: ${Platform.OS}`);
}

/**
 * 创建回调对象
 * @param callbacks 回调对象
 * @returns 处理后的回调对象
 */
function createCallbackObject(callbacks: any): Object {
  const result: any = {};
  
  // 遍历回调对象
  for (const key in callbacks) {
    if (Object.prototype.hasOwnProperty.call(callbacks, key) && typeof callbacks[key] === 'function') {
      // 创建新的回调函数，将其绑定到原始对象
      result[key] = {
        __callbackID: callbacks[key],
      };
    }
  }
  
  return result;
}

// 导出模块
export default {
  // 初始化SDK
  initSDK(config: SDKConfig, callback: SDKCallback = {}): void {
    NativeShenzhiAdGromore.initSDK(config, createCallbackObject(callback));
  },

  // 加载开屏广告
  loadSplashAd(config: SplashAdConfig, callback: SplashAdCallback = {}): void {
    NativeSplashAdModule.loadSplashAd(config, createCallbackObject(callback));
  },

  // 展示开屏广告
  showSplashAd(callback: {
    onSuccess?: () => void;
    onFail?: (code: number, message: string) => void;
  } = {}): void {
    NativeSplashAdModule.showSplashAd(createCallbackObject(callback));
  },

  // 检查开屏广告缓存是否可用
  isSplashAdCacheAvailable(): Promise<boolean> {
    return new Promise((resolve) => {
      NativeSplashAdModule.isSplashAdCacheAvailable({
        onComplete: (available: boolean) => {
          resolve(available);
        },
      });
    });
  },

  // 销毁开屏广告
  destroySplashAd(): void {
    NativeSplashAdModule.destroySplashAd();
  },

  // 获取广告的eCPM信息
  getEcpmInfo(): Promise<EcpmInfo> {
    return new Promise((resolve, reject) => {
      NativeSplashAdModule.getEcpmInfo({
        onSuccess: (ecpmInfo: EcpmInfo) => {
          resolve(ecpmInfo);
        },
        onFail: (code: number, message: string) => {
          reject({ code, message });
        },
      });
    });
  },

  // 获取广告的详细信息
  getAdInfo(): Promise<AdInfo> {
    return new Promise((resolve, reject) => {
      NativeSplashAdModule.getAdInfo({
        onSuccess: (adInfo: AdInfo) => {
          resolve(adInfo);
        },
        onFail: (code: number, message: string) => {
          reject({ code, message });
        },
      });
    });
  },

  // 检查广告是否准备好展示
  isAdReady(): Promise<boolean> {
    return new Promise((resolve) => {
      NativeSplashAdModule.isAdReady({
        onComplete: (isReady: boolean) => {
          resolve(isReady);
        },
      });
    });
  },

  // 乘法示例方法
  multiply(a: number, b: number): Promise<number> {
    return NativeShenzhiAdGromore.multiply(a, b);
  },
};
