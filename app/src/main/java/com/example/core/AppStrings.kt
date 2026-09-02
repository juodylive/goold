package com.example.core

object AppStrings {
    // Current active language helper
    // Supported: "ar" (Arabic) and "en" (English)

    // Navigation Tabs
    fun navDetect(isAr: Boolean) = if (isAr) "كشف" else "Detect"
    fun navCalibrate(isAr: Boolean) = if (isAr) "معايرة" else "Calibrate"
    fun navHardware(isAr: Boolean) = if (isAr) "الأجهزة" else "Hardware"
    fun navHistory(isAr: Boolean) = if (isAr) "السجل" else "History"
    fun navSettings(isAr: Boolean) = if (isAr) "الإعدادات" else "Settings"

    // Top Bar
    fun appTitle(isAr: Boolean) = if (isAr) "ميتال سكان" else "METALSCAN"
    fun appPro(isAr: Boolean) = if (isAr) "برو" else "PRO"
    fun appSubtitle(isAr: Boolean) = if (isAr) "مقياس المغناطيسية عالي الدقة وبيانات الاستشعار" else "PRECISION MAGNETOMETER & TELEMETRY"
    fun sensorPhone(isAr: Boolean) = if (isAr) "مستشعر الهاتف" else "Phone Sensor"
    fun sensorBle(isAr: Boolean) = if (isAr) "بلوتوث BLE" else "BLE Sensor"
    fun sensorUsb(isAr: Boolean) = if (isAr) "منفذ USB" else "USB Sensor"
    fun sensorSim(isAr: Boolean) = if (isAr) "محاكاة تجريبية" else "Simulation"
    fun statusLive(isAr: Boolean) = if (isAr) "نشط" else "LIVE"
    fun statusReady(isAr: Boolean) = if (isAr) "جاهز" else "READY"
    fun statusConnecting(isAr: Boolean) = if (isAr) "جاري الاتصال" else "CONNECTING"
    fun statusDisconnected(isAr: Boolean) = if (isAr) "غير متصل" else "DISCONNECTED"

    // Detect Screen
    fun modeLabel(isAr: Boolean) = if (isAr) "النمط" else "MODE"
    fun zeroBaseline(isAr: Boolean) = if (isAr) "تصفير" else "ZERO"
    fun audioFeedback(isAr: Boolean) = if (isAr) "التنبيه الصوتي" else "Audio Feedback"
    fun startScan(isAr: Boolean) = if (isAr) "بدء المسح" else "START SCAN"
    fun stopScan(isAr: Boolean) = if (isAr) "إيقاف المسح" else "STOP SCAN"
    fun calibrateBtn(isAr: Boolean) = if (isAr) "معايرة" else "CALIBRATE"
    fun saveLogBtn(isAr: Boolean) = if (isAr) "حفظ السجل" else "SAVE LOG"
    fun microtesla(isAr: Boolean) = if (isAr) "ميكروتيسلا (µT)" else "µT (MICROTESLA)"

    // Readout Cards
    fun baseline(isAr: Boolean) = if (isAr) "المرجع" else "BASELINE"
    fun delta(isAr: Boolean) = if (isAr) "الفارق (Δ)" else "DELTA (Δ)"
    fun threshold(isAr: Boolean) = if (isAr) "عتبة التنبيه" else "THRESHOLD"
    fun signal(isAr: Boolean) = if (isAr) "قوة الإشارة" else "SIGNAL"
    fun stability(isAr: Boolean) = if (isAr) "الاستقرار" else "STABILITY"
    fun snr(isAr: Boolean) = if (isAr) "نسبة الإشارة (SNR)" else "SNR"
    fun confidence(isAr: Boolean) = if (isAr) "درجة الثقة" else "CONFIDENCE"

