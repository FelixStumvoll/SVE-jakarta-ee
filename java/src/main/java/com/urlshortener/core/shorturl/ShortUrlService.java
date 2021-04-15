package com.urlshortener.core.shorturl;

import com.urlshortener.core.dtos.CreateShortUrlDto;
import com.urlshortener.core.dtos.ShortUrlDto;
import com.urlshortener.core.dtos.UpdateShortUrlDto;
import lombok.NonNull;

import java.util.List;

public interface ShortUrlService {
    ShortUrlDto findByShortname(@NonNull String shortName);

    List<ShortUrlDto> findAll(long userId);

    ShortUrlDto findById(long id, long userId);

    ShortUrlDto update(@NonNull UpdateShortUrlDto updateShortUrlDto);

    void delete(long id, long userId);

    ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto);
}
