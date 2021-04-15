package com.urlshortener.api.exceptionmappers;

import com.urlshortener.api.ErrorResponse;
import com.urlshortener.core.exceptions.EntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundHandler implements ExceptionMapper<EntityNotFoundException> {
    @Override
    public Response toResponse(EntityNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.of(e.getMessage())).build();
    }
}
