package com.nexora.timelance.domain.repository

import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.relation.SkillWithTags

interface SkillRepository {

    fun saveSkill(skill: Skill): Skill
    fun updateSkill(skill: Skill): Skill
    fun deleteSkill(skill: Skill)
    fun getSkillById(id: String): Skill
    fun getAll(): List<Skill>

    fun getSkillWithTags(skillId: String): SkillWithTags?
    fun getAllSkillsWithTags(): List<SkillWithTags>

    fun addTagToSkill(skillId: String, tagId: String)
    fun removeTagFromSkill(skillId: String, tagId: String)
    fun setTagsForSkill(skillId: String, tagIds: List<String>)
}