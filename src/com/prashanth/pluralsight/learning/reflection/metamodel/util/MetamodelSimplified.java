package com.prashanth.pluralsight.learning.reflection.metamodel.util;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Column;
import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MetamodelSimplified {

    private Class<?> clss;

    public static MetamodelSimplified of(Class<?> clss) {
        return new MetamodelSimplified(clss);
    }

    public MetamodelSimplified(Class<?> clss) {
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

    public String buildInsertRequest() {
        String columnElements = buildColumnNames();
        String parameters = buildQuestionMarkElements();
        return "insert into " + this.clss.getSimpleName() + "(" + columnElements + ") values (" + parameters + ")" ;
    }

    private String buildQuestionMarkElements() {
        int numberOfColumns = getColumns().size() + 1;
        return IntStream.range(0, numberOfColumns)
                .mapToObj(index -> "?")
                .collect(Collectors.joining(", "));
    }

    private String buildColumnNames() {
        String primaryKeyName = getPrimaryKey().getName();
        List<String> columnNames = getColumns().stream()
                .map(ColumnField::getName)
                .collect(Collectors.toList());
        columnNames.add(0 , primaryKeyName);
        return String.join(", ", columnNames);
    }
}
