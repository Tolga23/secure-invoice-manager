package com.thardal.secureinvoicemanager.base.service;

import com.thardal.secureinvoicemanager.base.entity.BaseEntity;
import com.thardal.secureinvoicemanager.base.exceptions.NotFoundException;
import com.thardal.secureinvoicemanager.user.enums.UserErrorMessages;
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

    public E getByIdWithControl(Long id) {
        Optional<E> entityOptional = findById(id);

        E entity;
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
        } else {
            throw new NotFoundException(UserErrorMessages.USER_NOT_FOUND);
        }

        return entity;
    }

}
