package org.urlshortener.api.controllers;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.urlshortener.api.AuthConstants;
import org.urlshortener.api.dtos.CreateUserDto;
import org.urlshortener.api.dtos.LoginDto;
import org.urlshortener.core.dtos.UserDto;
import org.urlshortener.core.user.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;

@Path("")
public class LoginController {

    private final UserService userService;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;
    @ConfigProperty(name = "urlshortener.login.jwtlifespan")
    Long jwtLifespan;

    @Inject
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/register")
    public Response register(@Valid CreateUserDto createUserDto) {
        this.userService.create(
                new UserDto(
                        createUserDto.getName(),
                        createUserDto.getRole(),
                        createUserDto.getPassword(),
                        null
                ));
        return Response.ok("user " + createUserDto.getName() + " created").build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("login")
    public Response login(
            LoginDto loginDto) {
        var user = this.userService.authenticate(loginDto.getUsername(), loginDto.getPassword());

        var token = Jwt
                .issuer(this.issuer)
                .upn(user.getName())
                .claim(AuthConstants.ID_CLAIM, user.getId())
                .groups(user.getRole().value)
                .expiresIn(Duration.ofSeconds(this.jwtLifespan))
                .sign();
        return Response.ok(token).build();
    }
}
