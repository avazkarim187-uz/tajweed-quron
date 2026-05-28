import { View, Text, StyleSheet } from 'react-native';

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>AI Tajweed Ustoz</Text>
      <Text style={styles.subtitle}>
        Tajweed qoidalarini o'rganing va mashq qiling
      </Text>
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
    fontSize: 28,
    fontWeight: 'bold',
    color: '#1B5E20',
    marginBottom: 12,
  },
  subtitle: {
    fontSize: 16,
    color: '#424242',
    textAlign: 'center',
  },
});
