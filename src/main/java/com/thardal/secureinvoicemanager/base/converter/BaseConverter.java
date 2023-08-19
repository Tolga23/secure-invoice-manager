package com.thardal.secureinvoicemanager.base.converter;

import java.util.List;

public interface BaseConverter<E,D> {
    D toDto(E e);
    E toEntity(D d);
    List<E> toEntityList(List<D> d);
    List<D> toDtoList(List<E> e);
}
