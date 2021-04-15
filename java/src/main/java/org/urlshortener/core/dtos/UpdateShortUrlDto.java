package org.urlshortener.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShortUrlDto {
    private String shortName;
    private String url;
    private long id;
    private long userId;
}
