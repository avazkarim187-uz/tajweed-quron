import React, { createContext, useContext, ReactNode } from 'react';

interface ThemeColors {
  primary: string;
  primaryDark: string;
  accent: string;
  background: string;
  surface: string;
  text: string;
  textSecondary: string;
}

interface ThemeContextType {
  colors: ThemeColors;
}

const defaultColors: ThemeColors = {
  primary: '#1B5E20',
  primaryDark: '#0D3B12',
  accent: '#FFD700',
  background: '#FFFFFF',
  surface: '#F5F5F5',
  text: '#212121',
  textSecondary: '#757575',
};

const ThemeContext = createContext<ThemeContextType>({ colors: defaultColors });

interface ThemeProviderProps {
  children: ReactNode;
}

export function ThemeProvider({ children }: ThemeProviderProps) {
  return (
    <ThemeContext.Provider value={{ colors: defaultColors }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  return useContext(ThemeContext);
}
