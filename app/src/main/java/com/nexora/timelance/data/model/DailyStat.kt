package com.nexora.timelance.data.model

data class DailyStat(val day: String, val hours: Int, val minutes: Int) {
    companion object {
        fun calculateHeight(hours: Int, minutes: Int, maxHours: Int, maxMinutes: Int): Int {
            val totalMinutes = hours * 60 + minutes
            val maxTotalMinutes = maxHours * 60 + maxMinutes
            val maxColumnHeight = 250
            return (totalMinutes.toFloat() / maxTotalMinutes * maxColumnHeight).toInt()
        }
    }
}

