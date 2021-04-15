package com.urlshortener.dal.repositories.shorturl

import com.urlshortener.dal.entities.ShortUrl
import javax.validation.Valid

interface ShortUrlRepository {
    fun merge(@Valid shortUrl: ShortUrl): ShortUrl
    fun deleteById(id: Long, userId: Long)
    fun countByShortName(shortName: String): Long
    fun findByShortName(shortName: String): ShortUrl?
    fun findByIdAndUserId(id: Long, userId: Long): ShortUrl?
    fun findAllForUser(userId: Long): List<ShortUrl>
}