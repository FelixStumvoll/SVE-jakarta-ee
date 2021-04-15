package com.urlshortener.core.dtos

data class CreateShortUrlDto(
    val shortName: String?,
    val url: String,
    val userId: Long
)

data class ShortUrlDto(
    val shortName: String,
    val url: String,
    val id: Long,
    val userId: Long
)

data class UpdateShortUrlDto(
    val shortName: String?,
    val url: String?,
    val id: Long,
    val userId: Long
)