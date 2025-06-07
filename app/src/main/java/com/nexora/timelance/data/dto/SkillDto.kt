package com.nexora.timelance.data.dto

import com.nexora.timelance.domain.model.entity.Tag
import java.util.UUID

data class SkillDto(

    val skillId: String = UUID.randomUUID().toString(),
    val name: String,
    val groupTags: List<Tag>,
    val timeTotalSeconds: Long
)
