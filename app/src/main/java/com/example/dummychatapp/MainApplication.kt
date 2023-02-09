package com.example.dummychatapp

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException

class MainApplication:Application(),LifecycleObserver {
    companion object{
        lateinit var instance:MainApplication
        lateinit var mSocket: Socket
    }
    private var url:String ="https://107.21.98.110:7888"
    override fun onCreate() {
        super.onCreate()
        instance=this

        try {
            mSocket = IO.socket(url)
            mSocket=mSocket.connect()
//            if(mSocket.connected()) {
//                Log.d("Server", "Connected")
//            }
        } catch (e: URISyntaxException) {
            throw  RuntimeException(e);
        }

    }
}