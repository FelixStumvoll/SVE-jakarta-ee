package org.urlshortener.core.exceptions;

public class EntityModificationException extends RuntimeException {
    public EntityModificationException(String message) {
        super(message);
    }
}
