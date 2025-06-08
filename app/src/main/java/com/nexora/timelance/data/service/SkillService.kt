package com.nexora.timelance.data.service

import com.nexora.timelance.data.dto.GroupHistorySkill
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.entity.Tag

interface SkillService {

    fun createSkill(skillDto: SkillDto): Skill
    fun addTrackHistoryBySkillId(history: HistorySkill)
    fun getSkillBySkillId(skillId: String): SkillDto
    fun getHistoryBySkillId(skillId: String): GroupHistorySkill
    fun getAllSkills(): List<SkillDto>
    fun getTagsBySkillId(skillId: String): List<Tag>
}