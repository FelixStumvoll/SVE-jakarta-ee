package com.urlshortener.dal.repositories.user;

import com.urlshortener.dal.entities.User;
import lombok.NonNull;

import javax.validation.Valid;

public interface UserRepository {
    User merge(@Valid User user);

    void deleteById(long id);

    User findByName(@NonNull String name);

    User findById(long userId);

    boolean existsByName(@NonNull String name);
}
