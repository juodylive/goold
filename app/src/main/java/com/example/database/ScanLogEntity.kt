package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_logs")
data class ScanLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val peakStrengthUt: Float,
    val deltaUt: Float,
    val sensorType: String,
    val targetClassification: String,
    val estimatedMaterial: String,
    val durationSeconds: Int = 5,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val altitude: Double? = null,
    val userNotes: String = "",
    val modeName: String = "Quick Scan"
)
