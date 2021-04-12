package com.urlshortener.api.controllers

import com.urlshortener.api.dtos.ApiCreateShortUrlDto
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto
import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.services.shorturl.ShortUrlService
import java.net.URI
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/short-url")
@RequestScoped
class ShortUrlController(
    @Inject private val shortUrlService: ShortUrlService,
) {
    @GET
    @RolesAllowed("Premium", "Free")
    fun getAll(@Context securityContext: SecurityContext): List<ShortUrlDto> =
        shortUrlService.findAll(securityContext.userPrincipal.name)

    @GET
    @RolesAllowed("Premium", "Free")
    @Path("/{id}")
    fun getById(
        @PathParam("id") id: Long,
        @Context securityContext: SecurityContext,
    ): ShortUrlDto {
        return shortUrlService.findById(id, securityContext.userPrincipal.name)
    }

    @POST
    @RolesAllowed("Premium", "Free")
    fun create(
        @Valid createShortUrlDto: ApiCreateShortUrlDto,
        @Context securityContext: SecurityContext,
    ): Response =
        shortUrlService.create(
            CreateShortUrlDto(
                createShortUrlDto.shortName,
                createShortUrlDto.url,
                securityContext.userPrincipal.name
            )
        ).let {
            Response.created(URI("/short-url/${it.id}")).build()
        }

    @PATCH
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
                securityContext.userPrincipal.name
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
        shortUrlService.delete(id, securityContext.userPrincipal.name)
        return Response.ok().build()
    }
}