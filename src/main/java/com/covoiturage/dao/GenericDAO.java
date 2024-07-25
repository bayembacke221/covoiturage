package com.covoiturage.dao;

import java.util.List;

public interface GenericDAO<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(T entity);
}
