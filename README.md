# react-native-shenzhi-ad-gromore

React Native wrapper for Gromore Ad SDK

## Installation

```sh
npm install react-native-shenzhi-ad-gromore
```

## Usage

### 初始化SDK

在应用启动时（如App.tsx或index.js中）初始化SDK：

```js
import { initSDK } from 'react-native-shenzhi-ad-gromore';

// 基本初始化
initSDK(
  {
    appId: '您的穿山甲AppID', // 必填参数
    useMediation: true, // 是否启用聚合功能
    debug: __DEV__, // 调试模式
    supportMultiProcess: true, // 支持多进程
  },
  {
    onSuccess: () => {
      console.log('穿山甲SDK初始化成功');
      // 初始化成功后才能加载广告
    },
    onFail: (code, message) => {
      console.error(`穿山甲SDK初始化失败: ${code}, ${message}`);
    },
  }
);

// 带隐私合规配置的初始化
initSDK(
  {
    appId: '您的穿山甲AppID',
    useMediation: true,
    debug: __DEV__,
    // 隐私控制配置
    privacyConfig: {
      canUseLocation: false, // 是否允许SDK使用地理位置
      canUsePhoneState: false, // 是否允许SDK获取手机参数
      canUseAndroidId: false, // 是否允许获取AndroidID
      canUseAppList: false, // 是否允许上报应用列表
      canUseWriteExternal: true, // 是否允许使用外部存储
      // 更多隐私设置...
    },
  },
  {
    onSuccess: () => console.log('初始化成功'),
    onFail: (code, message) => console.error('初始化失败', code, message),
  }
);
```

#### 完整初始化参数说明

##### InitConfig 参数

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | :--: | ---- |
| appId | string | ✅ | 应用ID，必填参数 |
| appName | string | ❌ | 应用名称 |
| paid | boolean | ❌ | 是否为计费用户 |
| keywords | string | ❌ | 用户画像关键词列表（多个关键词用逗号分隔） |
| debug | boolean | ❌ | 是否开启调试模式（日志输出/测试广告） |
| supportMultiProcess | boolean | ❌ | 是否支持多进程 |
| useMediation | boolean | ❌ | 是否启用聚合功能（聚合广告必开） |
| privacyConfig | PrivacyConfig | ❌ | 隐私配置信息，详见下表 |

##### PrivacyConfig 参数

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | :--: | ---- |
| canUseLocation | boolean | ❌ | 是否允许SDK主动使用地理位置信息 |
| canUseAppList | boolean | ❌ | 是否允许SDK上报手机APP安装列表 |
| canUsePhoneState | boolean | ❌ | 是否允许SDK主动获取手机硬件参数 |
| devImei | string | ❌ | 当canUsePhoneState=false时，可传入IMEI信息 |
| canUseWriteExternal | boolean | ❌ | 是否允许SDK使用WRITE_EXTERNAL_STORAGE权限 |
| devOaid | string | ❌ | 自定义OAID信息 |
| canUseAndroidId | boolean | ❌ | 是否允许SDK获取Android ID |
| androidId | string | ❌ | 自定义Android ID |
| canUseOaid | boolean | ❌ | 是否可以使用OAID |
| limitPersonalAds | boolean | ❌ | 是否限制个性化推荐接口 |
| programmaticRecommend | boolean | ❌ | 是否启用程序化广告推荐，true启用，false不启用 |

##### 回调参数

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | :--: | ---- |
| onSuccess | () => void | ❌ | 初始化成功回调 |
| onFail | (code: number, message: string) => void | ❌ | 初始化失败回调，返回错误码和错误信息 |

> **注意事项**:
> 
> 1. 初始化必须在主线程中调用，SDK会将初始化操作放在子线程执行
> 2. 请确保在初始化成功回调后再请求广告
> 3. 隐私合规是必要的，建议在获得用户同意后再进行初始化

### 开屏广告

开屏广告是在应用启动或页面切换时展示的全屏广告，通常在应用启动页（Splash页面）使用。

