import React from 'react';
import { View, Text, StyleSheet, TextStyle } from 'react-native';
import { Colors } from '../constants/colors';

interface TajweedColorTextProps {
  text: string;
  ruleIds: string;
  style?: TextStyle;
}

const ruleColorMap: Record<number, string> = {
  1: Colors.tajweed.izhor,
  2: Colors.tajweed.idghom,
  3: Colors.tajweed.idghom,
  4: Colors.tajweed.ikhfo,
  5: Colors.tajweed.iqlab,
  6: Colors.tajweed.ghunna,
  7: Colors.tajweed.qalqala,
  8: Colors.tajweed.madd,
  9: Colors.tajweed.madd,
  10: Colors.tajweed.madd,
  11: Colors.tajweed.ikhfo,
  12: Colors.tajweed.idghom,
};

export function TajweedColorText({ text, ruleIds, style }: TajweedColorTextProps) {
  const ids = ruleIds.split(',').map((id) => parseInt(id.trim(), 10));
  const primaryColor = ids.length > 0 ? ruleColorMap[ids[0]] || Colors.primary.main : Colors.primary.main;

  return (
    <View style={styles.container}>
      <Text style={[styles.arabic, { color: primaryColor }, style]}>
        {text}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'flex-end',
  },
  arabic: {
    fontSize: 26,
    lineHeight: 46,
    textAlign: 'right',
    writingDirection: 'rtl',
  },
});