    // Classification
    fun targetClassification(isAr: Boolean) = if (isAr) "تصنيف الهدف" else "TARGET CLASSIFICATION"
    fun estimated(isAr: Boolean) = if (isAr) "تقديري" else "ESTIMATED"
    fun externalRequired(isAr: Boolean) = if (isAr) "يتطلب مستشعر خارجي" else "EXT SENSOR REQ"
    fun ferromagneticScore(isAr: Boolean) = if (isAr) "معامل المغنطة الحديدية:" else "Ferromagnetic Score:"
    fun conductivityVdi(isAr: Boolean) = if (isAr) "معامل التوصيل VDI:" else "Conductivity VDI:"
    fun phaseAngle(isAr: Boolean) = if (isAr) "زاوية الطور:" else "Phase Angle:"

    // Classification Titles & Subtitles
    fun classNoAnomalyTitle(isAr: Boolean) = if (isAr) "لا يوجد اضطراب مغناطيسي" else "No Magnetic Anomaly"
    fun classNoAnomalySub(isAr: Boolean) = if (isAr) "المجال المغناطيسي المحيط طبيعي ومستقر" else "Ambient geomagnetic baseline normal"

    fun classWeakAnomalyTitle(isAr: Boolean) = if (isAr) "اضطراب مغناطيسي ضعيف" else "Weak Magnetic Anomaly"
    fun classWeakAnomalySub(isAr: Boolean) = if (isAr) "احتمال وجود جسم معدني بعيد أو هيكل صغير" else "Possible distant metallic object or small structure"

    fun classStrongAnomalyTitle(isAr: Boolean) = if (isAr) "جسم مغناطيسي قوي" else "Strong Magnetic Object"
    fun classStrongAnomalySub(isAr: Boolean) = if (isAr) "معدن مغناطيسي أو سلك كهربائي قريب" else "High-amplitude magnetic deflection detected nearby"

    fun classFerrousTitle(isAr: Boolean) = if (isAr) "معدن حديدي (حديد / فولاذ)" else "Ferrous Metal (Iron / Steel)"
    fun classFerrousSub(isAr: Boolean) = if (isAr) "استجابة مغناطيسية نموذجية لمواد حديدية أو أنابيب" else "Characteristic ferromagnetic signature detected"

    fun classUnknownMetallicTitle(isAr: Boolean) = if (isAr) "اضطراب معدني غير محدد" else "Unknown Metallic Anomaly"
    fun classUnknownMetallicSub(isAr: Boolean) = if (isAr) "تغير مفاجئ في المجال المغناطيسي" else "Transient magnetic flux disturbance"

    fun classExtFerrousTitle(isAr: Boolean) = if (isAr) "حديد / خردة حديدية" else "Ferrous Metal (Iron / Nails / Trash)"
    fun classExtFerrousSub(isAr: Boolean) = if (isAr) "تم التمييز بواسطة المستشعر الخارجي (VDI منخفض)" else "Low VDI phase angle identified by external coil"

    fun classExtNonFerrousTitle(isAr: Boolean) = if (isAr) "معدن غير حديدي" else "Non-Ferrous Metal"
    fun classExtNonFerrousSub(isAr: Boolean) = if (isAr) "معدن موصل غير مغناطيسي تم التحقق منه بالملف الخارجي" else "Verified non-magnetic conductive target by coil"

    fun classExtGoldTitle(isAr: Boolean) = if (isAr) "هدف محتمل (نطاق الذهب / النيكل)" else "Target Candidate (Gold / Nickel Range)"
    fun classExtGoldSub(isAr: Boolean) = if (isAr) "استجابة طورية متوسطة عبر المستشعر الخارجي" else "Mid-conductive phase response from external coil"

    fun classExtSilverTitle(isAr: Boolean) = if (isAr) "هدف عالي التوصيل (نطاق الفضة / العملات)" else "High-Conductivity (Silver / Coin Range)"
    fun classExtSilverSub(isAr: Boolean) = if (isAr) "توصيل مرتفع وزاوية طور عالية عبر المستشعر الخارجي" else "High VDI phase response from external coil"

