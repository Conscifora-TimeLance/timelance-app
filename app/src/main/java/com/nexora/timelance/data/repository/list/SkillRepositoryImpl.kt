package com.nexora.timelance.data.repository.list

import androidx.compose.ui.util.fastSumBy
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.entity.TagSkillGroup
import com.nexora.timelance.domain.model.relation.SkillWithTags
import com.nexora.timelance.domain.repository.HistorySkillRepository
import com.nexora.timelance.domain.repository.SkillRepository
import com.nexora.timelance.domain.repository.TagRepository

class SkillRepositoryImpl(
    private val tagsRepository: TagRepository,
    private val historySkillRepository: HistorySkillRepository
): SkillRepository {

    val skills = mutableListOf<Skill>()
    val skillsWithTags = mutableListOf<TagSkillGroup>()

    override fun saveSkill(skill: Skill): Skill {
        skills.add(skill)
        return skill
    }

    override fun updateSkill(skill: Skill): Skill {
        val index = skills.indexOf(skills.first { it.id == skill.id })
        return skills.set(index, skill)
    }

    override fun deleteSkill(skill: Skill) {
        skills.remove(skill)
    }

    override fun getSkillById(id: String): Skill {
        return skills.first { it.id == id }
    }

    override fun getAll(): List<Skill> {
        return skills
    }

    override fun getSkillWithTags(skillId: String): SkillWithTags? {
        val skill = skills.first { it.id == skillId }
        val tags = skillsWithTags.filter { it.idSkill == skillId }.map { tagsRepository.getTagById(it.idTag) }.toList()
        return SkillWithTags(skill, tags)
    }

    override fun getAllSkillsWithTags(): List<SkillWithTags> {
        TODO("Not yet implemented")
    }

    override fun updateTotalSeconds() {
        skills.forEach { it ->
            val sumOfHistory = historySkillRepository.getHistoryBySkillId(it.id).sumOf { it.timeTackedSeconds }
            updateSkill(it.copy(timeTotalSeconds = sumOfHistory))
        }
    }

    override fun addTagToSkill(skillId: String, tagId: String) {
        TODO("Not yet implemented")
    }

    override fun removeTagFromSkill(skillId: String, tagId: String) {
        TODO("Not yet implemented")
    }

    override fun setTagsForSkill(skillId: String, tagIds: List<String>) {
        TODO("Not yet implemented")
    }
}