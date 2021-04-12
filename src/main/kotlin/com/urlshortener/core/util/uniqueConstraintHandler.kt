package com.urlshortener.core.util

import com.urlshortener.core.exceptions.EntityModificationException
import java.sql.SQLException
import javax.transaction.UserTransaction
import javax.validation.ConstraintViolationException

fun <T> UserTransaction.withUniqueConstraintHandling(
    constraintName: String,
    message: String,
    block: () -> T
): T = try {
    execute(block)
} catch (ex: ConstraintViolationException) {
    throw ex
} catch (ex: Exception) {
    val sqlException = ex.getException<SQLException>()
    val msg =
        if (sqlException != null && sqlException.message?.contains(constraintName, ignoreCase = true) == true) {
            message
        } else "error modifying entity"

    throw EntityModificationException(msg)
}