package com.urlshortener.api.controllers.util

import com.urlshortener.util.tryElse
import org.mindrot.jbcrypt.BCrypt.checkpw

fun getUserIdFromToken(token: String, authSecret: String): String =
    token
        .split(":")
        .takeIf {
            it.size == 2 &&
                    tryElse(false) {
                        checkpw("${it[0]}:${authSecret}", it[1])
                    }
        }?.first()
        ?: throw InvalidAuthenticationException()

class InvalidAuthenticationException : Exception("Invalid authentication parameter")