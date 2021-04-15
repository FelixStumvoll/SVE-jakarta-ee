package com.urlshortener.api.controllers;

import com.urlshortener.api.AuthConstants;
import com.urlshortener.api.dtos.ApiCreateShortUrlDto;
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto;
import com.urlshortener.core.dtos.CreateShortUrlDto;
import com.urlshortener.core.dtos.ShortUrlDto;
import com.urlshortener.core.dtos.UpdateShortUrlDto;
import com.urlshortener.core.shorturl.ShortUrlService;
import lombok.SneakyThrows;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonNumber;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.List;

@Path("/short-url")
@RequestScoped
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @Inject
    public ShortUrlController(ShortUrlService shortUrlService) {this.shortUrlService = shortUrlService;}

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"Premium", "Free"})
    public List<ShortUrlDto> getAll(@Context SecurityContext securityContext) {
        return this.shortUrlService.findAll(this.getUserIdFromContext(securityContext));
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"Premium", "Free"})
    public ShortUrlDto getById(@PathParam("id") long id, @Context SecurityContext securityContext) {
        return this.shortUrlService.findById(id, this.getUserIdFromContext(securityContext));
    }

    @SneakyThrows
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"Premium", "Free"})
    public Response create(@Valid ApiCreateShortUrlDto createShortUrlDto, @Context SecurityContext securityContext) {
        var newShortUrl = this.shortUrlService.create(
                new CreateShortUrlDto(createShortUrlDto.getShortName(), createShortUrlDto.getUrl(),
                                      this.getUserIdFromContext(securityContext)));
        return Response.created(new URI("/short-url/" + newShortUrl.getId())).entity(newShortUrl).build();
    }

    @SneakyThrows
    @PATCH
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    @RolesAllowed({"Premium"})
    public Response update(@PathParam("id") long id, @Valid ApiUpdateShortUrlDto updateShortUrlDto, @Context SecurityContext securityContext) {
        var newShortUrl = this.shortUrlService.update(
                new UpdateShortUrlDto(updateShortUrlDto.getShortName(), updateShortUrlDto.getUrl(), id,
                                      this.getUserIdFromContext(securityContext)));
        return Response.ok(newShortUrl).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Premium"})
    public Response delete(@PathParam("id") long id, @Context SecurityContext securityContext) {
        this.shortUrlService.delete(id, this.getUserIdFromContext(securityContext));
        return Response.ok().build();
    }

    private long getUserIdFromContext(SecurityContext securityContext) {
        return
                ((JsonWebToken) securityContext.getUserPrincipal()).<JsonNumber>getClaim(
                        AuthConstants.ID_CLAIM).longValue();
    }
}
