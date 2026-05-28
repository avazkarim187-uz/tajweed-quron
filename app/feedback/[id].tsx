import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '../../src/contexts/ThemeContext';
import { Colors } from '../../src/constants/colors';
import { ScoreCircle } from '../../src/components/ScoreCircle';
import { ArabicText } from '../../src/components/ArabicText';
import { RecordingResult } from '../../src/data/models';
import { getRecordingResults } from '../../src/services/storage';

export default function FeedbackScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { theme } = useTheme();

  const [result, setResult] = useState<RecordingResult | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadResult();
  }, [id]);

  async function loadResult() {
    try {
      const results = await getRecordingResults();
      const found = results.find((r) => r.id === parseInt(id || '0', 10));
      setResult(found || null);
    } catch {
      // Keep empty
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return (
      <View style={[styles.container, { backgroundColor: theme.colors.background, justifyContent: 'center', alignItems: 'center' }]}>
        <ActivityIndicator size="large" color={Colors.primary.main} />
      </View>
    );
  }

  if (!result) {
    return (
      <View style={[styles.container, { backgroundColor: theme.colors.background, justifyContent: 'center', alignItems: 'center' }]}>
        <Ionicons name="alert-circle-outline" size={48} color={theme.colors.onSurfaceVariant} />
        <Text style={[styles.noResultText, { color: theme.colors.onSurfaceVariant }]}>
          Natija topilmadi
        </Text>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => router.push('/(tabs)/practice')}
        >
          <Text style={styles.backButtonText}>Mashqqa qaytish</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const errors = result.errors ? JSON.parse(result.errors) : [];

  return (
    <ScrollView
      style={[styles.container, { backgroundColor: theme.colors.background }]}
      contentContainerStyle={styles.content}
    >
      <View style={styles.scoreSection}>
        <ScoreCircle score={result.score} size={140} />
        <Text style={[styles.scoreLabel, { color: theme.colors.onSurfaceVariant }]}>
          Sizning balingiz
        </Text>
      </View>

      {result.transcription ? (
        <View style={[styles.card, { backgroundColor: theme.colors.surface }]}>
          <Text style={[styles.cardTitle, { color: theme.colors.onSurface }]}>
            Aniqlangan matn
          </Text>
          <ArabicText text={result.transcription} size={24} style={{ color: Colors.tertiary.main, textAlign: 'center' }} />
        </View>
      ) : null}

      {errors.length > 0 && (
        <View style={[styles.card, { backgroundColor: Colors.error.container }]}>
          <Text style={[styles.cardTitle, { color: Colors.error.onContainer }]}>
            Xatolar ({errors.length})
          </Text>
          {errors.slice(0, 5).map((error: { expected: string; got: string }, index: number) => (
            <View key={index} style={styles.errorRow}>
              <Text style={[styles.errorExpected, { color: Colors.primary.main }]}>
                {error.expected || '(tushirilgan)'}
              </Text>
              <Text style={[styles.errorArrow, { color: Colors.error.onContainer }]}>{' \u2192 '}</Text>
              <Text style={[styles.errorGot, { color: Colors.error.main }]}>
                {error.got || "(yo'q)"}
              </Text>
            </View>
          ))}
          {errors.length > 5 && (
            <Text style={[styles.moreErrors, { color: Colors.error.onContainer }]}>
              +{errors.length - 5} ta boshqa xato
            </Text>
          )}
        </View>
      )}

      {result.score >= 70 ? (
        <View style={[styles.card, { backgroundColor: Colors.primary.container }]}>
          <Text style={[styles.feedbackPositive, { color: Colors.primary.dark }]}>
            Yaxshi natija! Mashq qilishda davom eting.
          </Text>
        </View>
      ) : (
        <View style={[styles.card, { backgroundColor: Colors.error.container }]}>
          <Text style={[styles.errorText, { color: Colors.error.onContainer }]}>
            Tajvid qoidalarini yaxshiroq rioya qiling. Harflarni aniq talaffuz qiling.
          </Text>
        </View>
      )}

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
  noResultText: {
    fontSize: 16,
    marginTop: 12,
    marginBottom: 20,
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
  errorRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 4,
    alignSelf: 'flex-start',
  },
  errorExpected: {
    fontSize: 14,
    fontWeight: '500',
  },
  errorArrow: {
    fontSize: 14,
  },
  errorGot: {
    fontSize: 14,
    fontWeight: '500',
  },
  moreErrors: {
    fontSize: 12,
    marginTop: 4,
    alignSelf: 'flex-start',
  },
  errorText: {
    fontSize: 14,
    lineHeight: 20,
  },
  feedbackPositive: {
    fontSize: 14,
    lineHeight: 20,
    fontWeight: '500',
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
