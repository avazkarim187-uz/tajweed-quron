import { Colors } from './colors';

export interface AppTheme {
  colors: {
    primary: string;
    primaryDark: string;
    primaryLight: string;
    primaryContainer: string;
    onPrimaryContainer: string;
    secondary: string;
    secondaryLight: string;
    secondaryContainer: string;
    onSecondaryContainer: string;
    tertiary: string;
    tertiaryLight: string;
    tertiaryContainer: string;
    onTertiaryContainer: string;
    background: string;
    surface: string;
    surfaceVariant: string;
    onSurface: string;
    onSurfaceVariant: string;
    error: string;
    errorContainer: string;
    onErrorContainer: string;
    onPrimary: string;
    onSecondary: string;
    onTertiary: string;
  };
  spacing: {
    xs: number;
    sm: number;
    md: number;
    lg: number;
    xl: number;
    xxl: number;
  };
  typography: {
    titleLarge: { fontSize: number; fontWeight: string };
    titleMedium: { fontSize: number; fontWeight: string };
    titleSmall: { fontSize: number; fontWeight: string };
    bodyLarge: { fontSize: number; fontWeight: string };
    bodyMedium: { fontSize: number; fontWeight: string };
    bodySmall: { fontSize: number; fontWeight: string };
    labelLarge: { fontSize: number; fontWeight: string };
    labelMedium: { fontSize: number; fontWeight: string };
  };
  borderRadius: {
    sm: number;
    md: number;
    lg: number;
    xl: number;
    full: number;
  };
}

const spacing = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48,
};

const typography = {
  titleLarge: { fontSize: 22, fontWeight: '700' as const },
  titleMedium: { fontSize: 18, fontWeight: '600' as const },
  titleSmall: { fontSize: 16, fontWeight: '600' as const },
  bodyLarge: { fontSize: 16, fontWeight: '400' as const },
  bodyMedium: { fontSize: 14, fontWeight: '400' as const },
  bodySmall: { fontSize: 12, fontWeight: '400' as const },
  labelLarge: { fontSize: 14, fontWeight: '500' as const },
  labelMedium: { fontSize: 12, fontWeight: '500' as const },
};

const borderRadius = {
  sm: 4,
  md: 8,
  lg: 12,
  xl: 16,
  full: 9999,
};

export const lightTheme: AppTheme = {
  colors: {
    primary: Colors.primary.main,
    primaryDark: Colors.primary.dark,
    primaryLight: Colors.primary.light,
    primaryContainer: Colors.primary.container,
    onPrimaryContainer: Colors.primary.onContainer,
    secondary: Colors.secondary.main,
    secondaryLight: Colors.secondary.light,
    secondaryContainer: Colors.secondary.container,
    onSecondaryContainer: Colors.secondary.onContainer,
    tertiary: Colors.tertiary.main,
    tertiaryLight: Colors.tertiary.light,
    tertiaryContainer: Colors.tertiary.container,
    onTertiaryContainer: Colors.tertiary.onContainer,
    background: Colors.background.light,
    surface: Colors.surface.light,
    surfaceVariant: Colors.surface.variant,
    onSurface: Colors.onSurface.light,
    onSurfaceVariant: Colors.onSurface.variant,
    error: Colors.error.main,
    errorContainer: Colors.error.container,
    onErrorContainer: Colors.error.onContainer,
    onPrimary: Colors.onPrimary,
    onSecondary: Colors.onSecondary,
    onTertiary: Colors.onTertiary,
  },
  spacing,
  typography,
  borderRadius,
};

export const darkTheme: AppTheme = {
  colors: {
    primary: Colors.primary.light,
    primaryDark: Colors.primary.main,
    primaryLight: Colors.primary.light,
    primaryContainer: Colors.primary.dark,
    onPrimaryContainer: Colors.primary.container,
    secondary: Colors.secondary.light,
    secondaryLight: Colors.secondary.main,
    secondaryContainer: Colors.secondary.onContainer,
    onSecondaryContainer: Colors.secondary.container,
    tertiary: Colors.tertiary.light,
    tertiaryLight: Colors.tertiary.main,
    tertiaryContainer: Colors.tertiary.onContainer,
    onTertiaryContainer: Colors.tertiary.container,
    background: Colors.background.dark,
    surface: Colors.surface.dark,
    surfaceVariant: Colors.surface.variantDark,
    onSurface: Colors.onSurface.dark,
    onSurfaceVariant: Colors.onSurface.dark,
    error: Colors.error.container,
    errorContainer: Colors.error.onContainer,
    onErrorContainer: Colors.error.container,
    onPrimary: Colors.primary.dark,
    onSecondary: Colors.secondary.container,
    onTertiary: Colors.tertiary.onContainer,
  },
  spacing,
  typography,
  borderRadius,
};

export function getTheme(isDark: boolean): AppTheme {
  return isDark ? darkTheme : lightTheme;
}
