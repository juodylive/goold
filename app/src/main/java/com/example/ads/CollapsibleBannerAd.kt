package com.example.ads

import android.os.Bundle
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.AppStrings
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun CollapsibleBannerAd(
    appLanguage: String = "ar",
    modifier: Modifier = Modifier
) {
    if (!AdConfig.isAdsEnabled) return

    val isCollapsed by AdMobManager.isBannerCollapsed.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var adLoadedSuccessfully by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DetectorSurfaceDark)
            .border(1.dp, DetectorSurfaceBorder)
            .testTag("collapsible_banner_container")
    ) {
        // Collapsible Header / Toggle Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .clickable { AdMobManager.toggleBannerCollapsed() }
                .padding(horizontal = 10.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (adLoadedSuccessfully) AmberRadar else TextMuted)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = when (appLanguage) {
                        "ar" -> "إعلان ممول (Google AdMob)"
                        "de" -> "Gesponserte Anzeige (AdMob)"
                        "es" -> "Anuncio patrocinado (AdMob)"
                        "pt" -> "Anúncio patrocinado (AdMob)"
                        "fr" -> "Annonce sponsorisée (AdMob)"
                        "tr" -> "Sponsorlu Reklam (AdMob)"
                        else -> "Sponsored Ad (Google AdMob)"
                    },
                    fontSize = 10.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isCollapsed) {
                        when (appLanguage) {
                            "ar" -> "إظهار"
                            "de" -> "Einblenden"
                            "es" -> "Mostrar"
                            "pt" -> "Mostrar"
                            "fr" -> "Afficher"
                            "tr" -> "Göster"
                            else -> "Show"
                        }
                    } else {
                        when (appLanguage) {
                            "ar" -> "طي الإعلان"
                            "de" -> "Einklappen"
                            "es" -> "Plegar"
                            "pt" -> "Recolher"
                            "fr" -> "Réduire"
                            "tr" -> "Daralt"
                            else -> "Collapse"
                        }
                    },
                    fontSize = 9.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = if (isCollapsed) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Ad",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Animated Expandable Ad View Body
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(DetectorDarkBg),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admob_banner_view"),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = AdConfig.bannerAdUnitId

                            // Request official AdMob Collapsible Banner parameter
                            val extras = Bundle().apply {
                                putString("collapsible", "bottom")
                            }
                            val adRequest = AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                                .build()

                            adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    super.onAdLoaded()
                                    adLoadedSuccessfully = true
                                    Log.d("AdMobBanner", "Banner ad loaded successfully")
                                }

                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    super.onAdFailedToLoad(error)
                                    adLoadedSuccessfully = false
                                    Log.w("AdMobBanner", "Banner ad failed: ${error.message}")
                                }
                            }

                            loadAd(adRequest)
                        }
                    },
                    update = { adView ->
                        // adView is updated when needed
                    }
                )

                // Placeholder / Fallback indicator when ad is loading or in offline mode
                if (!adLoadedSuccessfully) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (appLanguage) {
                                "ar" -> "مساحة إعلانية نشطة (Google AdMob Test Unit جاهز للربط)"
                                "de" -> "Aktiver Werbeplatz (Google AdMob Test-Unit bereit)"
                                "es" -> "Espacio publicitario activo (Unidad de prueba AdMob lista)"
                                "pt" -> "Espaço publicitário ativo (Unidade de teste AdMob pronta)"
                                "fr" -> "Espace publicitaire actif (Unité de test AdMob prête)"
                                "tr" -> "Aktif reklam alanı (Google AdMob Test Birimi hazır)"
                                else -> "Active Ad Space (Google AdMob Test Unit Ready)"
                            },
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
