package com.iasiris.muniapp.workmanager

import jakarta.inject.Inject

class ProductSyncManager @Inject constructor(
    private val workManagerHelper: WorkManagerHelper
) {
    fun schedulePeriodicSync() {
        workManagerHelper.schedulePeriodicTask<ProductSyncWorker>(
            uniqueWorkName = "PRODUCT_SYNC_WORK"
        )
    }

    fun syncNow() {
        workManagerHelper.scheduleOneTimeTask<ProductSyncWorker>(
            uniqueWorkName = "PRODUCT_SYNC_NOW"
        )
    }
}