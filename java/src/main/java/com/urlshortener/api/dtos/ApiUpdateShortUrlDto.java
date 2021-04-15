package com.urlshortener.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiUpdateShortUrlDto {
    private String shortName;
    private String url;
}
