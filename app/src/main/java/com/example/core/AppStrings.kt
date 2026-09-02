package com.example.core

object AppStrings {
    // Supported language codes:
    // "ar" -> Arabic
    // "en" -> English
    // "de" -> German (Deutsch)
    // "es" -> Spanish (Español)
    // "pt" -> Portuguese (Português)
    // "fr" -> French (Français)
    // "tr" -> Turkish (Türkçe)

    data class LanguageInfo(
        val code: String,
        val englishName: String,
        val nativeName: String,
        val flag: String,
        val isRtl: Boolean = false
    )

    val SUPPORTED_LANGUAGES = listOf(
        LanguageInfo("ar", "Arabic", "العربية", "🇸🇦", isRtl = true),
        LanguageInfo("en", "English", "English", "🇺🇸", isRtl = false),
        LanguageInfo("de", "German", "Deutsch", "🇩🇪", isRtl = false),
        LanguageInfo("es", "Spanish", "Español", "🇪🇸", isRtl = false),
        LanguageInfo("pt", "Portuguese", "Português", "🇧🇷", isRtl = false),
        LanguageInfo("fr", "French", "Français", "🇫🇷", isRtl = false),
        LanguageInfo("tr", "Turkish", "Türkçe", "🇹🇷", isRtl = false)
    )

    fun getLanguageInfo(code: String): LanguageInfo {
        return SUPPORTED_LANGUAGES.find { it.code == code } ?: SUPPORTED_LANGUAGES[1] // Default to English
    }

    fun isRtl(code: String): Boolean = code == "ar"

    private fun resolve(lang: String, ar: String, en: String, de: String, es: String, pt: String, fr: String, tr: String): String {
        return when (lang) {
            "ar" -> ar
            "de" -> de
            "es" -> es
            "pt" -> pt
            "fr" -> fr
            "tr" -> tr
            else -> en
        }
    }

    // Overload helper for Boolean isAr
    private fun resolve(isAr: Boolean, ar: String, en: String): String = if (isAr) ar else en

    // Navigation Tabs
    fun navDetect(lang: String) = resolve(lang, "كشف", "Detect", "Erkennen", "Detectar", "Detectar", "Détecter", "Tespit")
    fun navDetect(isAr: Boolean) = resolve(isAr, "كشف", "Detect")

    fun navCalibrate(lang: String) = resolve(lang, "معايرة", "Calibrate", "Kalibrieren", "Calibrar", "Calibrar", "Calibrer", "Kalibrasyon")
    fun navCalibrate(isAr: Boolean) = resolve(isAr, "معايرة", "Calibrate")

    fun navHardware(lang: String) = resolve(lang, "الأجهزة", "Hardware", "Hardware", "Hardware", "Hardware", "Matériel", "Donanım")
    fun navHardware(isAr: Boolean) = resolve(isAr, "الأجهزة", "Hardware")

    fun navHistory(lang: String) = resolve(lang, "السجل", "History", "Verlauf", "Historial", "Histórico", "Historique", "Geçmiş")
    fun navHistory(isAr: Boolean) = resolve(isAr, "السجل", "History")

    fun navSettings(lang: String) = resolve(lang, "الإعدادات", "Settings", "Optionen", "Ajustes", "Configurações", "Paramètres", "Ayarlar")
    fun navSettings(isAr: Boolean) = resolve(isAr, "الإعدادات", "Settings")

    // Top Bar
    fun appTitle(lang: String) = resolve(lang, "ميتال سكان", "METALSCAN", "METALSCAN", "METALSCAN", "METALSCAN", "METALSCAN", "METALSCAN")
    fun appTitle(isAr: Boolean) = resolve(isAr, "ميتال سكان", "METALSCAN")

    fun appPro(lang: String) = resolve(lang, "برو", "PRO", "PRO", "PRO", "PRO", "PRO", "PRO")
    fun appPro(isAr: Boolean) = resolve(isAr, "برو", "PRO")

    fun appSubtitle(lang: String) = resolve(
        lang,
        "مقياس المغناطيسية عالي الدقة وبيانات الاستشعار",
        "PRECISION MAGNETOMETER & TELEMETRY",
        "PRÄZISIONSMAGNETOMETER & TELEMETRIE",
        "MAGNETÓMETRO DE PRECISIÓN Y TELEMETRÍA",
        "MAGNETÔMETRO DE PRECISÃO E TELEMETRIA",
        "MAGNÉTOMÈTRE DE PRÉCISION ET TÉLÉMÉTRIE",
        "HASSAS MANYETOMETRE VE TELEMETRİ"
    )
    fun appSubtitle(isAr: Boolean) = resolve(isAr, "مقياس المغناطيسية عالي الدقة وبيانات الاستشعار", "PRECISION MAGNETOMETER & TELEMETRY")

    fun sensorPhone(lang: String) = resolve(lang, "مستشعر الهاتف", "Phone Sensor", "Telefonsensor", "Sensor Teléfono", "Sensor Telefone", "Capteur Téléphone", "Telefon Sensörü")
    fun sensorPhone(isAr: Boolean) = resolve(isAr, "مستشعر الهاتف", "Phone Sensor")

    fun sensorBle(lang: String) = resolve(lang, "بلوتوث BLE", "BLE Sensor", "BLE-Sensor", "Sensor BLE", "Sensor BLE", "Capteur BLE", "BLE Sensörü")
    fun sensorBle(isAr: Boolean) = resolve(isAr, "بلوتوث BLE", "BLE Sensor")

    fun sensorUsb(lang: String) = resolve(lang, "منفذ USB", "USB Sensor", "USB-Sensor", "Sensor USB", "Sensor USB", "Capteur USB", "USB Sensörü")
    fun sensorUsb(isAr: Boolean) = resolve(isAr, "منفذ USB", "USB Sensor")

    fun sensorSim(lang: String) = resolve(lang, "محاكاة تجريبية", "Simulation", "Simulation", "Simulación", "Simulação", "Simulation", "Simülasyon")
    fun sensorSim(isAr: Boolean) = resolve(isAr, "محاكاة تجريبية", "Simulation")

    fun statusLive(lang: String) = resolve(lang, "نشط", "LIVE", "AKTIV", "EN VIVO", "AO VIVO", "EN DIRECT", "CANLI")
    fun statusLive(isAr: Boolean) = resolve(isAr, "نشط", "LIVE")

    fun statusReady(lang: String) = resolve(lang, "جاهز", "READY", "BEREIT", "LISTO", "PRONTO", "PRÊT", "HAZIR")
    fun statusReady(isAr: Boolean) = resolve(isAr, "جاهز", "READY")

    fun statusConnecting(lang: String) = resolve(lang, "جاري الاتصال", "CONNECTING", "VERBINDUNG", "CONECTANDO", "CONECTANDO", "CONNEXION", "BAĞLANIYOR")
    fun statusConnecting(isAr: Boolean) = resolve(isAr, "جاري الاتصال", "CONNECTING")

    fun statusDisconnected(lang: String) = resolve(lang, "غير متصل", "DISCONNECTED", "GETRENNT", "DESCONECTADO", "DESCONECTADO", "DÉCONNECTÉ", "BAĞLANTI KESİLDİ")
    fun statusDisconnected(isAr: Boolean) = resolve(isAr, "غير متصل", "DISCONNECTED")

    // Detect Screen
    fun modeLabel(lang: String) = resolve(lang, "النمط", "MODE", "MODUS", "MODO", "MODO", "MODE", "MOD")
    fun modeLabel(isAr: Boolean) = resolve(isAr, "النمط", "MODE")

    fun zeroBaseline(lang: String) = resolve(lang, "تصفير", "ZERO", "NULLEN", "CERAR", "ZERAR", "ZÉRO", "SIFIRLA")
    fun zeroBaseline(isAr: Boolean) = resolve(isAr, "تصفير", "ZERO")

    fun audioFeedback(lang: String) = resolve(lang, "التنبيه الصوتي", "Audio Feedback", "Audio-Feedback", "Audio", "Áudio", "Retour Audio", "Sesli Geri Bildirim")
    fun audioFeedback(isAr: Boolean) = resolve(isAr, "التنبيه الصوتي", "Audio Feedback")

    fun startScan(lang: String) = resolve(lang, "بدء المسح", "START SCAN", "SCAN STARTEN", "INICIAR ESCANEO", "INICIAR VARREDURA", "DÉMARRER LE SCAN", "TARAMAYI BAŞLAT")
    fun startScan(isAr: Boolean) = resolve(isAr, "بدء المسح", "START SCAN")

    fun stopScan(lang: String) = resolve(lang, "إيقاف المسح", "STOP SCAN", "SCAN STOPPEN", "DETENER ESCANEO", "PARAR VARREDURA", "ARRÊTER LE SCAN", "TARAMAYI DURDUR")
    fun stopScan(isAr: Boolean) = resolve(isAr, "إيقاف المسح", "STOP SCAN")

    fun calibrateBtn(lang: String) = resolve(lang, "معايرة", "CALIBRATE", "KALIBRIEREN", "CALIBRAR", "CALIBRAR", "CALIBRER", "KALİBRE ET")
    fun calibrateBtn(isAr: Boolean) = resolve(isAr, "معايرة", "CALIBRATE")

    fun saveLogBtn(lang: String) = resolve(lang, "حفظ السجل", "SAVE LOG", "LOG SPEICHERN", "GUARDAR REGISTRO", "SALVAR REGISTRO", "ENREGISTRER LOG", "KAYDET")
    fun saveLogBtn(isAr: Boolean) = resolve(isAr, "حفظ السجل", "SAVE LOG")

    fun microtesla(lang: String) = resolve(lang, "ميكروتيسلا (µT)", "µT (MICROTESLA)", "µT (MIKROTESLA)", "µT (MICROTESLA)", "µT (MICROTESLA)", "µT (MICROTESLA)", "µT (MİKROTESLA)")
    fun microtesla(isAr: Boolean) = resolve(isAr, "ميكروتيسلا (µT)", "µT (MICROTESLA)")

    // Readout Cards
    fun baseline(lang: String) = resolve(lang, "المرجع", "BASELINE", "BASISWERT", "LÍNEA BASE", "LINHA DE BASE", "RÉFÉRENCE", "TABAN ÇİZGİSİ")
    fun baseline(isAr: Boolean) = resolve(isAr, "المرجع", "BASELINE")

