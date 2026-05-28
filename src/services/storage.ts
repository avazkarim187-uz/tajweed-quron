import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import { UserProgress, RecordingResult, AppSettings } from '../data/models';

export enum StorageKeys {
  PROGRESS = '@tajweed_progress',
  API_KEY = '@tajweed_api_key',
  RECORDINGS = '@tajweed_recordings',
  SETTINGS = '@tajweed_settings',
  THEME_MODE = '@tajweed_theme_mode',
}

const defaultSettings: AppSettings = {
  themeMode: 'system',
  apiKey: null,
  notificationsEnabled: false,
  dailyReminderTime: null,
};

export async function saveProgress(progress: UserProgress[]): Promise<void> {
  await AsyncStorage.setItem(StorageKeys.PROGRESS, JSON.stringify(progress));
}

export async function getProgress(): Promise<UserProgress[]> {
  const data = await AsyncStorage.getItem(StorageKeys.PROGRESS);
  if (!data) return [];
  return JSON.parse(data) as UserProgress[];
}

export async function saveApiKey(key: string): Promise<void> {
  await SecureStore.setItemAsync('tajweed_api_key', key);
}

export async function getApiKey(): Promise<string | null> {
  return SecureStore.getItemAsync('tajweed_api_key');
}

export async function deleteApiKey(): Promise<void> {
  await SecureStore.deleteItemAsync('tajweed_api_key');
}

export async function saveRecordingResult(result: RecordingResult): Promise<void> {
  const existing = await getRecordingResults();
  existing.push(result);
  await AsyncStorage.setItem(StorageKeys.RECORDINGS, JSON.stringify(existing));
}

export async function getRecordingResults(): Promise<RecordingResult[]> {
  const data = await AsyncStorage.getItem(StorageKeys.RECORDINGS);
  if (!data) return [];
  return JSON.parse(data) as RecordingResult[];
}

export async function saveSettings(settings: AppSettings): Promise<void> {
  await AsyncStorage.setItem(StorageKeys.SETTINGS, JSON.stringify(settings));
}

export async function getSettings(): Promise<AppSettings> {
  const data = await AsyncStorage.getItem(StorageKeys.SETTINGS);
  if (!data) return defaultSettings;
  return JSON.parse(data) as AppSettings;
}

export async function clearAll(): Promise<void> {
  const keys = Object.values(StorageKeys);
  await AsyncStorage.multiRemove(keys);
  await SecureStore.deleteItemAsync('tajweed_api_key');
}
