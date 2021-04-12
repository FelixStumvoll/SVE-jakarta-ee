package org.urlshortener.core.shorturl.impl;

import lombok.NonNull;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.urlshortener.core.dtos.CreateShortUrlDto;
import org.urlshortener.core.dtos.ShortUrlDto;
import org.urlshortener.core.dtos.UpdateShortUrlDto;
import org.urlshortener.core.exceptions.EntityModificationException;
import org.urlshortener.core.exceptions.EntityNotFoundException;
import org.urlshortener.core.exceptions.ShortNameAlreadyExistsException;
import org.urlshortener.core.namegenerator.NameGenerator;
import org.urlshortener.core.shorturl.ShortUrlService;
import org.urlshortener.dal.entities.Constraints;
import org.urlshortener.dal.entities.ShortUrl;
import org.urlshortener.dal.repositories.ShortUrlRepository;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.urlshortener.core.util.ExceptionUtils.getException;
import static org.urlshortener.core.util.TransactionUtils.executeForResult;

public class ShortUrlServiceImpl implements ShortUrlService {

    ShortUrlRepository shortUrlRepository;

    NameGenerator nameGenerator;

    UserTransaction transaction;

    @ConfigProperty(name = "urlshortener.generatedNameLength")
    int generatedNameLength;

    @Inject
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, NameGenerator nameGenerator, UserTransaction transaction) {
        this.shortUrlRepository = shortUrlRepository;
        this.nameGenerator = nameGenerator;
        this.transaction = transaction;
    }

    @Override
    public ShortUrlDto findByShortname(@NonNull String shortName) {
        var shortUrl = this.shortUrlRepository.findByShortName(shortName);
        if (shortUrl == null) {
            throw new EntityNotFoundException(notFoundByShortNameMessage(shortName));
        }

        return this.toDto(shortUrl);
    }

    @Override
    public List<ShortUrlDto> findAll(@NonNull String userId) {
        var shortUrls = this.shortUrlRepository.findAllByUserId(userId);
        return shortUrls.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ShortUrlDto findById(@NonNull Long id, @NonNull String userId) {
        return this.toDto(this.getById(id, userId));
    }

    @Override
    public ShortUrlDto update(@NonNull UpdateShortUrlDto updateShortUrlDto) {
        return this.shortNameConstraintHandler(updateShortUrlDto.getShortName(), "update", () -> {

        });
    }

    @Override
    public void delete(@NonNull Long id, @NonNull String userId) {
        this.shortUrlRepository.
    }

    @Override
    public ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto) {
        return null;
    }

    private ShortUrl getById(Long id, String userId) {
        var shortUrl = this.shortUrlRepository.findByIdAndUserId(id, userId);
        if (shortUrl == null) {
            throw new EntityNotFoundException(notFoundByIdMessage(id));
        }

        return shortUrl;
    }

    private ShortUrlDto shortNameConstraintHandler(String shortName, String modificationType, Supplier<ShortUrl> block) {
        try {
            return this.toDto(executeForResult(this.transaction, block));
        } catch (Exception ex) {
            if (shortName != null) {
                var sqlException = getException(ex, SQLException.class);
                if (sqlException != null && sqlException.getMessage().toLowerCase(Locale.ROOT).contains(
                        Constraints.shortUrlConstraint)) {
                    throw new ShortNameAlreadyExistsException(shortName);
                }
            }
        }

        throw new EntityModificationException("could not " + modificationType + " short url")
    }

    private ShortUrlDto toDto(ShortUrl shortUrl) {
        return new ShortUrlDto(shortUrl.getShortName(), shortUrl.getUrl(), shortUrl.getId(), shortUrl.getUserId());
    }

    private static String notFoundByIdMessage(Long id) {
        return "Short url with id " + id + " not found";
    }

    private static String notFoundByShortNameMessage(String shortName) {
        return "Short url with name " + shortName + " not found";
    }
}
