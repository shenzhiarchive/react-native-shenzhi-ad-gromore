import { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
} from 'react-native';

import ShenzhiAdGromore from 'react-native-shenzhi-ad-gromore';
import type {
  SplashAdConfig,
  SplashAdCallback,
  EcpmInfo,
} from 'react-native-shenzhi-ad-gromore';

// 请替换为您的广告代码ID
const SPLASH_AD_CODE_ID = '103424257';

export function SplashAdDemo() {
  const [isInitialized, setIsInitialized] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [ecpmInfo, setEcpmInfo] = useState<EcpmInfo | null>(null);
  const [logs, setLogs] = useState<string[]>([]);

  // 添加日志
  const addLog = (log: string) => {
    setLogs((prevLogs) => [log, ...prevLogs]);
  };

  // 初始化SDK
  const initSDK = () => {
    try {
      // 替换为您的应用ID
      const appId = '5666682';

      ShenzhiAdGromore.initSDK({
        appId: appId,
        debug: true,
      });
      setIsInitialized(true);
      addLog('SDK初始化成功');
    } catch (error) {
      addLog(`SDK初始化失败: ${error}`);
    }
  };

  // 加载开屏广告
  const loadSplashAd = () => {
    if (!isInitialized) {
      addLog('请先初始化SDK');
      return;
    }

    setIsLoading(true);
    addLog('开始加载开屏广告');

    try {
      // 配置开屏广告
      const config: SplashAdConfig = {
        codeId: SPLASH_AD_CODE_ID,
        timeOut: 3500, // 超时时间
      };

      // 设置广告回调
      const callback: SplashAdCallback = {
        onSplashLoadSuccess: () => {
          addLog('开屏广告加载成功');
        },
        onSplashLoadFail: (code: number, message: string) => {
          setIsLoading(false);
          addLog(`开屏广告加载失败: ${code}, ${message}`);
        },
        onSplashRenderSuccess: () => {
          addLog('开屏广告渲染成功，即将自动展示');
          // 广告渲染成功后，自动获取eCPM信息
          getEcpmInfo();
        },
        onSplashRenderFail: (code: number, message: string) => {
          setIsLoading(false);
          addLog(`开屏广告渲染失败: ${code}, ${message}`);
        },
        onSplashAdShow: () => {
          addLog('开屏广告展示');
        },
        onSplashAdClick: () => {
          addLog('开屏广告被点击');
        },
        onSplashAdClose: () => {
          setIsLoading(false);
          addLog('开屏广告关闭');
        },
      };

      // 加载开屏广告
      ShenzhiAdGromore.loadSplashAd(config, callback);
    } catch (error) {
      setIsLoading(false);
      addLog(`加载开屏广告出错: ${error}`);
    }
  };

  // 获取eCPM信息
  const getEcpmInfo = async () => {
    try {
      addLog('正在获取eCPM信息...');
      const info = await ShenzhiAdGromore.getEcpmInfo();
      setEcpmInfo(info);
      addLog('获取eCPM信息成功');
    } catch (error) {
      if (
        error &&
        typeof error === 'object' &&
        'code' in error &&
        'message' in error
      ) {
        const { code, message } = error as { code: number; message: string };
        addLog(`获取eCPM信息失败: ${code}, ${message}`);
      } else {
        addLog(`获取eCPM信息出错: ${error}`);
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.scrollView}>
        <View style={styles.section}>
          <Text style={styles.sectionTitle}>穿山甲开屏广告演示</Text>

          {/* 控制按钮 */}
          <View style={styles.buttonContainer}>
            <TouchableOpacity
              style={[styles.button, isInitialized && styles.disabledButton]}
              disabled={isInitialized}
              onPress={initSDK}
            >
              <Text style={styles.buttonText}>初始化SDK</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.button, isLoading && styles.disabledButton]}
              disabled={!isInitialized || isLoading}
              onPress={loadSplashAd}
            >
              <Text style={styles.buttonText}>加载并展示开屏广告</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.button]}
              disabled={!isInitialized}
              onPress={getEcpmInfo}
            >
              <Text style={styles.buttonText}>获取eCPM信息</Text>
            </TouchableOpacity>
          </View>

          {/* eCPM信息 */}
          {ecpmInfo && (
            <View style={styles.infoContainer}>
              <Text style={styles.infoTitle}>eCPM信息:</Text>
              <Text style={styles.infoText}>
                eCPM级别: {ecpmInfo.ecpmLevel}
              </Text>
              <Text style={styles.infoText}>多竞价: {ecpmInfo.multiPrice}</Text>
              <Text style={styles.infoText}>CPM: {ecpmInfo.cpm}</Text>
            </View>
          )}

          {/* 日志显示 */}
          <View style={styles.logContainer}>
            <Text style={styles.logTitle}>操作日志:</Text>
            {logs.map((log, index) => (
              <Text key={index} style={styles.logText}>
                {log}
              </Text>
            ))}
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  scrollView: {
    flex: 1,
  },
  section: {
    padding: 16,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 16,
    color: '#333',
  },
  buttonContainer: {
    flexDirection: 'column',
    marginBottom: 16,
  },
  button: {
    backgroundColor: '#4285F4',
    padding: 12,
    borderRadius: 8,
    marginBottom: 12,
    alignItems: 'center',
  },
  disabledButton: {
    backgroundColor: '#A9A9A9',
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  infoContainer: {
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 8,
    marginBottom: 16,
  },
  infoTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#333',
  },
  infoText: {
    fontSize: 14,
    marginBottom: 4,
    color: '#666',
  },
  logContainer: {
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 8,
  },
  logTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#333',
  },
  logText: {
    fontSize: 14,
    marginBottom: 4,
    color: '#666',
  },
});
