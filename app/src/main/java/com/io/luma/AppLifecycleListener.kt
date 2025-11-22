package com.io.luma

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifecycleListener : DefaultLifecycleObserver {

    var isForeground: Boolean = false
        private set

    override fun onStart(owner: LifecycleOwner) {
        isForeground = true
        Log.d("AppState", "App in FOREGROUND")
    }

    override fun onStop(owner: LifecycleOwner) {
        isForeground = false
        Log.d("AppState", "App in BACKGROUND")
    }
}
