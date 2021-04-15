package com.urlshortener.api.controllers;

import com.urlshortener.core.shorturl.ShortUrlService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/")
@RequestScoped
public class RedirectController {

    private final ShortUrlService shortUrlService;

    @Inject
    public RedirectController(ShortUrlService shortUrlService) {this.shortUrlService = shortUrlService;}

    @GET
    @Path("/{shortName}")
    public Response redirectTo(@PathParam("shortName") String shortName) {
        var shortUrl = this.shortUrlService.findByShortname(shortName);
        return Response.temporaryRedirect(URI.create(shortUrl.getUrl())).build();
    }
}
