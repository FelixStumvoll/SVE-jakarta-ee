package com.urlshortener.core.services.shorturl.impl

import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.exceptions.EntityNotFoundException
import com.urlshortener.core.services.namegenerator.NameGenerator
import com.urlshortener.core.services.shorturl.ShortUrlService
import com.urlshortener.core.util.withUniqueConstraintHandling
import com.urlshortener.dal.entities.ShortUrl
import com.urlshortener.dal.entities.shortNameConstraint
import com.urlshortener.dal.repositories.ShortUrlRepository
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.transaction.UserTransaction

@RequestScoped
class ShortUrlServiceImpl(
    @Inject private val shortUrlRepository: ShortUrlRepository,
    @Inject private val nameGenerator: NameGenerator,
    @Inject private val transaction: UserTransaction,
) : ShortUrlService {

    @ConfigProperty(name = "urlshortener.generatedNameLength")
    private var generatedNameLength: Int = 0

    @Transactional
    override fun findByShortname(shortName: String): ShortUrlDto =
        shortUrlRepository.findByShortName(shortName)?.toDto() ?: throw EntityNotFoundException(
            notFoundByShortNameMessage(shortName)
        )

    @Transactional
    override fun findAll(userId: String): List<ShortUrlDto> =
        shortUrlRepository.findAllByUserId(userId).map { it.toDto() }

    @Transactional
    override fun findById(id: Long, userId: String) = getById(id, userId).toDto()

    override fun update(entity: UpdateShortUrlDto): ShortUrlDto =
        transaction.withUniqueConstraintHandling(shortNameConstraint, shortNameAlreadyExistsMessage(entity.shortName)) {
            val persistedEntity = getById(entity.id, entity.userId)

            entity.shortName?.let {
                persistedEntity.shortName = it
            }

            entity.url?.let {
                persistedEntity.url = it
            }

            persistedEntity
        }.toDto()

    override fun create(createShortUrlDto: CreateShortUrlDto): ShortUrlDto =
        transaction.withUniqueConstraintHandling(
            shortNameConstraint,
            shortNameAlreadyExistsMessage(createShortUrlDto.shortName)
        ) {
            shortUrlRepository.merge(
                ShortUrl(
                    createShortUrlDto.url,
                    createShortUrlDto.shortName ?: getUniqueShortName(),
                    createShortUrlDto.userId
                )
            )
        }.toDto()

    @Transactional
    override fun delete(id: Long, userId: String) =
        if (shortUrlRepository.existsByIdAndUserId(id, userId)) {
            shortUrlRepository.deleteById(id)
        } else throw EntityNotFoundException(notFoundByIdMessage(id))

    private fun getById(id: Long, userId: String): ShortUrl =
        shortUrlRepository.findByIdAndUserId(id, userId) ?: throw EntityNotFoundException(
            notFoundByIdMessage(
                id
            )
        )

    private fun ShortUrl.toDto(): ShortUrlDto = ShortUrlDto(shortName, url, id!!, userId)

    private fun getUniqueShortName(): String {
        var generatedName: String

        do {
            generatedName = nameGenerator.generateName(generatedNameLength)
        } while (shortUrlRepository.countByShortName(generatedName) != 0L)

        return generatedName
    }

    companion object {
        private fun notFoundByIdMessage(id: Long) = "Short url with id $id not found"
        private fun notFoundByShortNameMessage(shortName: String) = "Short url with name $shortName not found"
        private fun shortNameAlreadyExistsMessage(shortName: String?) =
            "Short url with name $shortName already exists"
    }
}