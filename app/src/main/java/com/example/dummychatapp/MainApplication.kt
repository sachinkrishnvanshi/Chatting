package com.example.dummychatapp

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

class MainApplication : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}