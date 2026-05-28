package com.tajweed.ustoz.data.local

import com.tajweed.ustoz.data.model.TajweedRule

object TajweedData {

    fun getDefaultRules(): List<TajweedRule> = listOf(
        TajweedRule(
            id = 1,
            name = "إظهار",
            nameUz = "Izhori halqiy",
            description = "Nun sokin yoki tanvin halq harflaridan oldin kelganda aniq o'qiladi",
            arabicExample = "مِنْ عِلْمٍ|مَنْ آمَنَ|مِنْ هَادٍ|يَنْأَوْنَ|مِنْ حَكِيمٍ|مِنْ غِلٍّ|مِنْ خَيْرٍ",
            explanation = "Izhor - nun sokin yoki tanvin 6 ta halq harflaridan (ء ه ع ح غ خ) oldin kelganda, nun sokin yoki tanvin aniq va ravshan o'qiladi. Buning sababi - nun harfining makhraji halq harflaridan uzoqda joylashgan.",
            audioUrl = null,
            category = "Nun Sakin",
            lettersList = "ء ه ع ح غ خ"
        ),
        TajweedRule(
            id = 2,
            name = "إدغام بلا غنة",
            nameUz = "Idg'om bila g'unna",
            description = "Nun sokin lom yoki ro harflaridan oldin kelganda g'unnasiz qo'shiladi",
            arabicExample = "مِن رَّبِّهِمْ|مِن لَّدُنْهُ|مِن رَّحِيمٍ|يَكُن لَّهُ",
            explanation = "Idg'om bila g'unna - nun sokin yoki tanvin lom (ل) yoki ro (ر) harflaridan oldin kelganda, nun sokin keyingi harfga qo'shiladi va g'unna (burun ovozi) chiqarilmaydi. Natijada faqat lom yoki ro harfi tashdid bilan o'qiladi.",
            audioUrl = null,
            category = "Nun Sakin",
            lettersList = "ل ر"
        ),
        TajweedRule(
            id = 3,
            name = "إدغام مع الغنة",
            nameUz = "Idg'om ma'al g'unna",
            description = "Nun sokin yo, nun, mim yoki vov harflaridan oldin kelganda g'unna bilan qo'shiladi",
            arabicExample = "مَن يَقُولُ|مِن نِّعْمَةٍ|مِن مَّاءٍ|مِن وَلِيٍّ",
            explanation = "Idg'om ma'al g'unna - nun sokin yoki tanvin yo (ي), nun (ن), mim (م) yoki vov (و) harflaridan oldin kelganda, nun sokin keyingi harfga qo'shiladi va 2 haraka g'unna (burun ovozi) chiqariladi. Bu harflar 'yanmu' so'zi bilan yodda saqlanadi.",
            audioUrl = null,
            category = "Nun Sakin",
            lettersList = "ي ن م و"
        ),
        TajweedRule(
            id = 4,
            name = "إخفاء",
            nameUz = "Ikhfo haqiqiy",
            description = "Nun sokin 15 ta ikhfo harflaridan oldin kelganda yashirinadi",
            arabicExample = "مِنْ قَبْلِ|أَنْتُمْ|مَنْصُوراً|يُنْفِقُونَ|مِنْ ذَلِكَ",
            explanation = "Ikhfo haqiqiy - nun sokin yoki tanvin 15 ta ikhfo harflaridan oldin kelganda, nun harfi izhor va idg'om orasida - ya'ni yashirin holda o'qiladi. Bu holatda g'unna 2 haraka davom etadi. Ikhfo harflari: ت ث ج د ذ ز س ش ص ض ط ظ ف ق ك",
            audioUrl = null,
            category = "Nun Sakin",
            lettersList = "ت ث ج د ذ ز س ش ص ض ط ظ ف ق ك"
        ),
        TajweedRule(
            id = 5,
            name = "إقلاب",
            nameUz = "Iqlab",
            description = "Nun sokin yoki tanvin 'bo' harfidan oldin kelganda mim'ga aylanadi",
            arabicExample = "مِنْ بَعْدِ|أَنبِئْهُم|سَمِيعٌ بَصِيرٌ|مِنْ بَيْنِهِمْ",
            explanation = "Iqlab - nun sokin yoki tanvin bo (ب) harfidan oldin kelganda, nun yoki tanvin mim (م) harfiga aylantiriladi va g'unna bilan ikhfo qilinadi (ya'ni lablar yopilgan holda burun orqali ovoz chiqariladi). Bu qoidaning yagona harfi bo (ب).",
            audioUrl = null,
            category = "Nun Sakin",
            lettersList = "ب"
        ),
        TajweedRule(
            id = 6,
            name = "غنة",
            nameUz = "G'unna",
            description = "Mushaddad nun yoki meem harflarida 2 haraka g'unna chiqariladi",
            arabicExample = "إِنَّ|ثُمَّ|مِنَ النَّاسِ|إِنَّا أَعْطَيْنَاكَ",
            explanation = "G'unna - burun bo'shlig'idan chiqadigan yoqimli tovush. Mushaddad (tashdidli) nun (نّ) yoki mim (مّ) harflarida 2 haraka miqdorida g'unna chiqariladi. G'unna eng kuchli holati mushaddad harflarda namoyon bo'ladi.",
            audioUrl = null,
            category = "Ghunna",
            lettersList = "نّ مّ"
        ),
        TajweedRule(
            id = 7,
            name = "قلقلة",
            nameUz = "Qalqala",
            description = "Qof, to, bo, jim, dol harflari sokin bo'lganda qaltiraydigan ovoz chiqadi",
            arabicExample = "الْحَقُّ|يَجْعَلُونَ|خَلَقْنَا|أَحَدْ|يَطْمَعُ|بِسَبَبٍ",
            explanation = "Qalqala - qof (ق), to (ط), bo (ب), jim (ج), dol (د) harflari sokin bo'lganda ularning makhrajida qaltiraydigan (sakraydigan) ovoz hosil bo'ladi. Bu harflar 'qutb jad' iborasi bilan yodda saqlanadi. Qalqala kuchli (kubra) va kuchsiz (sughra) bo'ladi.",
            audioUrl = null,
            category = "Qalqala",
            lettersList = "ق ط ب ج د"
        ),
        TajweedRule(
            id = 8,
            name = "مد طبيعي",
            nameUz = "Madd tabii'iy",
            description = "Madd harfi (alif, vov, yo) kelganda 2 haraka cho'ziladi",
            arabicExample = "قَالَ|يَقُولُ|فِيهَا|نُوحِيهَا|كِتَابٌ",
            explanation = "Madd tabii'iy (tabiiy cho'zilish) - madd harflaridan (alif, vov, yo) keyin hamza yoki sukun kelmagan holatda 2 haraka cho'ziladi. Alif oldida fat'ha, vov oldida dhamma, yo oldida kasra bo'lishi shart. Bu eng oddiy va asosiy madd turi.",
            audioUrl = null,
            category = "Madd",
            lettersList = "ا و ي"
        ),
        TajweedRule(
            id = 9,
            name = "مد متصل",
            nameUz = "Madd muttasil",
            description = "Madd harfidan keyin hamza bir so'zda kelganda 4-5 haraka cho'ziladi",
            arabicExample = "جَاءَ|سُوءٌ|جِيءَ|السَّمَاءِ|شَاءَ",
            explanation = "Madd muttasil (bog'liq madd) - madd harfidan keyin hamza (ء) bir so'z ichida kelganda, 4-5 haraka cho'ziladi. Bu madd vojib (majburiy) hisoblanadi, ya'ni uni qisqartirish mumkin emas. Madd muttasil Qur'onda juda ko'p uchraydi.",
            audioUrl = null,
            category = "Madd",
            lettersList = "ا و ي + ء (bir so'zda)"
        ),
        TajweedRule(
            id = 10,
            name = "مد منفصل",
            nameUz = "Madd munfasil",
            description = "Madd harfidan keyin hamza keyingi so'zda kelganda 4-5 haraka cho'ziladi",
            arabicExample = "بِمَا أُنْزِلَ|قَالُوا آمَنَّا|فِي أَنفُسِهِمْ|يَا أَيُّهَا",
            explanation = "Madd munfasil (ajratilgan madd) - birinchi so'z madd harfi bilan tugab, keyingi so'z hamza bilan boshlanganda 4-5 haraka cho'ziladi. Bu madd joiz (ixtiyoriy) hisoblanadi. Ba'zi qiroat uslublarida 2 haraka ham qabul qilinadi.",
            audioUrl = null,
            category = "Madd",
            lettersList = "ا و ي + ء (ikki so'zda)"
        ),
        TajweedRule(
            id = 11,
            name = "إخفاء شفوي",
            nameUz = "Ikhfo shafaviy",
            description = "Mim sokin 'bo' harfidan oldin kelganda labda yashirinadi",
            arabicExample = "تَرْمِيهِمْ بِحِجَارَةٍ|أَنتُمْ بِهِ|يَعْتَصِمْ بِاللَّهِ|هُمْ بَارِزُونَ",
            explanation = "Ikhfo shafaviy (lab ikhfosi) - mim sokin (مْ) bo (ب) harfidan oldin kelganda, mim harfi lablar orasida yashirin holda o'qiladi va 2 haraka g'unna chiqariladi. Bu qoida faqat mim sokin va bo harfi orasida amal qiladi.",
            audioUrl = null,
            category = "Meem Sakin",
            lettersList = "ب"
        ),
        TajweedRule(
            id = 12,
            name = "إدغام شفوي",
            nameUz = "Idg'om shafaviy",
            description = "Mim sokin mim harfidan oldin kelganda qo'shilib g'unna bilan o'qiladi",
            arabicExample = "لَهُمْ مَا|كُنْتُمْ مُؤْمِنِينَ|أَمْ مَنْ|هُمْ مِنْ",
            explanation = "Idg'om shafaviy (idg'omi mithlain) - mim sokin (مْ) mim (م) harfidan oldin kelganda, ikki mim birlashib, mushaddad mim hosil bo'ladi va 2 haraka g'unna chiqariladi. Bunda lablar to'liq yopiladi.",
            audioUrl = null,
            category = "Meem Sakin",
            lettersList = "م"
        )
    )
}
