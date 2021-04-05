package com.urlshortener

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
@EnableConfigurationProperties(ShortUrlConfiguration::class)
class UrlShortenerApplication {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**").allowedMethods("*")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<UrlShortenerApplication>(*args)
}

@ConstructorBinding
@ConfigurationProperties("urlshortener")
data class ShortUrlConfiguration(val generatedNameLength: Int, val authSecret: String)