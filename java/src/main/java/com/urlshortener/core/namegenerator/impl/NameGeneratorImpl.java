package com.urlshortener.core.namegenerator.impl;

import com.urlshortener.core.namegenerator.NameGenerator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NameGeneratorImpl implements NameGenerator {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    /***
     * found on https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
     */
    @Override
    public String generateName(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            var index = (int) (characters.length() * Math.random());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
