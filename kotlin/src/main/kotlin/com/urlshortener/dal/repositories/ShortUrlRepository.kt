package com.urlshortener.dal.repositories

import com.urlshortener.dal.entities.ShortUrl
import javax.validation.Valid

interface ShortUrlRepository {
    fun merge(@Valid shortUrl: ShortUrl): ShortUrl
    fun deleteById(id: Long, userId: String)
    fun countByShortName(shortName: String): Long
    fun findByShortName(shortName: String): ShortUrl?
    fun findByIdAndUserId(id: Long, userId: String): ShortUrl?
    fun findAllByUserId(userId: String): List<ShortUrl>
}