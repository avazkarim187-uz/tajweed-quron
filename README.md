# AI Tajweed Ustoz - Tajvid o'rgatuvchi ilova

## Ilova haqida (About)

AI Tajweed Ustoz - bu sun'iy intellekt yordamida Qur'on tilovati va tajvid qoidalarini o'rgatuvchi React Native/Expo ilovasi. Ilova OpenAI Whisper API orqali foydalanuvchining ovozini tahlil qilib, tajvid qoidalariga rioya qilishni baholaydi va xatolarni real-time ko'rsatadi.

## Xususiyatlar (Features)

- 12 ta tajvid qoidalari darslari (Izhor, Idg'om, Ikhfo, Iqlab, Ghunna, Qalqala, Madd va boshqalar)
- Qur'on oyatlari (Fotiha, Yosin, Mulk, Kavsar, Ixlos, Falaq, Nos suralari) bet raqamlari bilan
- Ovozni yozib olish va AI tahlili (OpenAI Whisper API)
- Real-time tajvid xatolari ko'rsatish (rangli belgilash)
- Har bir qoida bo'yicha mashq rejimi
- Progress tracking (o'sish kuzatuvi) - haftalik faollik
- Sozlamalar (API kalit, mavzu sozlash)
- Zamonaviy UI - islomiy yashil/oltin rang palitrasi
- Yorug'/Qorong'u mavzu qo'llab-quvvatlash

## Tech Stack

- Expo SDK 51
- React Native + TypeScript
- expo-av (audio yozib olish)
- expo-router (navigatsiya)
- AsyncStorage (mahalliy ma'lumotlar)
- OpenAI Whisper API (nutqni tahlil qilish)
- React Context (holat boshqaruvi)

## O'rnatish (Setup)

### Talablar (Requirements)

- Node.js 18 yoki undan yuqori
- npm yoki yarn
- Expo Go ilovasi (telefonda)

### Qadamlar (Steps)

1. Repositoriyani klonlash va ishga tushirish:

```bash
git clone https://github.com/avazkarim187-uz/tajweed-quron.git
cd tajweed-quron
npm install
npx expo start
```

2. Telefonda Expo Go ilovasini oching va QR kodni skanerlang.

## OpenAI API sozlash

1. https://platform.openai.com/ ga o'ting
2. API kalit yarating
3. Ilovada Sozlamalar -> API kalitni kiriting

## Loyiha tuzilmasi (Project Structure)

```
tajweed-quron/
├── app/                    - Expo Router sahifalar
│   ├── (tabs)/            - Tab navigatsiya ekranlari
│   │   ├── index.tsx      - Bosh sahifa
│   │   ├── lessons.tsx    - Darslar ro'yxati
│   │   ├── quran.tsx      - Qur'on suralari
│   │   ├── practice.tsx   - Mashq ro'yxati
│   │   └── progress.tsx   - O'sish
│   ├── lesson/[id].tsx    - Dars tafsiloti
│   ├── quran/[surahNumber].tsx - Sura o'qish
│   ├── practice/[ruleId].tsx   - Mashq sessiyasi
│   ├── recording.tsx      - Ovoz yozish
│   ├── feedback/[id].tsx  - Natija
│   └── settings.tsx       - Sozlamalar
├── src/
│   ├── components/        - Qayta ishlatiladigan komponentlar
│   ├── constants/         - Ranglar va mavzu
│   ├── contexts/          - React Context'lar
│   ├── data/              - Tajvid qoidalari va Qur'on ma'lumotlari
│   └── services/          - API va xizmatlar
├── app.json               - Expo konfiguratsiya
├── package.json           - Bog'liqliklar
└── tsconfig.json          - TypeScript sozlamalari
```

## Tajvid ranglari (Tajweed Colors)

| Rang | Qoida | Tavsif |
|------|-------|--------|
| Yashil (#4CAF50) | Izhor | Aniq o'qish |
| Ko'k (#2196F3) | Idg'om | Qo'shish |
| To'q sariq (#FF9800) | Ikhfo | Yashirish |
| Binafsha (#9C27B0) | Iqlab | Aylantirish |
| Qizil (#F44336) | Ghunna | Burun ovozi |
| Jigarrang (#795548) | Qalqala | Qaltirash |
| Zangori (#009688) | Madd | Cho'zish |

## Ekranlar (Screens)

1. **Bosh sahifa** - Tezkor navigatsiya, kundalik mashq, statistika
2. **Darslar** - 12 ta tajvid qoidalari, misollar bilan
3. **Qur'on** - Suralar ro'yxati, oyatlarni o'qish, bet raqamlari
4. **Mashq** - Qoida bo'yicha mashq, yozib olish va AI tahlili
5. **O'sish** - Umumiy ball, haftalik faollik, qoida bo'yicha natijalar
6. **Sozlamalar** - API kalit, mavzu, ma'lumotlarni tozalash

## Ishga tushirish (Running)

```bash
npx expo start
```

Development build yoki Expo Go ilovasi orqali ishga tushirishingiz mumkin. Expo Go ilovasida QR kodni skanerlang va ilova telefoningizda ishga tushadi.

## Eslatma (Note)

Firebase integratsiyasi keyinchalik qo'shilishi mumkin. Hozircha barcha ma'lumotlar AsyncStorage orqali mahalliy saqlanadi.

## Hissa qo'shish (Contributing)

1. Fork qiling
2. Feature branch yarating (`git checkout -b feature/yangi-xususiyat`)
3. O'zgarishlarni commit qiling (`git commit -m 'feat: yangi xususiyat'`)
4. Branch'ga push qiling (`git push origin feature/yangi-xususiyat`)
5. Pull Request oching

## Litsenziya (License)

MIT License
