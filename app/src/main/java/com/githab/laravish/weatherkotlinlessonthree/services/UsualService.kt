package com.githab.laravish.weatherkotlinlessonthree.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import kotlinx.coroutines.*

class UsualService : Service(), CoroutineScope by MainScope() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        launch {
            delay(1000L)
            Toast.makeText(this@UsualService, "services started", Toast.LENGTH_LONG).show()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    companion object {
        fun start(context: Context) {
            context.startService(Intent(context, UsualService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, UsualService::class.java))
        }
    }
}