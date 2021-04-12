package org.urlshortener.api.dtos;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordDto {
    @NotNull
    private String password;
}
