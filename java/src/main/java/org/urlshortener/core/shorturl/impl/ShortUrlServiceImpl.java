package org.urlshortener.core.shorturl.impl;

import lombok.NonNull;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.urlshortener.core.dtos.CreateShortUrlDto;
import org.urlshortener.core.dtos.ShortUrlDto;
import org.urlshortener.core.dtos.UpdateShortUrlDto;
import org.urlshortener.core.exceptions.EntityNotFoundException;
import org.urlshortener.core.namegenerator.NameGenerator;
import org.urlshortener.core.shorturl.ShortUrlService;
import org.urlshortener.dal.entities.Constraints;
import org.urlshortener.dal.entities.ShortUrl;
import org.urlshortener.dal.repositories.ShortUrlRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.stream.Collectors;

import static org.urlshortener.core.util.TransactionUtils.withUniqueConstraintHandling;

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

        return toDto(shortUrl);
    }

    @Override
    public List<ShortUrlDto> findAll(@NonNull String userId) {
        var shortUrls = this.shortUrlRepository.findAllByUserId(userId);
        return shortUrls.stream().map(ShortUrlServiceImpl::toDto).collect(Collectors.toList());
    }

    @Override
    public ShortUrlDto findById(@NonNull Long id, @NonNull String userId) {
        return toDto(this.getById(id, userId));
    }

    @Override
    public ShortUrlDto update(@NonNull UpdateShortUrlDto updateShortUrlDto) {
        return withUniqueConstraintHandling(this.transaction, Constraints.shortUrlConstraint,
                                            shortUrlAlreadyExistsMessage(updateShortUrlDto.getShortName()), () -> {
                    var persistedEntity = this.getById(updateShortUrlDto.getId(), updateShortUrlDto.getUserId());

                    if (updateShortUrlDto.getShortName() != null) {
                        persistedEntity.setShortName(updateShortUrlDto.getShortName());
                    }

                    if (updateShortUrlDto.getUrl() != null) {
                        persistedEntity.setUrl(updateShortUrlDto.getUrl());
                    }

                    return toDto(persistedEntity);
                });
    }

    @Transactional
    @Override
    public void delete(@NonNull Long id, @NonNull String userId) {
        this.shortUrlRepository.deleteById(id, userId);
    }

    @Override
    public ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto) {
        return withUniqueConstraintHandling(this.transaction, Constraints.shortUrlConstraint,
                                            shortUrlAlreadyExistsMessage(createShortUrlDto.getShortName()), () -> {
                    var shortName = createShortUrlDto.getShortName() != null ? createShortUrlDto.getShortName() : this.getUniqueShortName();
                    return toDto(this.shortUrlRepository.merge(
                            new ShortUrl(null, createShortUrlDto.getUrl(), shortName,
                                         createShortUrlDto.getUserId())));

                });
    }

    private ShortUrl getById(Long id, String userId) {
        var shortUrl = this.shortUrlRepository.findByIdAndUserId(id, userId);
        if (shortUrl == null) {
            throw new EntityNotFoundException(notFoundByIdMessage(id));
        }

        return shortUrl;
    }

    private static ShortUrlDto toDto(ShortUrl shortUrl) {
        return new ShortUrlDto(shortUrl.getShortName(), shortUrl.getUrl(), shortUrl.getId(), shortUrl.getUserId());
    }

    private String getUniqueShortName() {
        String generatedName;

        do {
            generatedName = this.nameGenerator.generateName(this.generatedNameLength);
        } while (this.shortUrlRepository.countByShortName(generatedName) != 0L);

        return generatedName;
    }

    private static String notFoundByIdMessage(Long id) {
        return "Short url with id " + id + " not found";
    }

    private static String notFoundByShortNameMessage(String shortName) {
        return "Short url with name " + shortName + " not found";
    }

    private static String shortUrlAlreadyExistsMessage(String shortName) {
        return "Short url with name " + shortName + " already exists";
    }
}
