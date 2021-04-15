package com.urlshortener.core.dtos;

import com.urlshortener.dal.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

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


