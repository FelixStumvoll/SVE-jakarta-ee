package com.urlshortener.dal.repositories.impl

import com.urlshortener.dal.entities.User
import com.urlshortener.dal.repositories.UserRepository
import javax.enterprise.context.RequestScoped
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@RequestScoped
class UserRepositoryImpl(@PersistenceContext private val em: EntityManager) : UserRepository{
    override fun merge(user: User): User = em.merge(user)

    override fun deleteById(id: Long) {
        em.createQuery("delete from User u where u.id = :id").run {
            setParameter("id", id)
            executeUpdate()
        }
    }

    override fun findByName(name: String): User? =
        em.createQuery("select u from User u where u.name = :name", User::class.java).run {
            setParameter("name", name)
            singleResult
        }

    override fun findById(userId: Long): User? = em.find(User::class.java, userId)

    override fun existsByName(name: String): Boolean =
        em.createQuery("select count(u) from User u where u.name = :name", java.lang.Long::class.java)
            .run {
                setParameter("name", name)
                singleResult.toLong() != 0L
            }
}