package org.urlshortener.dal.util;

import javax.persistence.TypedQuery;

public class TypedQueryUtils {
    public static <T> T singleResult(TypedQuery<T> query) {
        if (query.getResultList().isEmpty()) return null;
        else return query.getResultList().get(0);
    }
}
