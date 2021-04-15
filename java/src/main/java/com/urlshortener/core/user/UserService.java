package com.urlshortener.core.user;

import com.urlshortener.core.dtos.UserDto;
import lombok.NonNull;

public interface UserService {
    UserDto findByName(@NonNull String name);

    UserDto findById(long id);

    void delete(long id);

    UserDto create(@NonNull UserDto userDto);

    UserDto authenticate(@NonNull String username, @NonNull String password);
}
