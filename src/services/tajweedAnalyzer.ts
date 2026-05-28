export interface TajweedError {
  position: number;
  expected: string;
  got: string;
  type: string;
}

export interface TajweedAnalysisResult {
  score: number;
  errors: TajweedError[];
  feedback: string;
}

export function normalizeArabic(text: string): string {
  return text
    .replace(/[\u0610-\u061A\u064B-\u065F\u0670\u06D6-\u06DC\u06DF-\u06E8\u06EA-\u06ED\uFE70-\uFE7F]/g, '')
    .replace(/\s+/g, ' ')
    .trim();
}

function calculateSimilarity(str1: string, str2: string): number {
  if (str1 === str2) return 100;
  if (str1.length === 0 || str2.length === 0) return 0;

  const len1 = str1.length;
  const len2 = str2.length;

  const matrix: number[][] = [];

  for (let i = 0; i <= len1; i++) {
    matrix[i] = [i];
  }
  for (let j = 0; j <= len2; j++) {
    matrix[0][j] = j;
  }

  for (let i = 1; i <= len1; i++) {
    for (let j = 1; j <= len2; j++) {
      const cost = str1[i - 1] === str2[j - 1] ? 0 : 1;
      matrix[i][j] = Math.min(
        matrix[i - 1][j] + 1,
        matrix[i][j - 1] + 1,
        matrix[i - 1][j - 1] + cost
      );
    }
  }

  const distance = matrix[len1][len2];
  const maxLen = Math.max(len1, len2);
  return Math.round((1 - distance / maxLen) * 100);
}

function getFeedback(score: number): string {
  if (score >= 90) return 'Ajoyib! Talaffuzingiz juda yaxshi!';
  if (score >= 70) return 'Yaxshi! Ozgina xatolar bor.';
  if (score >= 50) return "O'rtacha. Mashq qilishda davom eting.";
  return "Ko'proq mashq kerak. Qaytadan urinib ko'ring.";
}

// NOTE: This is a pronunciation accuracy checker, not a full tajweed rule compliance analyzer.
// It compares the consonant skeleton of the recitation against the expected text using
// Levenshtein distance after stripping diacritical marks. A true tajweed analyzer would
// require a custom ML model that evaluates ghunna, madd, ikhfa, and other rule-specific
// application from the audio signal directly.

export function analyzeRecitation(
  expectedText: string,
  transcribedText: string
): TajweedAnalysisResult {
  const normalizedExpected = normalizeArabic(expectedText);
  const normalizedTranscribed = normalizeArabic(transcribedText);

  const score = calculateSimilarity(normalizedExpected, normalizedTranscribed);

  const expectedWords = normalizedExpected.split(' ');
  const transcribedWords = normalizedTranscribed.split(' ');

  const errors: TajweedError[] = [];
  const maxWords = Math.max(expectedWords.length, transcribedWords.length);

  for (let i = 0; i < maxWords; i++) {
    const expected = expectedWords[i] || '';
    const got = transcribedWords[i] || '';

    if (expected !== got) {
      let type = 'mismatch';
      if (!got) type = 'missing';
      else if (!expected) type = 'extra';

      errors.push({
        position: i,
        expected,
        got,
        type,
      });
    }
  }

  const feedback = getFeedback(score);

  return { score, errors, feedback };
}
