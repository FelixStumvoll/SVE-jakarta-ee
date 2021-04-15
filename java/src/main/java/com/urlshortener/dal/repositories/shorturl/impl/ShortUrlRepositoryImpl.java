package com.urlshortener.dal.repositories.shorturl.impl;

import com.urlshortener.dal.entities.ShortUrl;
import com.urlshortener.dal.repositories.shorturl.ShortUrlRepository;
import com.urlshortener.dal.util.TypedQueryUtils;
import lombok.NonNull;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequestScoped
public class ShortUrlRepositoryImpl implements ShortUrlRepository {

    @PersistenceContext
    private final EntityManager em;

    public ShortUrlRepositoryImpl(EntityManager entityManager) {this.em = entityManager;}

    @Override
    public ShortUrl merge(@NonNull ShortUrl shortUrl) {
        return this.em.merge(shortUrl);
    }

    @Override
    public void deleteById(long id, long userId) {
        var query = this.em.createQuery("delete from ShortUrl s where s.id = :id and s.user.id = :userId");
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public Long countByShortName(@NonNull String shortName) {
        var query = this.em
                .createQuery("select count(s) from ShortUrl s where s.shortName = :shortName", Long.class);
        query.setParameter("shortName", shortName);
        return TypedQueryUtils.singleResult(query);
    }

    @Override
    public ShortUrl findByShortName(@NonNull String shortName) {
        var query = this.em
                .createQuery("select s from ShortUrl s where s.shortName = :shortName", ShortUrl.class);
        query.setParameter("shortName", shortName);
        return TypedQueryUtils.singleResult(query);
    }

    @Override
    public ShortUrl findByIdAndUserId(long id, long userId) {
        var query = this.em
                .createQuery("select s from ShortUrl s where s.id = :id and s.user.id = :userid", ShortUrl.class);
        query.setParameter("id", id);
        query.setParameter("userid", userId);
        return TypedQueryUtils.singleResult(query);
    }

    @Override
    public List<ShortUrl> findAllForUser(long userId) {
        var query =
                this.em.createQuery("select s from ShortUrl s where s.user.id = :userid", ShortUrl.class);
        query.setParameter("userid", userId);
        return query.getResultList();
    }
}