    fun classExtCopperTitle(isAr: Boolean) = if (isAr) "هدف عالي التوصيل (نطاق النحاس / البرونز)" else "High-Conductivity (Copper / Bronze)"
    fun classExtCopperSub(isAr: Boolean) = if (isAr) "توصيل كهربائي مرتفع عبر المستشعر الخارجي" else "High phase conductivity detected via external sensor"

    fun classExtAluminumTitle(isAr: Boolean) = if (isAr) "هدف متوسط التوصيل (نطاق الألومنيوم / الرصاص)" else "Mid-Conductivity (Aluminum / Lead)"
    fun classExtAluminumSub(isAr: Boolean) = if (isAr) "استجابة متوسطة عبر المستشعر الخارجي" else "Mid-range phase signature detected via external sensor"

    // Graph
    fun oscillogram(isAr: Boolean) = if (isAr) "مخطط التذبذب (µT)" else "OSCILLOGRAM (µT)"
    fun graphRaw(isAr: Boolean) = if (isAr) "الخام" else "Raw"
    fun graphFiltered(isAr: Boolean) = if (isAr) "المصفى" else "Filtered"
    fun graphBase(isAr: Boolean) = if (isAr) "المرجع" else "Base"
    fun graphThreshold(isAr: Boolean) = if (isAr) "العتبة" else "Threshold"

    // Disclaimer
    fun disclaimerTitle(isAr: Boolean) = if (isAr) "تنبيه فيزيائي حول دقة المستشعرات" else "Physics & Sensor Accuracy Notice"
    fun disclaimerBody(isAr: Boolean) = if (isAr)
        "• مقياس المغناطيسية للهاتف: يقيس التدفق المغناطيسي الأرضي (µT) بثلاثة أبعاد. شديد الحساسية للمواد الحديدية والفولاذ والمغناطيس والأسلاك الحية. لا يمكنه فيزيائياً تمييز أو كشف الذهب أو الفضة بمفرده دون جهاز كشف نبضي أو كهرومغناطيسي.\n\n• وضع المستشعر الخارجي: توصيل ملف بحث خارجي عبر BLE أو USB يتيح قياس التوصيلية الحقيقية وزاوية الطور وهوية الهدف (VDI).\n\n• إخلاء مسؤولية: جميع التصنيفات هي تقديرات برمجية رياضية وليست مضمونة بنسبة 100%."
    else
        "• Built-In Phone Magnetometer: Measures geomagnetic flux (µT) in 3 dimensions. Highly sensitive to ferromagnetic materials (iron, steel, neodymium magnets, power lines). It cannot physically detect or discriminate non-ferrous metals such as gold, silver, or copper.\n\n• External Hardware Sensor Mode: Connecting an external search coil or pulse-induction/VLF detector via BLE/USB allows receiving true conductivity, phase angle, and Target ID (VDI).\n\n• Safety Notice: All displayed classifications are empirical signal estimates and never scientifically guaranteed."

    // Save Dialog
    fun saveLogTitle(isAr: Boolean) = if (isAr) "حفظ سجل الكشف" else "Save Detection Log"
    fun saveLogNotesHint(isAr: Boolean) = if (isAr) "أضف ملاحظات ميدانية (مثل: عمق 10 سم، قرب الصخور)..." else "Add field notes (e.g. Depth ~10cm, near tree)..."
    fun saveToHistory(isAr: Boolean) = if (isAr) "حفظ في السجل" else "Save to History"
    fun cancel(isAr: Boolean) = if (isAr) "إلغاء" else "Cancel"
    fun peakField(isAr: Boolean) = if (isAr) "أعلى إشارة:" else "Peak Field:"
    fun gpsLocation(isAr: Boolean) = if (isAr) "الموقع GPS:" else "GPS Location:"

