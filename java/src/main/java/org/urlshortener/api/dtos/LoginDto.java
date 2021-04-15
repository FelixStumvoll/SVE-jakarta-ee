package org.urlshortener.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LoginDto {
    @NotNull
    private String password;
    @NotNull
    private String username;
}
