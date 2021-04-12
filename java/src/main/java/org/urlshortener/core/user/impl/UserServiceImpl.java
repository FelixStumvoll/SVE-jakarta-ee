package org.urlshortener.core.user.impl;

import lombok.NonNull;
import org.urlshortener.core.dtos.UserDto;
import org.urlshortener.core.exceptions.EntityNotFoundException;
import org.urlshortener.core.user.UserService;
import org.urlshortener.dal.entities.Constraints;
import org.urlshortener.dal.entities.User;
import org.urlshortener.dal.repositories.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

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
    public UserDto findByName(@NonNull String name) {
        var user = this.userRepository.findByName(name);
        if (user == null) {
            throw new EntityNotFoundException(notFoundByNameMessage(name));
        }

        return this.toDto(user);
    }

    @Override
    public UserDto findById(@NonNull Long id) {
        return this.toDto(this.userRepository.findById(id));
    }

    @Override
    public void delete(@NonNull Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public UserDto create(UserDto userDto) {
        return withUniqueConstraintHandling(this.transaction, Constraints.shortUrlConstraint,
                alreadyExistsMessage(userDto.getName()), () -> toDto(this.userRepository.merge(
                        new User(
                                null,
                                userDto.getName(),
                                userDto.getRole(),
                                userDto.getPassword()
                        ))));
    }

    @Override
    public boolean authenticate(@NonNull UserDto userDto, @NonNull String password) {
        return userDto.getPassword().equals(password);
    }

    private UserDto toDto(User user) {
        return new UserDto(
                user.getName(),
                user.getRole(),
                user.getPassword(),
                user.getId()
            );
    }

    private String notFoundByNameMessage(String name) { return "Short url with name " + name + " not found"; }
    private String alreadyExistsMessage(String name) { return "Short url with name $shortName already exists"; }

}
