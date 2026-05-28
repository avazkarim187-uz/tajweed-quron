import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { UserProgress } from '../data/models';
import { saveProgress, getProgress } from '../services/storage';

interface ProgressContextType {
  progress: UserProgress[];
  updateProgress: (ruleId: number, score: number) => void;
  markLessonComplete: (ruleId: number) => void;
  getProgressForRule: (ruleId: number) => UserProgress | undefined;
  getOverallScore: () => number;
  getWeeklyActivity: () => number[];
  getStreak: () => number;
  getMasteredRulesCount: () => number;
}

const ProgressContext = createContext<ProgressContextType | undefined>(undefined);

interface ProgressProviderProps {
  children: ReactNode;
}

export function ProgressProvider({ children }: ProgressProviderProps) {
  const [progress, setProgress] = useState<UserProgress[]>([]);

  useEffect(() => {
    loadProgress();
  }, []);

  async function loadProgress() {
    try {
      const data = await getProgress();
      setProgress(data);
    } catch {
      // Keep empty
    }
  }

  function persistProgress(updated: UserProgress[]) {
    setProgress(updated);
    saveProgress(updated).catch(() => {});
  }

  function updateProgress(ruleId: number, score: number) {
    setProgress((prev) => {
      const existing = prev.find((p) => p.ruleId === ruleId);
      let updated: UserProgress[];

      if (existing) {
        updated = prev.map((p) =>
          p.ruleId === ruleId
            ? {
                ...p,
                practiceScore: Math.max(p.practiceScore, score),
                totalAttempts: p.totalAttempts + 1,
                successfulAttempts:
                  score >= 70 ? p.successfulAttempts + 1 : p.successfulAttempts,
                lastPracticeDate: Date.now(),
              }
            : p
        );
      } else {
        const newEntry: UserProgress = {
          id: prev.length + 1,
          ruleId,
          lessonCompleted: false,
          practiceScore: score,
          lastPracticeDate: Date.now(),
          totalAttempts: 1,
          successfulAttempts: score >= 70 ? 1 : 0,
        };
        updated = [...prev, newEntry];
      }

      saveProgress(updated).catch(() => {});
      return updated;
    });
  }

  function markLessonComplete(ruleId: number) {
    setProgress((prev) => {
      const existing = prev.find((p) => p.ruleId === ruleId);
      let updated: UserProgress[];

      if (existing) {
        updated = prev.map((p) =>
          p.ruleId === ruleId ? { ...p, lessonCompleted: true } : p
        );
      } else {
        const newEntry: UserProgress = {
          id: prev.length + 1,
          ruleId,
          lessonCompleted: true,
          practiceScore: 0,
          lastPracticeDate: Date.now(),
          totalAttempts: 0,
          successfulAttempts: 0,
        };
        updated = [...prev, newEntry];
      }

      saveProgress(updated).catch(() => {});
      return updated;
    });
  }

  const getProgressForRule = useCallback(
    (ruleId: number): UserProgress | undefined => {
      return progress.find((p) => p.ruleId === ruleId);
    },
    [progress]
  );

  const getOverallScore = useCallback((): number => {
    if (progress.length === 0) return 0;
    const total = progress.reduce((sum, p) => sum + p.practiceScore, 0);
    return Math.round(total / progress.length);
  }, [progress]);

  const getWeeklyActivity = useCallback((): number[] => {
    const now = Date.now();
    const dayMs = 24 * 60 * 60 * 1000;
    const days: number[] = [0, 0, 0, 0, 0, 0, 0];

    for (let i = 0; i < 7; i++) {
      const dayStart = now - (6 - i) * dayMs;
      const dayEnd = dayStart + dayMs;
      const count = progress.filter(
        (p) => p.lastPracticeDate >= dayStart && p.lastPracticeDate < dayEnd
      ).length;
      days[i] = count;
    }

    return days;
  }, [progress]);

  const getStreak = useCallback((): number => {
    if (progress.length === 0) return 0;

    const dayMs = 24 * 60 * 60 * 1000;
    const now = Date.now();
    let streak = 0;

    for (let i = 0; i < 365; i++) {
      const dayStart = now - i * dayMs;
      const dayEnd = dayStart + dayMs;
      const hasActivity = progress.some(
        (p) => p.lastPracticeDate >= dayStart - dayMs && p.lastPracticeDate < dayEnd
      );
      if (hasActivity) {
        streak++;
      } else {
        break;
      }
    }

    return streak;
  }, [progress]);

  const getMasteredRulesCount = useCallback((): number => {
    return progress.filter((p) => p.practiceScore >= 80).length;
  }, [progress]);

  return (
    <ProgressContext.Provider
      value={{
        progress,
        updateProgress,
        markLessonComplete,
        getProgressForRule,
        getOverallScore,
        getWeeklyActivity,
        getStreak,
        getMasteredRulesCount,
      }}
    >
      {children}
    </ProgressContext.Provider>
  );
}

export function useProgress(): ProgressContextType {
  const context = useContext(ProgressContext);
  if (!context) {
    throw new Error('useProgress must be used within a ProgressProvider');
  }
  return context;
}
