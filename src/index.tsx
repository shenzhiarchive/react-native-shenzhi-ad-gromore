import ShenzhiAdGromore from './NativeShenzhiAdGromore';
import type { InitConfig, InitCallback, PrivacyConfig } from './NativeShenzhiAdGromore';

export function multiply(a: number, b: number): number {
  return ShenzhiAdGromore.multiply(a, b);
}

/**
 * 初始化穿山甲广告SDK
 * @param config 初始化配置
 * @param callback 初始化结果回调
 */
export function initSDK(config: InitConfig, callback?: InitCallback): void {
  ShenzhiAdGromore.initSDK(config, callback || {});
}

// 导出类型定义
export type { InitConfig, InitCallback, PrivacyConfig };
