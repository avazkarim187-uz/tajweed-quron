import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { Colors } from '../../src/constants/colors';

export default function HomeScreen() {
  const router = useRouter();
  const { theme } = useTheme();

  const completedRules = 3;
  const overallScore = 72;
  const streakDays = 5;

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={styles.header}>
        <Text style={[styles.greeting, { color: theme.colors.onSurface }]}>
          Assalomu alaykum!
        </Text>
        <Text style={[styles.subtitle, { color: theme.colors.onSurfaceVariant }]}>
          Tajvid o'rganishda davom eting
        </Text>
        <TouchableOpacity
          style={styles.settingsButton}
          onPress={() => router.push('/settings')}
        >
          <Ionicons name="settings-outline" size={24} color={theme.colors.onSurface} />
        </TouchableOpacity>
      </View>

      <View style={[styles.statsCard, { backgroundColor: theme.colors.surface }]}>
        <View style={styles.statsRow}>
          <View style={styles.statItem}>
            <Text style={[styles.statValue, { color: Colors.primary.main }]}>
              {completedRules}
            </Text>
            <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>
              Qoidalar
            </Text>
          </View>
          <View style={styles.statDivider} />
          <View style={styles.statItem}>
            <Text style={[styles.statValue, { color: Colors.secondary.main }]}>
              {overallScore}%
            </Text>
            <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>
              Ball
            </Text>
          </View>
          <View style={styles.statDivider} />
          <View style={styles.statItem}>
            <Text style={[styles.statValue, { color: Colors.tertiary.main }]}>
              {streakDays}
            </Text>
            <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>
              Kun ketma-ket
            </Text>
          </View>
        </View>
      </View>

      <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
        Tezkor harakatlar
      </Text>
      <View style={styles.quickActions}>
        <TouchableOpacity
          style={[styles.actionCard, { backgroundColor: Colors.primary.container }]}
          onPress={() => router.push('/(tabs)/lessons')}
        >
          <Ionicons name="book" size={32} color={Colors.primary.dark} />
          <Text style={[styles.actionText, { color: Colors.primary.dark }]}>Darslar</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.actionCard, { backgroundColor: Colors.tertiary.container }]}
          onPress={() => router.push('/(tabs)/quran')}
        >
          <Ionicons name="library" size={32} color={Colors.tertiary.main} />
          <Text style={[styles.actionText, { color: Colors.tertiary.main }]}>Qur'on</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.actionCard, { backgroundColor: Colors.secondary.container }]}
          onPress={() => router.push('/(tabs)/practice')}
        >
          <Ionicons name="mic" size={32} color={Colors.secondary.onContainer} />
          <Text style={[styles.actionText, { color: Colors.secondary.onContainer }]}>Mashq</Text>
        </TouchableOpacity>
      </View>

      <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
        So'nggi natijalar
      </Text>
      <View style={[styles.recentCard, { backgroundColor: theme.colors.surface }]}>
        <Ionicons name="time-outline" size={20} color={theme.colors.onSurfaceVariant} />
        <Text style={[styles.recentText, { color: theme.colors.onSurfaceVariant }]}>
          Hozircha natijalar yo'q. Mashq qilishni boshlang!
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
    paddingBottom: 32,
  },
  header: {
    marginBottom: 20,
    position: 'relative',
  },
  greeting: {
    fontSize: 26,
    fontWeight: '700',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 16,
  },
  settingsButton: {
    position: 'absolute',
    top: 0,
    right: 0,
    padding: 4,
  },
  statsCard: {
    borderRadius: 12,
    padding: 20,
    marginBottom: 24,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  statsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  statItem: {
    alignItems: 'center',
    flex: 1,
  },
  statValue: {
    fontSize: 28,
    fontWeight: '700',
    marginBottom: 4,
  },
  statLabel: {
    fontSize: 12,
  },
  statDivider: {
    width: 1,
    height: 40,
    backgroundColor: '#E0E0E0',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
  },
  quickActions: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 24,
  },
  actionCard: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 16,
    borderRadius: 12,
    marginHorizontal: 4,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  actionText: {
    fontSize: 13,
    fontWeight: '600',
    marginTop: 8,
  },
  recentCard: {
    borderRadius: 12,
    padding: 20,
    flexDirection: 'row',
    alignItems: 'center',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  recentText: {
    fontSize: 14,
    marginLeft: 12,
    flex: 1,
  },
});
