package com.acous.airqualityindex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AQIApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}