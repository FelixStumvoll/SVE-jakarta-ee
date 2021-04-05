package com.urlshortener.api

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.urlshortener.api.controllers.util.InvalidAuthenticationException
import com.urlshortener.core.exceptions.EntityModificationException
import com.urlshortener.core.exceptions.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAuthenticationException::class)
    fun invalidAuthenticationException(ex: InvalidAuthenticationException) =
        ErrorResponse(ex.message)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun missingRequestHeaderException(ex: MissingRequestHeaderException) =
        ErrorResponse("Header ${ex.headerName} missing")


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(ex: MethodArgumentNotValidException) =
        ErrorResponse.of(ex.fieldErrors.map { "${it.field} ${it.defaultMessage}" })

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(ex: ConstraintViolationException) =
        ErrorResponse.of(ex.constraintViolations.map { "${it.propertyPath} ${it.message}" })

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(ex: HttpMessageNotReadableException): ErrorResponse<String> =
        when (val c = ex.cause) {
            is MissingKotlinParameterException -> "${c.parameter.name} is required"
            is MismatchedInputException -> "cannot parse ${c.path.last().fieldName}"
            else -> "Invalid Request"
        }.let { ErrorResponse(it) }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(ex: EntityNotFoundException) = ErrorResponse(ex.message)


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityModificationException::class)
    fun entityCreationException(ex: EntityModificationException) = ErrorResponse(ex.message)
}

