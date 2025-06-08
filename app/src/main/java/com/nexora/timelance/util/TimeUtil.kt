package com.nexora.timelance.util

import java.util.Locale
import java.util.StringJoiner

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
            val sj = StringJoiner(" ")
            if (hours > 0) sj.add(String.format(Locale.getDefault(),"%d h", hours))
            if (minutes > 0) sj.add(String.format(Locale.getDefault(),"%d m", minutes))
            if (remainingSeconds > 0) sj.add(String.format(Locale.getDefault(),"%d s", remainingSeconds))
            return sj.toString()
        }
    }
}