package com.github.kovah101.chargemycar

import android.app.Application
import timber.log.Timber

class ChargeMyCarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Timber to allow easy logging
        Timber.plant(Timber.DebugTree())
    }
}