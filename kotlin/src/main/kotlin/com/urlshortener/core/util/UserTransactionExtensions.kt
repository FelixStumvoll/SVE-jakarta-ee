package com.urlshortener.core.util

import com.urlshortener.core.exceptions.EntityModificationException
import java.sql.SQLException
import javax.transaction.UserTransaction
import javax.validation.ConstraintViolationException

inline fun <T> UserTransaction.execute(block: () -> T): T = try {
    begin()
    val res = block()
    commit()
    res
} catch (ex: RuntimeException) {
    rollback()
    throw  ex
}

fun <T> UserTransaction.withUniqueConstraintHandling(
    constraintName: String,
    message: String,
    block: () -> T
): T = try {
    execute(block)
} catch (ex: Exception) {

    ex.findException<ConstraintViolationException>()?.let {
        throw it
    }
    val sqlException = ex.findException<SQLException>()
    if (sqlException != null && sqlException.message?.contains(constraintName, ignoreCase = true) == true)
        throw EntityModificationException(message)


    throw ex
}