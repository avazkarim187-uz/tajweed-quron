import React, { useState, useEffect, useRef } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { useTheme } from '../../src/contexts/ThemeContext';
import { useProgress } from '../../src/contexts/ProgressContext';
import { tajweedRules } from '../../src/data/tajweedRules';
import { quranAyahs } from '../../src/data/quranData';
import { Colors } from '../../src/constants/colors';
import { ArabicText } from '../../src/components/ArabicText';
import { RecordingButton } from '../../src/components/RecordingButton';
import { ScoreCircle } from '../../src/components/ScoreCircle';
import { QuranAyah } from '../../src/data/models';
import * as audioRecorder from '../../src/services/audioRecorder';
import { transcribeAudio } from '../../src/services/whisperApi';
import { analyzeRecitation, TajweedAnalysisResult } from '../../src/services/tajweedAnalyzer';
import { getApiKey, saveRecordingResult } from '../../src/services/storage';

export default function PracticeSessionScreen() {
  const { ruleId } = useLocalSearchParams<{ ruleId: string }>();
  const { theme } = useTheme();
  const { updateProgress } = useProgress();
  const router = useRouter();

  const ruleIdNum = parseInt(ruleId || '0', 10);
  const rule = tajweedRules.find((r) => r.id === ruleIdNum);

  const matchingAyahs = quranAyahs.filter((ayah) => {
    const ids = ayah.tajweedRuleIds.split(',').map((id) => parseInt(id.trim(), 10));
    return ids.includes(ruleIdNum);
  });

  const [currentAyah, setCurrentAyah] = useState<QuranAyah | null>(null);
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [analysisResult, setAnalysisResult] = useState<TajweedAnalysisResult | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const recordingRef = useRef<{ recording: unknown } | null>(null);

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
    setRecordingTime(0);
    setAnalysisResult(null);
    setErrorMessage(null);
  }

  async function handleRecordPress() {
    if (isRecording) {
      await handleStopRecording();
    } else {
      await handleStartRecording();
    }
  }

  async function handleStartRecording() {
    try {
      setErrorMessage(null);
      setAnalysisResult(null);

      const hasPermission = await audioRecorder.requestPermissions();
      if (!hasPermission) {
        setErrorMessage("Mikrofon ruxsati berilmagan. Sozlamalardan ruxsat bering.");
        return;
      }

      const recording = await audioRecorder.startRecording();
      recordingRef.current = { recording };
      setIsRecording(true);
      setRecordingTime(0);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : "Yozib olishni boshlashda xatolik";
      setErrorMessage(msg);
    }
  }

  async function handleStopRecording() {
    try {
      setIsRecording(false);

      if (!recordingRef.current) {
        setErrorMessage("Yozuv topilmadi");
        return;
      }

      const recording = recordingRef.current.recording as Awaited<ReturnType<typeof audioRecorder.startRecording>>;
      const uri = await audioRecorder.stopRecording(recording);
      recordingRef.current = null;

      if (!currentAyah) return;

      setIsAnalyzing(true);

      const apiKey = await getApiKey();
      if (!apiKey) {
        setErrorMessage("API kalit sozlanmagan. Sozlamalar bo'limida API kalitni kiriting.");
        setIsAnalyzing(false);
        return;
      }

      const transcription = await transcribeAudio(uri, apiKey);

      if (!transcription.success) {
        setErrorMessage(transcription.error || "Ovozni tanib olishda xatolik yuz berdi");
        setIsAnalyzing(false);
        return;
      }

      const result = analyzeRecitation(currentAyah.arabicText, transcription.text || '');
      setAnalysisResult(result);
      updateProgress(ruleIdNum, result.score);

      const recordingResult = {
        id: Date.now(),
        ruleId: ruleIdNum,
        ayahId: currentAyah.id,
        audioPath: uri,
        transcription: transcription.text || '',
        score: result.score,
        errors: JSON.stringify(result.errors),
        timestamp: Date.now(),
      };
      await saveRecordingResult(recordingResult);

      setIsAnalyzing(false);
      router.push(`/feedback/${recordingResult.id}`);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : "Tahlil qilishda xatolik yuz berdi";
      setErrorMessage(msg);
      setIsAnalyzing(false);
    }
  }

  function formatTime(seconds: number): string {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
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

        {isAnalyzing && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <ActivityIndicator size="large" color={Colors.primary.main} />
            <Text style={[styles.analyzingText, { color: theme.colors.onSurfaceVariant }]}>
              Ovoz tahlil qilinmoqda...
            </Text>
          </View>
        )}

        {errorMessage && !isAnalyzing && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <Text style={[styles.errorMessage, { color: Colors.error.main }]}>
              {errorMessage}
            </Text>
          </View>
        )}

        {analysisResult && !isAnalyzing && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <View style={styles.scoreRow}>
              <ScoreCircle score={analysisResult.score} size={80} />
              <View style={styles.feedbackColumn}>
                <Text style={[styles.feedbackText, { color: theme.colors.onSurface }]}>
                  {analysisResult.feedback}
                </Text>
              </View>
            </View>

            {analysisResult.errors.length > 0 && (
              <View style={styles.errorsSection}>
                <Text style={[styles.errorsTitle, { color: theme.colors.onSurface }]}>
                  Xatolar:
                </Text>
                {analysisResult.errors.slice(0, 5).map((error, index) => (
                  <View key={index} style={styles.errorRow}>
                    <Text style={[styles.errorExpected, { color: Colors.primary.main }]}>
                      {error.expected || '(tushirilgan)'}
                    </Text>
                    <Text style={[styles.errorArrow, { color: theme.colors.onSurfaceVariant }]}>
                      {' → '}
                    </Text>
                    <Text style={[styles.errorGot, { color: Colors.error.main }]}>
                      {error.got || '(yo\'q)'}
                    </Text>
                  </View>
                ))}
                {analysisResult.errors.length > 5 && (
                  <Text style={[styles.moreErrors, { color: theme.colors.onSurfaceVariant }]}>
                    +{analysisResult.errors.length - 5} ta boshqa xato
                  </Text>
                )}
              </View>
            )}
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
  analyzingText: {
    fontSize: 14,
    textAlign: 'center',
    marginTop: 12,
  },
  errorMessage: {
    fontSize: 14,
    textAlign: 'center',
  },
  scoreRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 12,
  },
  feedbackColumn: {
    flex: 1,
    marginLeft: 16,
  },
  feedbackText: {
    fontSize: 15,
    fontWeight: '500',
  },
  errorsSection: {
    marginTop: 8,
    borderTopWidth: 1,
    borderTopColor: '#E0E0E0',
    paddingTop: 12,
  },
  errorsTitle: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 8,
  },
  errorRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 4,
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
