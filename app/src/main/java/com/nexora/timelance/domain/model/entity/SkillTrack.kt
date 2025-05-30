package com.nexora.timelance.domain.model.entity

import java.util.UUID

data class SkillTrack(
    var idTask:UUID,
    var nameTrack:String,
    var secondsCurrent:Long
)
