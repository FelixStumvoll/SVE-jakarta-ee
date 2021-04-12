package org.urlshortener.core.util;

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

    public static <T> T withUniqueConstraintHandling(UserTransaction transaction, String constraintName, String message, Supplier<T> block) {
        try {
            return executeForResult(transaction, block);
        } catch (ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            var sqlException = getException(ex, SQLException.class);
            String msg = sqlException != null && sqlException
                    .getMessage()
                    .toLowerCase(Locale.ROOT)
                    .contains(constraintName) ?
                    message : "error modifying entity";

            throw new EntityModificationException(msg);
        }
    }
}
