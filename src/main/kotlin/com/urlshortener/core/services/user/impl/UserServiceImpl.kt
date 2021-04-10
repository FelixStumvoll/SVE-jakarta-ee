package com.urlshortener.core.services.user.impl

import com.urlshortener.core.dtos.UserDto
import com.urlshortener.core.exceptions.EntityModificationException
import com.urlshortener.core.exceptions.EntityNotFoundException
import com.urlshortener.core.services.user.UserService
import com.urlshortener.dal.entities.User
import com.urlshortener.dal.repositories.UserRepository
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional

@RequestScoped
@Transactional
class UserServiceImpl (
    @Inject private val userRepository: UserRepository
) : UserService{
    override fun findByName(name: String): UserDto =
        userRepository.findByName(name)?.toDto() ?: throw EntityNotFoundException(
            UserServiceImpl.notFoundByNameMessage(name)
        )

    override fun findById(id: Long): UserDto? =
        userRepository.findById(id)?.toDto()

    override fun delete(id: Long) =
        userRepository.deleteById(id)

    override fun create(userDto: UserDto): UserDto =
        if(userRepository.existsByName(userDto.name))
            throw EntityModificationException(UserServiceImpl.alreadyExistsMessage)
        else
            userRepository.merge(
                User(
                    userDto.name,
                    userDto.role,
                    hashPassword(userDto.password),
                    0
                )
            ).toDto()

    override fun authenticate(userDto: UserDto, password: String): Boolean = userDto.password == hashPassword(password)

    private fun User.toDto(): UserDto = UserDto(name, role, password, 0, id!!)

    companion object {
        private fun hashPassword(password: String) = password //TODO
        private fun notFoundByNameMessage(shortName: String) = "User with name $shortName not found"
        private const val alreadyExistsMessage = "User with same name already exists"
    }
}