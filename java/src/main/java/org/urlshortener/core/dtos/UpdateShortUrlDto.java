package org.urlshortener.core.dtos;

import lombok.Data;

@Data
public class UpdateShortUrlDto {
    private String shortName;
    private String url;
    private Long id;
    private String userId;
}
