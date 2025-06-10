package com.nexora.timelance.ui.model

import com.nexora.timelance.domain.model.entity.HistorySkill
import java.time.LocalDate

data class DailyStat(val day: LocalDate, val totalSeconds: Long, val totalMinutes: Long, val totalHours: Long) {

    companion object {

        fun createStat (date: LocalDate, histories: List<HistorySkill>): DailyStat {
            val trackedSeconds = histories.sumOf { it.timeTrackedSeconds }
            val trackedMinutes = (trackedSeconds % 3600) / 60
            val trackedHours = trackedSeconds / 3600
            return DailyStat(date, trackedSeconds, trackedMinutes, trackedHours)
        }

        fun calculateHeight(seconds: Long, maxSeconds: Long): Float {
            val maxColumnHeight = 200f
            return if (maxSeconds > 0) {
                (seconds.toFloat() / maxSeconds) * maxColumnHeight
            } else {
                0f
            }
        }
    }
}

