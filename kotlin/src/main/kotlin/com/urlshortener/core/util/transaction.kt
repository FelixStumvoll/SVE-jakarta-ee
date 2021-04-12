package com.urlshortener.core.util

import javax.transaction.UserTransaction

inline fun <T> UserTransaction.execute(block: () -> T): T = try {
    begin()
    val res = block()
    commit()
    res
} catch (ex: RuntimeException) {
    rollback()
    throw  ex
}