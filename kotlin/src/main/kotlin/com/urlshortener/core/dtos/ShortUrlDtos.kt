package com.urlshortener.core.dtos

data class CreateShortUrlDto(
    val shortName: String?,
    val url: String,
    val userId: String
)

data class ShortUrlDto(
    val shortName: String,
    val url: String,
    val id: Long,
    val userId: String
)

data class UpdateShortUrlDto(
    val shortName: String?,
    val url: String?,
    val id: Long,
    val userId: String
)