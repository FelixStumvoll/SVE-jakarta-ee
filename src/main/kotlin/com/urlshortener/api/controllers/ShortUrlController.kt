package com.urlshortener.api.controllers

import com.urlshortener.api.dtos.ApiCreateShortUrlDto
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto
import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.services.shorturl.ShortUrlService
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Response

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
    fun create(
        @Valid createShortUrlDto: ApiCreateShortUrlDto,
    ): Response =
        shortUrlService.create(
            CreateShortUrlDto(
                createShortUrlDto.shortName,
                createShortUrlDto.url,
                "" //Todo implement jwt
            )
        ).let {
            Response.created(URI("/short-url/${it.id}")).build()
        }

    @PUT
    @Path("/{id}")
    fun update(
        updateDto: ApiUpdateShortUrlDto,
        @PathParam("id") id: Long,
    ): Response =
        shortUrlService.update(
            UpdateShortUrlDto(
                updateDto.shortName,
                updateDto.url,
                id,
                "" //Todo implement jwt
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