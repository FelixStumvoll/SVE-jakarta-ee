package com.urlshortener.api.dtos

import javax.validation.constraints.NotNull

annotation class ApiDto

@ApiDto
data class ApiUpdateShortUrlDto(
    var shortName: String?,
    var url: String?
)

@ApiDto
data class ApiCreateShortUrlDto(
    var shortName: String?,
    @field:NotNull
    var url: String
)