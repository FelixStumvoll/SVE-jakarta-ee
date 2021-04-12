package org.urlshortener.dal.repositories.impl;

import lombok.NonNull;
import org.urlshortener.dal.entities.ShortUrl;
import org.urlshortener.dal.repositories.ShortUrlRepository;

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
    public void deleteById(@NonNull Long id, @NonNull String userId) {
        var query = this.em.createQuery("delete from ShortUrl s where s.id = :id and s.userId = :userId");
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public Long countByShortName(@NonNull String shortName) {
        var query = this.em
                .createQuery("select count(s) from ShortUrl s where s.shortName = :shortName", Long.class);
        query.setParameter("shortName", shortName);
        return query.getSingleResult();
    }

    @Override
    public ShortUrl findByShortName(@NonNull String shortName) {
        var query = this.em
                .createQuery("select s from ShortUrl s where s.shortName = :shortName", ShortUrl.class);
        query.setParameter("shortName", shortName);
        return query.getSingleResult();
    }

    @Override
    public ShortUrl findByIdAndUserId(Long id, String userId) {
        var query = this.em
                .createQuery("select s from ShortUrl s where s.id = :id and s.userId = :userid", ShortUrl.class);
        query.setParameter("id", id);
        query.setParameter("userid", userId);
        return query.getSingleResult();
    }

    @Override
    public boolean existsByIdAndUserId(Long id, String userId) {
        var query = this.em
                .createQuery("select count(s) from ShortUrl s where s.id = :id and s.userId = :userid", Long.class);
        query.setParameter("id", id);
        query.setParameter("userid", userId);
        return query.getSingleResult() != 0;
    }

    @Override
    public List<ShortUrl> findAllByUserId(String userId) {
        var query =
                this.em.createQuery("select s from ShortUrl s where s.userId = :userid", ShortUrl.class);
        query.setParameter("userid", userId);
        return query.getResultList();
    }
}
