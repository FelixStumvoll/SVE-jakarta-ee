package com.urlshortener.api

class ErrorResponse<T>(vararg val errors: T) {
    companion object {
        inline fun <reified T> of(errors: List<T>) = ErrorResponse(*errors.toTypedArray())
    }
}