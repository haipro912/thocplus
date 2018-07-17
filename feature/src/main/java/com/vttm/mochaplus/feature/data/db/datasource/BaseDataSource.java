package com.vttm.mochaplus.feature.data.db.datasource;

import com.vttm.mochaplus.feature.data.db.datasource.exceptions.RepositoryException;

import java.util.List;

public interface BaseDataSource<T> {

    List<T> findAll();

    T insert(T element) throws RepositoryException;

    List<T> insertAll(List<T> elementList) throws RepositoryException;

    void remove(T element);

    void removeAll();
}
