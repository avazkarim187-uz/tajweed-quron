package com.tajweed.ustoz.data.local

import com.tajweed.ustoz.data.model.QuranAyah

object QuranData {

    fun getDefaultAyahs(): List<QuranAyah> = buildList {
        // Surah Al-Fatiha (1) - 7 oyat, 1-bet
        addAll(getSurahAlFatiha())
        // Surah Ya-Sin (36) - birinchi 5 oyat, 440-bet
        addAll(getSurahYaSin())
        // Surah Al-Mulk (67) - birinchi 5 oyat, 562-bet
        addAll(getSurahAlMulk())
        // Surah Al-Kawthar (108) - 3 oyat, 602-bet
        addAll(getSurahAlKawthar())
        // Surah Al-Ikhlas (112) - 4 oyat, 604-bet
        addAll(getSurahAlIkhlas())
        // Surah Al-Falaq (113) - 5 oyat, 604-bet
        addAll(getSurahAlFalaq())
        // Surah An-Nas (114) - 6 oyat, 604-bet
        addAll(getSurahAnNas())
    }

    private fun getSurahAlFatiha(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 1,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 1,
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",
            transliterationUz = "Bismillahir rohmanir rohiym",
            translationUz = "Mehribon va rahmli Allohning nomi bilan",
            tajweedRuleIds = "8,6",
            page = 1
        ),
        QuranAyah(
            id = 2,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 2,
            arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            transliterationUz = "Alhamdu lillahi robbil 'aalamiin",
            translationUz = "Barcha hamdlar olamlarning Robbi Allohga xosdir",
            tajweedRuleIds = "2,8,6",
            page = 1
        ),
        QuranAyah(
            id = 3,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 3,
            arabicText = "الرَّحْمَنِ الرَّحِيمِ",
            transliterationUz = "Ar-rohmanir rohiym",
            translationUz = "U Mehribon va Rahmlidir",
            tajweedRuleIds = "8,6",
            page = 1
        ),
        QuranAyah(
            id = 4,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 4,
            arabicText = "مَالِكِ يَوْمِ الدِّينِ",
            transliterationUz = "Maaliki yawmid diin",
            translationUz = "Jazo kunining Egasidir",
            tajweedRuleIds = "8,6",
            page = 1
        ),
        QuranAyah(
            id = 5,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 5,
            arabicText = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            transliterationUz = "Iyyaaka na'budu wa iyyaaka nasta'iin",
            translationUz = "Faqat Senga ibodat qilamiz va faqat Sendan yordam so'raymiz",
            tajweedRuleIds = "6,8,1",
            page = 1
        ),
        QuranAyah(
            id = 6,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 6,
            arabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            transliterationUz = "Ihdinas sirootal mustaqiym",
            translationUz = "Bizni to'g'ri yo'lga hidoyat qilgin",
            tajweedRuleIds = "8,7",
            page = 1
        ),
        QuranAyah(
            id = 7,
            surahNumber = 1,
            surahName = "الفاتحة",
            surahNameUz = "Fotiha",
            ayahNumber = 7,
            arabicText = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            transliterationUz = "Sirootal laziina an'amta 'alayhim g'oyril mag'duubi 'alayhim walad dooolliin",
            translationUz = "O'zing in'om qilganlarning yo'liga, g'azabga uchraganlarning va adashganlarning yo'liga emas",
            tajweedRuleIds = "1,8,4,6",
            page = 1
        )
    )

    private fun getSurahYaSin(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 100,
            surahNumber = 36,
            surahName = "يس",
            surahNameUz = "Yosin",
            ayahNumber = 1,
            arabicText = "يس",
            transliterationUz = "Yaa Siin",
            translationUz = "Yo Sin",
            tajweedRuleIds = "8",
            page = 440
        ),
        QuranAyah(
            id = 101,
            surahNumber = 36,
            surahName = "يس",
            surahNameUz = "Yosin",
            ayahNumber = 2,
            arabicText = "وَالْقُرْآنِ الْحَكِيمِ",
            transliterationUz = "Wal qur'aanil hakiim",
            translationUz = "Hikmatli Qur'onga qasam",
            tajweedRuleIds = "8,9",
            page = 440
        ),
        QuranAyah(
            id = 102,
            surahNumber = 36,
            surahName = "يس",
            surahNameUz = "Yosin",
            ayahNumber = 3,
            arabicText = "إِنَّكَ لَمِنَ الْمُرْسَلِينَ",
            transliterationUz = "Innaka laminal mursaliin",
            translationUz = "Albatta, sen yuborilgan payg'ambarlardansan",
            tajweedRuleIds = "6,8",
            page = 440
        ),
        QuranAyah(
            id = 103,
            surahNumber = 36,
            surahName = "يس",
            surahNameUz = "Yosin",
            ayahNumber = 4,
            arabicText = "عَلَى صِرَاطٍ مُسْتَقِيمٍ",
            transliterationUz = "'Alaa sirootim mustaqiim",
            translationUz = "To'g'ri yo'ldasan",
            tajweedRuleIds = "8,4",
            page = 440
        ),
        QuranAyah(
            id = 104,
            surahNumber = 36,
            surahName = "يس",
            surahNameUz = "Yosin",
            ayahNumber = 5,
            arabicText = "تَنْزِيلَ الْعَزِيزِ الرَّحِيمِ",
            transliterationUz = "Tanziilal 'aziizir rohiym",
            translationUz = "Qudratli va Rahmli Zotning nozil qilgani",
            tajweedRuleIds = "4,8,6",
            page = 440
        )
    )

    private fun getSurahAlMulk(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 200,
            surahNumber = 67,
            surahName = "الملك",
            surahNameUz = "Mulk",
            ayahNumber = 1,
            arabicText = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliterationUz = "Tabaarokallaazii biyadihil mulku wa huwa 'alaa kulli shay'in qodiir",
            translationUz = "Qo'lida mulk bo'lgan Zot barakotlidir. U har narsaga qodirdir",
            tajweedRuleIds = "8,6,9",
            page = 562
        ),
        QuranAyah(
            id = 201,
            surahNumber = 67,
            surahName = "الملك",
            surahNameUz = "Mulk",
            ayahNumber = 2,
            arabicText = "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا وَهُوَ الْعَزِيزُ الْغَفُورُ",
            transliterationUz = "Allaazii khalaqal mawta wal hayaata liyabluwakum ayyukum ahsanu 'amalaa wa huwal 'aziizul g'ofuur",
            translationUz = "U o'lim va hayotni yaratdi - sizlarning qaysingiz chiroyliroq amal qilishini sinash uchun. U Qudratli va Kechiruvchidir",
            tajweedRuleIds = "7,8,1",
            page = 562
        ),
        QuranAyah(
            id = 202,
            surahNumber = 67,
            surahName = "الملك",
            surahNameUz = "Mulk",
            ayahNumber = 3,
            arabicText = "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا مَا تَرَى فِي خَلْقِ الرَّحْمَنِ مِنْ تَفَاوُتٍ فَارْجِعِ الْبَصَرَ هَلْ تَرَى مِنْ فُطُورٍ",
            transliterationUz = "Allaazii khalaqa sab'a samaawaatin tibaaqan maa taraa fii khalqir rohmaani min tafaawut. Farji'il basara hal taraa min futuur",
            translationUz = "U yetti qavat osmonni tabaqama-tabaqa qilib yaratdi. Rahmanning yaratishida hech qanday nomutanosiblik ko'rmaysan",
            tajweedRuleIds = "7,4,8,6",
            page = 562
        ),
        QuranAyah(
            id = 203,
            surahNumber = 67,
            surahName = "الملك",
            surahNameUz = "Mulk",
            ayahNumber = 4,
            arabicText = "ثُمَّ ارْجِعِ الْبَصَرَ كَرَّتَيْنِ يَنقَلِبْ إِلَيْكَ الْبَصَرُ خَاسِئًا وَهُوَ حَسِيرٌ",
            transliterationUz = "Summar ji'il basara karrotayni yanqalib ilaykal basaru khoosi'an wa huwa hasiir",
            translationUz = "So'ng ko'zni qayta-qayta tikib qara, ko'z senga xor va toliqib qaytadi",
            tajweedRuleIds = "6,7,4,8",
            page = 562
        ),
        QuranAyah(
            id = 204,
            surahNumber = 67,
            surahName = "الملك",
            surahNameUz = "Mulk",
            ayahNumber = 5,
            arabicText = "وَلَقَدْ زَيَّنَّا السَّمَاءَ الدُّنْيَا بِمَصَابِيحَ وَجَعَلْنَاهَا رُجُومًا لِلشَّيَاطِينِ وَأَعْتَدْنَا لَهُمْ عَذَابَ السَّعِيرِ",
            transliterationUz = "Wa laqad zayyannassamaa'ad dunyaa bimasoobiiha wa ja'alnaahaa rujuumal lishshayaatiini wa a'tadnaa lahum 'azaabas sa'iir",
            translationUz = "Biz dunyoga eng yaqin osmonni chiroqlar bilan bezadik va ularni shaytonlarga otish qurollari qildik. Ularga olovli azob tayyorlab qo'ydik",
            tajweedRuleIds = "7,9,6,8",
            page = 562
        )
    )

    private fun getSurahAlKawthar(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 300,
            surahNumber = 108,
            surahName = "الكوثر",
            surahNameUz = "Kavsar",
            ayahNumber = 1,
            arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
            transliterationUz = "Innaa a'toynakal kawsar",
            translationUz = "Albatta, Biz senga Kavsarni berdik",
            tajweedRuleIds = "6,1,8",
            page = 602
        ),
        QuranAyah(
            id = 301,
            surahNumber = 108,
            surahName = "الكوثر",
            surahNameUz = "Kavsar",
            ayahNumber = 2,
            arabicText = "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
            transliterationUz = "Fasolli lirobbika wanhar",
            translationUz = "Bas, Robbingga namoz o'qi va qurbonlik qil",
            tajweedRuleIds = "6,4",
            page = 602
        ),
        QuranAyah(
            id = 302,
            surahNumber = 108,
            surahName = "الكوثر",
            surahNameUz = "Kavsar",
            ayahNumber = 3,
            arabicText = "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
            transliterationUz = "Inna shaani'aka huwal abtar",
            translationUz = "Albatta, sening dushmaning - uning o'zi nasli kesikdir",
            tajweedRuleIds = "6,7,1",
            page = 602
        )
    )

    private fun getSurahAlIkhlas(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 400,
            surahNumber = 112,
            surahName = "الإخلاص",
            surahNameUz = "Ixlos",
            ayahNumber = 1,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            transliterationUz = "Qul huwallahu ahad",
            translationUz = "Ayting: 'U Allah yagonadir'",
            tajweedRuleIds = "7,1",
            page = 604
        ),
        QuranAyah(
            id = 401,
            surahNumber = 112,
            surahName = "الإخلاص",
            surahNameUz = "Ixlos",
            ayahNumber = 2,
            arabicText = "اللَّهُ الصَّمَدُ",
            transliterationUz = "Allahus somad",
            translationUz = "Allah behojat (hamma Unga muhtoj)dir",
            tajweedRuleIds = "6,7",
            page = 604
        ),
        QuranAyah(
            id = 402,
            surahNumber = 112,
            surahName = "الإخلاص",
            surahNameUz = "Ixlos",
            ayahNumber = 3,
            arabicText = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            transliterationUz = "Lam yalid walam yuulad",
            translationUz = "U tug'magan va tug'ilmagan",
            tajweedRuleIds = "7,3",
            page = 604
        ),
        QuranAyah(
            id = 403,
            surahNumber = 112,
            surahName = "الإخلاص",
            surahNameUz = "Ixlos",
            ayahNumber = 4,
            arabicText = "وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ",
            transliterationUz = "Walam yakul lahu kufuwan ahad",
            translationUz = "Va Unga hech kim teng bo'lmagan",
            tajweedRuleIds = "2,7,1",
            page = 604
        )
    )

    private fun getSurahAlFalaq(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 500,
            surahNumber = 113,
            surahName = "الفلق",
            surahNameUz = "Falaq",
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            transliterationUz = "Qul a'uuzu birobbil falaq",
            translationUz = "Ayting: 'Tong Robbisidan panoh so'rayman'",
            tajweedRuleIds = "7,8,6",
            page = 604
        ),
        QuranAyah(
            id = 501,
            surahNumber = 113,
            surahName = "الفلق",
            surahNameUz = "Falaq",
            ayahNumber = 2,
            arabicText = "مِنْ شَرِّ مَا خَلَقَ",
            transliterationUz = "Min sharri maa khalaq",
            translationUz = "U yaratgan narsalarning yomonligidan",
            tajweedRuleIds = "4,6,7",
            page = 604
        ),
        QuranAyah(
            id = 502,
            surahNumber = 113,
            surahName = "الفلق",
            surahNameUz = "Falaq",
            ayahNumber = 3,
            arabicText = "وَمِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            transliterationUz = "Wa min sharri g'oosiqin izaa waqab",
            translationUz = "Qorong'ulik cho'kkanda uning yomonligidan",
            tajweedRuleIds = "4,6,7,8",
            page = 604
        ),
        QuranAyah(
            id = 503,
            surahNumber = 113,
            surahName = "الفلق",
            surahNameUz = "Falaq",
            ayahNumber = 4,
            arabicText = "وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
            transliterationUz = "Wa min sharrin naffaasaati fil 'uqad",
            translationUz = "Tugunlarga dam soluvchi (sehrgar) ayollarning yomonligidan",
            tajweedRuleIds = "4,6,8,7",
            page = 604
        ),
        QuranAyah(
            id = 504,
            surahNumber = 113,
            surahName = "الفلق",
            surahNameUz = "Falaq",
            ayahNumber = 5,
            arabicText = "وَمِنْ شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliterationUz = "Wa min sharri haasidin izaa hasad",
            translationUz = "Hasad qilganda hasadchining yomonligidan",
            tajweedRuleIds = "4,6,8,7",
            page = 604
        )
    )

    private fun getSurahAnNas(): List<QuranAyah> = listOf(
        QuranAyah(
            id = 600,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            transliterationUz = "Qul a'uuzu birobbinnaas",
            translationUz = "Ayting: 'Odamlarning Robbisidan panoh so'rayman'",
            tajweedRuleIds = "7,8,6",
            page = 604
        ),
        QuranAyah(
            id = 601,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 2,
            arabicText = "مَلِكِ النَّاسِ",
            transliterationUz = "Malikinnaas",
            translationUz = "Odamlarning Podshohidan",
            tajweedRuleIds = "6,8",
            page = 604
        ),
        QuranAyah(
            id = 602,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 3,
            arabicText = "إِلَهِ النَّاسِ",
            transliterationUz = "Ilaahinnaas",
            translationUz = "Odamlarning Ilohidan",
            tajweedRuleIds = "6,8",
            page = 604
        ),
        QuranAyah(
            id = 603,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 4,
            arabicText = "مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            transliterationUz = "Min sharril waswaasil khannaas",
            translationUz = "Yashirinib vasvasaga soluvchining yomonligidan",
            tajweedRuleIds = "4,6,8,1",
            page = 604
        ),
        QuranAyah(
            id = 604,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 5,
            arabicText = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            transliterationUz = "Allaazii yuwaswisu fii suduurinnaas",
            translationUz = "U odamlarning ko'kragiga vasvasa soladi",
            tajweedRuleIds = "8,6",
            page = 604
        ),
        QuranAyah(
            id = 605,
            surahNumber = 114,
            surahName = "الناس",
            surahNameUz = "Nos",
            ayahNumber = 6,
            arabicText = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliterationUz = "Minal jinnati wannaas",
            translationUz = "Jinlar va odamlardan (bo'lgan vasvasachi)",
            tajweedRuleIds = "6,3",
            page = 604
        )
    )
}
