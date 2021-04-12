package com.urlshortener.api

import com.urlshortener.core.exceptions.AuthenticationException
import com.urlshortener.core.exceptions.EntityModificationException
import com.urlshortener.core.exceptions.EntityNotFoundException
import com.urlshortener.core.exceptions.ShortNameAlreadyExistsException
import javax.validation.ConstraintViolationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class EntityNotFoundHandler : ExceptionMapper<EntityNotFoundException> {
    override fun toResponse(ex: EntityNotFoundException): Response =
        Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(ex.message!!)).build()
}

@Provider
class EntityModificationHandler : ExceptionMapper<EntityModificationException> {
    override fun toResponse(ex: EntityModificationException): Response =
        Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(ex.message!!)).build()
}

@Provider
class ConstraintViolationHandler : ExceptionMapper<ConstraintViolationException> {
    override fun toResponse(ex: ConstraintViolationException): Response =
        Response.status(Response.Status.BAD_REQUEST)
            .entity(ErrorResponse.of(ex.constraintViolations.map { "${it.propertyPath.last()} ${it.message}" })).build()
}

@Provider
class ShortNameAlreadyExistsHandler : ExceptionMapper<ShortNameAlreadyExistsException> {
    override fun toResponse(ex: ShortNameAlreadyExistsException): Response =
        Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(ex.message!!)).build()
}

@Provider
class AuthenticationExceptionHandler : ExceptionMapper<AuthenticationException> {
    override fun toResponse(ex: AuthenticationException): Response =
        Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse(ex.message!!)).build()
}
