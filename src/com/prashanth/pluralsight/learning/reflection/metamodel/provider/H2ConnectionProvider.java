package com.prashanth.pluralsight.learning.reflection.metamodel.provider;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Provides;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionProvider {

    @Provides
    public Connection buildConnection() throws SQLException {
        return DriverManager
                .getConnection("", "sa", "");
    }
}
