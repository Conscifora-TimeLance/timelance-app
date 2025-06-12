package com.nexora.timelance.data.repository.list

import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.domain.model.entity.TagSkillGroup
import com.nexora.timelance.domain.repository.SkillTagGroupRepository

class TagSkillGroupRepositoryList(

): SkillTagGroupRepository {

    private val skillTagGroupList = mutableListOf<TagSkillGroup>()

    override fun addTagToSkill(skillId: String, tagId: String) {
        skillTagGroupList.add(TagSkillGroup(skillId, tagId))
    }

    override fun getGroupsBySkillId(skillId: String): List<TagSkillGroup> {
        return skillTagGroupList.filter { it.skillId == skillId }
    }

    override fun getGroupsByTags(selectedTags: List<Tag>): List<TagSkillGroup> {
        return skillTagGroupList.filter { group -> selectedTags.any() { group.tagId == it.id } }
    }
}