package org.urlshortener.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.urlshortener.dal.entities.UserRole;

@Data
@AllArgsConstructor
public class UserDto {
    @NonNull
    private String name;
    @NonNull
    private UserRole role;
    @NonNull
    private String password;
    private Long id;
}


