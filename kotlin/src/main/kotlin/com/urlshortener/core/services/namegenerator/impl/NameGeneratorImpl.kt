package com.urlshortener.core.services.namegenerator.impl

import com.urlshortener.core.services.namegenerator.NameGenerator
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class NameGeneratorImpl : NameGenerator {
    private final val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun generateName(length: Int): String =
        (1..length)
            .map { charPool.random() }
            .joinToString("")
}