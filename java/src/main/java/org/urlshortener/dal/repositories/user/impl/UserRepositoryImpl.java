package org.urlshortener.dal.repositories.user.impl;

import lombok.NonNull;
import org.urlshortener.dal.entities.User;
import org.urlshortener.dal.repositories.user.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.urlshortener.dal.util.TypedQueryUtils.singleResult;

@RequestScoped
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public User merge(User user) {
        return this.em.merge(user);
    }

    @Override
    public void deleteById(long id) {
        var query = this.em.createQuery("delete from User u where u.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public User findByName(@NonNull String name) {
        var query = this.em
                .createQuery("select u from User u where u.name = :name", User.class);
        query.setParameter("name", name);
        return singleResult(query);
    }

    @Override
    public User findById(long userId) { return this.em.find(User.class, userId); }

    @Override
    public boolean existsByName(@NonNull String name) {
        var query = this.em
                .createQuery("select count(u) from User u where u.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() != 0;
    }
}
