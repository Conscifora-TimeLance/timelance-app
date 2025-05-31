package com.nexora.timelance.corotutine

import com.nexora.timelance.ui.model.SkillTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimeTracker {

    var job : Job? = null
    val scope : CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun start (skillTrack: SkillTrack) {
        if (job?.isActive == true) {
            println("Timer is already running for ${skillTrack.nameTrack}!")
            return
        }

        println("Starting timer for skill: ${skillTrack.nameTrack}")
        skillTrack.secondsCurrent = System.currentTimeMillis()

        job = scope.launch {
            while (isActive) {
                val elapsedTime = (System.currentTimeMillis() - skillTrack.secondsCurrent) / 1000 // Секунды
                println("Skill '${skillTrack.nameTrack}': Time elapsed: ${elapsedTime}s")
                delay(1000L)
            }
        }

    }

    fun stop (){
        if (job?.isActive != true) {
            println("No timer is running!")
            return
        }

        job?.cancel()
        val totalTime = (System.currentTimeMillis()) / 1000
        println("Timer stopped. Total time: ${totalTime}s")
    }

}