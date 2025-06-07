package com.nexora.timelance.domain.repository

import com.nexora.timelance.domain.model.entity.TagSkillGroup

interface SkillTagGroupRepository {

    fun addTagToSkill(skillId: String, tagId: String)
    fun getGroupsBySkillId(skillId: String): List<TagSkillGroup>
}