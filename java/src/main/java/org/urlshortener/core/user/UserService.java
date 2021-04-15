package org.urlshortener.core.user;

import lombok.NonNull;
import org.urlshortener.core.dtos.UserDto;

public interface UserService {
    UserDto findByName(@NonNull String name);

    UserDto findById(long id);

    void delete(long id);

    UserDto create(@NonNull UserDto userDto);

    UserDto authenticate(@NonNull String username, @NonNull String password);
}
