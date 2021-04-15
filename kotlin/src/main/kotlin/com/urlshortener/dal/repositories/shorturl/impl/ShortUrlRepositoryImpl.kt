@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.urlshortener.dal.repositories.shorturl.impl

import com.urlshortener.dal.entities.ShortUrl
import com.urlshortener.dal.repositories.shorturl.ShortUrlRepository
import com.urlshortener.dal.util.singleResult
import javax.enterprise.context.RequestScoped
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.lang.Long as JavaLong

@RequestScoped
class ShortUrlRepositoryImpl(@PersistenceContext private val em: EntityManager) : ShortUrlRepository {
    override fun merge(shortUrl: ShortUrl): ShortUrl = em.merge(shortUrl)

    override fun deleteById(id: Long, userId: Long) {
        em.createQuery("delete from ShortUrl s where s.id = :id and s.user.id = :userId").run {
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
            singleResult()
        }

    override fun findByIdAndUserId(id: Long, userId: Long): ShortUrl? =
        em.createQuery("select s from ShortUrl s where s.id = :id and s.user.id = :userid", ShortUrl::class.java).run {
            setParameter("id", id)
            setParameter("userid", userId)
            singleResult()
        }

    override fun findAllForUser(userId: Long): List<ShortUrl> =
        em.createQuery("select s from ShortUrl s where s.user.id = :userid", ShortUrl::class.java).run {
            setParameter("userid", userId)
            resultList
        }
}