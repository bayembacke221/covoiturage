package com.covoiturage.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {
    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T findById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(entityClass, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        }
    }

    @Override
    public void save(T entity) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
        }
    }
}
