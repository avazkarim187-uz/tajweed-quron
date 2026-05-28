import React from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { quranAyahs } from '../../src/data/quranData';
import { tajweedRules } from '../../src/data/tajweedRules';
import { Colors } from '../../src/constants/colors';
import { ArabicText } from '../../src/components/ArabicText';
import { QuranAyah } from '../../src/data/models';

const categoryColors: Record<string, string> = {
  'Nun Sakin': Colors.tajweed.izhor,
  'Ghunna': Colors.tajweed.ghunna,
  'Qalqala': Colors.tajweed.qalqala,
  'Madd': Colors.tajweed.madd,
  'Meem Sakin': Colors.tajweed.ikhfo,
};

export default function QuranReaderScreen() {
  const { surahNumber } = useLocalSearchParams<{ surahNumber: string }>();
  const { theme } = useTheme();

  const surahNum = parseInt(surahNumber || '0', 10);
  const ayahs = quranAyahs.filter((a) => a.surahNumber === surahNum);

  if (ayahs.length === 0) {
    return (
      <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
        <Text style={[styles.errorText, { color: theme.colors.error }]}>
          Sura topilmadi
        </Text>
      </View>
    );
  }

  const surahName = ayahs[0].surahName;
  const surahNameUz = ayahs[0].surahNameUz;

  function getRuleTags(ruleIds: string) {
    return ruleIds.split(',').map((id) => {
      const ruleId = parseInt(id.trim(), 10);
      const rule = tajweedRules.find((r) => r.id === ruleId);
      if (!rule) return null;
      const color = categoryColors[rule.category] || Colors.primary.main;
      return { id: ruleId, name: rule.nameUz, color };
    }).filter(Boolean);
  }

  function renderAyah({ item }: { item: QuranAyah }) {
    const tags = getRuleTags(item.tajweedRuleIds);

    return (
      <View style={[styles.ayahCard, { backgroundColor: theme.colors.surface }]}>
        <View style={styles.ayahHeader}>
          <View style={styles.ayahBadge}>
            <Text style={styles.ayahBadgeText}>{item.ayahNumber}</Text>
          </View>
          <TouchableOpacity style={styles.micButton}>
            <Ionicons name="mic-outline" size={18} color={Colors.primary.main} />
          </TouchableOpacity>
        </View>

        <ArabicText text={item.arabicText} size={26} style={{ color: theme.colors.onSurface, textAlign: 'center', width: '100%' }} />

        <Text style={[styles.transliteration, { color: theme.colors.onSurfaceVariant }]}>
          {item.transliterationUz}
        </Text>
        <Text style={[styles.translation, { color: theme.colors.onSurface }]}>
          {item.translationUz}
        </Text>

        {tags.length > 0 && (
          <View style={styles.tagsRow}>
            {tags.map((tag) => tag && (
              <View
                key={tag.id}
                style={[styles.tag, { backgroundColor: tag.color + '20' }]}
              >
                <Text style={[styles.tagText, { color: tag.color }]}>
                  {tag.name}
                </Text>
              </View>
            ))}
          </View>
        )}
      </View>
    );
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <FlatList
        data={ayahs}
        renderItem={renderAyah}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.list}
        showsVerticalScrollIndicator={false}
        ListHeaderComponent={
          <View style={styles.surahHeader}>
            <ArabicText text={surahName} size={32} style={{ color: Colors.primary.dark, textAlign: 'center' }} />
            <Text style={[styles.surahNameUz, { color: theme.colors.onSurface }]}>
              {surahNameUz} surasi
            </Text>
            {surahNum !== 1 && surahNum !== 9 && (
              <View style={[styles.bismillah, { backgroundColor: theme.colors.surfaceVariant }]}>
                <ArabicText
                  text={'\u0628\u0650\u0633\u0652\u0645\u0650 \u0627\u0644\u0644\u0651\u064E\u0647\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0652\u0645\u064E\u0646\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0650\u064A\u0645\u0650'}
                  size={22}
                  style={{ color: Colors.primary.dark, textAlign: 'center' }}
                />
              </View>
            )}
          </View>
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  list: {
    padding: 16,
    paddingBottom: 32,
  },
  errorText: {
    fontSize: 16,
    textAlign: 'center',
    marginTop: 40,
  },
  surahHeader: {
    alignItems: 'center',
    marginBottom: 20,
    paddingTop: 8,
  },
  surahNameUz: {
    fontSize: 16,
    fontWeight: '500',
    marginTop: 4,
    marginBottom: 12,
  },
  bismillah: {
    borderRadius: 8,
    padding: 12,
    width: '100%',
    alignItems: 'center',
  },
  ayahCard: {
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  ayahHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  ayahBadge: {
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: Colors.primary.container,
    alignItems: 'center',
    justifyContent: 'center',
  },
  ayahBadgeText: {
    fontSize: 12,
    fontWeight: '700',
    color: Colors.primary.dark,
  },
  micButton: {
    padding: 6,
  },
  transliteration: {
    fontSize: 14,
    fontStyle: 'italic',
    marginTop: 10,
    lineHeight: 20,
  },
  translation: {
    fontSize: 14,
    marginTop: 6,
    lineHeight: 20,
  },
  tagsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 10,
    gap: 6,
  },
  tag: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 8,
  },
  tagText: {
    fontSize: 10,
    fontWeight: '600',
  },
});