    // Calibration Screen
    fun calibScreenTitle(isAr: Boolean) = if (isAr) "معايرة المستشعر والفحص الذاتي" else "Sensor Calibration & Self-Test"
    fun calibActiveProcess(isAr: Boolean) = if (isAr) "إجراءات المعايرة النشطة (9 خطوات)" else "Active 9-Step Calibration"
    fun calibMotionGuide(isAr: Boolean) = if (isAr) "دليل الحركة (حركة رقم 8)" else "Motion Guide (Figure-8)"
    fun calibMotionDesc(isAr: Boolean) = if (isAr) "حرك الهاتف ببطء في الهواء بعيداً عن المعادن الكبيرة والأجهزة الكهربائية لمعايرة الحساس ثلاثي المحاور." else "Move the phone slowly in a 3D figure-8 pattern away from large metallic structures to calibrate all 3 axes."
    fun calibStartBtn(isAr: Boolean) = if (isAr) "بدء المعايرة الموجهة" else "Start Guided Calibration"
    fun calibProgress(isAr: Boolean) = if (isAr) "جاري المعايرة..." else "Calibrating..."
    fun calibComplete(isAr: Boolean) = if (isAr) "اكتملت المعايرة بنجاح" else "Calibration Complete"
    fun calibQuality(isAr: Boolean) = if (isAr) "جودة المعايرة:" else "Calibration Quality:"
    fun calibBaselineNoise(isAr: Boolean) = if (isAr) "المجال المرجعي / الضوضاء:" else "Baseline / Noise Floor:"
    fun calibQualityExcellent(isAr: Boolean) = if (isAr) "ممتاز (ضجيج منخفض، استجابة مثالية)" else "EXCELLENT (Low Noise, Optimal)"
    fun calibQualityGood(isAr: Boolean) = if (isAr) "جيد (تداخل طفيف)" else "GOOD (Minor Noise Detected)"
    fun calibQualityPoor(isAr: Boolean) = if (isAr) "ضعيف (تداخل كهرومغناطيسي مرتفع)" else "POOR (High Interference)"

    // Calibration 9 Steps
    fun calibStep1(isAr: Boolean) = if (isAr) "١. الفحص الذاتي لسجلات المستشعر" else "1. Sensor Self-Test & Polling Rate"
    fun calibStep2(isAr: Boolean) = if (isAr) "٢. قياس المجال المغناطيسي المحيط" else "2. Measure Ambient Geomagnetic Field"
    fun calibStep3(isAr: Boolean) = if (isAr) "٣. جمع عينات حركية متعددة" else "3. Multi-Point Motion Sampling"
    fun calibStep4(isAr: Boolean) = if (isAr) "٤. حساب خط الأساس الهندسي (X, Y, Z)" else "4. Compute Geometric Baseline"
    fun calibStep5(isAr: Boolean) = if (isAr) "٥. تحليل مستوى الضوضاء والتباين" else "5. Analyze Noise Floor & Variance"
    fun calibStep6(isAr: Boolean) = if (isAr) "٦. حساب عتبة التنبيه الديناميكية" else "6. Calculate Dynamic Threshold"
    fun calibStep7(isAr: Boolean) = if (isAr) "٧. التعويض المستمر عن الانحراف البيئي" else "7. Continuous Environmental Compensation"
    fun calibStep8(isAr: Boolean) = if (isAr) "٨. ضبط الحساسية اليدوية" else "8. Sensitivity & Range Adjustment"
    fun calibStep9(isAr: Boolean) = if (isAr) "٩. تقييم جودة المعايرة النهائية" else "9. Final Quality Assessment"

