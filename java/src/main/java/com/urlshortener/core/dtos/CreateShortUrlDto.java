package com.urlshortener.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShortUrlDto {
    private String shortName;
    @NonNull
    private String url;
    private long userId;
}
