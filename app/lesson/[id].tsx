import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { tajweedRules } from '../../src/data/tajweedRules';
import { Colors } from '../../src/constants/colors';
import { ArabicText } from '../../src/components/ArabicText';

const categoryColors: Record<string, string> = {
  'Nun Sakin': Colors.tajweed.izhor,
  'Ghunna': Colors.tajweed.ghunna,
  'Qalqala': Colors.tajweed.qalqala,
  'Madd': Colors.tajweed.madd,
  'Meem Sakin': Colors.tajweed.ikhfo,
};

export default function LessonDetailScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { theme } = useTheme();

  const rule = tajweedRules.find((r) => r.id === parseInt(id || '0', 10));

  if (!rule) {
    return (
      <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
        <Text style={[styles.errorText, { color: theme.colors.error }]}>
          Qoida topilmadi
        </Text>
      </View>
    );
  }

  const categoryColor = categoryColors[rule.category] || Colors.primary.main;
  const examples = rule.arabicExample.split('|');

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={styles.headerSection}>
        <ArabicText text={rule.name} size={36} style={{ color: Colors.primary.dark, textAlign: 'center' }} />
        <Text style={[styles.uzbekName, { color: theme.colors.onSurface }]}>
          {rule.nameUz}
        </Text>
        <View style={[styles.badge, { backgroundColor: categoryColor + '20' }]}>
          <Text style={[styles.badgeText, { color: categoryColor }]}>
            {rule.category}
          </Text>
        </View>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
          <Ionicons name="text" size={16} color={Colors.primary.main} /> Harflar
        </Text>
        <Text style={[styles.lettersText, { color: theme.colors.onSurface }]}>
          {rule.lettersList}
        </Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
          <Ionicons name="information-circle" size={16} color={Colors.primary.main} /> Tushuntirish
        </Text>
        <Text style={[styles.explanationText, { color: theme.colors.onSurfaceVariant }]}>
          {rule.explanation}
        </Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
          <Ionicons name="documents" size={16} color={Colors.primary.main} /> Misollar
        </Text>
        {examples.map((example, index) => (
          <View key={index} style={[styles.exampleCard, { backgroundColor: theme.colors.surfaceVariant }]}>
            <ArabicText text={example.trim()} size={26} style={{ color: Colors.primary.dark }} />
          </View>
        ))}
      </View>

      <TouchableOpacity
        style={styles.practiceButton}
        onPress={() => router.push(`/practice/${rule.id}`)}
        activeOpacity={0.7}
      >
        <Ionicons name="mic" size={20} color="#FFFFFF" />
        <Text style={styles.practiceButtonText}>Mashq qilish</Text>
      </TouchableOpacity>
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
  errorText: {
    fontSize: 16,
    textAlign: 'center',
    marginTop: 40,
  },
  headerSection: {
    alignItems: 'center',
    marginBottom: 24,
    paddingTop: 8,
  },
  uzbekName: {
    fontSize: 20,
    fontWeight: '600',
    marginTop: 8,
    marginBottom: 8,
  },
  badge: {
    paddingHorizontal: 14,
    paddingVertical: 5,
    borderRadius: 14,
  },
  badgeText: {
    fontSize: 13,
    fontWeight: '600',
  },
  section: {
    borderRadius: 12,
    padding: 16,
    marginBottom: 14,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 12,
  },
  lettersText: {
    fontSize: 28,
    textAlign: 'right',
    writingDirection: 'rtl',
    lineHeight: 44,
  },
  explanationText: {
    fontSize: 14,
    lineHeight: 22,
  },
  exampleCard: {
    borderRadius: 8,
    padding: 12,
    marginBottom: 8,
    alignItems: 'center',
  },
  practiceButton: {
    backgroundColor: '#2E7D32',
    borderRadius: 12,
    paddingVertical: 16,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 8,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
  },
  practiceButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
    marginLeft: 8,
  },
});
