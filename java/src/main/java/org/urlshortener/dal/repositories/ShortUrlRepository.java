package org.urlshortener.dal.repositories;

import lombok.NonNull;
import org.urlshortener.dal.entities.ShortUrl;

import javax.validation.Valid;
import java.util.List;

public interface ShortUrlRepository {
    ShortUrl merge(@NonNull @Valid ShortUrl shortUrl);

    void deleteById(@NonNull Long id);

    Long countByShortName(@NonNull String shortName);

    ShortUrl findByShortName(@NonNull String shortName);

    ShortUrl findByIdAndUserId(Long id, String userId);

    boolean existsByIdAndUserId(Long id, String userId);

    List<ShortUrl> findAllByUserId(String userId);
}
