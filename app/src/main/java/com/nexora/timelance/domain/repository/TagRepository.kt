package com.nexora.timelance.domain.repository

import com.nexora.timelance.domain.model.entity.Tag

interface TagRepository {
    fun saveTag(tag: Tag): Tag
    fun updateTag(tag: Tag): Tag
    fun deleteTag(tag: Tag)
    fun getById(idTag: String): Tag
}