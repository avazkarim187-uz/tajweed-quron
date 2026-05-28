export async function transcribeAudio(
  audioUri: string,
  apiKey: string
): Promise<{ success: boolean; text?: string; error?: string }> {
  if (!apiKey) {
    return {
      success: false,
      error: "API kalit sozlanmagan. Sozlamalar bo'limida API kalitni kiriting.",
    };
  }

  try {
    const formData = new FormData();
    formData.append('file', {
      uri: audioUri,
      name: 'audio.m4a',
      type: 'audio/m4a',
    } as unknown as Blob);
    formData.append('model', 'whisper-1');
    formData.append('language', 'ar');

    const apiResponse = await fetch(
      'https://api.openai.com/v1/audio/transcriptions',
      {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${apiKey}`,
        },
        body: formData,
      }
    );

    if (!apiResponse.ok) {
      const errorData = await apiResponse.json().catch(() => null);
      const message =
        errorData?.error?.message || `Xatolik yuz berdi (${apiResponse.status})`;
      return { success: false, error: message };
    }

    const data = await apiResponse.json();
    return { success: true, text: data.text };
  } catch (err: unknown) {
    const message =
      err instanceof Error ? err.message : "Noma'lum xatolik yuz berdi";
    return { success: false, error: message };
  }
}
