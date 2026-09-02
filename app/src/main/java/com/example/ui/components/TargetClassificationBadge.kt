package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.TargetClassification
import com.example.core.TargetClassificationType
import com.example.ui.theme.AluminumEstimatedColor
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CopperEstimatedColor
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.FerrousIronColor
import com.example.ui.theme.GoldEstimatedColor
import com.example.ui.theme.NonFerrousGeneralColor
import com.example.ui.theme.SilverEstimatedColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TargetClassificationBadge(
    classification: TargetClassification,
    modifier: Modifier = Modifier
) {
    val badgeColor = when (classification.type) {
        TargetClassificationType.PHONE_NO_ANOMALY -> TextSecondary
        TargetClassificationType.PHONE_WEAK_MAGNETIC_ANOMALY -> CyanGlow
        TargetClassificationType.PHONE_STRONG_MAGNETIC_OBJECT -> CrimsonAlert
        TargetClassificationType.PHONE_FERROUS_MAGNETIC -> FerrousIronColor
        TargetClassificationType.PHONE_UNKNOWN_METALLIC_ANOMALY -> AmberRadar
        TargetClassificationType.EXTERNAL_FERROUS -> FerrousIronColor
        TargetClassificationType.EXTERNAL_NON_FERROUS_GENERAL -> NonFerrousGeneralColor
        TargetClassificationType.EXTERNAL_GOLD_LIKE_ESTIMATED -> GoldEstimatedColor
        TargetClassificationType.EXTERNAL_SILVER_LIKE_ESTIMATED -> SilverEstimatedColor
        TargetClassificationType.EXTERNAL_COPPER_LIKE_ESTIMATED -> CopperEstimatedColor
        TargetClassificationType.EXTERNAL_ALUMINUM_LIKE_ESTIMATED -> AluminumEstimatedColor
        TargetClassificationType.EXTERNAL_UNKNOWN -> AmberRadar
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DetectorSurfaceCard, RoundedCornerShape(12.dp))
            .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("target_classification_badge")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(badgeColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TARGET CLASSIFICATION",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
            }

            if (classification.isEstimatedOnly) {
                Text(
                    text = "ESTIMATED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AmberRadar,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(AmberRadar.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = classification.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = classification.subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        if (classification.isExternalSensorRequired) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(DetectorDarkBg, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = null,
                    tint = CyanGlow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Connect external hardware sensor coil for full VDI discrimination",
                    fontSize = 11.sp,
                    color = CyanGlow
                )
            }
        }
    }
}
