package com.urlshortener.core.services.user.impl

import com.urlshortener.core.dtos.UserDto
import com.urlshortener.core.exceptions.EntityNotFoundException
import com.urlshortener.core.services.user.UserService
import com.urlshortener.core.util.withUniqueConstraintHandling
import com.urlshortener.dal.entities.User
import com.urlshortener.dal.entities.userNameConstraint
import com.urlshortener.dal.repositories.UserRepository
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.transaction.UserTransaction

@RequestScoped
class UserServiceImpl(
    @Inject private val userRepository: UserRepository,
    @Inject private val userTransaction: UserTransaction
) : UserService {
    @Transactional
    override fun findByName(name: String): UserDto =
        userRepository.findByName(name)?.toDto() ?: throw EntityNotFoundException(
            notFoundByNameMessage(name)
        )

    @Transactional
    override fun findById(id: Long): UserDto? =
        userRepository.findById(id)?.toDto()

    @Transactional
    override fun delete(id: Long) =
        userRepository.deleteById(id)

    override fun create(userDto: UserDto): UserDto =
        userTransaction.withUniqueConstraintHandling(userNameConstraint, alreadyExistsMessage(userDto.name)) {
            userRepository.merge(
                User(
                    userDto.name,
                    userDto.role,
                    userDto.password,
                    0
                )
            )
        }.toDto()

    override fun authenticate(userDto: UserDto, password: String): Boolean = userDto.password == password

    private fun User.toDto(): UserDto = UserDto(name, role, password, id!!)

    companion object {
        private fun notFoundByNameMessage(name: String) = "User with name $name not found"
        private fun alreadyExistsMessage(name: String) = "User with name $name already exists"
    }
}