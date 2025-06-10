package com.nexora.timelance.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.core.app.NotificationCompat
import com.nexora.timelance.R
import com.nexora.timelance.corotutine.TimeTracker
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTime

class TimerForegroundService : Service() {

    private var isRunning = false
    val time = mutableLongStateOf(0)
    private lateinit var timerThread: Thread
    private val tracker = TimeTracker()

    companion object {
        const val ACTION_STOP_TIMER = "com.nexora.timelance.data.serviceACTION_STOP_TIMER"
        const val NOTIFICATION_CHANNEL_ID = "TimerChannel"
        const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_TIMER -> stopTimer()
            else -> startTimer()
        }
        return START_NOT_STICKY
    }

    private fun startTimer() {
        createNotificationChannel()
        tracker.start(time)
        timerThread = Thread {
            while (tracker.isRunning) {
                Thread.sleep(1000)
                updateNotification(time.longValue)
            }
        }
        timerThread.start()

        startForeground(NOTIFICATION_ID, createNotification(time.longValue))
    }

    private fun stopTimer() {
        tracker.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotification(elapsedTime: Long): Notification {
        val stopIntent = Intent(this, TimerForegroundService::class.java).apply {
            action = ACTION_STOP_TIMER
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText(secondsToTime(elapsedTime))
            .setSmallIcon(R.drawable.play)
            .addAction(R.drawable.pause, "Stop", stopPendingIntent)
            .build()
    }

    private fun updateNotification(elapsedTime: Long) {
        val notification = createNotification(elapsedTime)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Timer Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}