#### 使用方法

```js
import { SplashAd } from 'react-native-shenzhi-ad-gromore';

// 加载并展示开屏广告
SplashAd.loadAndShowSplashAd(
  {
    codeId: '您的开屏广告位ID', // 必填参数
    useCache: true, // 设置为true开启自动缓存功能，下次启动更快展示广告
    timeOut: 3500, // 超时时间(ms)，默认3500ms
  },
  {
    // 加载回调
    onSplashLoadSuccess: () => {
      console.log('开屏广告物料、素材加载成功');
    },
    onSplashLoadFail: (code, message) => {
      console.error(`开屏广告加载失败: ${code}, ${message}`);
    },
    
    // 渲染回调
    onSplashRenderSuccess: () => {
      console.log('开屏广告渲染成功，自动展示');
    },
    onSplashRenderFail: (code, message) => {
      console.error(`开屏广告渲染失败: ${code}, ${message}`);
    },
    
    // 广告事件回调
    onSplashAdShow: () => {
      console.log('开屏广告展示');
    },
    onSplashAdClick: () => {
      console.log('开屏广告被点击');
    },
    onSplashAdClose: () => {
      console.log('开屏广告关闭');
      // 无需手动销毁，内部自动处理
    },
  }
);
```

#### 自动缓存工作流程

当设置`useCache: true`时，开屏广告将自动缓存并管理广告生命周期：

1. 首次加载广告时，加载并展示广告
2. 广告展示完成后，自动销毁广告资源
3. 同时在后台自动开始预加载下一个广告，用于下次使用
4. 下次调用`loadAndShowSplashAd`时，如果有缓存的广告且在有效期内，将直接使用缓存广告

这种方式简化了开发者的使用流程，无需手动管理缓存逻辑和广告销毁。

#### 完整参数说明

##### SplashAdConfig 参数

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | :--: | ---- |
| codeId | string | ✅ | 聚合广告位ID |
| useCache | boolean | ❌ | 是否使用并自动管理缓存，默认false |
| timeOut | number | ❌ | 广告超时时间(ms)，默认3500ms |
| width | number | ❌ | 广告宽度(px)，默认屏幕宽度 |
| height | number | ❌ | 广告高度(px)，默认屏幕高度 |

##### SplashAdCallback 回调

| 回调名 | 参数 | 说明 |
| ------ | ---- | ---- |
| onSplashLoadSuccess | 无 | 广告物料、素材加载成功 |
| onSplashLoadFail | (code: number, message: string) | 广告物料、素材加载失败或超时 |
| onSplashRenderSuccess | 无 | 广告渲染成功，将自动展示 |
| onSplashRenderFail | (code: number, message: string) | 广告渲染失败 |
| onSplashAdShow | 无 | 广告展示 |
| onSplashAdClick | 无 | 广告被点击 |
| onSplashAdClose | 无 | 广告关闭（用户点击跳过/倒计时结束） |

> **注意事项**:
> 
> 1. 开屏广告宽高须符合：width >= 70% 屏幕宽，height >= 50% 屏幕高
> 2. 需确保SDK初始化成功后再加载广告
> 3. 广告销毁和缓存预加载已在内部处理，开发者无需关心
> 4. 目前仅支持Android平台

## Android配置

### 移除测试工具

在发布生产版本时，必须移除测试工具（tools-release.aar）。我们提供了几种方式来自动移除：

1. **命令行参数方式**：
   ```bash
   ./gradlew assembleRelease -PexcludeTestTools=true
   ```

2. **自动在Release构建中排除**：
   当执行任何包含"Release"或"release"的任务时，构建过程会自动忽略测试工具的依赖：
   ```bash
   ./gradlew assembleRelease
   ```

3. **手动执行移除任务**：
   也可以显式执行移除测试工具的任务：
   ```bash
   ./gradlew removeTestTools
   ```

> **重要提示**：测试工具仅用于开发阶段测试和调试，在提交生产版本前必须确保其被移除。


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
