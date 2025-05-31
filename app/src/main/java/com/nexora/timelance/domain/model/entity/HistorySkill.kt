package com.nexora.timelance.domain.model.entity

import java.time.LocalDate

data class HistorySkill(
    val id: String,
    val skillId: String,
    val timeTackedSeconds: Long,
    val date: LocalDate
)
