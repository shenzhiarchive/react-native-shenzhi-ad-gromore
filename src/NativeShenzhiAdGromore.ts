import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

// 隐私控制选项
export interface PrivacyConfig {
  // 是否允许SDK主动使用地理位置信息
  canUseLocation?: boolean;
  // 是否允许SDK上报手机APP安装列表
  canUseAppList?: boolean;
  // 是否允许SDK主动获取手机硬件参数
  canUsePhoneState?: boolean;
  // 当canUsePhoneState=false时，可传入IMEI信息
  devImei?: string;
  // 是否允许SDK使用WRITE_EXTERNAL_STORAGE权限
  canUseWriteExternal?: boolean;
  // 自定义OAID信息
  devOaid?: string;
  // 是否允许SDK获取Android ID
  canUseAndroidId?: boolean;
  // 自定义Android ID
  androidId?: string;
  // 是否可以使用OAID
  canUseOaid?: boolean;
  // 是否限制个性化推荐接口
  limitPersonalAds?: boolean;
  // 是否启用程序化广告推荐
  programmaticRecommend?: boolean;
}

// 初始化配置
export interface InitConfig {
  // 必选参数，应用ID
  appId: string;
  // 可选参数，设置应用名称
  appName?: string;
  // 是否为计费用户
  paid?: boolean;
  // 用户画像关键词列表
  keywords?: string;
  // 是否开启调试模式
  debug?: boolean;
  // 是否支持多进程
  supportMultiProcess?: boolean;
  // 是否启用聚合功能
  useMediation?: boolean;
  // 隐私配置信息
  privacyConfig?: PrivacyConfig;
}

// 初始化结果回调
export interface InitCallback {
  onSuccess?: () => void;
  onFail?: (code: number, message: string) => void;
}

export interface Spec extends TurboModule {
  multiply(a: number, b: number): number;
  
  // 初始化SDK
  initSDK(config: InitConfig, callback: InitCallback): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('ShenzhiAdGromore');
