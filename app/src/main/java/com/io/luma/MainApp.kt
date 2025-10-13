package com.io.luma

import android.app.Application
import com.io.luma.utils.TokenManager

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}