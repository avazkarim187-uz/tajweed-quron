import React from 'react';
import { Text, StyleSheet, TextStyle } from 'react-native';

interface ArabicTextProps {
  text: string;
  style?: TextStyle;
  size?: number;
}

export function ArabicText({ text, style, size = 28 }: ArabicTextProps) {
  return (
    <Text style={[styles.arabic, { fontSize: size, lineHeight: size * 1.8 }, style]}>
      {text}
    </Text>
  );
}

const styles = StyleSheet.create({
  arabic: {
    textAlign: 'right',
    writingDirection: 'rtl',
    fontWeight: '400',
    color: '#1C1B1F',
  },
});
