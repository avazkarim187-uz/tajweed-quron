import React, { useState, useEffect, useRef } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { useTheme } from '../src/contexts/ThemeContext';
import { Colors } from '../src/constants/colors';
import { RecordingButton } from '../src/components/RecordingButton';

export default function RecordingScreen() {
  const { theme } = useTheme();
  const [isRecording, setIsRecording] = useState(false);
  const [recordingTime, setRecordingTime] = useState(0);
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null);

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

  function handleRecordPress() {
    if (isRecording) {
      setIsRecording(false);
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
          {!isRecording && recordingTime > 0 && (
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
});
