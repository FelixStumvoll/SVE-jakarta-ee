package com.urlshortener.core.services.user

import com.urlshortener.core.dtos.UserDto

interface UserService {
    fun findByName(name: String): UserDto
    fun findById(id: Long): UserDto?
    fun delete(id: Long)
    fun create(userDto: UserDto): UserDto
    fun authenticate(userDto: UserDto, password: String): Boolean
}