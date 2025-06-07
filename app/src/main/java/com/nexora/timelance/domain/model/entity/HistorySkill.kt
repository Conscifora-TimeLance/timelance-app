package com.nexora.timelance.domain.model.entity

import java.time.LocalDate
import java.util.UUID

data class HistorySkill(
    val id: String = UUID.randomUUID().toString(),
    val skillId: String,
    val timeTackedSeconds: Long,
    val date: LocalDate
)
