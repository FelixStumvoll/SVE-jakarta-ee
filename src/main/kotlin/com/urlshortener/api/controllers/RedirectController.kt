package com.urlshortener.api.controllers

import com.urlshortener.core.services.shorturl.ShortUrlService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping
class RedirectController(private val shortUrlService: ShortUrlService) {
    @GetMapping("/{shortName}")
    fun redirectTo(response: HttpServletResponse, @PathVariable("shortName") shortName: String) {
        val shortUrl = shortUrlService.findByShortname(shortName)
        response.status = HttpStatus.TEMPORARY_REDIRECT.value()
        response.addHeader("Location", shortUrl.url)
    }
}