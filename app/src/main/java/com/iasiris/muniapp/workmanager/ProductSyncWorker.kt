package com.iasiris.muniapp.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iasiris.muniapp.domain.usecase.product.GetProductsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("ProductSyncWorker", " ========= Starting product sync... ===========")
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                ProductSyncWorkerEntryPoint::class.java
            )
            val getProductsUseCase = entryPoint.getProductsUseCase()
            Log.d("ProductSyncWorker", " Fetching products...")
            getProductsUseCase.invoke(true)
            Log.d("ProductSyncWorker", " Product sync completed successfully.")
            Result.success()
        } catch (e: Exception) {
            Log.e("ProductSyncWorker", "Error during product sync: ${e.message}", e)
            Result.retry()
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProductSyncWorkerEntryPoint {
    fun getProductsUseCase(): GetProductsUseCase
}