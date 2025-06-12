package com.nexora.timelance.data.service

import com.nexora.timelance.data.dto.SkillHistoryDto
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.entity.Tag

interface SkillService {

    fun createSkill(skillDto: SkillDto): Skill
    fun addTrackHistoryBySkillId(history: HistorySkill)
    fun getSkillById(skillId: String): SkillDto
    fun getHistoryBySkillId(skillId: String): SkillHistoryDto
    fun getAllSkills(): List<SkillDto>
    fun getTagsBySkillId(skillId: String): List<Tag>
    fun getAllHistory(): List<SkillHistoryDto>
    fun getSkillsByTags(selectedTags: List<Tag>): List<SkillDto>
}