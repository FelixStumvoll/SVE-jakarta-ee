package org.urlshortener.dal.repositories.shorturl;

import lombok.NonNull;
import org.urlshortener.dal.entities.ShortUrl;

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
