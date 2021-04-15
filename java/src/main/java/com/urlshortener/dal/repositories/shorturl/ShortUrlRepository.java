package com.urlshortener.dal.repositories.shorturl;

import com.urlshortener.dal.entities.ShortUrl;
import lombok.NonNull;

import javax.validation.Valid;
import java.util.List;

public interface ShortUrlRepository {
    ShortUrl merge(@NonNull @Valid ShortUrl shortUrl);

    void deleteById(long id, long userId);

    Long countByShortName(@NonNull String shortName);

    ShortUrl findByShortName(@NonNull String shortName);

    ShortUrl findByIdAndUserId(long id, long userId);

    List<ShortUrl> findAllForUser(long userId);
}
