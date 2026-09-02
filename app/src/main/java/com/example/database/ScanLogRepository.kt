package com.example.database

import kotlinx.coroutines.flow.Flow

class ScanLogRepository(
    private val scanLogDao: ScanLogDao
) {
    val allLogs: Flow<List<ScanLogEntity>> = scanLogDao.getAllLogs()

    suspend fun insertLog(log: ScanLogEntity): Long {
        return scanLogDao.insertLog(log)
    }

    suspend fun deleteLogById(id: Long) {
        scanLogDao.deleteLogById(id)
    }

    suspend fun clearAll() {
        scanLogDao.clearAllLogs()
    }
}
