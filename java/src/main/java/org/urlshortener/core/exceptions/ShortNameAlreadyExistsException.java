package org.urlshortener.core.exceptions;

public class ShortNameAlreadyExistsException extends RuntimeException {
    public ShortNameAlreadyExistsException(String shortName) {
        super("Url with shortname " + shortName + " already exists");
    }
}