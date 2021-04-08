package com.urlshortener.api.controllers

import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/login")
class LoginController() {
    @GET
    @Path("/{userName}")
    fun mockLogin(@PathParam("userName") userName: String,
                  @ConfigProperty(name = "mp.jwt.verify.issuer") issuer: String): Response {
        val token: String = Jwt
                .issuer("https://example.com/issuer")
                .upn(userName)
                .groups("user")
                .sign()
        return Response.ok(token).build()
    }
}