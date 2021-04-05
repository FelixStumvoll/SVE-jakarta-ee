package com.urlshortener.dal.repositories

import com.urlshortener.dal.entities.ShortUrl
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ShortUrlRepository : CrudRepository<ShortUrl, Long> {
    fun countByShortName(shortName: String): Int
    fun findByShortName(shortName: String): ShortUrl?
    fun findByIdAndUserId(id: Long, userId: String): ShortUrl?
    fun existsByIdAndUserId(id: Long, userId: String): Boolean
    fun findAllByUserId(userId: String): List<ShortUrl>
}