package com.prashanth.pluralsight.learning.reflection.metamodel.orm;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Inject;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.ColumnField;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.MetamodelSimplified;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

public class ManagedEntityManager<T> implements EntityManager<T> {

    private AtomicLong idGenerator = new AtomicLong(0L);

    @Inject
    Connection connection;

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {
        MetamodelSimplified metamodel = MetamodelSimplified.of(t.getClass());
        String sql = metamodel.buildInsertRequest();
        try(PreparedStatement statement = prepareStatementsWith(sql).andParameters(t)) {
            statement.executeUpdate();
        }
    }

    @Override
    public T find(Class<T> clss, long primaryKey) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        MetamodelSimplified metamodel = MetamodelSimplified.of(clss);
        String sql = metamodel.buildSelectRequest();
        try(PreparedStatement statement = prepareStatementsWith(sql).andPrimaryKey(primaryKey)) {
            ResultSet resultSet = statement.executeQuery();
            return buildInstanceFrom(clss, resultSet);
        }
    }

    private T buildInstanceFrom(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        MetamodelSimplified metamodelSimplified = MetamodelSimplified.of(clss);
        T t = clss.getConstructor().newInstance();
        Field primaryKeyField = metamodelSimplified.getPrimaryKey().getField();
        String primaryKeyName = metamodelSimplified.getPrimaryKey().getName();
        Class<?> primaryKeyType = metamodelSimplified.getPrimaryKey().getType();

        resultSet.next();
        if(primaryKeyType == long.class) {
            long primaryKey = resultSet.getInt(primaryKeyName);
            primaryKeyField.setAccessible(true);
            primaryKeyField.set(t, primaryKey);
        }

        for (ColumnField columnField: metamodelSimplified.getColumns()) {
            Field field = columnField.getField();
            field.setAccessible(true);
            Class<?> columnType = columnField.getType();
            String columnName = columnField.getName();
            if(columnType == int.class) {
                int value = resultSet.getInt(columnName);
                field.set(t, value);
            } else if(columnType == String.class){
                String value = resultSet.getString(columnName);
                field.set(t, value);
            }
        }
        return t;
    }

    private PreparedStatementWrapper prepareStatementsWith(String sql) throws SQLException {
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

        public PreparedStatement andPrimaryKey(Object primaryKey) throws SQLException {
            if (primaryKey.getClass() == Long.class) {
                statement.setLong(1, (Long) primaryKey);
            }
            return statement;
        }
    }
}
