# AI Tajweed Ustoz - Tajvid o'rgatuvchi ilova

## Ilova haqida (About)

AI Tajweed Ustoz - bu sun'iy intellekt yordamida Qur'on tilovati va tajvid qoidalarini o'rgatuvchi Android ilovasi.

## Xususiyatlar (Features)

- 12 ta tajvid qoidalari darslari (Izhor, Idg'om, Ikhfo, Iqlab, Ghunna, Qalqala, Madd va boshqalar)
- Qur'on oyatlari (Fotiha, Yosin, Mulk, Ixlos, Falaq, Nos, Kavsar suralari)
- Ovozni yozib olish va AI tahlili (OpenAI Whisper API)
- Real-time tajvid xatolari ko'rsatish (rangli belgilash)
- Har bir qoida bo'yicha mashq rejimi
- Progress tracking (o'sish kuzatuvi)
- Firebase autentifikatsiya va sinxronlash
- Offline rejim (Room DB)

## Tech Stack

- Kotlin + Jetpack Compose (Native Android)
- OpenAI Whisper API (nutqni matnga o'girish)
- Firebase (Auth, Firestore, Storage)
- Room DB (mahalliy ma'lumotlar bazasi)
- Hilt (Dependency Injection)
- Material3 Design

## O'rnatish (Setup)

### Talablar (Requirements)

- Android Studio Hedgehog (2023.1.1) yoki undan yuqori
- JDK 17
- Android SDK 34
- Min Android 7.0 (API 24)

### Qadamlar (Steps)

1. Repositoriyani klonlash:

```bash
git clone https://github.com/avazkarim187-uz/tajweed-quron.git
```

2. Android Studio'da ochish

3. Firebase sozlash:
   - https://console.firebase.google.com/ ga o'ting
   - Yangi loyiha yarating
   - Android ilovani qo'shing (package: com.tajweed.ustoz)
   - google-services.json ni yuklab oling
   - app/google-services.json faylni almashtiring
   - Authentication bo'limida Email/Password va Google Sign-In ni yoqing
   - Firestore Database yarating

4. OpenAI API kalitini olish:
   - https://platform.openai.com/ ga o'ting
   - API kalit yarating
   - Ilovada Settings -> API sozlamalari orqali kiriting

5. Gradle Sync va Run

## Loyiha tuzilmasi (Project Structure)

```
app/src/main/java/com/tajweed/ustoz/
├── data/           - modellar, Room DB, repository, remote services
│   ├── local/      - Room DAO, database, converters
│   ├── model/      - data class'lar (TajweedRule, QuranAyah, etc.)
│   ├── remote/     - Firebase, Whisper API service
│   └── repository/ - repository interface va implementation
├── di/             - Hilt dependency injection modullari
├── domain/         - use case'lar
├── navigation/     - ekranlar va navigatsiya
└── ui/             - Jetpack Compose ekranlar va komponentlar
    ├── components/ - qayta ishlatiladigan UI komponentlar
    ├── screens/    - barcha ekranlar (home, lessons, quran, practice, recording, progress, settings, auth)
    └── theme/      - rang, tipografiya, shakl, mavzu
```

## Tajvid ranglari (Tajweed Colors)

| Rang | Qoida | Tavsif |
|------|-------|--------|
| Yashil | Izhor | Aniq o'qish |
| Ko'k | Idg'om | Qo'shish |
| To'q sariq | Ikhfo | Yashirish |
| Binafsha | Iqlab | Aylantirish |
| Qizil | Ghunna | Burun ovozi |
| Jigarrang | Qalqala | Qaltirash |
| Zangori | Madd | Cho'zish |

## Ekranlar (Screens)

1. **Bosh sahifa** - Tezkor navigatsiya, kundalik mashq, so'nggi natijalar
2. **Darslar** - 12 ta tajvid qoidalari, har biri misollar bilan
3. **Qur'on** - Suralar ro'yxati, oyatlarni o'qish va yozib olish
4. **Mashq** - Qoida bo'yicha mashq, yozib olish va AI tahlili
5. **O'sish** - Umumiy ball, haftalik faollik, qoida bo'yicha natijalar
6. **Sozlamalar** - Til, mavzu, API kalit, hisob boshqaruvi

## API Integratsiya

### OpenAI Whisper API

Ilova OpenAI Whisper API'dan foydalanib ovozli yozuvlarni matnga o'giradi va tajvid tahlilini amalga oshiradi.

```
POST https://api.openai.com/v1/audio/transcriptions
Authorization: Bearer YOUR_API_KEY
Content-Type: multipart/form-data
```

### Firebase

- **Authentication**: Email/password va Google Sign-In
- **Firestore**: Foydalanuvchi progressini sinxronlash
- **Storage**: Audio fayllarni saqlash (kelajakda)

## Ishga tushirish (Running)

```bash
./gradlew assembleDebug
./gradlew installDebug
```

## Testlar (Testing)

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Hissa qo'shish (Contributing)

1. Fork qiling
2. Feature branch yarating (`git checkout -b feature/yangi-xususiyat`)
3. O'zgarishlarni commit qiling (`git commit -m 'feat: yangi xususiyat'`)
4. Branch'ga push qiling (`git push origin feature/yangi-xususiyat`)
5. Pull Request oching

## Litsenziya (License)

MIT License
