package com.nexora.timelance.util

import java.util.Locale

class TimeUtil {

    companion object {
        fun secondsToTime(seconds: Long): String {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            return String.format(Locale.getDefault(),
                "%02d:%02d:%02d", hours, minutes, remainingSeconds)
        }

        fun secondsToTrackedTime(seconds: Long): String {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            return String.format(Locale.getDefault(),
                "%d h %d m %d s", hours, minutes, remainingSeconds)
        }
    }
}