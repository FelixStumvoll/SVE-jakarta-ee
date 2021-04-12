package org.urlshortener.api.exceptionmappers;

import org.urlshortener.api.ErrorResponse;
import org.urlshortener.core.exceptions.AuthenticationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionHandler implements ExceptionMapper<AuthenticationException> {
    @Override
    public Response toResponse(AuthenticationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.of(e.getMessage())).build();
    }
}
