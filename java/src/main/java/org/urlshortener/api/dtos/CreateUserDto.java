package org.urlshortener.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.urlshortener.dal.entities.UserRole;

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
