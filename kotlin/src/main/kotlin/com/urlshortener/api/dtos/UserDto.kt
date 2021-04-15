package com.urlshortener.api.dtos

import com.urlshortener.dal.entities.UserRole
import com.urlshortener.util.annotations.NoArgs
import javax.validation.constraints.NotNull

@NoArgs
data class CreateUserDto(
    @field:NotNull
    var name: String,
    @field:NotNull
    var role: UserRole,
    @field:NotNull
    var password: String
)

@NoArgs
data class LoginDto(
    @field:NotNull
    var username: String,
    @field:NotNull
    var password: String
)