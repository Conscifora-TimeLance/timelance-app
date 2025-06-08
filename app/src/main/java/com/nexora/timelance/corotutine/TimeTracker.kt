package com.nexora.timelance.corotutine

import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nexora.timelance.ui.model.SkillTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class TimeTracker {

    var isRunning by mutableStateOf(false)
    var job : Job? = null
    val scope : CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun start(time: MutableLongState) {
        if (job?.isActive == true) {
            return
        }

        isRunning = true
        val skillTrack = SkillTrack(UUID.randomUUID().toString(), 0)
        skillTrack.secondsCurrent = System.currentTimeMillis()

        job = scope.launch {
            try {
                while (isActive) {
                    time.longValue = (System.currentTimeMillis() - skillTrack.secondsCurrent) / 1000
                    delay(1000L)
                }
            } finally {
                isRunning = false
            }
        }
    }

    fun stop (){
        job?.cancel()
        isRunning = false
    }
}