package com.nexora.timelance.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.compose.runtime.mutableLongStateOf
import androidx.core.app.NotificationCompat
import com.nexora.timelance.R
import com.nexora.timelance.corotutine.TimeTracker
import com.nexora.timelance.util.TimeUtil.Companion.secondsToTime

class TimerForegroundService : Service() {
    private var currentSkillId: String? = null
    private var currentSkillName: String = ""
    val time = mutableLongStateOf(0)
    private val tracker = TimeTracker()
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (currentSkillId == null) return

            updateNotification(time.longValue)
            sendTimeUpdateBroadcast()

            if (currentSkillId != null) {
                handler.postDelayed(this, 1000)
            }
        }
    }

    companion object {
        const val ACTION_STOP_TIMER = "ACTION_STOP_TIMER"
        const val ACTION_START_TIMER = "ACTION_START_TIMER"
        const val ACTION_UPDATE_TIME = "ACTION_UPDATE_TIME"
        const val EXTRA_TIME_VALUE = "extra_time_value"
        const val EXTRA_SKILL_ID = "extra_skill_id"
        const val EXTRA_SKILL_NAME = "extra_skill_name"
        const val NOTIFICATION_CHANNEL_ID = "TimerChannel"
        const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_TIMER -> {
                val skillId = intent.getStringExtra(EXTRA_SKILL_ID)
                if (skillId == currentSkillId) {
                    stopTimer()
                }
            }
            ACTION_START_TIMER -> {
                val skillId = intent.getStringExtra(EXTRA_SKILL_ID)
                val skillName = intent.getStringExtra(EXTRA_SKILL_NAME) ?: ""
                startTimer(skillId, skillName)
            }
        }
        return START_STICKY
    }

    private fun startTimer(skillId: String?, skillName: String) {
        if (skillId == null) return
        if (skillId == currentSkillId) return // Already tracking this skill

        // Stop any existing timer
        if (currentSkillId != null) {
            stopTimer()
        }

        currentSkillId = skillId
        currentSkillName = skillName
        time.longValue = 0
        tracker.start(time)
        createNotificationChannel()
        handler.post(updateRunnable)
        startForeground(NOTIFICATION_ID, createNotification(time.longValue))
    }

    private fun stopTimer() {
        if (currentSkillId == null) return

        tracker.stop()
        handler.removeCallbacks(updateRunnable)
        currentSkillId = null
        currentSkillName = ""
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun sendTimeUpdateBroadcast() {
        val intent = Intent(ACTION_UPDATE_TIME).apply {
            putExtra(EXTRA_TIME_VALUE, time.longValue)
            putExtra(EXTRA_SKILL_ID, currentSkillId)
        }
        sendBroadcast(intent)
    }

    private fun createNotification(elapsedTime: Long): Notification {
        val stopIntent = Intent(this, TimerForegroundService::class.java).apply {
            action = ACTION_STOP_TIMER
            putExtra(EXTRA_SKILL_ID, currentSkillId)
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tracking: $currentSkillName")
            .setContentText(secondsToTime(elapsedTime))
            .setSmallIcon(R.drawable.play)
            .addAction(R.drawable.pause, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
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
        ).apply {
            description = "Shows timer progress"
            setShowBadge(false)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}