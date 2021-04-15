package com.urlshortener.dal.entities;

public enum UserRole {
    Premium("Premium"),
    Peasant("Free");

    public final String value;

    UserRole(String value) {
        this.value = value;
    }


}
