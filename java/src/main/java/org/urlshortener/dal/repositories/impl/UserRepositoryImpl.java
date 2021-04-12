package org.urlshortener.dal.repositories.impl;

import org.urlshortener.dal.entities.User;
import org.urlshortener.dal.repositories.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public User merge(User user)  {
        return this.em.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        var query = this.em.createQuery("delete from User u where u.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public User findByName(String name) {
        var query = this.em
                .createQuery("select u from User u where u.name = :name", User.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public User findById(Long userId) { return this.em.find(User.class, userId); };

    @Override
    public boolean existsByName(String name) {
        var query = this.em
                .createQuery("select count(u) from User u where u.name = :name", Long.class);
        query.setParameter("name", name);
        return query.getSingleResult() != 0;
    }
}
