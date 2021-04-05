package com.urlshortener.core.services.shorturl

import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import org.springframework.stereotype.Service

@Service
interface ShortUrlService {
    fun findByShortname(shortName: String): ShortUrlDto
    fun findAll(userId: String): List<ShortUrlDto>
    fun findById(id: Long, userId: String): ShortUrlDto
    fun update(entity: UpdateShortUrlDto): ShortUrlDto
    fun delete(id: Long, userId: String)
    fun create(createShortUrlDto: CreateShortUrlDto): ShortUrlDto
}