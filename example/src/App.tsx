import React from 'react';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import SplashAdDemo from './SplashAdDemo';

const App: React.FC = () => {
  return (
    <SafeAreaProvider>
      <SplashAdDemo />
    </SafeAreaProvider>
  );
};

export default App;
