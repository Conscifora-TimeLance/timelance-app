package com.nexora.timelance.corotutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Timer {

    var job:Job? = null
    val scope = CoroutineScope(Dispatchers.Default)

    fun start (skillName:String, durationSeconds: Long) {
        job?.cancel()

        job = scope.launch {
            println("Starting timer skill")

            for (i in durationSeconds downTo 1) {
                delay(1000L)
                println("Time left: $i seconds")
            }

            println("Skill '$skillName' tracked for $durationSeconds seconds")
        }

    }

    fun stop () {
        job?.cancel()
        println("Timer stopped")
    }

}