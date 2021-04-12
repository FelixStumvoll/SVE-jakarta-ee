package org.urlshortener.api.controllers;

import com.nimbusds.oauth2.sdk.id.Issuer;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.urlshortener.api.dtos.CreateUserDto;
import org.urlshortener.api.dtos.PasswordDto;
import org.urlshortener.core.dtos.UserDto;
import org.urlshortener.core.exceptions.AuthenticationException;
import org.urlshortener.core.user.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.time.Duration;

@Path("/login")
public class LoginController{

    private UserService userService;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;
    @ConfigProperty(name = "urlshortener.login.jwtlifespan")
    Long jwtLifespan;

    @Inject
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/register")
    public Response mockRegister(@Valid CreateUserDto createUserDto){
        userService.create(
                new UserDto(
                        createUserDto.getName(),
                        createUserDto.getRole(),
                        createUserDto.getPassword(),
                        null
                ));
        return Response.ok("user " + createUserDto.getName() + " created").build();
    }

    @POST
    @Path("/{userName}")
    public Response mockLogin(
            @PathParam("userName") String userName,
            PasswordDto passwordDto) {
        var user = userService.findByName(userName);
        if(!userService.authenticate(user, passwordDto.getPassword())){
            throw new AuthenticationException("Wrong Passowrd or User");
        }

        var token = Jwt
                .issuer(issuer)
                .upn(user.getName())
                .groups(user.getRole().toString())
                .expiresIn(Duration.ofSeconds(jwtLifespan))
                .sign();
        return Response.ok(token).build();
    }
}
