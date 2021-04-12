package com.urlshortener.core.exceptions

class EntityModificationException(message: String) : RuntimeException(message)
class EntityNotFoundException(message: String) : RuntimeException(message)
class AuthenticationException(message: String): RuntimeException(message)