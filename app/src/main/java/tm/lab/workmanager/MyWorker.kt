package tm.lab.workmanager

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep


class MyWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    companion object {
        const val TAG = "MY_WORKER"
        fun run(ctx: Context): LiveData<WorkInfo> {
            val work = OneTimeWorkRequestBuilder<MyWorker>()
                .build()
            val manager = WorkManager.getInstance(ctx)
            manager.enqueue(work)
            return manager.getWorkInfoByIdLiveData(work.id)
        }
    }

    override suspend fun doWork(): Result {
        var a = 0
        for (x in 0..10) {
            a = x
            Log.i(TAG, "wynik = $a")
            val progressData = workDataOf(TAG to a)
            setProgress(progressData)
            try {
                delay(500)
            } catch (e: CancellationException) {
                Log.e(TAG, "FAILURE + ${e.message}")
            }
        }
        val b = a
        return Result.success(workDataOf(TAG to b))
    }
}
