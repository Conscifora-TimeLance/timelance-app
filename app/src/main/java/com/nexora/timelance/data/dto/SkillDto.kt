package com.nexora.timelance.data.dto

import com.nexora.timelance.domain.model.entity.Tag

data class SkillDto(
    val name:String,
    val groupTags:List<Tag>,
    val timeTotalSeconds:Int
)
