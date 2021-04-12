package com.urlshortener.api.dtos

import com.urlshortener.util.annotations.NoArgs
import javax.validation.constraints.NotNull

@NoArgs
data class ApiUpdateShortUrlDto(
    var shortName: String?,
    var url: String?
)

@NoArgs
data class ApiCreateShortUrlDto(
    var shortName: String?,
    @field:NotNull
    var url: String
)