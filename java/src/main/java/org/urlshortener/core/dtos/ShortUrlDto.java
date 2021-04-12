package org.urlshortener.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDto {
    private String shortName;
    private String url;
    private Long id;
    private String userId;
}
