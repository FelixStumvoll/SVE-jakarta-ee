package org.urlshortener.core.shorturl;

import lombok.NonNull;
import org.urlshortener.core.dtos.CreateShortUrlDto;
import org.urlshortener.core.dtos.ShortUrlDto;
import org.urlshortener.core.dtos.UpdateShortUrlDto;

import java.util.List;

public interface ShortUrlService {
    ShortUrlDto findByShortname(@NonNull String shortName);

    List<ShortUrlDto> findAll(long userId);

    ShortUrlDto findById(long id, long userId);

    ShortUrlDto update(@NonNull UpdateShortUrlDto updateShortUrlDto);

    void delete(long id, long userId);

    ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto);
}