    fun delta(lang: String) = resolve(lang, "الفارق (Δ)", "DELTA (Δ)", "DELTA (Δ)", "DELTA (Δ)", "DELTA (Δ)", "DELTA (Δ)", "FARK (Δ)")
    fun delta(isAr: Boolean) = resolve(isAr, "الفارق (Δ)", "DELTA (Δ)")

    fun threshold(lang: String) = resolve(lang, "عتبة التنبيه", "THRESHOLD", "SCHWELLE", "UMBRAL", "LIMIAR", "SEUIL", "EŞİK DEĞERİ")
    fun threshold(isAr: Boolean) = resolve(isAr, "عتبة التنبيه", "THRESHOLD")

    fun signal(lang: String) = resolve(lang, "قوة الإشارة", "SIGNAL", "SIGNAL", "SEÑAL", "SINAL", "SIGNAL", "SİNYAL")
    fun signal(isAr: Boolean) = resolve(isAr, "قوة الإشارة", "SIGNAL")

    fun stability(lang: String) = resolve(lang, "الاستقرار", "STABILITY", "STABILITÄT", "ESTABILIDAD", "ESTABILIDADE", "STABILITÉ", "KARARLILIK")
    fun stability(isAr: Boolean) = resolve(isAr, "الاستقرار", "STABILITY")

    fun snr(lang: String) = resolve(lang, "نسبة الإشارة (SNR)", "SNR", "SNR", "SNR", "SNR", "SNR", "SNR")
    fun snr(isAr: Boolean) = resolve(isAr, "نسبة الإشارة (SNR)", "SNR")

    fun confidence(lang: String) = resolve(lang, "درجة الثقة", "CONFIDENCE", "ZUVERLÄSSIGKEIT", "CONFIANZA", "CONFIANÇA", "CONFIANCE", "GÜVEN ORANI")
    fun confidence(isAr: Boolean) = resolve(isAr, "درجة الثقة", "CONFIDENCE")

    // Target Classification
    fun targetClassification(lang: String) = resolve(lang, "تصنيف الهدف", "TARGET CLASSIFICATION", "ZIELKLASSIFIZIERUNG", "CLASIFICACIÓN DE OBJETIVO", "CLASSIFICAÇÃO DO ALVO", "CLASSIFICATION DE LA CIBLE", "HEDEF SINIFLANDIRMASI")
    fun targetClassification(isAr: Boolean) = resolve(isAr, "تصنيف الهدف", "TARGET CLASSIFICATION")

    fun estimated(lang: String) = resolve(lang, "تقديري", "ESTIMATED", "GESCHÄTZT", "ESTIMADO", "ESTIMADO", "ESTIMÉ", "TAHMİNİ")
    fun estimated(isAr: Boolean) = resolve(isAr, "تقديري", "ESTIMATED")

    fun externalRequired(lang: String) = resolve(lang, "يتطلب مستشعر خارجي", "EXT SENSOR REQ", "EXT. SENSOR ERFORDERLICH", "SENSOR EXT REQUERIDO", "SENSOR EXT NECESSÁRIO", "CAPTEUR EXT REQUIS", "HARİCİ SENSÖR GEREKLİ")
    fun externalRequired(isAr: Boolean) = resolve(isAr, "يتطلب مستشعر خارجي", "EXT SENSOR REQ")

    fun ferromagneticScore(lang: String) = resolve(lang, "معامل المغنطة الحديدية:", "Ferromagnetic Score:", "Ferromagnetischer Wert:", "Puntuación Ferromagnética:", "Pontuação Ferromagnética:", "Score Ferromagnétique:", "Ferromanyetik Skor:")
    fun ferromagneticScore(isAr: Boolean) = resolve(isAr, "معامل المغنطة الحديدية:", "Ferromagnetic Score:")

    fun conductivityVdi(lang: String) = resolve(lang, "معامل التوصيل VDI:", "Conductivity VDI:", "Leitfähigkeit VDI:", "Conductividad VDI:", "Condutividade VDI:", "Conductivité VDI:", "İletkenlik VDI:")
    fun conductivityVdi(isAr: Boolean) = resolve(isAr, "معامل التوصيل VDI:", "Conductivity VDI:")

    fun phaseAngle(lang: String) = resolve(lang, "زاوية الطور:", "Phase Angle:", "Phasenwinkel:", "Ángulo de Fase:", "Ângulo de Fase:", "Angle de Phase:", "Faz Açısı:")
    fun phaseAngle(isAr: Boolean) = resolve(isAr, "زاوية الطور:", "Phase Angle:")

    // Classification Titles & Subtitles
    fun classNoAnomalyTitle(lang: String) = resolve(lang, "لا يوجد اضطراب مغناطيسي", "No Magnetic Anomaly", "Keine magnetische Anomalie", "Sin anomalía magnética", "Sem anomalia magnética", "Aucune anomalie magnétique", "Manyetik anomali yok")
    fun classNoAnomalyTitle(isAr: Boolean) = resolve(isAr, "لا يوجد اضطراب مغناطيسي", "No Magnetic Anomaly")

    fun classNoAnomalySub(lang: String) = resolve(lang, "المجال المغناطيسي المحيط طبيعي ومستقر", "Ambient geomagnetic baseline normal", "Normales geomagnetisches Umgebungsfeld", "Línea base geomagnética ambiental normal", "Linha de base geomagnética ambiente normal", "Champ géomagnétique ambiant normal", "Ortam jeomanyetik taban çizgisi normal")
    fun classNoAnomalySub(isAr: Boolean) = resolve(isAr, "المجال المغناطيسي المحيط طبيعي ومستقر", "Ambient geomagnetic baseline normal")

    fun classWeakAnomalyTitle(lang: String) = resolve(lang, "اضطراب مغناطيسي ضعيف", "Weak Magnetic Anomaly", "Schwache magnetische Anomalie", "Anomalía magnética débil", "Anomalia magnética fraca", "Faible anomalie magnétique", "Zayıf manyetik anomali")
    fun classWeakAnomalyTitle(isAr: Boolean) = resolve(isAr, "اضطراب مغناطيسي ضعيف", "Weak Magnetic Anomaly")

    fun classWeakAnomalySub(lang: String) = resolve(lang, "احتمال وجود جسم معدني بعيد أو هيكل صغير", "Possible distant metallic object or small structure", "Mögliches entferntes Metallobjekt oder kleine Struktur", "Posible objeto metálico lejano o estructura pequeña", "Possível objeto metálico distante ou estrutura pequena", "Objet métallique lointain ou petite structure possible", "Olası uzak metalik nesne veya küçük yapı")
    fun classWeakAnomalySub(isAr: Boolean) = resolve(isAr, "احتمال وجود جسم معدني بعيد أو هيكل صغير", "Possible distant metallic object or small structure")

    fun classStrongAnomalyTitle(lang: String) = resolve(lang, "جسم مغناطيسي قوي", "Strong Magnetic Object", "Starkes magnetisches Objekt", "Objeto magnético fuerte", "Objeto magnético forte", "Objet magnétique puissant", "Güçlü manyetik nesne")
    fun classStrongAnomalyTitle(isAr: Boolean) = resolve(isAr, "جسم مغناطيسي قوي", "Strong Magnetic Object")

    fun classStrongAnomalySub(lang: String) = resolve(lang, "معدن مغناطيسي أو سلك كهربائي قريب", "High-amplitude magnetic deflection detected nearby", "Starke magnetische Auslenkung in der Nähe erkannt", "Desviación magnética de alta amplitud cercana", "Desvio magnético de alta amplitude próximo", "Forte déviation magnétique détectée à proximité", "Yakında yüksek genlikli manyetik sapma tespit edildi")
    fun classStrongAnomalySub(isAr: Boolean) = resolve(isAr, "معدن مغناطيسي أو سلك كهربائي قريب", "High-amplitude magnetic deflection detected nearby")

    fun classFerrousTitle(lang: String) = resolve(lang, "معدن حديدي (حديد / فولاذ)", "Ferrous Metal (Iron / Steel)", "Eisenmetall (Eisen / Stahl)", "Metal Ferroso (Hierro / Acero)", "Metal Ferroso (Ferro / Aço)", "Métal Ferreux (Fer / Acier)", "Demir İçeren Metal (Demir / Çelik)")
    fun classFerrousTitle(isAr: Boolean) = resolve(isAr, "معدن حديدي (حديد / فولاذ)", "Ferrous Metal (Iron / Steel)")

    fun classFerrousSub(lang: String) = resolve(lang, "استجابة مغناطيسية نموذجية لمواد حديدية أو أنابيب", "Characteristic ferromagnetic signature detected", "Typische ferromagnetische Signatur erkannt", "Firma ferromagnética característica detectada", "Assinatura ferromagnética característica detectada", "Signature ferromagnétique caractéristique détectée", "Karakteristik ferromanyetik imza tespit edildi")
    fun classFerrousSub(isAr: Boolean) = resolve(isAr, "استجابة مغناطيسية نموذجية لمواد حديدية أو أنابيب", "Characteristic ferromagnetic signature detected")

    fun classUnknownMetallicTitle(lang: String) = resolve(lang, "اضطراب معدني غير محدد", "Unknown Metallic Anomaly", "Unbekannte metallische Anomalie", "Anomalía metálica desconocida", "Anomalia metálica desconhecida", "Anomalie métallique inconnue", "Bilinmeyen metalik anomali")
    fun classUnknownMetallicTitle(isAr: Boolean) = resolve(isAr, "اضطراب معدني غير محدد", "Unknown Metallic Anomaly")

    fun classUnknownMetallicSub(lang: String) = resolve(lang, "تغير مفاجئ في المجال المغناطيسي", "Transient magnetic flux disturbance", "Vorübergehende Störung des Magnetflusses", "Perturbación transitoria del flujo magnético", "Perturbação transitória do fluxo magnético", "Perturbation transitoire du flux magnétique", "Geçici manyetik akı bozulması")
    fun classUnknownMetallicSub(isAr: Boolean) = resolve(isAr, "تغير مفاجئ في المجال المغناطيسي", "Transient magnetic flux disturbance")

