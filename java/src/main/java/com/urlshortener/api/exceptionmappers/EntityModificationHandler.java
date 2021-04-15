package com.urlshortener.api.exceptionmappers;

import com.urlshortener.api.ErrorResponse;
import com.urlshortener.core.exceptions.EntityModificationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityModificationHandler implements ExceptionMapper<EntityModificationException> {
    @Override
    public Response toResponse(EntityModificationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.of(e.getMessage())).build();
    }
}