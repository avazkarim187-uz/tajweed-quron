import React from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
import { useTheme } from '../../src/contexts/ThemeContext';
import { getSurahList, SurahInfo } from '../../src/data/quranData';
import { Colors } from '../../src/constants/colors';

export default function QuranScreen() {
  const router = useRouter();
  const { theme } = useTheme();
  const surahList = getSurahList();

  function renderSurah({ item }: { item: SurahInfo }) {
    return (
      <TouchableOpacity
        style={[styles.card, { backgroundColor: theme.colors.surface }]}
        onPress={() => router.push(`/quran/${item.number}`)}
        activeOpacity={0.7}
      >
        <View style={styles.numberCircle}>
          <Text style={styles.numberText}>{item.number}</Text>
        </View>
        <View style={styles.cardContent}>
          <Text style={[styles.arabicName, { color: Colors.primary.dark }]}>
            {item.arabicName}
          </Text>
          <Text style={[styles.uzbekName, { color: theme.colors.onSurface }]}>
            {item.uzbekName}
          </Text>
          <Text style={[styles.meta, { color: theme.colors.onSurfaceVariant }]}>
            {item.ayahCount} oyat
          </Text>
        </View>
        <View style={styles.pageInfo}>
          <Text style={[styles.pageLabel, { color: theme.colors.onSurfaceVariant }]}>
            {item.page}-bet
          </Text>
        </View>
      </TouchableOpacity>
    );
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <FlatList
        data={surahList}
        renderItem={renderSurah}
        keyExtractor={(item) => item.number.toString()}
        contentContainerStyle={styles.list}
        showsVerticalScrollIndicator={false}
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
  card: {
    borderRadius: 12,
    padding: 14,
    marginBottom: 10,
    flexDirection: 'row',
    alignItems: 'center',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  numberCircle: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: Colors.primary.container,
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 12,
  },
  numberText: {
    fontSize: 14,
    fontWeight: '700',
    color: Colors.primary.dark,
  },
  cardContent: {
    flex: 1,
  },
  arabicName: {
    fontSize: 22,
    fontWeight: '500',
    writingDirection: 'rtl',
    textAlign: 'left',
    marginBottom: 2,
  },
  uzbekName: {
    fontSize: 15,
    fontWeight: '500',
  },
  meta: {
    fontSize: 12,
    marginTop: 2,
  },
  pageInfo: {
    alignItems: 'flex-end',
  },
  pageLabel: {
    fontSize: 12,
  },
});
