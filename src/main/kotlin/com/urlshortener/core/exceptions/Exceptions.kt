package com.urlshortener.core.exceptions

class EntityModificationException(message: String) : RuntimeException(message)
class EntityNotFoundException(message: String) : RuntimeException(message)
class ShortNameAlreadyExistsException(shortname: String): RuntimeException("Url with shortname $shortname already exists")
class AuthenticationException(message: String): RuntimeException(message)