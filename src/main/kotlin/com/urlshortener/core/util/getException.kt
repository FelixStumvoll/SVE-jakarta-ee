package com.urlshortener.core.util

inline fun <reified T> Throwable.getException(): Throwable? {
    var exFind = this

    while (exFind.cause != null) {
        if (exFind.cause is T) {
            return exFind.cause
        }

        exFind = exFind.cause!!
    }
    return null
}