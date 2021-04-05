package com.urlshortener.util

inline fun <T> tryElse(default: T, func: () -> T): T =
    try {
        func()
    } catch (_: Exception) {
        default
    }