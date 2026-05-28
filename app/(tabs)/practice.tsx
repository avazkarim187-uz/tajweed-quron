import { View, Text, StyleSheet } from 'react-native';

export default function PracticeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Mashq</Text>
      <Text style={styles.subtitle}>Qiroat mashqlari</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
    backgroundColor: '#FFFFFF',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#1B5E20',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: '#424242',
  },
});