    fun classExtFerrousTitle(lang: String) = resolve(lang, "حديد / خردة حديدية", "Ferrous Metal (Iron / Nails / Trash)", "Eisenmetall (Eisen / Nägel / Schrott)", "Metal Ferroso (Hierro / Clavos / Basura)", "Metal Ferroso (Ferro / Pregos / Sucata)", "Métal Ferreux (Fer / Clous / Déchets)", "Demir / Çiviler / Hurda")
    fun classExtFerrousTitle(isAr: Boolean) = resolve(isAr, "حديد / خردة حديدية", "Ferrous Metal (Iron / Nails / Trash)")

    fun classExtFerrousSub(lang: String) = resolve(lang, "تم التمييز بواسطة المستشعر الخارجي (VDI منخفض)", "Low VDI phase angle identified by external coil", "Niedriger VDI-Phasenwinkel durch externe Spule", "Bajo ángulo de fase VDI identificado por bobina externa", "Baixo ângulo de fase VDI identificado por bobina externa", "Faible angle de phase VDI identifié par bobine externe", "Harici bobin ile düşük VDI faz açısı belirlendi")
    fun classExtFerrousSub(isAr: Boolean) = resolve(isAr, "تم التمييز بواسطة المستشعر الخارجي (VDI منخفض)", "Low VDI phase angle identified by external coil")

    fun classExtNonFerrousTitle(lang: String) = resolve(lang, "معدن غير حديدي", "Non-Ferrous Metal", "Nichteisenmetall", "Metal No Ferroso", "Metal Não Ferroso", "Métal Non Ferreux", "Demir Dışı Metal")
    fun classExtNonFerrousTitle(isAr: Boolean) = resolve(isAr, "معدن غير حديدي", "Non-Ferrous Metal")

    fun classExtNonFerrousSub(lang: String) = resolve(lang, "معدن موصل غير مغناطيسي تم التحقق منه بالملف الخارجي", "Verified non-magnetic conductive target by coil", "Verifiziertes nichtmagnetisches leitfähiges Ziel", "Objetivo conductor no magnético verificado por bobina", "Alvo condutivo não magnético verificado por bobina", "Cible conductrice non magnétique vérifiée par bobine", "Bobin ile doğrulanmış iletken manyetik olmayan hedef")
    fun classExtNonFerrousSub(isAr: Boolean) = resolve(isAr, "معدن موصل غير مغناطيسي تم التحقق منه بالملف الخارجي", "Verified non-magnetic conductive target by coil")

    fun classExtGoldTitle(lang: String) = resolve(lang, "هدف محتمل (نطاق الذهب / النيكل)", "Target Candidate (Gold / Nickel Range)", "Zielkandidat (Gold / Nickel-Bereich)", "Candidato a Objetivo (Rango Oro / Níquel)", "Candidato a Alvo (Faixa Ouro / Níquel)", "Candidat Cible (Gamme Or / Nickel)", "Hedef Adayı (Altın / Nikel Aralığı)")
    fun classExtGoldTitle(isAr: Boolean) = resolve(isAr, "هدف محتمل (نطاق الذهب / النيكل)", "Target Candidate (Gold / Nickel Range)")

    fun classExtGoldSub(lang: String) = resolve(lang, "استجابة طورية متوسطة عبر المستشعر الخارجي", "Mid-conductive phase response from external coil", "Mittlere Phasenantwort von externer Spule", "Respuesta de fase media de la bobina externa", "Resposta de fase média da bobina externa", "Réponse de phase moyenne de la bobine externe", "Harici bobinden orta faz tepkisi")
    fun classExtGoldSub(isAr: Boolean) = resolve(isAr, "استجابة طورية متوسطة عبر المستشعر الخارجي", "Mid-conductive phase response from external coil")

    fun classExtSilverTitle(lang: String) = resolve(lang, "هدف عالي التوصيل (نطاق الفضة / العملات)", "High-Conductivity (Silver / Coin Range)", "Hohe Leitfähigkeit (Silber / Münzen)", "Alta Conductividad (Rango Plata / Monedas)", "Alta Condutividade (Faixa Prata / Moedas)", "Haute Conductivité (Gamme Argent / Monnaies)", "Yüksek İletkenlik (Gümüş / Madeni Para)")
    fun classExtSilverTitle(isAr: Boolean) = resolve(isAr, "هدف عالي التوصيل (نطاق الفضة / العملات)", "High-Conductivity (Silver / Coin Range)")

    fun classExtSilverSub(lang: String) = resolve(lang, "توصيل مرتفع وزاوية طور عالية عبر المستشعر الخارجي", "High VDI phase response from external coil", "Hohe VDI-Phasenantwort von externer Spule", "Alta respuesta de fase VDI de la bobina externa", "Alta resposta de fase VDI da bobina externa", "Réponse de phase VDI élevée de la bobine externe", "Harici bobinden yüksek VDI faz yanıtı")
    fun classExtSilverSub(isAr: Boolean) = resolve(isAr, "هدف عالي التوصيل (نطاق الفضة / العملات)", "High VDI phase response from external coil")

    fun classExtCopperTitle(lang: String) = resolve(lang, "هدف عالي التوصيل (نطاق النحاس / البرونز)", "High-Conductivity (Copper / Bronze)", "Hohe Leitfähigkeit (Kupfer / Bronze)", "Alta Conductividad (Cobre / Bronce)", "Alta Condutividade (Cobre / Bronze)", "Haute Conductivité (Cuivre / Bronze)", "Yüksek İletkenlik (Bakır / Bronz)")
    fun classExtCopperTitle(isAr: Boolean) = resolve(isAr, "هدف عالي التوصيل (نطاق النحاس / البرونز)", "High-Conductivity (Copper / Bronze)")

    fun classExtCopperSub(lang: String) = resolve(lang, "توصيل كهربائي مرتفع عبر المستشعر الخارجي", "High phase conductivity detected via external sensor", "Hohe Leitfähigkeit über externen Sensor erkannt", "Alta conductividad detectada mediante sensor externo", "Alta condutividade detectada via sensor externo", "Haute conductivité détectée via le capteur externe", "Harici sensör ile yüksek iletkenlik tespit edildi")
    fun classExtCopperSub(isAr: Boolean) = resolve(isAr, "توصيل كهربائي مرتفع عبر المستشعر الخارجي", "High phase conductivity detected via external sensor")

    fun classExtAluminumTitle(lang: String) = resolve(lang, "هدف متوسط التوصيل (نطاق الألومنيوم / الرصاص)", "Mid-Conductivity (Aluminum / Lead)", "Mittlere Leitfähigkeit (Aluminium / Blei)", "Conductividad Media (Aluminio / Plomo)", "Condutividade Média (Alumínio / Chumbo)", "Conductivité Moyenne (Aluminium / Plomb)", "Orta İletkenlik (Alüminyum / Kurşun)")
    fun classExtAluminumTitle(isAr: Boolean) = resolve(isAr, "هدف متوسط التوصيل (نطاق الألومنيوم / الرصاص)", "Mid-Conductivity (Aluminum / Lead)")

    fun classExtAluminumSub(lang: String) = resolve(lang, "استجابة متوسطة عبر المستشعر الخارجي", "Mid-range phase signature detected via external sensor", "Mittlere Phasensignatur über externen Sensor erkannt", "Firma de fase media detectada por sensor externo", "Assinatura de fase média detectada por sensor externo", "Signature de phase moyenne détectée par capteur externe", "Harici sensörle orta faz imzası tespit edildi")
    fun classExtAluminumSub(isAr: Boolean) = resolve(isAr, "استجابة متوسطة عبر المستشعر الخارجي", "Mid-range phase signature detected via external sensor")

    // Graph
    fun oscillogram(lang: String) = resolve(lang, "مخطط التذبذب (µT)", "OSCILLOGRAM (µT)", "OSZILLOGRAMM (µT)", "OSCILOGRAMA (µT)", "OSCILOGRAMA (µT)", "OSCILLOGRAMME (µT)", "OSİLOGRAM (µT)")
    fun oscillogram(isAr: Boolean) = resolve(isAr, "مخطط التذبذب (µT)", "OSCILLOGRAM (µT)")

    fun graphRaw(lang: String) = resolve(lang, "الخام", "Raw", "Roh", "Crudo", "Bruto", "Brut", "Ham")
    fun graphRaw(isAr: Boolean) = resolve(isAr, "الخام", "Raw")

    fun graphFiltered(lang: String) = resolve(lang, "المصفى", "Filtered", "Gefiltert", "Filtrado", "Filtrado", "Filtré", "Filtrelenmiş")
    fun graphFiltered(isAr: Boolean) = resolve(isAr, "المصفى", "Filtered")

    fun graphBase(lang: String) = resolve(lang, "المرجع", "Base", "Basis", "Base", "Base", "Base", "Taban")
    fun graphBase(isAr: Boolean) = resolve(isAr, "المرجع", "Base")

    fun graphThreshold(lang: String) = resolve(lang, "العتبة", "Threshold", "Schwelle", "Umbral", "Limiar", "Seuil", "Eşik")
    fun graphThreshold(isAr: Boolean) = resolve(isAr, "العتبة", "Threshold")

    // Disclaimer
    fun disclaimerTitle(lang: String) = resolve(lang, "تنبيه فيزيائي حول دقة المستشعرات", "Physics & Sensor Accuracy Notice", "Hinweis zu Physik & Sensorgenauigkeit", "Aviso de Física y Precisión de Sensores", "Aviso de Física e Precisão dos Sensores", "Avis sur la Physique et la Précision des Capteurs", "Fizik ve Sensör Hassasiyeti Bildirimi")
    fun disclaimerTitle(isAr: Boolean) = resolve(isAr, "تنبيه فيزيائي حول دقة المستشعرات", "Physics & Sensor Accuracy Notice")

