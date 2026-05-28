import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { ThemeProvider } from '../src/contexts/ThemeContext';
import { ProgressProvider } from '../src/contexts/ProgressContext';

export default function RootLayout() {
  return (
    <ThemeProvider>
      <ProgressProvider>
        <Stack
          screenOptions={{
            headerStyle: { backgroundColor: '#1B5E20' },
            headerTintColor: '#FFFFFF',
            headerTitleStyle: { fontWeight: 'bold' },
          }}
        >
          <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
          <Stack.Screen
            name="settings"
            options={{ title: 'Sozlamalar' }}
          />
          <Stack.Screen
            name="lesson/[id]"
            options={{ title: 'Dars' }}
          />
          <Stack.Screen
            name="quran/[surahNumber]"
            options={{ title: "Qur'on" }}
          />
          <Stack.Screen
            name="practice/[ruleId]"
            options={{ title: 'Mashq' }}
          />
          <Stack.Screen
            name="recording"
            options={{ title: 'Yozib olish' }}
          />
          <Stack.Screen
            name="feedback/[id]"
            options={{ title: 'Natija' }}
          />
        </Stack>
        <StatusBar style="light" />
      </ProgressProvider>
    </ThemeProvider>
  );
}
