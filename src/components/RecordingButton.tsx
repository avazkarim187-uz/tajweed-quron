import React from 'react';
import { TouchableOpacity, StyleSheet, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { Colors } from '../constants/colors';

interface RecordingButtonProps {
  isRecording: boolean;
  onPress: () => void;
  size?: number;
}

export function RecordingButton({ isRecording, onPress, size = 80 }: RecordingButtonProps) {
  const bgColor = isRecording ? Colors.error.main : Colors.primary.main;

  return (
    <TouchableOpacity
      onPress={onPress}
      activeOpacity={0.7}
      style={[
        styles.button,
        {
          width: size,
          height: size,
          borderRadius: size / 2,
          backgroundColor: bgColor,
        },
      ]}
    >
      <View style={styles.innerContent}>
        <Ionicons
          name={isRecording ? 'stop' : 'mic'}
          size={size * 0.4}
          color="#FFFFFF"
        />
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    elevation: 6,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
  },
  innerContent: {
    alignItems: 'center',
    justifyContent: 'center',
  },
});
