package com.nexora.timelance

import com.nexora.timelance.corotutine.TimeTracker
import com.nexora.timelance.corotutine.Timer
import com.nexora.timelance.data.model.SkillTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class MainTester {
}

suspend fun main () {

    var timeSeconds:Long = 10;
    var addTime:Long = 3;

    val timer = Timer()
    val tracker = TimeTracker()

    timer.start("Java", timeSeconds)
    tracker.start(SkillTrack(UUID.randomUUID(),"Kotlin",0))

    withContext(Dispatchers.IO) {
        Thread.sleep(timeSeconds * 1000 + addTime * 1000)
    }
    timer.stop()



}