package com.nexora.timelance.data.service

import com.nexora.timelance.domain.model.entity.Tag

interface TagService {

    fun createTag(tag: Tag): Tag
    fun getTagById(id: String): Tag
    fun getAllTags(): List<Tag>
}