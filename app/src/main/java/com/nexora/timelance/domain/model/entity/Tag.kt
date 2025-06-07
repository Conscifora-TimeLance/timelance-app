package com.nexora.timelance.domain.model.entity

import java.util.UUID

data class Tag(
    val id: String = UUID.randomUUID().toString(),
    val name: String
)
