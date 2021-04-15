package com.urlshortener.core.shorturl.impl;

import com.urlshortener.core.dtos.CreateShortUrlDto;
import com.urlshortener.core.dtos.ShortUrlDto;
import com.urlshortener.core.dtos.UpdateShortUrlDto;
import com.urlshortener.core.exceptions.EntityNotFoundException;
import com.urlshortener.core.namegenerator.NameGenerator;
import com.urlshortener.core.shorturl.ShortUrlService;
import com.urlshortener.dal.entities.Constraints;
import com.urlshortener.dal.entities.ShortUrl;
import com.urlshortener.dal.repositories.shorturl.ShortUrlRepository;
import com.urlshortener.dal.repositories.user.UserRepository;
import lombok.NonNull;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.stream.Collectors;

import static com.urlshortener.core.util.TransactionUtils.withUniqueConstraintHandling;

@RequestScoped
public class ShortUrlServiceImpl implements ShortUrlService {

    final ShortUrlRepository shortUrlRepository;

    final UserRepository userRepository;

    final NameGenerator nameGenerator;

    final UserTransaction transaction;

    @ConfigProperty(name = "urlshortener.generatedNameLength")
    int generatedNameLength;

    @Inject
    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, UserRepository userRepository, NameGenerator nameGenerator, UserTransaction transaction) {
        this.shortUrlRepository = shortUrlRepository;
        this.userRepository = userRepository;
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
    public List<ShortUrlDto> findAll(long userId) {
        var shortUrls = this.shortUrlRepository.findAllForUser(userId);
        return shortUrls.stream().map(ShortUrlServiceImpl::toDto).collect(Collectors.toList());
    }

    @Override
    public ShortUrlDto findById(long id, long userId) {
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
    public void delete(long id, long userId) {
        this.shortUrlRepository.deleteById(id, userId);
    }

    @Override
    public ShortUrlDto create(@NonNull CreateShortUrlDto createShortUrlDto) {
        return withUniqueConstraintHandling(this.transaction, Constraints.shortUrlConstraint,
                                            shortUrlAlreadyExistsMessage(createShortUrlDto.getShortName()), () -> {
                    var shortName = createShortUrlDto.getShortName() != null ? createShortUrlDto.getShortName() : this.getUniqueShortName();
                    var user = this.userRepository.findById(createShortUrlDto.getUserId());

                    if (user == null) {
                        throw new EntityNotFoundException(userNotFoundByIdMessage(createShortUrlDto.getUserId()));
                    }

                    return toDto(this.shortUrlRepository.merge(
                            new ShortUrl(null, createShortUrlDto.getUrl(), shortName, user)));

                });
    }

    private ShortUrl getById(long id, long userId) {
        var shortUrl = this.shortUrlRepository.findByIdAndUserId(id, userId);
        if (shortUrl == null) {
            throw new EntityNotFoundException(notFoundByIdMessage(id));
        }

        return shortUrl;
    }

    private static ShortUrlDto toDto(ShortUrl shortUrl) {
        return new ShortUrlDto(shortUrl.getShortName(), shortUrl.getUrl(), shortUrl.getId(),
                               shortUrl.getUser().getId());
    }

    private String getUniqueShortName() {
        String generatedName;

        do {
            generatedName = this.nameGenerator.generateName(this.generatedNameLength);
        } while (this.shortUrlRepository.countByShortName(generatedName) != 0L);

        return generatedName;
    }

    private static String notFoundByIdMessage(long id) {
        return "Short url with id " + id + " not found";
    }

    private static String notFoundByShortNameMessage(String shortName) {
        return "Short url with name " + shortName + " not found";
    }

    private static String shortUrlAlreadyExistsMessage(String shortName) {
        return "Short url with name " + shortName + " already exists";
    }

    private static String userNotFoundByIdMessage(long id) {
        return "user with id " + id + " not found";
    }
}
