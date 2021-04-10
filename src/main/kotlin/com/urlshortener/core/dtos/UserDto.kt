package com.urlshortener.core.dtos

import com.urlshortener.dal.entities.UserRole

data class UserDto(
    val name: String,
    val role: UserRole,
    val password: String,
    val createdUrls: Int,
    val id: Long?
)
