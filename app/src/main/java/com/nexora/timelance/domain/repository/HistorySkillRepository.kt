package com.nexora.timelance.domain.repository

import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.relation.SkillWithTags

interface HistorySkillRepository {

    fun saveHistory(history: HistorySkill): HistorySkill
    fun updateHistory(history: HistorySkill): HistorySkill
    fun deleteHistory(history: HistorySkill)
    fun getHistoryById(id: String): HistorySkill
    fun getAll(): List<HistorySkill>
    fun getHistoryBySkillId(skillId: String): List<HistorySkill>

}