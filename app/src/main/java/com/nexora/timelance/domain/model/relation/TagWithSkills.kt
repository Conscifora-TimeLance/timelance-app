package com.nexora.timelance.domain.model.relation

import com.nexora.timelance.domain.model.entity.Skill
import com.nexora.timelance.domain.model.entity.Tag

data class TagWithSkills(
    val tag: Tag,
    val skills: List<Skill>
)
