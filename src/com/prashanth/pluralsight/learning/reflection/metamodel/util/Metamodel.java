package com.prashanth.pluralsight.learning.reflection.metamodel.util;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Column;
import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Metamodel<T> {

    private Class<T> clss;

    public static <T> Metamodel<T> of(Class<T> clss) {
        return new Metamodel<>(clss);
    }

    public Metamodel(Class<T> clss) {
        this.clss = clss;
    }

    public PrimaryKeyField getPrimaryKey() {
        Field[] declaredFields = clss.getDeclaredFields();
        for (Field field: declaredFields) {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if(primaryKey != null) {
                PrimaryKeyField primaryKeyField = new PrimaryKeyField(field);
                return primaryKeyField;
            }
        }
        throw new IllegalArgumentException("No primary key found for the class " + clss.getSimpleName());
    }

    public List<ColumnField> getColumns() {
        List<ColumnField> columnFields = new ArrayList<>();
        Field[] declaredFields = clss.getDeclaredFields();
        for (Field field: declaredFields) {
            Column column = field.getAnnotation(Column.class);
            if(column != null) {
                ColumnField columnField = new ColumnField(field);
                columnFields.add(columnField);
            }
        }
        return columnFields;
    }
}