    fun disclaimerBody(lang: String) = resolve(
        lang,
        "• مقياس المغناطيسية للهاتف: يقيس التدفق المغناطيسي الأرضي (µT) بثلاثة أبعاد. شديد الحساسية للمواد الحديدية والفولاذ والمغناطيس والأسلاك الحية. لا يمكنه فيزيائياً تمييز أو كشف الذهب أو الفضة بمفرده دون جهاز كشف نبضي أو كهرومغناطيسي.\n\n• وضع المستشعر الخارجي: توصيل ملف بحث خارجي عبر BLE أو USB يتيح قياس التوصيلية الحقيقية وزاوية الطور وهوية الهدف (VDI).\n\n• إخلاء مسؤولية: جميع التصنيفات هي تقديرات برمجية رياضية وليست مضمونة بنسبة 100%.",
        "• Built-In Phone Magnetometer: Measures geomagnetic flux (µT) in 3 dimensions. Highly sensitive to ferromagnetic materials (iron, steel, neodymium magnets, power lines). It cannot physically detect or discriminate non-ferrous metals such as gold, silver, or copper.\n\n• External Hardware Sensor Mode: Connecting an external search coil or pulse-induction/VLF detector via BLE/USB allows receiving true conductivity, phase angle, and Target ID (VDI).\n\n• Safety Notice: All displayed classifications are empirical signal estimates and never scientifically guaranteed.",
        "• Smartphone-Magnetometer: Misst das geomagnetische Feld (µT) in 3D. Hochempfindlich gegenüber ferromagnetischen Metallen (Eisen, Stahl, Magnete, Stromleitungen). Kann bauartbedingt keine Nichteisenmetalle wie Gold oder Silber ohne externe Spule erkennen.\n\n• Externer Hardware-Modus: Der Anschluss einer externen Spule über BLE/USB ermöglicht die Messung echter Leitfähigkeit, Phasenwinkel und VDI-Zielidentifikation.\n\n• Hinweis: Alle Klassifizierungen sind mathematische Signalanalysen ohne Garantie.",
        "• Magnetómetro del Teléfono: Mide el flujo geomagnético (µT) en 3D. Altamente sensible a materiales ferromagnéticos (hierro, acero, imanes, cables con corriente). No puede detectar metales no ferrosos como oro o plata sin una bobina externa.\n\n• Modo Sensor Externo: Conectar una bobina externa por BLE/USB permite medir conductividad real, ángulo de fase e ID de objetivo (VDI).\n\n• Aviso: Las clasificaciones son estimaciones matemáticas y no están garantizadas.",
        "• Magnetômetro do Telefone: Mede o fluxo geomagnético (µT) em 3D. Altamente sensível a materiais ferromagnéticos (ferro, aço, ímãs, fios elétricos). Não detecta ouro ou prata sem bobina externa.\n\n• Modo Sensor Externo: Conectar uma bobina externa via BLE/USB permite medir condutividade real, ângulo de fase e ID do alvo (VDI).\n\n• Aviso: As classificações são estimativas matemáticas sem garantia absoluta.",
        "• Magnétomètre du Téléphone: Mesure le flux géomagnétique (µT) en 3D. Très sensible aux métaux ferromagnétiques (fer, acier, aimants, câbles). Ne peut pas détecter l'or ou l'argent sans bobine externe.\n\n• Mode Capteur Externe: Connecter une bobine externe via BLE/USB permet de mesurer la conductivité réelle, l'angle de phase et l'ID de cible (VDI).\n\n• Remarque: Toutes les classifications sont des estimations mathématiques sans garantie absolue.",
        "• Telefon Manyetometresi: 3 boyutta jeomanyetik akıyı (µT) ölçer. Ferromanyetik malzemelere (demir, çelik, mıknatıs, elektrik hatları) son derece duyarlıdır. Harici bobin olmadan altın veya gümüş gibi demir dışı metalleri ayırt edemez.\n\n• Harici Sensör Modu: BLE/USB üzerinden harici bir arama başlığı bağlamak gerçek iletkenlik, faz açısı ve Hedef Kimliği (VDI) ölçümünü sağlar.\n\n• Uyarı: Tüm sınıflandırmalar matematiksel sinyal tahminleridir ve garanti edilmez."
    )
    fun disclaimerBody(isAr: Boolean) = resolve(isAr, disclaimerBody("ar"), disclaimerBody("en"))

    // Save Dialog
    fun saveLogTitle(lang: String) = resolve(lang, "حفظ سجل الكشف", "Save Detection Log", "Erfassungsprotokoll speichern", "Guardar Registro de Detección", "Salvar Registro de Detecção", "Enregistrer le Journal de Détection", "Tespit Günlüğünü Kaydet")
    fun saveLogTitle(isAr: Boolean) = resolve(isAr, "حفظ سجل الكشف", "Save Detection Log")

    fun saveLogNotesHint(lang: String) = resolve(
        lang,
        "أضف ملاحظات ميدانية (مثل: عمق 10 سم، قرب الصخور)...",
        "Add field notes (e.g. Depth ~10cm, near tree)...",
        "Feldnotizen hinzufügen (z. B. Tiefe ca. 10 cm, nahe Baum)...",
        "Añadir notas de campo (ej. Profundidad ~10cm, cerca del árbol)...",
        "Adicionar notas de campo (ex: Profundidade ~10cm, perto da árvore)...",
        "Ajouter des notes de terrain (ex: Profondeur ~10cm, près de l'arbre)...",
        "Saha notları ekleyin (ör. Derinlik ~10cm, ağaç yanı)..."
    )
    fun saveLogNotesHint(isAr: Boolean) = resolve(isAr, saveLogNotesHint("ar"), saveLogNotesHint("en"))

    fun saveLogNotesPlaceholder(lang: String) = resolve(
        lang,
        "ملاحظات إضافية...",
        "Additional notes...",
        "Zusätzliche Notizen...",
        "Notas adicionales...",
        "Notas adicionais...",
        "Notes supplémentaires...",
        "Ek notlar..."
    )
    fun saveLogNotesPlaceholder(isAr: Boolean) = resolve(isAr, saveLogNotesPlaceholder("ar"), saveLogNotesPlaceholder("en"))

    fun connectExtSensorCoil(lang: String) = resolve(
        lang,
        "قم بتوصيل ملف بحث خارجي لتفعيل التمييز المتقدم و VDI",
        "Connect external detector coil for advanced VDI discrimination",
        "Schließen Sie eine externe Suchspule für erweiterte VDI-Unterscheidung an",
        "Conecte una bobina externa para discriminación VDI avanzada",
        "Conecte uma bobina externa para discriminação VDI avançada",
        "Connectez une bobine externe pour la discrimination VDI avancée",
        "Gelişmiş VDI ayrımı için harici arama başlığı bağlayın"
    )
    fun connectExtSensorCoil(isAr: Boolean) = resolve(isAr, connectExtSensorCoil("ar"), connectExtSensorCoil("en"))

    fun saveToHistory(lang: String) = resolve(lang, "حفظ في السجل", "Save to History", "Im Verlauf speichern", "Guardar en Historial", "Salvar no Histórico", "Enregistrer dans l'Historique", "Geçmişe Kaydet")
    fun saveToHistory(isAr: Boolean) = resolve(isAr, "حفظ في السجل", "Save to History")

    fun cancel(lang: String) = resolve(lang, "إلغاء", "Cancel", "Abbrechen", "Cancelar", "Cancelar", "Annuler", "İptal")
    fun cancel(isAr: Boolean) = resolve(isAr, "إلغاء", "Cancel")

    fun peakField(lang: String) = resolve(lang, "أعلى إشارة:", "Peak Field:", "Spitzenwert:", "Campo Máximo:", "Campo Máximo:", "Champ Maximal:", "Maksimum Alan:")
    fun peakField(isAr: Boolean) = resolve(isAr, "أعلى إشارة:", "Peak Field:")

    fun gpsLocation(lang: String) = resolve(lang, "الموقع GPS:", "GPS Location:", "GPS-Standort:", "Ubicación GPS:", "Localização GPS:", "Position GPS:", "GPS Konumu:")
    fun gpsLocation(isAr: Boolean) = resolve(isAr, "الموقع GPS:", "GPS Location:")

    // Calibration Screen
    fun calibScreenTitle(lang: String) = resolve(lang, "معايرة المستشعر والفحص الذاتي", "Sensor Calibration & Self-Test", "Sensorkalibrierung & Selbsttest", "Calibración y Autodiagnóstico", "Calibração e Autodiagnóstico", "Calibrage du Capteur & Auto-test", "Sensör Kalibrasyonu ve Kendi Kendini Test")
    fun calibScreenTitle(isAr: Boolean) = resolve(isAr, "معايرة المستشعر والفحص الذاتي", "Sensor Calibration & Self-Test")

    fun calibActiveProcess(lang: String) = resolve(lang, "إجراءات المعايرة النشطة (9 خطوات)", "Active 9-Step Calibration", "Aktive 9-Stufen-Kalibrierung", "Calibración Activa de 9 Pasos", "Calibração Ativa de 9 Etapas", "Calibrage Actif en 9 Étapes", "Aktif 9 Adımlı Kalibrasyon")
    fun calibActiveProcess(isAr: Boolean) = resolve(isAr, "إجراءات المعايرة النشطة (9 خطوات)", "Active 9-Step Calibration")

    fun calibMotionGuide(lang: String) = resolve(lang, "دليل الحركة (حركة رقم 8)", "Motion Guide (Figure-8)", "Bewegungsanleitung (Achter-Figur)", "Guía de Movimiento (Figura 8)", "Guia de Movimento (Figura em 8)", "Guide de Mouvement (Figure en 8)", "Hareket Kılavuzu (8 Şekli)")
    fun calibMotionGuide(isAr: Boolean) = resolve(isAr, "دليل الحركة (حركة رقم 8)", "Motion Guide (Figure-8)")

