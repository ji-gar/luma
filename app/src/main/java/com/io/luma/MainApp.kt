package com.io.luma

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.io.luma.alarmsystem.createAlarmChannel
import com.io.luma.utils.TokenManager

class MainApp : Application() {
    companion object {
        lateinit var appState: AppLifecycleListener
    }

    override fun onCreate() {
        super.onCreate()

        appState = AppLifecycleListener()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appState)
        TokenManager.init(this)
        createAlarmChannel(this)
    }
}