package org.urlshortener.dal.repositories.user;

import lombok.NonNull;
import org.urlshortener.dal.entities.User;

import javax.validation.Valid;

public interface UserRepository {
    User merge(@Valid User user);

    void deleteById(long id);

    User findByName(@NonNull String name);

    User findById(long userId);

    boolean existsByName(@NonNull String name);
}
