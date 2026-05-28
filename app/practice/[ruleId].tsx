import React, { useState, useEffect, useRef } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { useTheme } from '../../src/contexts/ThemeContext';
import { tajweedRules } from '../../src/data/tajweedRules';
import { quranAyahs } from '../../src/data/quranData';
import { Colors } from '../../src/constants/colors';
import { ArabicText } from '../../src/components/ArabicText';
import { RecordingButton } from '../../src/components/RecordingButton';
import { QuranAyah } from '../../src/data/models';

export default function PracticeSessionScreen() {
  const { ruleId } = useLocalSearchParams<{ ruleId: string }>();
  const { theme } = useTheme();

  const ruleIdNum = parseInt(ruleId || '0', 10);
  const rule = tajweedRules.find((r) => r.id === ruleIdNum);

  const matchingAyahs = quranAyahs.filter((ayah) => {
    const ids = ayah.tajweedRuleIds.split(',').map((id) => parseInt(id.trim(), 10));
    return ids.includes(ruleIdNum);
  });

  const [currentAyah, setCurrentAyah] = useState<QuranAyah | null>(null);
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const [hasRecorded, setHasRecorded] = useState(false);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

  useEffect(() => {
    pickRandomAyah();
  }, []);

  useEffect(() => {
    if (isRecording) {
      timerRef.current = setInterval(() => {
        setRecordingTime((prev) => prev + 1);
      }, 1000);
    } else {
      if (timerRef.current) {
        clearInterval(timerRef.current);
        timerRef.current = null;
      }
    }
    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
    };
  }, [isRecording]);

  function pickRandomAyah() {
    if (matchingAyahs.length === 0) return;
    const idx = Math.floor(Math.random() * matchingAyahs.length);
    setCurrentAyah(matchingAyahs[idx]);
    setHasRecorded(false);
    setRecordingTime(0);
  }

  function handleRecordPress() {
    if (isRecording) {
      setIsRecording(false);
      setHasRecorded(true);
    } else {
      setIsRecording(true);
      setRecordingTime(0);
    }
  }

  function formatTime(seconds: number): string {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  }

  if (!rule) {
    return (
      <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
        <Text style={[styles.errorText, { color: theme.colors.error }]}>
          Qoida topilmadi
        </Text>
      </View>
    );
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <View style={styles.content}>
        <Text style={[styles.ruleTitle, { color: theme.colors.onSurface }]}>
          {rule.nameUz}
        </Text>

        {currentAyah ? (
          <View style={[styles.ayahCard, { backgroundColor: theme.colors.surface }]}>
            <ArabicText
              text={currentAyah.arabicText}
              size={28}
              style={{ color: theme.colors.onSurface, textAlign: 'center', width: '100%' }}
            />
            <Text style={[styles.transliteration, { color: theme.colors.onSurfaceVariant }]}>
              {currentAyah.transliterationUz}
            </Text>
            <Text style={[styles.surahInfo, { color: theme.colors.onSurfaceVariant }]}>
              {currentAyah.surahNameUz} surasi, {currentAyah.ayahNumber}-oyat
            </Text>
          </View>
        ) : (
          <View style={[styles.ayahCard, { backgroundColor: theme.colors.surface }]}>
            <Text style={[styles.noAyah, { color: theme.colors.onSurfaceVariant }]}>
              Bu qoida uchun misol topilmadi
            </Text>
          </View>
        )}

        <View style={styles.recordingSection}>
          {isRecording && (
            <View style={styles.timerContainer}>
              <View style={styles.recordingIndicator} />
              <Text style={[styles.timerText, { color: Colors.error.main }]}>
                {formatTime(recordingTime)}
              </Text>
            </View>
          )}

          <RecordingButton
            isRecording={isRecording}
            onPress={handleRecordPress}
            size={80}
          />

          <Text style={[styles.recordHint, { color: theme.colors.onSurfaceVariant }]}>
            {isRecording ? 'Yozib olinmoqda...' : "Yozib olish uchun bosing"}
          </Text>
        </View>

        {hasRecorded && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <Text style={[styles.resultTitle, { color: theme.colors.onSurface }]}>
              Natija
            </Text>
            <Text style={[styles.resultText, { color: theme.colors.onSurfaceVariant }]}>
              Ovoz tahlil qilinmoqda... (FEAT-004 da ulanadi)
            </Text>
          </View>
        )}

        <TouchableOpacity
          style={[styles.nextButton, { opacity: currentAyah ? 1 : 0.5 }]}
          onPress={pickRandomAyah}
          disabled={!currentAyah}
        >
          <Text style={styles.nextButtonText}>Keyingi</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flex: 1,
    padding: 16,
  },
  errorText: {
    fontSize: 16,
    textAlign: 'center',
    marginTop: 40,
  },
  ruleTitle: {
    fontSize: 20,
    fontWeight: '600',
    textAlign: 'center',
    marginBottom: 16,
    marginTop: 8,
  },
  ayahCard: {
    borderRadius: 12,
    padding: 20,
    alignItems: 'center',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    marginBottom: 24,
  },
  transliteration: {
    fontSize: 14,
    fontStyle: 'italic',
    marginTop: 12,
    textAlign: 'center',
  },
  surahInfo: {
    fontSize: 12,
    marginTop: 8,
  },
  noAyah: {
    fontSize: 14,
    textAlign: 'center',
  },
  recordingSection: {
    alignItems: 'center',
    marginBottom: 24,
  },
  timerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  recordingIndicator: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: Colors.error.main,
    marginRight: 8,
  },
  timerText: {
    fontSize: 20,
    fontWeight: '600',
  },
  recordHint: {
    fontSize: 13,
    marginTop: 12,
  },
  resultCard: {
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  resultTitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  resultText: {
    fontSize: 14,
  },
  nextButton: {
    backgroundColor: '#2E7D32',
    borderRadius: 12,
    paddingVertical: 14,
    alignItems: 'center',
    marginTop: 'auto',
  },
  nextButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '600',
  },
});
