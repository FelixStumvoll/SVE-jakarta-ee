package org.urlshortener.core.user.impl;

import lombok.NonNull;
import org.urlshortener.core.dtos.UserDto;
import org.urlshortener.core.exceptions.AuthenticationException;
import org.urlshortener.core.exceptions.EntityNotFoundException;
import org.urlshortener.core.user.UserService;
import org.urlshortener.dal.entities.Constraints;
import org.urlshortener.dal.entities.User;
import org.urlshortener.dal.repositories.user.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.Collections;

import static org.urlshortener.core.util.TransactionUtils.withUniqueConstraintHandling;

@RequestScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTransaction transaction;

    @Inject
    public UserServiceImpl(UserRepository userRepository, UserTransaction transaction) {
        this.userRepository = userRepository;
        this.transaction = transaction;
    }

    @Override
    @Transactional
    public UserDto findByName(@NonNull String name) {
        var user = this.userRepository.findByName(name);
        if (user == null) {
            throw new EntityNotFoundException(this.notFoundByNameMessage(name));
        }

        return this.toDto(user);
    }

    @Override
    @Transactional
    public UserDto findById(long id) {
        return this.toDto(this.userRepository.findById(id));
    }

    @Override
    @Transactional
    public void delete(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public UserDto create(@NonNull UserDto userDto) {
        return withUniqueConstraintHandling(this.transaction, Constraints.userNameConstraint,
                                            this.alreadyExistsMessage(userDto.getName()),
                                            () -> this.toDto(this.userRepository.merge(
                                                    new User(
                                                            null,
                                                            userDto.getName(),
                                                            userDto.getRole(),
                                                            userDto.getPassword(),
                                                            Collections.emptySet()
                                                    ))));
    }

    @Override
    @Transactional
    public UserDto authenticate(@NonNull String username, @NonNull String password) {
        var user = this.userRepository.findByName(username);

        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Wrong username or password");
        }

        return this.toDto(user);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getName(),
                user.getRole(),
                user.getPassword(),
                user.getId()
        );
    }

    private String notFoundByNameMessage(String name) { return "User with name " + name + " not found"; }

    private String alreadyExistsMessage(String name) { return "User with name " + name + " already exists"; }
}
