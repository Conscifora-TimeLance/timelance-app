package com.nexora.timelance.domain.service

import com.nexora.timelance.data.dto.GroupHistorySkill
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill

interface SkillService {

    fun createSkill(skill: Skill)
    fun addTrackHistoryBySkillId(history: HistorySkill)
    fun getSkillBySkillId(skillId: String): Skill
    fun getHistoryBySkillId(skillId: String): GroupHistorySkill
    fun getAllSkills(): List<Skill>
}