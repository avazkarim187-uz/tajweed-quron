import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TextInput, TouchableOpacity, Switch, Alert, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useTheme, ThemeMode } from '../src/contexts/ThemeContext';
import { Colors } from '../src/constants/colors';
import { saveApiKey, getApiKey, clearAll } from '../src/services/storage';

export default function SettingsScreen() {
  const { theme, themeMode, setThemeMode } = useTheme();
  const [apiKey, setApiKeyState] = useState('');
  const [apiKeyConfigured, setApiKeyConfigured] = useState(false);
  const [notifications, setNotifications] = useState(false);

  useEffect(() => {
    loadApiKey();
  }, []);

  async function loadApiKey() {
    const key = await getApiKey();
    if (key) {
      setApiKeyState(key);
      setApiKeyConfigured(true);
    }
  }

  async function handleSaveApiKey() {
    if (apiKey.trim()) {
      await saveApiKey(apiKey.trim());
      setApiKeyConfigured(true);
      Alert.alert("Saqlandi", "API kalit muvaffaqiyatli saqlandi");
    }
  }

  function handleClearData() {
    Alert.alert(
      "Ma'lumotlarni tozalash",
      "Barcha ma'lumotlar o'chiriladi. Davom etasizmi?",
      [
        { text: "Bekor qilish", style: "cancel" },
        {
          text: "O'chirish",
          style: "destructive",
          onPress: async () => {
            await clearAll();
            setApiKeyState('');
            setApiKeyConfigured(false);
            Alert.alert("Tayyor", "Barcha ma'lumotlar tozalandi");
          },
        },
      ]
    );
  }

  const themeModes: { label: string; value: ThemeMode }[] = [
    { label: "Yorug'", value: 'light' },
    { label: "Qorong'u", value: 'dark' },
    { label: 'Tizim', value: 'system' },
  ];

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.cardTitle, { color: theme.colors.onSurface }]}>
          <Ionicons name="key" size={16} color={Colors.primary.main} /> OpenAI API kalit
        </Text>
        <TextInput
          style={[styles.input, { color: theme.colors.onSurface, borderColor: theme.colors.onSurfaceVariant + '40' }]}
          placeholder="sk-..."
          placeholderTextColor={theme.colors.onSurfaceVariant}
          value={apiKey}
          onChangeText={setApiKeyState}
          secureTextEntry
          autoCapitalize="none"
        />
        <TouchableOpacity style={styles.saveButton} onPress={handleSaveApiKey}>
          <Text style={styles.saveButtonText}>Saqlash</Text>
        </TouchableOpacity>
        <Text style={[styles.statusText, { color: apiKeyConfigured ? Colors.primary.main : Colors.error.main }]}>
          {apiKeyConfigured ? "API kalit sozlangan" : "API kalit sozlanmagan"}
        </Text>
      </View>

      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.cardTitle, { color: theme.colors.onSurface }]}>
          <Ionicons name="color-palette" size={16} color={Colors.primary.main} /> Mavzu
        </Text>
        <View style={styles.themeRow}>
          {themeModes.map((mode) => (
            <TouchableOpacity
              key={mode.value}
              style={[
                styles.themeOption,
                themeMode === mode.value && styles.themeOptionActive,
              ]}
              onPress={() => setThemeMode(mode.value)}
            >
              <Text style={[
                styles.themeOptionText,
                themeMode === mode.value && styles.themeOptionTextActive,
              ]}>
                {mode.label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <View style={styles.switchRow}>
          <Text style={[styles.switchLabel, { color: theme.colors.onSurface }]}>
            <Ionicons name="notifications" size={16} color={Colors.primary.main} /> Bildirishnomalar
          </Text>
          <Switch
            value={notifications}
            onValueChange={setNotifications}
            trackColor={{ false: '#E0E0E0', true: Colors.primary.light }}
            thumbColor={notifications ? Colors.primary.main : '#f4f3f4'}
          />
        </View>
      </View>

      <TouchableOpacity
        style={[styles.dangerButton, { backgroundColor: Colors.error.container }]}
        onPress={handleClearData}
      >
        <Ionicons name="trash" size={18} color={Colors.error.main} />
        <Text style={[styles.dangerButtonText, { color: Colors.error.main }]}>
          Ma'lumotlarni tozalash
        </Text>
      </TouchableOpacity>

      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.aboutTitle, { color: theme.colors.onSurface }]}>
          AI Tajweed Ustoz
        </Text>
        <Text style={[styles.aboutVersion, { color: theme.colors.onSurfaceVariant }]}>
          Versiya: 2.0.0
        </Text>
        <Text style={[styles.aboutDescription, { color: theme.colors.onSurfaceVariant }]}>
          Sun'iy intellekt yordamida Qur'on tajvid qoidalarini o'rganish ilovasi.
          OpenAI Whisper orqali talaffuzni baholash va shaxsiy tavsiyalar berish.
        </Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    padding: 16,
    paddingBottom: 40,
  },
  card: {
    borderRadius: 12,
    padding: 16,
    marginBottom: 14,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 12,
  },
  input: {
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
    fontSize: 14,
    marginBottom: 10,
  },
  saveButton: {
    backgroundColor: '#2E7D32',
    borderRadius: 8,
    paddingVertical: 10,
    alignItems: 'center',
    marginBottom: 8,
  },
  saveButtonText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '600',
  },
  statusText: {
    fontSize: 12,
    fontWeight: '500',
  },
  themeRow: {
    flexDirection: 'row',
    gap: 8,
  },
  themeOption: {
    flex: 1,
    paddingVertical: 10,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: '#E0E0E0',
    alignItems: 'center',
  },
  themeOptionActive: {
    backgroundColor: Colors.primary.container,
    borderColor: Colors.primary.main,
  },
  themeOptionText: {
    fontSize: 13,
    fontWeight: '500',
    color: '#757575',
  },
  themeOptionTextActive: {
    color: Colors.primary.dark,
    fontWeight: '600',
  },
  switchRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  switchLabel: {
    fontSize: 15,
    fontWeight: '500',
  },
  dangerButton: {
    borderRadius: 12,
    paddingVertical: 14,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 14,
  },
  dangerButtonText: {
    fontSize: 14,
    fontWeight: '600',
    marginLeft: 8,
  },
  aboutTitle: {
    fontSize: 18,
    fontWeight: '700',
    textAlign: 'center',
    marginBottom: 4,
  },
  aboutVersion: {
    fontSize: 13,
    textAlign: 'center',
    marginBottom: 10,
  },
  aboutDescription: {
    fontSize: 13,
    lineHeight: 20,
    textAlign: 'center',
  },
});
