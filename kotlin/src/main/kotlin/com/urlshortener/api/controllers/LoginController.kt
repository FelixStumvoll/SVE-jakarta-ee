package com.urlshortener.api.controllers

import com.urlshortener.api.ID_CLAIM
import com.urlshortener.api.dtos.CreateUserDto
import com.urlshortener.api.dtos.LoginDto
import com.urlshortener.core.dtos.UserDto
import com.urlshortener.core.services.user.UserService
import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/")
@RequestScoped
class LoginController(
    @Inject private val userService: UserService
) {
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    var issuer: String? = null

    @ConfigProperty(name = "urlshortener.login.jwtlifespan")
    var jwtLifespan: Long = 1000

    @POST
    @Path("/register")
    fun register(
        @Valid createUserDto: CreateUserDto,
    ): Response {
        userService.create(
            UserDto(
                createUserDto.name,
                createUserDto.role,
                createUserDto.password,
                null,
            )
        )
        return Response.ok("user ${createUserDto.name} created").build()
    }


    @POST
    @Path("/login")
    fun login(
        loginDto: LoginDto
    ): Response {
        val user = userService.authenticate(loginDto.username, loginDto.password)

        val token: String = Jwt
            .issuer(issuer)
            .upn(user.name)
            .claim(ID_CLAIM, user.id)
            .groups(user.role.type)
            .expiresIn(Duration.ofSeconds(jwtLifespan))
            .sign()
        return Response.ok(token).build()
    }
}