    fun calibMotionDesc(lang: String) = resolve(
        lang,
        "حرك الهاتف ببطء في الهواء بعيداً عن المعادن الكبيرة والأجهزة الكهربائية لمعايرة الحساس ثلاثي المحاور.",
        "Move the phone slowly in a 3D figure-8 pattern away from large metallic structures to calibrate all 3 axes.",
        "Bewegen Sie das Telefon langsam in einer 3D-Achterform fernab von großen Metallen, um alle 3 Achsen zu kalibrieren.",
        "Mueva el teléfono lentamente en forma de 8 en 3D lejos de metales grandes para calibrar los 3 ejes.",
        "Mova o telefone lentamente em padrão de 8 em 3D longe de metais grandes para calibrar os 3 eixos.",
        "Déplacez le téléphone lentement en forme de 8 3D loin des métaux pour calibrer les 3 axes.",
        "3 ekseni de kalibre etmek için telefonu büyük metallerden uzakta yavaşça 3 boyutlu 8 şeklinde hareket ettirin."
    )
    fun calibMotionDesc(isAr: Boolean) = resolve(isAr, calibMotionDesc("ar"), calibMotionDesc("en"))

    fun calibStartBtn(lang: String) = resolve(lang, "بدء المعايرة الموجهة", "Start Guided Calibration", "Geführte Kalibrierung starten", "Iniciar Calibración Guiada", "Iniciar Calibração Guiada", "Démarrer le Calibrage Guidé", "Rehberli Kalibrasyonu Başlat")
    fun calibStartBtn(isAr: Boolean) = resolve(isAr, "بدء المعايرة الموجهة", "Start Guided Calibration")

    fun calibProgress(lang: String) = resolve(lang, "جاري المعايرة...", "Calibrating...", "Kalibriere...", "Calibrando...", "Calibrando...", "Calibrage en cours...", "Kalibre ediliyor...")
    fun calibProgress(isAr: Boolean) = resolve(isAr, "جاري المعايرة...", "Calibrating...")

    fun calibComplete(lang: String) = resolve(lang, "اكتملت المعايرة بنجاح", "Calibration Complete", "Kalibrierung abgeschlossen", "Calibración Completada", "Calibração Concluída", "Calibrage Terminé", "Kalibrasyon Tamamlandı")
    fun calibComplete(isAr: Boolean) = resolve(isAr, "اكتملت المعايرة بنجاح", "Calibration Complete")

    fun calibQuality(lang: String) = resolve(lang, "جودة المعايرة:", "Calibration Quality:", "Kalibrierungsqualität:", "Calidad de Calibración:", "Qualidade da Calibração:", "Qualité du Calibrage:", "Kalibrasyon Kalitesi:")
    fun calibQuality(isAr: Boolean) = resolve(isAr, "جودة المعايرة:", "Calibration Quality:")

    fun calibBaselineNoise(lang: String) = resolve(lang, "المجال المرجعي / الضوضاء:", "Baseline / Noise Floor:", "Basiswert / Grundrauschen:", "Línea Base / Ruido:", "Linha Base / Ruído:", "Référence / Bruit de Fond:", "Taban Çizgisi / Gürültü Tabanı:")
    fun calibBaselineNoise(isAr: Boolean) = resolve(isAr, "المجال المرجعي / الضوضاء:", "Baseline / Noise Floor:")

    fun calibQualityExcellent(lang: String) = resolve(lang, "ممتاز (ضجيج منخفض، استجابة مثالية)", "EXCELLENT (Low Noise, Optimal)", "HERVORRAGEND (Geringes Rauschen, Optimal)", "EXCELENTE (Bajo Ruido, Óptimo)", "EXCELENTE (Baixo Ruído, Ótimo)", "EXCELLENT (Faible Bruit, Optimal)", "MÜKEMMEL (Düşük Gürültü, Optimum)")
    fun calibQualityExcellent(isAr: Boolean) = resolve(isAr, calibQualityExcellent("ar"), calibQualityExcellent("en"))

    fun calibQualityGood(lang: String) = resolve(lang, "جيد (تداخل طفيف)", "GOOD (Minor Noise Detected)", "GUT (Geringfügiges Rauschen)", "BUENO (Ruido Menor Detectado)", "BOM (Ruído Menor Detectado)", "BON (Bruit Mineur Détecté)", "İYİ (Küçük Gürültü Tespit Edildi)")
    fun calibQualityGood(isAr: Boolean) = resolve(isAr, calibQualityGood("ar"), calibQualityGood("en"))

    fun calibQualityPoor(lang: String) = resolve(lang, "ضعيف (تداخل كهرومغناطيسي مرتفع)", "POOR (High Interference)", "SCHLECHT (Hohe Störungen)", "POBRE (Alta Interferencia)", "RUIM (Alta Interferência)", "FAIBLE (Forte Interférence)", "ZAYIF (Yüksek Parazit)")
    fun calibQualityPoor(isAr: Boolean) = resolve(isAr, calibQualityPoor("ar"), calibQualityPoor("en"))

    // 9 Steps
    fun calibStep1(lang: String) = resolve(lang, "١. الفحص الذاتي لسجلات المستشعر", "1. Sensor Self-Test & Polling Rate", "1. Sensor-Selbsttest & Abtastrate", "1. Autodiagnóstico de Sensor y Tasa", "1. Autoteste de Sensor e Taxa", "1. Auto-test du Capteur & Taux", "1. Sensör Kendi Kendini Test & Hız")
    fun calibStep1(isAr: Boolean) = resolve(isAr, calibStep1("ar"), calibStep1("en"))

    fun calibStep2(lang: String) = resolve(lang, "٢. قياس المجال المغناطيسي المحيط", "2. Measure Ambient Geomagnetic Field", "2. Umgebungsmagnetfeld messen", "2. Medir Campo Geomagnético Ambiental", "2. Medir Campo Geomagnético Ambiente", "2. Mesurer Champ Géomagnétique Ambiant", "2. Ortam Jeomanyetik Alanını Ölç")
    fun calibStep2(isAr: Boolean) = resolve(isAr, calibStep2("ar"), calibStep2("en"))

    fun calibStep3(lang: String) = resolve(lang, "٣. جمع عينات حركية متعددة", "3. Multi-Point Motion Sampling", "3. Mehrpunkt-Bewegungsabtastung", "3. Muestreo de Movimiento Multipunto", "3. Amostragem de Movimento Multiponto", "3. Échantillonnage de Mouvement Multipoint", "3. Çok Noktalı Hareket Örneklemesi")
    fun calibStep3(isAr: Boolean) = resolve(isAr, calibStep3("ar"), calibStep3("en"))

    fun calibStep4(lang: String) = resolve(lang, "٤. حساب خط الأساس الهندسي (X, Y, Z)", "4. Compute Geometric Baseline", "4. Geometrischen Basiswert berechnen", "4. Calcular Línea Base Geométrica", "4. Calcular Linha Base Geométrica", "4. Calculer la Référence Géométrique", "4. Geometrik Taban Çizgisini Hesapla")
    fun calibStep4(isAr: Boolean) = resolve(isAr, calibStep4("ar"), calibStep4("en"))

    fun calibStep5(lang: String) = resolve(lang, "٥. تحليل مستوى الضوضاء والتباين", "5. Analyze Noise Floor & Variance", "5. Grundrauschen & Varianz analysieren", "5. Analizar Nivel de Ruido y Varianza", "5. Analisar Nível de Ruído e Variância", "5. Analyser Bruit de Fond & Variance", "5. Gürültü Tabanı ve Varyansı Analiz Et")
    fun calibStep5(isAr: Boolean) = resolve(isAr, calibStep5("ar"), calibStep5("en"))

    fun calibStep6(lang: String) = resolve(lang, "٦. حساب عتبة التنبيه الديناميكية", "6. Calculate Dynamic Threshold", "6. Dynamische Schwelle berechnen", "6. Calcular Umbral Dinámico", "6. Calcular Limiar Dinâmico", "6. Calculer le Seuil Dynamique", "6. Dinamik Eşik Değerini Hesapla")
    fun calibStep6(isAr: Boolean) = resolve(isAr, calibStep6("ar"), calibStep6("en"))

    fun calibStep7(lang: String) = resolve(lang, "٧. التعويض المستمر عن الانحراف البيئي", "7. Continuous Environmental Compensation", "7. Kontinuierliche Umgebungskompensation", "7. Compensación Ambiental Continua", "7. Compensação Ambiental Contínua", "7. Compensation Environnementale Continue", "7. Sürekli Çevresel Dengeleme")
    fun calibStep7(isAr: Boolean) = resolve(isAr, calibStep7("ar"), calibStep7("en"))

    fun calibStep8(lang: String) = resolve(lang, "٨. ضبط الحساسية اليدوية", "8. Sensitivity & Range Adjustment", "8. Empfindlichkeits- & Bereichsanpassung", "8. Ajuste de Sensibilidad y Rango", "8. Ajuste de Sensibilidade e Faixa", "8. Ajustement de Sensibilité & Plage", "8. Hassasiyet ve Aralık Ayarı")
    fun calibStep8(isAr: Boolean) = resolve(isAr, calibStep8("ar"), calibStep8("en"))

    fun calibStep9(lang: String) = resolve(lang, "٩. تقييم جودة المعايرة النهائية", "9. Final Quality Assessment", "9. Abschließende Qualitätsbewertung", "9. Evaluación Final de Calidad", "9. Avaliação Final de Qualidade", "9. Évaluation Finale de Qualité", "9. Son Kalite Değerlendirmesi")
    fun calibStep9(isAr: Boolean) = resolve(isAr, calibStep9("ar"), calibStep9("en"))

    // Devices & Hardware
    fun devicesScreenTitle(lang: String) = resolve(lang, "مستشعرات الأجهزة وواجهات الاتصال", "HARDWARE SENSORS & INTERFACES", "HARDWARE-SENSOREN & SCHNITTSTELLEN", "SENSORES DE HARDWARE E INTERFACES", "SENSORES DE HARDWARE E INTERFACES", "CAPTEURS MATÉRIELS ET INTERFACES", "DONANIM SENSÖRLERİ VE ARAYÜZLER")
    fun devicesScreenTitle(isAr: Boolean) = resolve(isAr, "مستشعرات الأجهزة وواجهات الاتصال", "HARDWARE SENSORS & INTERFACES")

    fun devicesTitle(lang: String) = resolve(lang, "مستشعرات الأجهزة الخارجية", "External Hardware Sensors", "Externe Hardwaresensoren", "Sensores de Hardware Externos", "Sensores de Hardware Externos", "Capteurs Matériels Externes", "Harici Donanım Sensörleri")
    fun devicesTitle(isAr: Boolean) = resolve(isAr, "مستشعرات الأجهزة الخارجية", "External Hardware Sensors")

