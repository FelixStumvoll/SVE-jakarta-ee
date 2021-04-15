package com.urlshortener.core.util;

public class ExceptionUtils {
    public static <T extends Throwable> T getException(Throwable t, Class<T> c) {
        var exFind = t.getCause();

        while (exFind != null) {
            if (c.isAssignableFrom(exFind.getClass())) {
                return (T) exFind;
            }

            exFind = exFind.getCause();
        }

        return null;
    }
}
