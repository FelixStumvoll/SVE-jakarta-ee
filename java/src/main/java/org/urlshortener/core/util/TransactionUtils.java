package org.urlshortener.core.util;

import javax.transaction.UserTransaction;
import java.util.function.Supplier;

public class TransactionUtils {
    public static <T> T executeForResult(UserTransaction transaction, Supplier<T> supplier) throws Exception {
        try {
            transaction.begin();
            var result = supplier.get();
            transaction.commit();
            return result;
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
    }
}
