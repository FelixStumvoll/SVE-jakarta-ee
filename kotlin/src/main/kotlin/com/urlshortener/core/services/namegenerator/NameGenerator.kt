package com.urlshortener.core.services.namegenerator

interface NameGenerator {
    fun generateName(length: Int): String
}