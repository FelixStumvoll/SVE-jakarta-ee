package com.urlshortener.api.controllers

import com.urlshortener.ShortUrlConfiguration
import com.urlshortener.api.controllers.util.getUserIdFromToken
import com.urlshortener.api.dtos.ApiCreateShortUrlDto
import com.urlshortener.api.dtos.ApiUpdateShortUrlDto
import com.urlshortener.core.dtos.CreateShortUrlDto
import com.urlshortener.core.dtos.ShortUrlDto
import com.urlshortener.core.dtos.UpdateShortUrlDto
import com.urlshortener.core.services.shorturl.ShortUrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

const val AuthHeader = "Authentication"

@RestController
@Validated
@RequestMapping("/short-url")
class ShortUrlController(
    private val shortUrlService: ShortUrlService,
    private val config: ShortUrlConfiguration
) {
    @GetMapping
    fun getAll(@RequestHeader(AuthHeader) authHeader: String): List<ShortUrlDto> =
        shortUrlService.findAll(getUserIdFromToken(authHeader, config.authSecret))

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
        @RequestHeader(AuthHeader) userId: String
    ): ResponseEntity<ShortUrlDto> =
        ok(shortUrlService.findById(id, getUserIdFromToken(userId, config.authSecret)))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody createShortUrlDto: ApiCreateShortUrlDto,
        @RequestHeader(AuthHeader) userId: String,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ShortUrlDto> =
        shortUrlService.create(
            CreateShortUrlDto(
                createShortUrlDto.shortName,
                createShortUrlDto.url,
                getUserIdFromToken(userId, config.authSecret)
            )
        )
            .let {
                created(uriComponentsBuilder.path("/short-url/{id}").build(it.id)).body(it)
            }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @RequestBody updateDto: ApiUpdateShortUrlDto,
        @PathVariable id: Long,
        @RequestHeader(AuthHeader) userId: String
    ): ResponseEntity<Unit> {
        shortUrlService.update(
            UpdateShortUrlDto(
                updateDto.shortName,
                updateDto.url,
                id,
                getUserIdFromToken(userId, config.authSecret)
            )
        )
        return ok().build()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun delete(
        @PathVariable id: Long,
        @RequestHeader(AuthHeader) userId: String
    ): ResponseEntity<Unit> {
        shortUrlService.delete(id, getUserIdFromToken(userId, config.authSecret))
        return ok().build()
    }
}