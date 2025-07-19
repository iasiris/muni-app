package com.iasiris.muniapp.workmanager

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit


class WorkManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val workManager = WorkManager.getInstance(context)

    inline fun <reified T : CoroutineWorker> schedulePeriodicTask(
        uniqueWorkName: String,
        repeatIntervalMillis: Long = 60,
        timeUnits: TimeUnit = TimeUnit.MINUTES,
        networkRequired: Boolean = true
    ) {
        val constraints = Constraints.Builder().apply {
            if (networkRequired) {
                setRequiredNetworkType(NetworkType.CONNECTED)
            }
        }.build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<T>(
            repeatIntervalMillis, timeUnits
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    inline fun <reified T : CoroutineWorker> scheduleOneTimeTask(
        uniqueWorkName: String? = null,
        networkRequired: Boolean = true
    ) {
        val constraints = Constraints.Builder().apply {
            if (networkRequired) {
                setRequiredNetworkType(NetworkType.CONNECTED)
            }
        }.build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<T>()
            .setConstraints(constraints)
            .build()

        if (uniqueWorkName != null) {
            workManager.enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.REPLACE,
                oneTimeWorkRequest
            )
        } else {
            workManager.enqueue(oneTimeWorkRequest)
        }
    }

    fun cancelWork(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }

}