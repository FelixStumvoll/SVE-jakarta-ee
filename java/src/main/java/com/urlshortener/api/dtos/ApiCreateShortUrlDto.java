package com.urlshortener.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ApiCreateShortUrlDto {
    private String shortName;
    @NotNull
    private String url;
}
