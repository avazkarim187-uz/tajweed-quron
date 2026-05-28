export interface TajweedRule {
  id: number;
  name: string;
  nameUz: string;
  description: string;
  arabicExample: string;
  explanation: string;
  audioUrl: string | null;
  category: string;
  lettersList: string;
}

export interface QuranAyah {
  id: number;
  surahNumber: number;
  surahName: string;
  surahNameUz: string;
  ayahNumber: number;
  arabicText: string;
  transliterationUz: string;
  translationUz: string;
  tajweedRuleIds: string;
  page: number;
}

export interface UserProgress {
  id: number;
  ruleId: number;
  lessonCompleted: boolean;
  practiceScore: number;
  lastPracticeDate: number;
  totalAttempts: number;
  successfulAttempts: number;
}

export interface RecordingResult {
  id: number;
  ruleId: number | null;
  ayahId: number | null;
  audioPath: string;
  transcription: string;
  score: number;
  errors: string;
  timestamp: number;
}

export interface PracticeSession {
  ruleId: number;
  ayahText: string;
  expectedPronunciation: string;
  attempts: number;
  bestScore: number;
}

export interface AppSettings {
  themeMode: 'light' | 'dark' | 'system';
  apiKey: string | null;
  notificationsEnabled: boolean;
  dailyReminderTime: string | null;
}