    fun phoneSensorTitle(lang: String) = resolve(lang, "المستشعر المغناطيسي الداخلي للهاتف", "Built-In Phone Magnetometer", "Integriertes Smartphone-Magnetometer", "Magnetómetro Interno del Teléfono", "Magnetômetro Interno do Telefone", "Magnétomètre Intégré du Téléphone", "Dahili Telefon Manyetometresi")
    fun phoneSensorTitle(isAr: Boolean) = resolve(isAr, "المستشعر المغناطيسي الداخلي للهاتف", "Built-In Phone Magnetometer")

    fun bleSensorTitle(lang: String) = resolve(lang, "ملف كاشف خارجي عبر بلوتوث BLE", "External BLE Detector Coil", "Externe BLE-Suchspule", "Bobina Detectora BLE Externa", "Bobina Detectora BLE Externa", "Bobine Détectrice BLE Externe", "Harici BLE Dedektör Başlığı")
    fun bleSensorTitle(isAr: Boolean) = resolve(isAr, "ملف كاشف خارجي عبر بلوتوث BLE", "External BLE Detector Coil")

    fun usbSensorTitle(lang: String) = resolve(lang, "كاشف تسلسلي عبر منفذ USB OTG", "USB OTG / Serial Detector", "USB OTG / Serieller Detektor", "Detector USB OTG / Serie", "Detector USB OTG / Serial", "Détecteur USB OTG / Série", "USB OTG / Seri Dedektör")
    fun usbSensorTitle(isAr: Boolean) = resolve(isAr, "كاشف تسلسلي عبر منفذ USB OTG", "USB OTG / Serial Detector")

    fun simSensorTitle(lang: String) = resolve(lang, "وضع المحاكاة التجريبي للمطورين", "Developer Simulation (TEST MODE)", "Entwickler-Simulation (TESTMODUS)", "Simulación de Desarrollador (MODO TEST)", "Simulação de Desenvolvedor (MODO TESTE)", "Simulation Développeur (MODE TEST)", "Geliştirici Simülasyonu (TEST MODU)")
    fun simSensorTitle(isAr: Boolean) = resolve(isAr, "وضع المحاكاة التجريبي للمطورين", "Developer Simulation (TEST MODE)")

    fun activeBadge(lang: String) = resolve(lang, "نشط", "ACTIVE", "AKTIV", "ACTIVO", "ATIVO", "ACTIF", "AKTİF")
    fun activeBadge(isAr: Boolean) = resolve(isAr, "نشط", "ACTIVE")

    fun selectBtn(lang: String) = resolve(lang, "اختيار", "Select", "Auswählen", "Seleccionar", "Selecionar", "Sélectionner", "Seç")
    fun selectBtn(isAr: Boolean) = resolve(isAr, "اختيار", "Select")

    fun bleScanBtn(lang: String) = resolve(lang, "البحث عن أجهزة BLE", "Scan BLE Coils", "BLE-Spulen suchen", "Escanear Bobinas BLE", "Buscar Bobinas BLE", "Scanner Bobines BLE", "BLE Başlıklarını Tara")
    fun bleScanBtn(isAr: Boolean) = resolve(isAr, "البحث عن أجهزة BLE", "Scan BLE Coils")

    fun bleStopScanBtn(lang: String) = resolve(lang, "إيقاف البحث", "Stop Scan", "Scan stoppen", "Detener Escaneo", "Parar Busca", "Arrêter le Scan", "Taramayı Durdur")
    fun bleStopScanBtn(isAr: Boolean) = resolve(isAr, "إيقاف البحث", "Stop Scan")

    // History Screen
    fun historyScreenTitle(lang: String) = resolve(lang, "سجل الكشوفات والإشارات", "DETECTION LOGS", "ERFASSUNGSPROTOKOLLE", "REGISTROS DE DETECCIÓN", "REGISTROS DE DETECÇÃO", "JOURNAUX DE DÉTECTION", "TESPİT GÜNLÜKLERİ")
    fun historyScreenTitle(isAr: Boolean) = resolve(isAr, "سجل الكشوفات والإشارات", "DETECTION LOGS")

    fun clearAllHistory(lang: String) = resolve(lang, "مسح كافة السجلات", "Clear All Logs", "Alle Protokolle löschen", "Borrar Todos los Registros", "Limpar Todos os Registros", "Effacer Tous les Journaux", "Tüm Günlükleri Temizle")
    fun clearAllHistory(isAr: Boolean) = resolve(isAr, "مسح كافة السجلات", "Clear All Logs")

    fun clearAllConfirm(lang: String) = resolve(
        lang,
        "هل أنت متأكد من رغبتك في حذف جميع السجلات المسجلة نهائياً؟",
        "Are you sure you want to permanently delete all recorded detection logs?",
        "Möchten Sie wirklich alle aufgezeichneten Erkennungsprotokolle dauerhaft löschen?",
        "¿Está seguro de que desea eliminar permanentemente todos los registros?",
        "Tem certeza de que deseja excluir permanentemente todos os registros?",
        "Êtes-vous sûr de vouloir supprimer définitivement tous les journaux enregistrés ?",
        "Kaydedilen tüm tespit günlüklerini kalıcı olarak silmek istediğinizden emin misiniz?"
    )
    fun clearAllConfirm(isAr: Boolean) = resolve(isAr, clearAllConfirm("ar"), clearAllConfirm("en"))

    fun noLogsTitle(lang: String) = resolve(lang, "لا توجد أحداث كشف مسجلة حتى الآن", "No detection events logged yet", "Noch keine Ereignisse protokolliert", "No hay eventos registrados aún", "Nenhum evento registrado ainda", "Aucun événement enregistré pour l'instant", "Henüz kaydedilmiş tespit olayı yok")
    fun noLogsTitle(isAr: Boolean) = resolve(isAr, "لا توجد أحداث كشف مسجلة حتى الآن", "No detection events logged yet")

    fun noLogsDesc(lang: String) = resolve(
        lang,
        "اضغط على زر 'حفظ السجل' في شاشة الكشف أثناء أو بعد الفحص لتسجيل الإشارة.",
        "Press the 'LOG' button on the detection screen during or after a scan to record signals.",
        "Drücken Sie auf dem Erkennungsbildschirm auf 'SPEICHERN', um Signale aufzuzeichnen.",
        "Presione el botón 'GUARDAR' en la pantalla de detección para registrar señales.",
        "Pressione o botão 'SALVAR' na tela de detecção para registrar sinais.",
        "Appuyez sur le bouton 'ENREGISTRER' sur l'écran de détection pour enregistrer les signaux.",
        "Sinyalleri kaydetmek için tarama sırasında veya sonrasında tespit ekranındaki 'KAYDET' düğmesine basın."
    )
    fun noLogsDesc(isAr: Boolean) = resolve(isAr, noLogsDesc("ar"), noLogsDesc("en"))

    fun viewMap(lang: String) = resolve(lang, "عرض في الخريطة", "View Map", "Karte anzeigen", "Ver Mapa", "Ver Mapa", "Voir la Carte", "Haritada Gör")
    fun viewMap(isAr: Boolean) = resolve(isAr, "عرض في الخريطة", "View Map")

    // Settings Screen
    fun settingsScreenTitle(lang: String) = resolve(lang, "أنماط الكشف وإعدادات الجهاز", "DETECTION MODES & INSTRUMENT SETTINGS", "ERKENNUNGSMODI & GERÄTEEINSTELLUNGEN", "MODOS DE DETECCIÓN Y AJUSTES", "MODOS DE DETECÇÃO E CONFIGURAÇÕES", "MODES DE DÉTECTION ET PARAMÈTRES", "TESPİT MODLARI VE CİHAZ AYARLARI")
    fun settingsScreenTitle(isAr: Boolean) = resolve(isAr, "أنماط الكشف وإعدادات الجهاز", "DETECTION MODES & INSTRUMENT SETTINGS")

    fun languageTitle(lang: String) = resolve(lang, "لغة التطبيق / Language", "App Language / لغة التطبيق", "App-Sprache / Language", "Idioma de la Aplicación", "Idioma do Aplicativo", "Langue de l'Application", "Uygulama Dili")
    fun languageTitle(isAr: Boolean) = resolve(isAr, "لغة التطبيق / Language", "App Language / لغة التطبيق")

    fun detectionModesSection(lang: String) = resolve(lang, "أنماط التشغيل المعدة مسبقاً", "OPERATING PRESET MODES", "BETRIEBS-VOREINSTELLUNGEN", "MODOS DE OPERACIÓN PREDETERMINADOS", "MODOS PREDEFINIDOS DE OPERAÇÃO", "MODES PRÉDÉFINIS DE FONCTIONNEMENT", "ÇALIŞMA ÖNAYAR MODLARI")
    fun detectionModesSection(isAr: Boolean) = resolve(isAr, "أنماط التشغيل المعدة مسبقاً", "OPERATING PRESET MODES")

    fun audioSynthSection(lang: String) = resolve(lang, "مخلق التنبيهات الصوتية (Synthesizer)", "AUDIO FEEDBACK SYNTHESIZER", "AUDIO-FEEDBACK-SYNTHESIZER", "SINTETIZADOR DE AUDIO", "SINTETIZADOR DE ÁUDIO", "SYNTHÉTISEUR AUDIO", "SESLİ GERİ BİLDİRİM SENTEZLEYİCİSİ")
    fun audioSynthSection(isAr: Boolean) = resolve(isAr, "مخلق التنبيهات الصوتية (Synthesizer)", "AUDIO FEEDBACK SYNTHESIZER")

    fun toneSynthesisMode(lang: String) = resolve(lang, "نمط تخليق النغمة", "Tone Synthesis Mode", "Tonsynthese-Modus", "Modo de Síntesis de Tono", "Modo de Síntese de Tom", "Mode de Synthèse de Tonalité", "Ton Sentezleme Modu")
    fun toneSynthesisMode(isAr: Boolean) = resolve(isAr, "نمط تخليق النغمة", "Tone Synthesis Mode")

