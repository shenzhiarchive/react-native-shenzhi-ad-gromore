# react-native-shenzhi-ad-gromore

React Native wrapper for Gromore Ad SDK

## Installation

```sh
npm install react-native-shenzhi-ad-gromore
```

## Usage


```js
import { multiply } from 'react-native-shenzhi-ad-gromore';

// ...

const result = multiply(3, 7);
```

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
