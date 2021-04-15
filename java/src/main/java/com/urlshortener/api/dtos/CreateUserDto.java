package com.urlshortener.api.dtos;

import com.urlshortener.dal.entities.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateUserDto {
    @NotNull
    String name;
    @NotNull
    UserRole role;
    @NotNull
    String password;
}
