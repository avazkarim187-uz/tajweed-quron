import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { Audio } from 'expo-av';
import { RecordingResult } from '../data/models';
import * as audioRecorder from '../services/audioRecorder';
import { getRecordingResults, saveRecordingResult } from '../services/storage';

interface RecordingState {
  isRecording: boolean;
  currentRecording: Audio.Recording | null;
  results: RecordingResult[];
}

interface RecordingContextType {
  state: RecordingState;
  startSession: () => Promise<void>;
  stopSession: () => Promise<string>;
  saveResult: (result: RecordingResult) => Promise<void>;
  getResults: () => RecordingResult[];
}

const RecordingContext = createContext<RecordingContextType | undefined>(undefined);

interface RecordingProviderProps {
  children: ReactNode;
}

export function RecordingProvider({ children }: RecordingProviderProps) {
  const [state, setState] = useState<RecordingState>({
    isRecording: false,
    currentRecording: null,
    results: [],
  });

  useEffect(() => {
    loadResults();
  }, []);

  async function loadResults() {
    try {
      const results = await getRecordingResults();
      setState((prev) => ({ ...prev, results }));
    } catch {
      // Keep empty
    }
  }

  async function startSession(): Promise<void> {
    const hasPermission = await audioRecorder.requestPermissions();
    if (!hasPermission) {
      throw new Error("Mikrofon ruxsati berilmagan. Sozlamalardan ruxsat bering.");
    }

    const recording = await audioRecorder.startRecording();
    setState((prev) => ({
      ...prev,
      isRecording: true,
      currentRecording: recording,
    }));
  }

  async function stopSession(): Promise<string> {
    if (!state.currentRecording) {
      throw new Error('Hech qanday yozuv topilmadi');
    }

    const uri = await audioRecorder.stopRecording(state.currentRecording);
    setState((prev) => ({
      ...prev,
      isRecording: false,
      currentRecording: null,
    }));
    return uri;
  }

  async function saveResult(result: RecordingResult): Promise<void> {
    await saveRecordingResult(result);
    setState((prev) => ({
      ...prev,
      results: [...prev.results, result],
    }));
  }

  function getResults(): RecordingResult[] {
    return state.results;
  }

  return (
    <RecordingContext.Provider
      value={{ state, startSession, stopSession, saveResult, getResults }}
    >
      {children}
    </RecordingContext.Provider>
  );
}

export function useRecording(): RecordingContextType {
  const context = useContext(RecordingContext);
  if (!context) {
    throw new Error('useRecording must be used within a RecordingProvider');
  }
  return context;
}
