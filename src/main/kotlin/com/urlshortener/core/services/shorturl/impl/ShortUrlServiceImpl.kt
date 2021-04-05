package com.urlshortener.core.services.shorturl.impl

import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.exceptions.EntityModificationException
import com.urlshortener.core.exceptions.EntityNotFoundException
import com.urlshortener.core.services.namegenerator.NameGenerator
import com.urlshortener.core.services.shorturl.ShortUrlService
import com.urlshortener.dal.entities.ShortUrl
import com.urlshortener.dal.repositories.ShortUrlRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional

@RequestScoped
@Transactional
class ShortUrlServiceImpl(
    @Inject private val shortUrlRepository: ShortUrlRepository,
    @Inject private val nameGenerator: NameGenerator,
    @ConfigProperty(name = "urlshortener.generatedNameLength") private var generatedNameLength: Int
) : ShortUrlService {
    override fun findByShortname(shortName: String): ShortUrlDto =
        shortUrlRepository.findByShortName(shortName)?.toDto() ?: throw EntityNotFoundException(
            notFoundByShortNameMessage(shortName)
        )

    override fun findAll(userId: String): List<ShortUrlDto> =
        shortUrlRepository.findAllByUserId(userId).map { it.toDto() }

    private fun getById(id: Long, userId: String): ShortUrl =
        shortUrlRepository.findByIdAndUserId(id, userId) ?: throw EntityNotFoundException(
            notFoundByIdMessage(
                id
            )
        )

    override fun findById(id: Long, userId: String) = getById(id, userId).toDto()

    override fun update(entity: UpdateShortUrlDto): ShortUrlDto {
        val persistedEntity = getById(entity.id, entity.userId)

        entity.shortName?.let {
            persistedEntity.shortName = it
        }

        entity.url?.let {
            persistedEntity.url = it
        }

        return shortUrlRepository.merge(persistedEntity).toDto()
    }

    override fun delete(id: Long, userId: String) =
        if (shortUrlRepository.existsByIdAndUserId(id, userId)) {
            shortUrlRepository.deleteById(id)
        } else throw EntityNotFoundException(notFoundByIdMessage(id))

    override fun create(createShortUrlDto: CreateShortUrlDto): ShortUrlDto =
        if (createShortUrlDto.shortName != null && shortUrlRepository.countByShortName(createShortUrlDto.shortName) != 0L)
            throw EntityModificationException(alreadyExistsMessage)
        else
            shortUrlRepository.merge(
                ShortUrl(
                    createShortUrlDto.url,
                    createShortUrlDto.shortName ?: getUniqueShortName(),
                    createShortUrlDto.userId
                )
            ).toDto()

    private fun ShortUrl.toDto(): ShortUrlDto = ShortUrlDto(shortName, url, id!!, userId)

    private fun getUniqueShortName(): String {
        val nameGenerator = nameGenerator::generateName
        var generatedName = nameGenerator(generatedNameLength)

        while (shortUrlRepository.countByShortName(generatedName) != 0L) {
            generatedName = nameGenerator(generatedNameLength)
        }

        return generatedName
    }

    companion object {
        private fun notFoundByIdMessage(id: Long) = "ShortUrl with id $id not found"
        private fun notFoundByShortNameMessage(shortName: String) = "ShortUrl with name $shortName not found"
        private const val alreadyExistsMessage = "ShortUrl with same shortName already exists"
    }
}