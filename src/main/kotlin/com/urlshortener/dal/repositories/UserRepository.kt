package com.urlshortener.dal.repositories

import com.urlshortener.dal.entities.User
import javax.validation.Valid

interface UserRepository {
    fun merge(@Valid user: User): User
    fun deleteById(id: Long)
    fun findByName(name: String): User?
    fun findById(userId: Long): User?
    fun existsByName(name: String): Boolean
}