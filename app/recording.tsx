import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import { useTheme } from '../src/contexts/ThemeContext';
import { Colors } from '../src/constants/colors';
import { RecordingButton } from '../src/components/RecordingButton';
import * as audioRecorder from '../src/services/audioRecorder';
import { transcribeAudio } from '../src/services/whisperApi';
import { getApiKey } from '../src/services/storage';

export default function RecordingScreen() {
  const { theme } = useTheme();
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const [isTranscribing, setIsTranscribing] = useState(false);
  const [transcriptionText, setTranscriptionText] = useState<string | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const recordingRef = useRef<{ recording: unknown } | null>(null);

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
      setTranscriptionText(null);

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

      setIsTranscribing(true);

      const apiKey = await getApiKey();
      if (!apiKey) {
        setErrorMessage("API kalit sozlanmagan. Sozlamalar bo'limida API kalitni kiriting.");
        setIsTranscribing(false);
        return;
      }

      const result = await transcribeAudio(uri, apiKey);

      if (result.success) {
        setTranscriptionText(result.text || '');
      } else {
        setErrorMessage(result.error || "Ovozni tanib olishda xatolik yuz berdi");
      }

      setIsTranscribing(false);
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : "Xatolik yuz berdi";
      setErrorMessage(msg);
      setIsTranscribing(false);
    }
  }

  function formatTime(seconds: number): string {
    const m = Math.floor(seconds / 60);
    const s = seconds % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  }

  return (
    <View style={[styles.container, { backgroundColor: theme.colors.background }]}>
      <View style={styles.content}>
        <Text style={[styles.instruction, { color: theme.colors.onSurface }]}>
          Oyatni o'qing va yozib oling
        </Text>
        <Text style={[styles.hint, { color: theme.colors.onSurfaceVariant }]}>
          Quyidagi tugmani bosib ovozingizni yozib oling. Aniq va ravshan o'qishga harakat qiling.
        </Text>

        <View style={styles.timerSection}>
          {isRecording && (
            <View style={styles.timerRow}>
              <View style={styles.recordingDot} />
              <Text style={[styles.timerText, { color: Colors.error.main }]}>
                {formatTime(recordingTime)}
              </Text>
            </View>
          )}
          {!isRecording && recordingTime > 0 && !isTranscribing && !transcriptionText && !errorMessage && (
            <Text style={[styles.doneText, { color: Colors.primary.main }]}>
              Yozib olindi: {formatTime(recordingTime)}
            </Text>
          )}
        </View>

        <View style={styles.buttonSection}>
          <RecordingButton
            isRecording={isRecording}
            onPress={handleRecordPress}
            size={90}
          />
          <Text style={[styles.buttonHint, { color: theme.colors.onSurfaceVariant }]}>
            {isRecording ? "To'xtatish uchun bosing" : "Boshlash uchun bosing"}
          </Text>
        </View>

        {isTranscribing && (
          <View style={styles.resultSection}>
            <ActivityIndicator size="large" color={Colors.primary.main} />
            <Text style={[styles.transcribingText, { color: theme.colors.onSurfaceVariant }]}>
              Ovoz tahlil qilinmoqda...
            </Text>
          </View>
        )}

        {errorMessage && !isTranscribing && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <Text style={[styles.errorText, { color: Colors.error.main }]}>
              {errorMessage}
            </Text>
          </View>
        )}

        {transcriptionText && !isTranscribing && (
          <View style={[styles.resultCard, { backgroundColor: theme.colors.surface }]}>
            <Text style={[styles.resultLabel, { color: theme.colors.onSurfaceVariant }]}>
              Natija:
            </Text>
            <Text style={[styles.transcriptionResult, { color: theme.colors.onSurface }]}>
              {transcriptionText}
            </Text>
          </View>
        )}
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
    justifyContent: 'center',
    alignItems: 'center',
  },
  instruction: {
    fontSize: 20,
    fontWeight: '600',
    textAlign: 'center',
    marginBottom: 8,
  },
  hint: {
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: 40,
    paddingHorizontal: 20,
  },
  timerSection: {
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 30,
  },
  timerRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  recordingDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: Colors.error.main,
    marginRight: 8,
  },
  timerText: {
    fontSize: 24,
    fontWeight: '600',
  },
  doneText: {
    fontSize: 16,
    fontWeight: '500',
  },
  buttonSection: {
    alignItems: 'center',
  },
  buttonHint: {
    fontSize: 13,
    marginTop: 16,
  },
  resultSection: {
    marginTop: 40,
    alignItems: 'center',
  },
  transcribingText: {
    fontSize: 14,
    marginTop: 12,
  },
  resultCard: {
    borderRadius: 12,
    padding: 16,
    marginTop: 30,
    width: '100%',
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.08,
    shadowRadius: 2,
  },
  errorText: {
    fontSize: 14,
    textAlign: 'center',
  },
  resultLabel: {
    fontSize: 12,
    marginBottom: 8,
  },
  transcriptionResult: {
    fontSize: 18,
    textAlign: 'right',
    lineHeight: 28,
  },
});
