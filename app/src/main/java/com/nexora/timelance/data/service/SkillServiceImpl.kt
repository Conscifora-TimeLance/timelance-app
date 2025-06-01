package com.nexora.timelance.data.service

import com.nexora.timelance.data.dto.GroupHistorySkill
import com.nexora.timelance.data.repository.list.HistorySkillRepositoryImpl
import com.nexora.timelance.data.repository.list.SkillRepositoryImpl
import com.nexora.timelance.data.repository.list.TagRepositoryImpl
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.service.SkillService

class SkillServiceImpl(): SkillService {

    private val tagRepository = TagRepositoryImpl()
    private val historySkillRepository = HistorySkillRepositoryImpl()
    private val skillRepository = SkillRepositoryImpl(tagRepository, historySkillRepository)

    override fun createSkill(skill: Skill) {
        skillRepository.saveSkill(skill)
    }

    override fun addTrackHistoryBySkillId(history: HistorySkill) {
        historySkillRepository.saveHistory(history)
        skillRepository.updateTotalSeconds()
    }

    override fun getSkillBySkillId(skillId: String): Skill {
        return skillRepository.getSkillById(skillId)
    }

    override fun getHistoryBySkillId(skillId: String): GroupHistorySkill {
        val skill = skillRepository.getSkillById(skillId)
        val histories = historySkillRepository.getHistoryBySkillId(skillId)
        return GroupHistorySkill(skill, histories)
    }

    override fun getAllSkills(): List<Skill> {
        return skillRepository.getAll()
    }
}