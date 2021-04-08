package com.urlshortener.api.controllers

import com.urlshortener.api.dtos.ApiCreateShortUrlDto
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto
import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.services.shorturl.ShortUrlService
import javax.annotation.security.RolesAllowed
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

@Path("/short-url")
class ShortUrlController(
    @Inject private val shortUrlService: ShortUrlService,
) {
    @GET
    fun getAll(): List<ShortUrlDto> =
        shortUrlService.findAll("")

    @GET
    @Path("/{id}")
    fun getById(
        @PathParam("id") id: Long,
    ): ShortUrlDto =
        shortUrlService.findById(id, "")

    @POST
    @RolesAllowed("user")
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

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
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
    @Path("/{id}")
    fun delete(
        @PathParam("id") id: Long,
    ): Response {
        shortUrlService.delete(id, "")
        return Response.ok().build()
    }
}