package com.prashanth.pluralsight.learning.reflection.metamodel.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2EntityManager<T> extends AbstractEntityManager<T> {
    public Connection buildConnection() throws SQLException {
        return DriverManager
                .getConnection("", "sa", "");
    }
}