    fun volume(lang: String) = resolve(lang, "مستوى الصوت", "Volume", "Lautstärke", "Volumen", "Volume", "Volume", "Ses Seviyesi")
    fun volume(isAr: Boolean) = resolve(isAr, "مستوى الصوت", "Volume")

    fun muteThreshold(lang: String) = resolve(lang, "عتبة الكتم (الحد الأدنى للإشارة %)", "Mute Threshold (Min Signal %)", "Stummschaltungsschwelle (Min Signal %)", "Umbral de Silencio (Señal Mín %)", "Limiar de Mudo (Sinal Mín %)", "Seuil de Silence (Signal Min %)", "Sessiz Eşiği (Min Sinyal %)")
    fun muteThreshold(isAr: Boolean) = resolve(isAr, "عتبة الكتم (الحد الأدنى للإشارة %)", "Mute Threshold (Min Signal %)")

    fun vibrationSection(lang: String) = resolve(lang, "التنبيه بالاهتزاز واللمس (Haptic)", "HAPTIC / VIBRATION FEEDBACK", "HAPTISCHES / VIBRATIONS-FEEDBACK", "RETROALIMENTACIÓN HÁPTICA / VIBRACIÓN", "FEEDBACK HÁPTICO / VIBRAÇÃO", "RETOUR HAPTIQUE / VIBRATION", "DOKUNSAL / TİTREŞİM GERİ BİLDİRİMİ")
    fun vibrationSection(isAr: Boolean) = resolve(isAr, "التنبيه بالاهتزاز واللمس (Haptic)", "HAPTIC / VIBRATION FEEDBACK")

    fun vibrationPattern(lang: String) = resolve(lang, "نمط الاهتزاز اللمسي", "Haptic Vibration Pattern", "Vibrationsmuster", "Patrón de Vibración", "Padrão de Vibração", "Modèle de Vibration", "Titreşim Deseni")
    fun vibrationPattern(isAr: Boolean) = resolve(isAr, "نمط الاهتزاز اللمسي", "Haptic Vibration Pattern")

    fun expertSection(lang: String) = resolve(lang, "معاملات فلتر الإشارة الرقمي (DSP)", "EXPERT SIGNAL FILTER PARAMETERS", "DSP-SIGNALFILTERPARAMETER", "PARÁMETROS DE FILTRO DSP", "PARÂMETROS DE FILTRO DSP", "PARAMÈTRES DU FILTRE DSP", "DSP SİNYAL FİLTRESİ PARAMETRELERİ")
    fun expertSection(isAr: Boolean) = resolve(isAr, "معاملات فلتر الإشارة الرقمي (DSP)", "EXPERT SIGNAL FILTER PARAMETERS")

    fun filterAlpha(lang: String) = resolve(lang, "معامل تنعيم الفلتر (α)", "IIR Filter Smoothing Alpha (α)", "IIR-Filterglättung Alpha (α)", "Suavizado de Filtro IIR Alfa (α)", "Suavização de Filtro IIR Alfa (α)", "Lissage Filtre IIR Alpha (α)", "IIR Filtre Yumuşatma Alfa (α)")
    fun filterAlpha(isAr: Boolean) = resolve(isAr, "معامل تنعيم الفلتر (α)", "IIR Filter Smoothing Alpha (α)")

    fun driftCompTitle(lang: String) = resolve(lang, "التعويض المستمر عن الانجراف البيئي", "Continuous Drift Compensation", "Kontinuierliche Drift-Kompensation", "Compensación Continua de Deriva", "Compensação Contínua de Deriva", "Compensation Continue de Dérive", "Sürekli Sürüklenme Dengelemesi")
    fun driftCompTitle(isAr: Boolean) = resolve(isAr, "التعويض المستمر عن الانجراف البيئي", "Continuous Drift Compensation")

    fun driftCompDesc(lang: String) = resolve(
        lang,
        "تتبع التغيرات البطيئة في المجال المغناطيسي الأرضي المحيط",
        "Slowly tracks background geomagnetic shifts",
        "Verfolgt langsame geomagnetische Hintergrundverschiebungen",
        "Rastrea cambios geomagnéticos lentos de fondo",
        "Rastreia mudanças geomagnéticas lentas de fundo",
        "Suit les variations géomagnétiques lentes en arrière-plan",
        "Arka plandaki yavaş jeomanyetik kaymaları takip eder"
    )
    fun driftCompDesc(isAr: Boolean) = resolve(isAr, driftCompDesc("ar"), driftCompDesc("en"))

    // Mode helper titles & descriptions
    fun modeTitle(mode: DetectionMode, lang: String = "en"): String {
        return when (mode) {
            DetectionMode.QUICK_SCAN -> resolve(lang, "مسح سريع", "Quick Scan", "Schnellscan", "Escaneo Rápido", "Varredura Rápida", "Balayage Rapide", "Hızlı Tarama")
            DetectionMode.DEEP_ANALYSIS -> resolve(lang, "تحليل عميق للإشارة", "Deep Signal Analysis", "Tiefensignalanalyse", "Análisis Profundo", "Análise Profunda", "Analyse Approfondie", "Derin Sinyal Analizi")
            DetectionMode.FERROUS_DETECTION -> resolve(lang, "التركيز على المعادن الحديدية", "Ferrous Metal Focus", "Eisenmetall-Fokus", "Enfoque Ferroso", "Foco Ferroso", "Focus Métaux Ferreux", "Demir Metal Odaklı")
            DetectionMode.NON_FERROUS_DETECTION -> resolve(lang, "تمييز المعادن غير الحديدية", "Non-Ferrous Discrimination", "NE-Metall-Diskriminierung", "Discriminación No Ferrosa", "Discriminação Não Ferrosa", "Discrimination Non Ferreuse", "Demir Dışı Ayrımı")
            DetectionMode.EXTERNAL_SENSOR_MODE -> resolve(lang, "وضع الأجهزة الخارجية", "External Hardware Mode", "Externer Hardwaremodus", "Modo Hardware Externo", "Modo Hardware Externo", "Mode Matériel Externe", "Harici Donanım Modu")
            DetectionMode.EXPERT_MANUAL -> resolve(lang, "الوضع الاحترافي / اليدوي", "Expert / Manual Mode", "Experten- / Manuellmodus", "Modo Experto / Manual", "Modo Especialista / Manual", "Mode Expert / Manuel", "Uzman / Manuel Mod")
        }
    }
    fun modeTitle(mode: DetectionMode, isAr: Boolean): String = modeTitle(mode, if (isAr) "ar" else "en")

    fun modeDesc(mode: DetectionMode, lang: String = "en"): String {
        return when (mode) {
            DetectionMode.QUICK_SCAN -> resolve(
                lang,
                "تصفية خفيفة لمسح سريع واستجابة فورية",
                "Light filtering for rapid sweeping & immediate response",
                "Leichte Filterung für schnelles Schwenken & sofortige Reaktion",
                "Filtrado ligero para barrido rápido y respuesta inmediata",
                "Filtragem leve para varredura rápida e resposta imediata",
                "Filtrage léger pour balayage rapide et réponse immédiate",
                "Hızlı tarama ve anında yanıt için hafif filtreleme"
            )
            DetectionMode.DEEP_ANALYSIS -> resolve(
                lang,
                "تصفية متطورة لتعزيز نسبة الإشارة وكشف الأهداف العميقة",
                "Enhanced multi-sample SNR filtering for subtle anomalies",
                "Verbesserte Mehrpunkt-SNR-Filterung für schwache Anomalien",
                "Filtrado SNR mejorado para anomalías sutiles",
                "Filtragem SNR aprimorada para anomalias sutis",
                "Filtrage SNR amélioré pour anomalies subtiles",
                "Hassas anomaliler için geliştirilmiş çoklu örnek SNR filtreleme"
            )
            DetectionMode.FERROUS_DETECTION -> resolve(
                lang,
                "مخصص للأجسام الفولاذية والحديدية والأنابيب والأسلاك",
                "Optimized for iron, steel, pipes, and ferromagnetic targets",
                "Optimiert für Eisen, Stahl, Rohre und Magnetziele",
                "Optimizado para hierro, acero, tuberías y dianas ferrosas",
                "Otimizado para ferro, aço, canos e alvos ferrosos",
                "Optimisé pour le fer, l'acier, les tuyaux et cibles ferreuses",
                "Demir, çelik, borular ve manyetik hedefler için optimize edilmiştir"
            )
            DetectionMode.NON_FERROUS_DETECTION -> resolve(
                lang,
                "تمييز الطور والتوصيلية (يتطلب ملف كاشف خارجي)",
                "Phase & conductivity discrimination (requires external hardware coil)",
                "Phasen- & Leitfähigkeitsunterscheidung (externe Spule erforderlich)",
                "Discriminación de fase y conductividad (requiere bobina externa)",
                "Discriminação de fase e condutividade (requer bobina externa)",
                "Discrimination de phase et conductivité (bobine externe requise)",
                "Faz ve iletkenlik ayrımı (harici donanım bobini gerektirir)"
            )
            DetectionMode.EXTERNAL_SENSOR_MODE -> resolve(
                lang,
                "استقبال مباشر للبيانات من جهاز كشف BLE أو USB",
                "Full telemetry streaming from connected BLE or USB hardware",
                "Vollständiges Telemetrie-Streaming von BLE- oder USB-Hardware",
                "Transmisión completa de telemetría desde hardware BLE o USB",
                "Streaming completo de telemetria de hardware BLE ou USB",
                "Diffusion complète de télémétrie depuis le matériel BLE ou USB",
                "Bağlı BLE veya USB donanımından tam telemetri akışı"
            )
            DetectionMode.EXPERT_MANUAL -> resolve(
                lang,
                "تحكم يدوي كامل في الحساسية وفلتر الترددات وخط الأساس",
                "Customizable sensitivity, threshold offset, filter, and ground balance",
                "Anpassbare Empfindlichkeit, Schwellenwert, Filter und Bodenabgleich",
                "Sensibilidad, umbral, filtro y balance de tierra personalizables",
                "Sensibilidade, limiar, filtro e balanço de solo personalizáveis",
                "Sensibilité, seuil, filtre et compensation de sol personnalisables",
                "Özelleştirilebilir hassasiyet, eşik ofseti, filtre ve zemin dengesi"
            )
        }
    }
    fun modeDesc(mode: DetectionMode, isAr: Boolean): String = modeDesc(mode, if (isAr) "ar" else "en")

