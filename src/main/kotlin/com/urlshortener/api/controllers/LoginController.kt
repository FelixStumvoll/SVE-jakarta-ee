package com.urlshortener.api.controllers

import com.urlshortener.api.dtos.CreateUserDto
import com.urlshortener.core.dtos.UserDto
import com.urlshortener.core.services.user.UserService
import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.Duration
import javax.inject.Inject
import javax.naming.AuthenticationException
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/login")
class LoginController(
    @Inject private val userService: UserService
) {
    @ConfigProperty(name = "mp.jwt.verify.issuer") var issuer: String? = null
    @ConfigProperty(name = "urlshortener.login.jwtlifespan") var jwtLifespan: Long = 1000

    @POST
    @Path("/register")
    fun mockRegister(
        @Valid createUserDto: CreateUserDto,
    ): Response {
        userService.create(UserDto(
            createUserDto.name,
            createUserDto.role,
            createUserDto.password,
            0,
            null,
        ))
        return Response.ok("user ${createUserDto.name} created").build()
    }


    @POST
    @Path("/{userName}")
    fun mockLogin(
        @PathParam("userName") userName: String,
        password: String
    ) : Response {
        val user = userService.findByName(userName)
        if (userService.authenticate(user, password)){
            throw AuthenticationException("Wrong Password or User")
        }
        val token: String = Jwt
                .issuer(issuer)
                .upn(user.name)
                .groups(user.role.type)
                .expiresIn(Duration.ofSeconds(jwtLifespan))
                .sign()
        return Response.ok(token).build()
    }
}