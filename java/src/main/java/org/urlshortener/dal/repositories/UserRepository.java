package org.urlshortener.dal.repositories;

import org.urlshortener.dal.entities.User;

import javax.validation.Valid;

public interface UserRepository {
    User merge(@Valid User user);
    void deleteById(Long id);
    User findByName(String name);
    User findById(Long userId);
    boolean existsByName(String name);
}
