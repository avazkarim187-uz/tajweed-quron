import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Colors } from '../constants/colors';

interface ScoreCircleProps {
  score: number;
  size?: number;
  color?: string;
}

export function ScoreCircle({ score, size = 120, color }: ScoreCircleProps) {
  const circleColor = color || (score >= 80 ? Colors.primary.main : score >= 50 ? Colors.secondary.main : Colors.error.main);
  const borderWidth = size * 0.08;

  return (
    <View
      style={[
        styles.circle,
        {
          width: size,
          height: size,
          borderRadius: size / 2,
          borderWidth,
          borderColor: circleColor,
        },
      ]}
    >
      <Text style={[styles.scoreText, { fontSize: size * 0.28, color: circleColor }]}>
        {score}%
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  circle: {
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'transparent',
  },
  scoreText: {
    fontWeight: '700',
  },
});
