package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AdMobManager {
    private const val TAG = "AdMobManager"

    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private var lastInterstitialShowTime = 0L
    private var screenTransitionCounter = 0

    private val _isAdLoadedState = MutableStateFlow(false)
    val isAdLoadedState: StateFlow<Boolean> = _isAdLoadedState.asStateFlow()

    private val _isBannerCollapsed = MutableStateFlow(false)
    val isBannerCollapsed: StateFlow<Boolean> = _isBannerCollapsed.asStateFlow()

    /**
     * Initialize Google Mobile Ads SDK.
     */
    fun initialize(context: Context) {
        try {
            MobileAds.initialize(context) { initializationStatus ->
                Log.d(TAG, "AdMob initialized successfully: ${initializationStatus.adapterStatusMap}")
                loadInterstitialAd(context.applicationContext)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize AdMob: ${e.message}", e)
        }
    }

    /**
     * Preload Interstitial Ad.
     */
    fun loadInterstitialAd(context: Context) {
        if (!AdConfig.isAdsEnabled || isAdLoading || interstitialAd != null) {
            return
        }

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            AdConfig.interstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isAdLoading = false
                    _isAdLoadedState.value = true
                    Log.d(TAG, "Interstitial Ad loaded successfully")

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d(TAG, "Interstitial Ad dismissed")
                            interstitialAd = null
                            _isAdLoadedState.value = false
                            // Preload the next ad for future transitions
                            loadInterstitialAd(context)
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.e(TAG, "Interstitial Ad failed to show: ${adError.message}")
                            interstitialAd = null
                            _isAdLoadedState.value = false
                            loadInterstitialAd(context)
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log.d(TAG, "Interstitial Ad showed full screen")
                            lastInterstitialShowTime = System.currentTimeMillis()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w(TAG, "Interstitial Ad failed to load: ${loadAdError.message}")
                    interstitialAd = null
                    isAdLoading = false
                    _isAdLoadedState.value = false
                }
            }
        )
    }

    /**
     * Triggered on user screen transitions (tab switches).
     * Implements intelligent frequency capping to prevent annoying the user:
     * 1. Checks if minimum navigations have occurred (e.g., 4 screen switches).
     * 2. Checks if minimum cooldown time has passed (e.g., 90s).
     * 3. Never shows an ad during active metal detection.
     */
    fun onScreenTransition(activity: Activity, isDetectingActive: Boolean): Boolean {
        if (!AdConfig.isAdsEnabled || isDetectingActive) {
            return false
        }

        screenTransitionCounter++
        val now = System.currentTimeMillis()
        val timeSinceLastAd = now - lastInterstitialShowTime

        if (screenTransitionCounter >= AdConfig.MIN_NAVIGATIONS_BETWEEN_INTERSTITIALS &&
            timeSinceLastAd >= AdConfig.MIN_TIME_BETWEEN_INTERSTITIALS_MS &&
            interstitialAd != null
        ) {
            try {
                interstitialAd?.show(activity)
                screenTransitionCounter = 0
                return true
            } catch (e: Exception) {
                Log.e(TAG, "Exception showing interstitial ad: ${e.message}", e)
            }
        }
        return false
    }

    fun toggleBannerCollapsed() {
        _isBannerCollapsed.value = !_isBannerCollapsed.value
    }

    fun setBannerCollapsed(collapsed: Boolean) {
        _isBannerCollapsed.value = collapsed
    }
}
