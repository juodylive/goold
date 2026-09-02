package com.example.ads

/**
 * AdMob Configuration for MetalScan Pro.
 * Pre-configured with official Google Mobile Ads Test Unit IDs for development/testing.
 * Replace with your production AdMob App ID and Ad Unit IDs before publishing on Google Play.
 */
object AdConfig {
    // Official Google AdMob Test Ad Unit IDs
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-5660393455301038/8262999328"
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5660393455301038/4692736623"
    
    // Set to your live production Ad Unit IDs when publishing
    var bannerAdUnitId: String = TEST_BANNER_AD_UNIT_ID
    var interstitialAdUnitId: String = TEST_INTERSTITIAL_AD_UNIT_ID

    // Anti-Annoyance / Frequency Capping Configuration
    const val MIN_NAVIGATIONS_BETWEEN_INTERSTITIALS = 4 // Number of screen switches before attempting ad
    const val MIN_TIME_BETWEEN_INTERSTITIALS_MS = 90_000L // 90 seconds cooldown between interstitial ads
    
    var isAdsEnabled: Boolean = true
    var isBannerCollapsible: Boolean = true
}
