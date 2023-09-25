package com.thardal.secureinvoicemanager.base.service;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseEntityService<E extends BaseEntity, D extends JpaRepository<E, Long>> {

    private final D dao;

    public List<E> findAll() {
        return dao.findAll();
    }

    public Optional<E> findById(Long id) {
        return dao.findById(id);
    }

    public E save(E entity) {
        return dao.save(entity);
    }

    public E update(E entity) {
        return dao.save(entity);
    }

    public void delete(E entity) {
        dao.delete(entity);
    }

    public D getDao() {
        return dao;
    }

}
