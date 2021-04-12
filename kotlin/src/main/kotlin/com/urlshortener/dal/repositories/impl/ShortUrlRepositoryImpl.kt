@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.urlshortener.dal.repositories.impl

import com.urlshortener.dal.entities.ShortUrl
import com.urlshortener.dal.repositories.ShortUrlRepository
import javax.enterprise.context.RequestScoped
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.lang.Long as JavaLong

@RequestScoped
class ShortUrlRepositoryImpl(@PersistenceContext private val em: EntityManager) : ShortUrlRepository {
    override fun merge(shortUrl: ShortUrl): ShortUrl = em.merge(shortUrl)

    override fun deleteById(id: Long, userId: String) {
        em.createQuery("delete from ShortUrl s where s.id = :id and s.userId = :userId").run {
            setParameter("id", id)
            setParameter("userId", userId)
            executeUpdate()
        }
    }

    override fun countByShortName(shortName: String): Long =
        em.createQuery("select count(s) from ShortUrl s where s.shortName = :shortName", JavaLong::class.java).run {
            setParameter("shortName", shortName)
            singleResult.toLong()
        }

    override fun findByShortName(shortName: String): ShortUrl? =
        em.createQuery("select s from ShortUrl s where s.shortName = :shortName", ShortUrl::class.java).run {
            setParameter("shortName", shortName)
            singleResult
        }

    override fun findByIdAndUserId(id: Long, userId: String): ShortUrl? =
        em.createQuery("select s from ShortUrl s where s.id = :id and s.userId = :userid", ShortUrl::class.java).run {
            setParameter("id", id)
            setParameter("userid", userId)
            singleResult
        }

    override fun findAllByUserId(userId: String): List<ShortUrl> =
        em.createQuery("select s from ShortUrl s where s.userId = :userid", ShortUrl::class.java).run {
            setParameter("userid", userId)
            resultList.toList()
        }
}