import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { Colors } from '../../src/constants/colors';
import { ScoreCircle } from '../../src/components/ScoreCircle';
import { ArabicText } from '../../src/components/ArabicText';

export default function FeedbackScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { theme } = useTheme();

  const mockScore = 78;
  const originalText = '\u0628\u0650\u0633\u0652\u0645\u0650 \u0627\u0644\u0644\u0651\u064E\u0647\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0652\u0645\u064E\u0646\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0650\u064A\u0645\u0650';
  const transcribedText = '\u0628\u0633\u0645 \u0627\u0644\u0644\u0647 \u0627\u0644\u0631\u062D\u0645\u0646 \u0627\u0644\u0631\u062D\u064A\u0645';

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={styles.scoreSection}>
        <ScoreCircle score={mockScore} size={140} />
        <Text style={[styles.scoreLabel, { color: theme.colors.onSurfaceVariant }]}>
          Sizning balingiz
        </Text>
      </View>

      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.cardTitle, { color: theme.colors.onSurface }]}>
          Asl matn
        </Text>
        <ArabicText text={originalText} size={24} style={{ color: theme.colors.onSurface, textAlign: 'center' }} />
      </View>

      <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
        <Text style={[styles.cardTitle, { color: theme.colors.onSurface }]}>
          Aniqlangan matn
        </Text>
        <ArabicText text={transcribedText} size={24} style={{ color: Colors.tertiary.main, textAlign: 'center' }} />
      </View>

      <View style={[styles.card, { backgroundColor: Colors.error.container }]}>
        <Text style={[styles.cardTitle, { color: Colors.error.onContainer }]}>
          Xatolar
        </Text>
        <Text style={[styles.errorText, { color: Colors.error.onContainer }]}>
          Tajvid qoidalarini yaxshiroq rioya qiling. Harflarni aniq talaffuz qiling.
        </Text>
      </View>

      <View style={styles.buttonRow}>
        <TouchableOpacity
          style={[styles.retryButton, { borderColor: Colors.primary.main }]}
          onPress={() => router.back()}
        >
          <Ionicons name="refresh" size={18} color={Colors.primary.main} />
          <Text style={[styles.retryText, { color: Colors.primary.main }]}>
            Qayta urinish
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => router.push('/(tabs)/practice')}
        >
          <Text style={styles.backButtonText}>Mashqqa qaytish</Text>
        </TouchableOpacity>
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
  scoreSection: {
    alignItems: 'center',
    marginVertical: 24,
  },
  scoreLabel: {
    fontSize: 14,
    marginTop: 8,
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
    alignItems: 'center',
  },
  cardTitle: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 10,
    alignSelf: 'flex-start',
  },
  errorText: {
    fontSize: 14,
    lineHeight: 20,
  },
  buttonRow: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 10,
  },
  retryButton: {
    flex: 1,
    borderWidth: 2,
    borderRadius: 12,
    paddingVertical: 14,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
  },
  retryText: {
    fontSize: 14,
    fontWeight: '600',
    marginLeft: 6,
  },
  backButton: {
    flex: 1,
    backgroundColor: '#2E7D32',
    borderRadius: 12,
    paddingVertical: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
  backButtonText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '600',
  },
});
