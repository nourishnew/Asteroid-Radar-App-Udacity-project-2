package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import androidx.work.WorkManager
import com.udacity.asteroidradar.Work.AsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class WorkManager : Application() {

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */

    private val applicationScope= CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }


    private fun delayedInit(){
        applicationScope.launch {
            setupRecurringWork()
        }
    }
}

private fun setupRecurringWork(){
    val constraint= Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

    val repeatingRequest= PeriodicWorkRequestBuilder<AsteroidWork>(
            1,
            TimeUnit.DAYS
    ).setConstraints(constraint).build()

    WorkManager.getInstance().enqueueUniquePeriodicWork(
            AsteroidWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
    )
}
