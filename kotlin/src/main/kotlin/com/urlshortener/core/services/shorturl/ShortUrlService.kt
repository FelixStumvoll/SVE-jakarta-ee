package com.urlshortener.core.services.shorturl

import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto

interface ShortUrlService {
    fun findByShortname(shortName: String): ShortUrlDto
    fun findAll(userId: Long): List<ShortUrlDto>
    fun findById(id: Long, userId: Long): ShortUrlDto
    fun update(entity: UpdateShortUrlDto): ShortUrlDto
    fun delete(id: Long, userId: Long)
    fun create(createShortUrlDto: CreateShortUrlDto): ShortUrlDto
}