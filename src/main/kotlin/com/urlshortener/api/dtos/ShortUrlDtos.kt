package com.urlshortener.api.dtos

data class ApiUpdateShortUrlDto(val shortName: String?, val url: String?)
data class ApiCreateShortUrlDto(val shortName: String, val url: String)