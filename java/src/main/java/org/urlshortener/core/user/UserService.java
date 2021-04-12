package org.urlshortener.core.user;

import org.urlshortener.core.dtos.UserDto;

public interface UserService {
    UserDto findByName(String name);
    UserDto findById(Long id);
    void delete(Long id);
    UserDto create(UserDto userDto);
    boolean authenticate(UserDto userDto, String password);
}