    // Hardware & Devices Screen
    fun devicesScreenTitle(isAr: Boolean) = if (isAr) "مستشعرات الأجهزة وواجهات الاتصال" else "HARDWARE SENSORS & INTERFACES"
    fun devicesTitle(isAr: Boolean) = if (isAr) "مستشعرات الأجهزة الخارجية" else "External Hardware Sensors"
    fun phoneSensorTitle(isAr: Boolean) = if (isAr) "المستشعر المغناطيسي الداخلي للهاتف" else "Built-In Phone Magnetometer"
    fun bleSensorTitle(isAr: Boolean) = if (isAr) "ملف كاشف خارجي عبر بلوتوث BLE" else "External BLE Detector Coil"
    fun usbSensorTitle(isAr: Boolean) = if (isAr) "كاشف تسلسلي عبر منفذ USB OTG" else "USB OTG / Serial Detector"
    fun simSensorTitle(isAr: Boolean) = if (isAr) "وضع المحاكاة التجريبي للمطورين" else "Developer Simulation (TEST MODE)"
    fun activeBadge(isAr: Boolean) = if (isAr) "نشط" else "ACTIVE"
    fun selectBtn(isAr: Boolean) = if (isAr) "اختيار" else "Select"
    fun bleScanBtn(isAr: Boolean) = if (isAr) "البحث عن أجهزة BLE" else "Scan BLE Coils"
    fun bleStopScanBtn(isAr: Boolean) = if (isAr) "إيقاف البحث" else "Stop Scan"

    // History Screen
    fun historyScreenTitle(isAr: Boolean) = if (isAr) "سجل الكشوفات والإشارات" else "DETECTION LOGS"
    fun clearAllHistory(isAr: Boolean) = if (isAr) "مسح كافة السجلات" else "Clear All Logs"
    fun clearAllConfirm(isAr: Boolean) = if (isAr) "هل أنت متأكد من رغبتك في حذف جميع السجلات المسجلة نهائياً؟" else "Are you sure you want to permanently delete all recorded detection logs?"
    fun noLogsTitle(isAr: Boolean) = if (isAr) "لا توجد أحداث كشف مسجلة حتى الآن" else "No detection events logged yet"
    fun noLogsDesc(isAr: Boolean) = if (isAr) "اضغط على زر 'حفظ السجل' في شاشة الكشف أثناء أو بعد الفحص لتسجيل الإشارة." else "Press the 'LOG' button on the detection screen during or after a scan to record signals."
    fun viewMap(isAr: Boolean) = if (isAr) "عرض في الخريطة" else "View Map"

    // Settings Screen
    fun settingsScreenTitle(isAr: Boolean) = if (isAr) "أنماط الكشف وإعدادات الجهاز" else "DETECTION MODES & INSTRUMENT SETTINGS"
    fun languageTitle(isAr: Boolean) = if (isAr) "لغة التطبيق / App Language" else "App Language / لغة التطبيق"
    fun detectionModesSection(isAr: Boolean) = if (isAr) "أنماط التشغيل المعدة مسبقاً" else "OPERATING PRESET MODES"
    fun audioSynthSection(isAr: Boolean) = if (isAr) "مخلق التنبيهات الصوتية (Synthesizer)" else "AUDIO FEEDBACK SYNTHESIZER"
    fun toneSynthesisMode(isAr: Boolean) = if (isAr) "نمط تخليق النغمة" else "Tone Synthesis Mode"
    fun volume(isAr: Boolean) = if (isAr) "مستوى الصوت" else "Volume"
    fun muteThreshold(isAr: Boolean) = if (isAr) "عتبة الكتم (الحد الأدنى للإشارة %)" else "Mute Threshold (Min Signal %)"
    fun vibrationSection(isAr: Boolean) = if (isAr) "التنبيه بالاهتزاز واللمس (Haptic)" else "HAPTIC / VIBRATION FEEDBACK"
    fun vibrationPattern(isAr: Boolean) = if (isAr) "نمط الاهتزاز اللمسي" else "Haptic Vibration Pattern"
    fun expertSection(isAr: Boolean) = if (isAr) "معاملات فلتر الإشارة الرقمي (DSP)" else "EXPERT SIGNAL FILTER PARAMETERS"
    fun filterAlpha(isAr: Boolean) = if (isAr) "معامل تنعيم الفلتر (α)" else "IIR Filter Smoothing Alpha (α)"
    fun driftCompTitle(isAr: Boolean) = if (isAr) "التعويض المستمر عن الانجراف البيئي" else "Continuous Drift Compensation"
    fun driftCompDesc(isAr: Boolean) = if (isAr) "تتبع التغيرات البطيئة في المجال المغناطيسي الأرضي المحيط" else "Slowly tracks background geomagnetic shifts"

