package com.example.ipcservice.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.example.ipcservice.IMusicService
import com.example.ipcservice.R
import com.example.ipcservice.screen.MainActivity

class MusicService : Service() {
    private val mBinder: IMusicService.Stub = object : IMusicService.Stub() {

        override fun getSongName(): String? = mPlayerManager?.songName

        override fun changeMediaStatus() {
            mPlayerManager?.changeMediaStatus()
        }

        override fun playSong() {
            mPlayerManager?.playSong()
        }

        override fun play() {
            mPlayerManager?.play()
        }

        override fun pause() {
            mPlayerManager?.pause()
        }

        override fun getCurrentDuration(): Int {
            return mPlayerManager?.currentDuration!!
        }

        override fun getTotalDuration(): Int {
            return mPlayerManager?.totalDuration!!
        }

    }
    private var mPlayerManager: MediaPlayerManager? = null

    override fun onCreate() {
        super.onCreate()
        mPlayerManager = MediaPlayerManager(applicationContext)
        showNotification()
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showNotification() {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("IPC Testing")
            .setContentText("IPC Testing")
            .setAutoCancel(true)
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }
}
