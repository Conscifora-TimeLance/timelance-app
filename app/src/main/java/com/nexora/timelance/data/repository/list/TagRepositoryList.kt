package com.nexora.timelance.data.repository.list

import com.nexora.timelance.domain.model.entity.Tag
import com.nexora.timelance.domain.repository.TagRepository

class TagRepositoryList: TagRepository {

    val tags = mutableListOf<Tag>()

    override fun saveTag(tag: Tag): Tag {
        tags.add(tag)
        return tag
    }

    override fun updateTag(tag: Tag): Tag {
        val index = tags.indexOf(tag)
        return tags.set(index, tag)
    }

    override fun deleteTag(tag: Tag) {
        tags.remove(tag)
    }

    override fun getById(idTag: String): Tag {
        return tags.first() { it.id == idTag }
    }

    override fun getAll(): List<Tag> {
        return tags
    }
}