    // Mode helper titles & descriptions
    fun modeTitle(mode: DetectionMode, isAr: Boolean): String {
        return when (mode) {
            DetectionMode.QUICK_SCAN -> if (isAr) "مسح سريع" else "Quick Scan"
            DetectionMode.DEEP_ANALYSIS -> if (isAr) "تحليل عميق للإشارة" else "Deep Signal Analysis"
            DetectionMode.FERROUS_DETECTION -> if (isAr) "التركيز على المعادن الحديدية" else "Ferrous Metal Focus"
            DetectionMode.NON_FERROUS_DETECTION -> if (isAr) "تمييز المعادن غير الحديدية" else "Non-Ferrous Discrimination"
            DetectionMode.EXTERNAL_SENSOR_MODE -> if (isAr) "وضع الأجهزة الخارجية" else "External Hardware Mode"
            DetectionMode.EXPERT_MANUAL -> if (isAr) "الوضع الاحترافي / اليدوي" else "Expert / Manual Mode"
        }
    }

    fun modeDesc(mode: DetectionMode, isAr: Boolean): String {
        return when (mode) {
            DetectionMode.QUICK_SCAN -> if (isAr) "تصفية خفيفة لمسح سريع واستجابة فورية" else "Light filtering for rapid sweeping & immediate response"
            DetectionMode.DEEP_ANALYSIS -> if (isAr) "تصفية متطورة لتعزيز نسبة الإشارة وكشف الأهداف العميقة" else "Enhanced multi-sample SNR filtering for subtle anomalies"
            DetectionMode.FERROUS_DETECTION -> if (isAr) "مخصص للأجسام الفولاذية والحديدية والأنابيب والأسلاك" else "Optimized for iron, steel, pipes, and ferromagnetic targets"
            DetectionMode.NON_FERROUS_DETECTION -> if (isAr) "تمييز الطور والتوصيلية (يتطلب ملف كاشف خارجي)" else "Phase & conductivity discrimination (requires external hardware coil)"
            DetectionMode.EXTERNAL_SENSOR_MODE -> if (isAr) "استقبال مباشر للبيانات من جهاز كشف BLE أو USB" else "Full telemetry streaming from connected BLE or USB hardware"
            DetectionMode.EXPERT_MANUAL -> if (isAr) "تحكم يدوي كامل في الحساسية وفلتر الترددات وخط الأساس" else "Customizable sensitivity, threshold offset, filter, and ground balance"
        }
    }

    // Tone helper
    fun toneName(tone: ToneType, isAr: Boolean): String {
        return when (tone) {
            ToneType.VCO_CONTINUOUS -> if (isAr) "نغمة متغيرة VCO" else "VCO Continuous"
            ToneType.PULSED_CLICKER -> if (isAr) "نبضات جيجر" else "Pulsed Geiger"
            ToneType.MULTI_TONE -> if (isAr) "نغمات متعددة" else "Multi-Tone"
        }
    }

    // Vibration helper
    fun vibrationModeName(vMode: VibrationMode, isAr: Boolean): String {
        return when (vMode) {
            VibrationMode.PROPORTIONAL_PULSE -> if (isAr) "نبضات متناسبة مع قوة الإشارة" else "Proportional Pulse (Fast pulsing as signal rises)"
            VibrationMode.CONTINUOUS_INTENSITY -> if (isAr) "اهتزاز مستمر متغير الشدة" else "Continuous Variable Intensity"
            VibrationMode.SHORT_PULSE -> if (isAr) "نبضة واحدة عند تجاوز العتبة" else "Single Pulse on Threshold Trigger"
            VibrationMode.OFF -> if (isAr) "معطل" else "Off"
        }
    }
}
