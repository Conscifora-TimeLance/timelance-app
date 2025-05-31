package com.nexora.timelance.data.dto

import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill

data class GroupHistorySkill(
    val skill: Skill,
    val histories: List<HistorySkill>
)