    // Tone helper
    fun toneName(tone: ToneType, lang: String = "en"): String {
        return when (tone) {
            ToneType.VCO_CONTINUOUS -> resolve(lang, "نغمة متغيرة VCO", "VCO Continuous", "VCO Kontinuierlich", "VCO Continuo", "VCO Contínuo", "VCO Continu", "VCO Sürekli")
            ToneType.PULSED_CLICKER -> resolve(lang, "نبضات جيجر", "Pulsed Geiger", "Geiger-Impulse", "Pulsos Geiger", "Pulsos Geiger", "Impulsions Geiger", "Geiger Darbeleri")
            ToneType.MULTI_TONE -> resolve(lang, "نغمات متعددة", "Multi-Tone", "Mehrton", "Multitono", "Multitom", "Multi-tonalité", "Çoklu Ton")
        }
    }
    fun toneName(tone: ToneType, isAr: Boolean): String = toneName(tone, if (isAr) "ar" else "en")

    // Vibration helper
    fun vibrationModeName(vMode: VibrationMode, lang: String = "en"): String {
        return when (vMode) {
            VibrationMode.PROPORTIONAL_PULSE -> resolve(
                lang,
                "نبضات متناسبة مع قوة الإشارة",
                "Proportional Pulse (Fast pulsing as signal rises)",
                "Proportionaler Impuls (Schneller bei steigendem Signal)",
                "Pulso Proporcional (Pulsos rápidos según señal)",
                "Pulso Proporcional (Pulsos rápidos conforme sinal)",
                "Pulsion Proportionnelle (Pulsations rapides selon signal)",
                "Orantılı Darbe (Sinyal arttıkça hızlanan darbeler)"
            )
            VibrationMode.CONTINUOUS_INTENSITY -> resolve(
                lang,
                "اهتزاز مستمر متغير الشدة",
                "Continuous Variable Intensity",
                "Kontinuierlich variable Intensität",
                "Intensidad Variable Continua",
                "Intensidade Variável Contínua",
                "Intensité Variable Continue",
                "Sürekli Değişken Yoğunluk"
            )
            VibrationMode.SHORT_PULSE -> resolve(
                lang,
                "نبضة واحدة عند تجاوز العتبة",
                "Single Pulse on Threshold Trigger",
                "Einzellimpuls bei Schwellenwertüberschreitung",
                "Pulso Único al Superar Umbral",
                "Pulso Único ao Atingir Limiar",
                "Pulsion Unique au Dépassement du Seuil",
                "Eşik Aşıldığında Tek Darbe"
            )
            VibrationMode.OFF -> resolve(lang, "معطل", "Off", "Aus", "Desactivado", "Desativado", "Désactivé", "Kapalı")
        }
    }
    fun vibrationModeName(vMode: VibrationMode, isAr: Boolean): String = vibrationModeName(vMode, if (isAr) "ar" else "en")

    // Additional Screen Helpers
    fun calibReady(lang: String) = resolve(lang, "جاهز لبدء المعايرة", "Ready to Calibrate", "Bereit zur Kalibrierung", "Listo para Calibrar", "Pronto para Calibrar", "Prêt à Calibrer", "Kalibrasyona Hazır")
    fun calibReCalibrate(lang: String) = resolve(lang, "إعادة معايرة المستشعر", "RE-CALIBRATE SENSOR", "SENSOR NEU KALIBRIEREN", "RECALIBRAR SENSOR", "RECALIBRAR SENSOR", "RECALIBRER LE CAPTEUR", "SENSÖRÜ YENİDEN KALİBRE ET")
    fun calibStartGuided(lang: String) = resolve(lang, "بدء المعايرة الذاتية", "START GUIDED CALIBRATION", "GEFÜHRTE KALIBRIERUNG STARTEN", "INICIAR CALIBRACIÓN GUIADA", "INICIAR CALIBRAÇÃO GUIADA", "LANCER LE CALIBRAGE GUIDÉ", "REHBERLİ KALİBRASYONU BAŞLAT")
    fun calibReportTitle(lang: String) = resolve(lang, "تقرير المعايرة", "CALIBRATION REPORT", "KALIBRIERUNGSBERICHT", "INFORME DE CALIBRACIÓN", "RELATÓRIO DE CALIBRAÇÃO", "RAPPORT DE CALIBRAGE", "KALİBRASYON RAPORU")
    fun calibQualityExcellentShort(lang: String) = resolve(lang, "ممتاز", "EXCELLENT", "HERVORRAGEND", "EXCELENTE", "EXCELENTE", "EXCELLENT", "MÜKEMMEL")
    fun calibQualityGoodShort(lang: String) = resolve(lang, "جيد", "GOOD", "GUT", "BUENO", "BOM", "BON", "İYİ")
    fun calibQualityPoorShort(lang: String) = resolve(lang, "ضعيف", "POOR", "SCHLECHT", "POBRE", "RUIM", "FAIBLE", "ZAYIF")
    fun calibBaseline(lang: String) = resolve(lang, "خط الأساس", "Calibrated Baseline", "Kalibrierte Basislinie", "Línea Base Calibrada", "Linha Base Calibrada", "Référence Calibrée", "Kalibre Edilmiş Taban Çizgisi")
    fun calibNoiseFloor(lang: String) = resolve(lang, "مستوى الضوضاء (σ)", "Noise Floor (σ)", "Grundrauschen (σ)", "Nivel de Ruido (σ)", "Nível de Ruído (σ)", "Bruit de Fond (σ)", "Gürültü Tabanı (σ)")
    fun calibThreshold(lang: String) = resolve(lang, "عتبة التنبيه", "Trigger Threshold", "Auslöseschwelle", "Umbral de Disparo", "Limiar de Disparo", "Seuil de Déclenchement", "Tetikleme Eşiği")
    fun calibManualSensitivity(lang: String) = resolve(lang, "ضبط الحساسية اليدوية", "MANUAL SENSITIVITY ADJUSTMENT", "MANUELLE EMPFINDLICHKEITSEINSTELLUNG", "AJUSTE MANUAL DE SENSIBILIDAD", "AJUSTE MANUAL DE SENSIBILIDADE", "RÉGLAGE MANUEL DE SENSIBILITÉ", "MANUEL HASSASİYET AYARI")
    fun calibSensitivityHint(lang: String) = resolve(
        lang,
        "الحساسية العالية ترصد الأهداف الصغيرة والبعيدة، لكن قد تتأثر بحركة اليد السريعة.",
        "Higher sensitivity detects smaller anomalies but may increase sensitivity to device movement.",
        "Höhere Empfindlichkeit erkennt kleinere Anomalien, reagiert aber empfindlicher auf Gerätebewegungen.",
        "Mayor sensibilidad detecta anomalías más pequeñas pero puede ser sensible al movimiento.",
        "Maior sensibilidade detecta anomalias menores, mas pode ser sensível ao movimento.",
        "Une sensibilité plus élevée détecte de plus petites anomalies mais réagit au mouvement.",
        "Daha yüksek hassasiyet daha küçük anomalileri tespit eder ancak cihaz hareketine duyarlılığı artırabilir."
    )

    fun discoveredDevices(lang: String) = resolve(lang, "الأجهزة المكتشفة", "DISCOVERED DEVICES", "ENTDECKTE GERÄTE", "DISPOSITIVOS DESCUBIERTOS", "DISPOSITIVOS DESCOBERTOS", "APPAREILS DÉCOUVERTS", "BULUNAN CİHAZLAR")
    fun connected(lang: String) = resolve(lang, "متصل", "Connected", "Verbunden", "Conectado", "Conectado", "Connecté", "Bağlı")
    fun connect(lang: String) = resolve(lang, "اتصال", "Connect", "Verbinden", "Conectar", "Conectar", "Connecter", "Bağlan")
    fun plugUsbHint(lang: String) = resolve(
        lang,
        "قم بتوصيل كابل USB OTG لمستشعر المعادن للتعرف على الجهاز تلقائياً.",
        "Plug in a USB OTG serial metal detector cable to auto-detect hardware.",
        "Schließen Sie ein USB-OTG-Kabel an, um die Hardware automatisch zu erkennen.",
        "Conecte un cable USB OTG para detectar automáticamente el hardware.",
        "Conecte um cabo USB OTG para detectar automaticamente o hardware.",
        "Branchez un câble USB OTG pour détecter automatiquement le matériel.",
        "Donanımı otomatik algılamak için bir USB OTG kablosu takın."
    )
    fun openUsb(lang: String) = resolve(lang, "فتح USB", "Open USB", "USB öffnen", "Abrir USB", "Abrir USB", "Ouvrir USB", "USB Aç")
    fun simDesc(lang: String) = resolve(lang, "توليد إشارات اصطناعية لاختبار الخوارزميات بدون هاردوير", "Generates synthetic signals for testing", "Erzeugt synthetische Signale zu Testzwecken", "Genera señales sintéticas para pruebas", "Gera sinais sintéticos para testes", "Génère des signaux synthétiques pour les tests", "Testler için sentetik sinyaller üretir")
    fun simVdiToggle(lang: String) = resolve(lang, "محاكاة هوية الهدف VDI الخارجية", "Simulate External Hardware VDI (Target ID)", "Externe VDI-Hardware simulieren", "Simular VDI de Hardware Externo", "Simular VDI de Hardware Externo", "Simuler le VDI Matériel Externe", "Harici Donanım VDI Simülasyonu")

    fun recordedLogsCount(count: Int, lang: String): String = when (lang) {
        "ar" -> "$count حدث كشف مسجل"
        "de" -> "$count aufgezeichnete Erkennungen"
        "es" -> "$count detecciones registradas"
        "pt" -> "$count detecções registradas"
        "fr" -> "$count détections enregistrées"
        "tr" -> "$count kayıtlı tespit olayı"
        else -> "$count recorded detection events"
    }
    fun deleteAll(lang: String) = resolve(lang, "مسح الكل", "Delete All", "Alle löschen", "Eliminar Todo", "Excluir Tudo", "Tout supprimer", "Tümünü Sil")
}
