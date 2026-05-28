import React from 'react';
import { View, Text, ScrollView, StyleSheet } from 'react-native';
import { useTheme } from '../../src/contexts/ThemeContext';
import { Colors } from '../../src/constants/colors';
import { ScoreCircle } from '../../src/components/ScoreCircle';
import { tajweedRules } from '../../src/data/tajweedRules';

const weekDays = ['Du', 'Se', 'Chor', 'Pay', 'Ju', 'Sha', 'Yak'];
const weeklyActivity = [3, 5, 2, 7, 4, 0, 1];

export default function ProgressScreen() {
  const { theme } = useTheme();

  const overallScore = 68;
  const totalAttempts = 24;
  const rulesMastered = 2;
  const daysActive = 5;

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={styles.scoreSection}>
        <ScoreCircle score={overallScore} size={140} />
        <Text style={[styles.scoreLabel, { color: theme.colors.onSurfaceVariant }]}>
          Umumiy ball
        </Text>
      </View>

      <View style={[styles.statsRow, { backgroundColor: theme.colors.surface }]}>
        <View style={styles.statItem}>
          <Text style={[styles.statValue, { color: Colors.primary.main }]}>{totalAttempts}</Text>
          <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>Urinishlar</Text>
        </View>
        <View style={styles.statDivider} />
        <View style={styles.statItem}>
          <Text style={[styles.statValue, { color: Colors.secondary.main }]}>{rulesMastered}</Text>
          <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>O'zlashtirilgan</Text>
        </View>
        <View style={styles.statDivider} />
        <View style={styles.statItem}>
          <Text style={[styles.statValue, { color: Colors.tertiary.main }]}>{daysActive}</Text>
          <Text style={[styles.statLabel, { color: theme.colors.onSurfaceVariant }]}>Kun faol</Text>
        </View>
      </View>

      <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
        Haftalik faollik
      </Text>
      <View style={[styles.weekCard, { backgroundColor: theme.colors.surface }]}>
        <View style={styles.weekRow}>
          {weekDays.map((day, index) => {
            const activity = weeklyActivity[index];
            const maxActivity = Math.max(...weeklyActivity, 1);
            const barHeight = (activity / maxActivity) * 60 + 8;
            return (
              <View key={day} style={styles.dayColumn}>
                <View
                  style={[
                    styles.dayBar,
                    {
                      height: barHeight,
                      backgroundColor: activity > 0 ? Colors.primary.light : '#E0E0E0',
                    },
                  ]}
                />
                <Text style={[styles.dayLabel, { color: theme.colors.onSurfaceVariant }]}>
                  {day}
                </Text>
              </View>
            );
          })}
        </View>
      </View>

      <Text style={[styles.sectionTitle, { color: theme.colors.onSurface }]}>
        Qoidalar bo'yicha
      </Text>
      {tajweedRules.map((rule) => {
        const ruleScore = Math.floor(Math.random() * 100);
        const ruleAttempts = Math.floor(Math.random() * 10);
        return (
          <View
            key={rule.id}
            style={[styles.ruleCard, { backgroundColor: theme.colors.surface }]}
          >
            <View style={styles.ruleHeader}>
              <Text style={[styles.ruleNameText, { color: theme.colors.onSurface }]}>
                {rule.nameUz}
              </Text>
              <Text style={[styles.ruleScore, { color: Colors.primary.main }]}>
                {ruleScore}%
              </Text>
            </View>
            <View style={styles.progressBarBg}>
              <View
                style={[
                  styles.progressBarFill,
                  { width: `${ruleScore}%`, backgroundColor: Colors.primary.light },
                ]}
              />
            </View>
            <Text style={[styles.ruleAttempts, { color: theme.colors.onSurfaceVariant }]}>
              {ruleAttempts} ta urinish
            </Text>
          </View>
        );
      })}
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
  scoreSection: {
    alignItems: 'center',
    marginBottom: 24,
    marginTop: 8,
  },
  scoreLabel: {
    fontSize: 14,
    marginTop: 8,
  },
  statsRow: {
    flexDirection: 'row',
    borderRadius: 12,
    padding: 16,
    marginBottom: 24,
    elevation: 3,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
  },
  statItem: {
    flex: 1,
    alignItems: 'center',
  },
  statValue: {
    fontSize: 22,
    fontWeight: '700',
    marginBottom: 2,
  },
  statLabel: {
    fontSize: 11,
  },
  statDivider: {
    width: 1,
    height: 36,
    backgroundColor: '#E0E0E0',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 12,
  },
  weekCard: {
    borderRadius: 12,
    padding: 16,
    marginBottom: 24,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
  },
  weekRow: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'flex-end',
    height: 90,
  },
  dayColumn: {
    alignItems: 'center',
    justifyContent: 'flex-end',
  },
  dayBar: {
    width: 24,
    borderRadius: 4,
    marginBottom: 6,
  },
  dayLabel: {
    fontSize: 11,
    fontWeight: '500',
  },
  ruleCard: {
    borderRadius: 12,
    padding: 14,
    marginBottom: 8,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  ruleHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  ruleNameText: {
    fontSize: 14,
    fontWeight: '600',
  },
  ruleScore: {
    fontSize: 14,
    fontWeight: '700',
  },
  progressBarBg: {
    height: 6,
    backgroundColor: '#E0E0E0',
    borderRadius: 3,
    marginBottom: 6,
  },
  progressBarFill: {
    height: 6,
    borderRadius: 3,
  },
  ruleAttempts: {
    fontSize: 11,
  },
});
