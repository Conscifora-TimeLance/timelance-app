package com.nexora.timelance.data.repository.list

import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.repository.HistorySkillRepository
import com.nexora.timelance.domain.repository.SkillRepository

class HistorySkillRepositoryImpl(): HistorySkillRepository {

    private val histories = mutableListOf<HistorySkill>()

    override fun saveHistory(history: HistorySkill): HistorySkill {
        histories.add(history)
        return history
    }

    override fun updateHistory(history: HistorySkill): HistorySkill {
        val index = histories.indexOf(history)
        return histories.set(index, history)
    }

    override fun deleteHistory(history: HistorySkill) {
        histories.remove(history)
    }

    override fun getHistoryById(id: String): HistorySkill {
        return histories.first { it.id == id }
    }

    override fun getAll(): List<HistorySkill> {
        return histories
    }

    override fun getHistoryBySkillId(skillId: String): List<HistorySkill> {
        return histories.filter { it.skillId == skillId }
    }
}