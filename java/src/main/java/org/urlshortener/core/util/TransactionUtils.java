package org.urlshortener.core.util;

import lombok.SneakyThrows;
import org.urlshortener.core.exceptions.EntityModificationException;

import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.function.Supplier;

import static org.urlshortener.core.util.ExceptionUtils.getException;

public class TransactionUtils {
    public static <T> T executeForResult(UserTransaction transaction, Supplier<T> supplier) throws Exception {
        try {
            transaction.begin();
            var result = supplier.get();
            transaction.commit();
            return result;
        } catch (RuntimeException ex) {
            transaction.rollback();
            throw ex;
        }
    }

    @SneakyThrows
    public static <T> T withUniqueConstraintHandling(UserTransaction transaction, String constraintName, String message, Supplier<T> block) {
        try {
            return executeForResult(transaction, block);
        } catch (Exception ex) {
            var constraintViolationException = getException(ex, ConstraintViolationException.class);

            if (constraintViolationException != null) {
                throw constraintViolationException;
            }

            var sqlException = getException(ex, SQLException.class);
            if (sqlException != null && sqlException
                    .getMessage()
                    .toLowerCase(Locale.ROOT)
                    .contains(constraintName)) {
                throw new EntityModificationException(message);
            }
            throw ex;
        }
    }
}
