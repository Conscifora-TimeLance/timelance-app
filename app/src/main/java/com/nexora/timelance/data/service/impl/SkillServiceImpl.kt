package com.nexora.timelance.data.service.impl

import com.nexora.timelance.data.dto.SkillHistoryDto
import com.nexora.timelance.data.dto.SkillDto
import com.nexora.timelance.data.repository.list.HistorySkillRepositoryList
import com.nexora.timelance.data.repository.list.SkillRepositoryList
import com.nexora.timelance.data.repository.list.TagRepositoryList
import com.nexora.timelance.data.repository.list.TagSkillGroupRepositoryList
import com.nexora.timelance.domain.model.entity.HistorySkill
import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.data.service.SkillService
import com.nexora.timelance.data.service.TagService
import com.nexora.timelance.domain.model.entity.Tag
import java.util.UUID

class SkillServiceImpl(): SkillService, TagService {

    private val tagRepository = TagRepositoryList()
    private val historySkillRepository = HistorySkillRepositoryList()
    private val skillRepository = SkillRepositoryList(tagRepository, historySkillRepository)
    private val tagSkillGroupRepository = TagSkillGroupRepositoryList()

    override fun createSkill(skillDto: SkillDto): Skill {
        val skill = Skill(skillDto.skillId ?: UUID.randomUUID().toString(),
            skillDto.name, skillDto.timeTotalSeconds)
        val saveSkill = skillRepository.saveSkill(skill)
        skillDto.groupTags.forEach { tag ->
            if (tagRepository.getById(tag.id) == null) {
                throw IllegalArgumentException("Tag not exist in database!")
            }
            tagSkillGroupRepository.addTagToSkill(skill.id, tag.id)
        }
        return saveSkill
    }

    override fun addTrackHistoryBySkillId(history: HistorySkill) {
        historySkillRepository.saveHistory(history)
        skillRepository.updateTotalSeconds()
    }

    override fun getSkillById(skillId: String): SkillDto {
        val skill = skillRepository.getSkillById(skillId)
        val skillTagGroups = tagSkillGroupRepository.getGroupsBySkillId(skillId)
        val tags = skillTagGroups.map { tagSkillGroup -> tagRepository.getById(tagSkillGroup.tagId) }
        return SkillDto(skillId, skill.name, tags, skill.timeTotalSeconds)
    }

    override fun getHistoryBySkillId(skillId: String): SkillHistoryDto {
        val skill = skillRepository.getSkillById(skillId)
        val histories = historySkillRepository.getHistoryBySkillId(skillId)
        return SkillHistoryDto(skill, histories)
    }

    override fun getAllSkills(): List<SkillDto> {
        return skillRepository.getAll().map { SkillDto(it.id, it.name, getTagsBySkillId(it.id), it.timeTotalSeconds) }
    }

    override fun getTagsBySkillId (skillId: String): List<Tag> {
        return tagSkillGroupRepository.getGroupsBySkillId(skillId).map { getTagById(it.tagId) }
    }

    override fun getAllHistory(): List<SkillHistoryDto> {
        return historySkillRepository.getAll()
            .groupBy { skillRepository.getSkillById(it.skillId) }
            .map { SkillHistoryDto(it.key, it.value) }
    }

    override fun getSkillsByTags(selectedTags: List<Tag>): List<SkillDto> {
        return tagSkillGroupRepository.getGroupsByTags(selectedTags)
            .map { group -> SkillDto(
                group.skillId,
                getSkillById(group.skillId).name,
                getTagsBySkillId(group.skillId),
                getSkillById(group.skillId).timeTotalSeconds) }
    }

    override fun createTag(tag: Tag): Tag {
        tagRepository.saveTag(tag)
        return tag
    }

    override fun getTagById(id: String): Tag {
        return tagRepository.getById(id)
    }

    override fun getAllTags(): List<Tag> {
        return tagRepository.getAll()
    }
}