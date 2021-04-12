package org.urlshortener.core.dtos;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateShortUrlDto {
    private String shortName;
    @NonNull
    private String url;
    @NonNull
    private String userId;
}
