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
} catch (ex: Exception) {

    ex.getException<ConstraintViolationException>()?.let {
        throw it
    }
    val sqlException = ex.getException<SQLException>()
    if (sqlException != null && sqlException.message?.contains(constraintName, ignoreCase = true) == true)
        throw EntityModificationException(message)


    throw ex
}