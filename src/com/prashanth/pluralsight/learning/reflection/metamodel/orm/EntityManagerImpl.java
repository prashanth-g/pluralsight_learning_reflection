package com.prashanth.pluralsight.learning.reflection.metamodel.orm;

import com.prashanth.pluralsight.learning.reflection.metamodel.util.ColumnField;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.MetamodelSimplified;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

public class EntityManagerImpl<T> implements EntityManager<T> {

    private AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {
        MetamodelSimplified metamodel = MetamodelSimplified.of(t.getClass());
        String sql = metamodel.buildInsertRequest();
        PreparedStatement statement = prepareStatementsWith(sql).andParameters(t);
        statement.executeUpdate();
    }

    private PreparedStatementWrapper prepareStatementsWith(String sql) throws SQLException {
        Connection connection = DriverManager
                .getConnection("", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sql);
        return new PreparedStatementWrapper(statement);
    }

    private class PreparedStatementWrapper {
        private PreparedStatement statement;
        public PreparedStatementWrapper(PreparedStatement statement) {
            this.statement = statement;
        }

        public PreparedStatement andParameters(T t) throws SQLException, IllegalAccessException {
            MetamodelSimplified metamodelSimplified = MetamodelSimplified.of(t.getClass());
            Class<?> primaryKeyType = metamodelSimplified.getPrimaryKey().getType();
            if(primaryKeyType == long.class) {
                long id = idGenerator.incrementAndGet();
                statement.setLong(1, id);
                Field field = metamodelSimplified.getPrimaryKey().getField();
                field.setAccessible(true);
                field.set(t, id);
            }
            for(int columnIndex = 0 ; columnIndex < metamodelSimplified.getColumns().size(); columnIndex ++) {
                ColumnField columnField = metamodelSimplified.getColumns().get(columnIndex);
                Class<?> columnType = columnField.getType();
                Field field = columnField.getField();
                field.setAccessible(true);
                Object value = field.get(t);
                if(columnType == int.class) {
                    statement.setInt(columnIndex + 2, (int) value);
                } else if (columnType == String.class) {
                    statement.setString(columnIndex + 2, (String) value);
                }
            }
            return statement;
        }
    }
}
