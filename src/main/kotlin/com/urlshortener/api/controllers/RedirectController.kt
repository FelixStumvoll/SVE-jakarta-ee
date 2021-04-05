package com.urlshortener.api.controllers

import com.urlshortener.core.services.shorturl.ShortUrlService
import java.net.URI
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Response

@Path("/short-url")
class RedirectController(private val shortUrlService: ShortUrlService) {
    @GET
    @Path("/{shortName}")
    fun redirectTo(@PathParam("shortName") shortName: String): Response {
        val shortUrl = shortUrlService.findByShortname(shortName)
        return Response.temporaryRedirect(URI.create(shortUrl.url)).build()
    }
}