package org.urlshortener.core.util;

public class ExceptionUtils {
    public static Throwable getException(Throwable t, Class<?> c) {
        var exFind = t.getCause();

        while (exFind != null) {
            if (c.isAssignableFrom(exFind.getClass())) {
                return exFind;
            }

            exFind = exFind.getCause();
        }

        return null;
    }
}
