package org.urlshortener.core.shorturl;

import lombok.NonNull;
import org.urlshortener.core.dtos.CreateShortUrlDto;
import org.urlshortener.core.dtos.ShortUrlDto;
import org.urlshortener.core.dtos.UpdateShortUrlDto;

import java.util.List;

public interface ShortUrlService {
    ShortUrlDto findByShortname(@NonNull String shortName);

    List<ShortUrlDto> findAll(@NonNull String userId);

    ShortUrlDto findById(@NonNull Long id, @NonNull String userId);

    ShortUrlDto update(@NonNull UpdateShortUrlDto updateShortUrlDto);

    void delete(@NonNull Long id, @NonNull String userId);

    ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto);
}
