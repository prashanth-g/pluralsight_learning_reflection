package com.prashanth.pluralsight.learning.reflection.metamodel.orm;

import java.sql.SQLException;

public interface EntityManager<T> {

    static <T> EntityManager<T> of(Class<T> clss) {
        return new EntityManagerImpl<>();
    }

    void persist(T t) throws SQLException, IllegalAccessException;
}
