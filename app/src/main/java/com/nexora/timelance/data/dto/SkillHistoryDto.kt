package com.nexora.timelance.data.dto

import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill

data class SkillHistoryDto(
    val skill: Skill,
    val histories: List<HistorySkill>
)
