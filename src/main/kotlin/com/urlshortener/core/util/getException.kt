package com.urlshortener.core.util

inline fun <reified T> Throwable.getException(): Throwable? {
    var exFind = this.cause

    while (exFind != null) {
        if (exFind is T) {
            return exFind
        }

        exFind = exFind.cause!!
    }
    return null
}