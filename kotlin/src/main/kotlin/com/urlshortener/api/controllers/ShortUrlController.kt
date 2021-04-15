package com.urlshortener.api.controllers

import com.urlshortener.api.ID_CLAIM
import com.urlshortener.api.dtos.ApiCreateShortUrlDto
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto
import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.services.shorturl.ShortUrlService
import org.eclipse.microprofile.jwt.JsonWebToken
import java.net.URI
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.json.JsonNumber
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/short-url")
@RequestScoped
class ShortUrlController(
    @Inject private val shortUrlService: ShortUrlService,
) {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Premium", "Free")
    fun getAll(@Context securityContext: SecurityContext): List<ShortUrlDto> =
        shortUrlService.findAll(securityContext.idClaim())

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Premium", "Free")
    @Path("/{id}")
    fun getById(
        @PathParam("id") id: Long,
        @Context securityContext: SecurityContext,
    ): ShortUrlDto {
        return shortUrlService.findById(id, securityContext.idClaim())
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Premium", "Free")
    fun create(
        @Valid createShortUrlDto: ApiCreateShortUrlDto,
        @Context securityContext: SecurityContext,
    ): Response =
        shortUrlService.create(
            CreateShortUrlDto(
                createShortUrlDto.shortName,
                createShortUrlDto.url,
                securityContext.idClaim()
            )
        ).let {
            Response.created(URI("/short-url/${it.id}")).entity(it).build()
        }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("Premium")
    fun update(
        updateDto: ApiUpdateShortUrlDto,
        @PathParam("id") id: Long,
        @Context securityContext: SecurityContext,
    ): Response =
        shortUrlService.update(
            UpdateShortUrlDto(
                updateDto.shortName,
                updateDto.url,
                id,
                securityContext.idClaim()
            )
        ).let {
            Response.ok(it).build()
        }

    @DELETE
    @RolesAllowed("Premium")
    @Path("/{id}")
    fun delete(
        @PathParam("id") id: Long,
        @Context securityContext: SecurityContext,
    ): Response {
        shortUrlService.delete(id, securityContext.idClaim())
        return Response.ok().build()
    }

    fun SecurityContext.idClaim(): Long = (userPrincipal as JsonWebToken).getClaim<JsonNumber>(ID_CLAIM).longValue()
}