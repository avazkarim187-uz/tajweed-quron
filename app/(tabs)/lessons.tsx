import React from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet } from 'react-native';
import { useRouter } from 'expo-router';
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

export default function LessonsListScreen() {
  const router = useRouter();
  const { theme } = useTheme();

  function renderRule({ item }: { item: TajweedRule }) {
    const categoryColor = categoryColors[item.category] || Colors.primary.main;

    return (
      <TouchableOpacity
        style={[styles.card, { backgroundColor: theme.colors.surface }]}
        onPress={() => router.push(`/lesson/${item.id}`)}
        activeOpacity={0.7}
      >
        <View style={styles.cardHeader}>
          <Text style={styles.arabicName}>{item.name}</Text>
          <View style={[styles.badge, { backgroundColor: categoryColor + '20' }]}>
            <Text style={[styles.badgeText, { color: categoryColor }]}>
              {item.category}
            </Text>
          </View>
        </View>
        <Text style={[styles.uzbekName, { color: theme.colors.onSurface }]}>
          {item.nameUz}
        </Text>
        <Text
          style={[styles.description, { color: theme.colors.onSurfaceVariant }]}
          numberOfLines={2}
        >
          {item.description}
        </Text>
      </TouchableOpacity>
    );
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <FlatList
        data={tajweedRules}
        renderItem={renderRule}
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
    marginBottom: 12,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  arabicName: {
    fontSize: 24,
    fontWeight: '600',
    color: '#1B5E20',
    writingDirection: 'rtl',
  },
  badge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
  },
  badgeText: {
    fontSize: 11,
    fontWeight: '600',
  },
  uzbekName: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 4,
  },
  description: {
    fontSize: 13,
    lineHeight: 18,
  },
});
