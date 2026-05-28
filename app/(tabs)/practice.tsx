import React from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { tajweedRules } from '../../src/data/tajweedRules';
import { Colors } from '../../src/constants/colors';
import { TajweedRule } from '../../src/data/models';

const categoryColors: Record<string, string> = {
  'Nun Sakin': Colors.tajweed.izhor,
  'Ghunna': Colors.tajweed.ghunna,
  'Qalqala': Colors.tajweed.qalqala,
  'Madd': Colors.tajweed.madd,
  'Meem Sakin': Colors.tajweed.ikhfo,
};

export default function PracticeListScreen() {
  const router = useRouter();
  const { theme } = useTheme();

  function renderItem({ item }: { item: TajweedRule }) {
    const categoryColor = categoryColors[item.category] || Colors.primary.main;
    const score = 0;
    const attempts = 0;

    return (
      <TouchableOpacity
        style={[styles.card, { backgroundColor: theme.colors.surface }]}
        onPress={() => router.push(`/practice/${item.id}`)}
        activeOpacity={0.7}
      >
        <View style={styles.cardLeft}>
          <Text style={[styles.ruleName, { color: theme.colors.onSurface }]}>
            {item.nameUz}
          </Text>
          <View style={[styles.badge, { backgroundColor: categoryColor + '20' }]}>
            <Text style={[styles.badgeText, { color: categoryColor }]}>
              {item.category}
            </Text>
          </View>
          <Text style={[styles.attempts, { color: theme.colors.onSurfaceVariant }]}>
            {attempts} ta urinish
          </Text>
        </View>
        <View style={styles.cardRight}>
          <View style={styles.scoreContainer}>
            <Text style={[styles.scoreText, { color: score > 0 ? Colors.primary.main : theme.colors.onSurfaceVariant }]}>
              {score}%
            </Text>
          </View>
          <Ionicons name="chevron-forward" size={20} color={theme.colors.onSurfaceVariant} />
        </View>
      </TouchableOpacity>
    );
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <FlatList
        data={tajweedRules}
        renderItem={renderItem}
        keyExtractor={(item) => item.id.toString()}
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
    padding: 16,
    marginBottom: 10,
    flexDirection: 'row',
    alignItems: 'center',
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  cardLeft: {
    flex: 1,
  },
  ruleName: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 6,
  },
  badge: {
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 10,
    marginBottom: 4,
  },
  badgeText: {
    fontSize: 11,
    fontWeight: '600',
  },
  attempts: {
    fontSize: 12,
  },
  cardRight: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  scoreContainer: {
    marginRight: 8,
  },
  scoreText: {
    fontSize: 18,
    fontWeight: '700',
  },
});
