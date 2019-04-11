package com.prashanth.pluralsight.learning.reflection.metamodel;

import com.prashanth.pluralsight.learning.reflection.metamodel.model.Person;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.ColumnField;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.Metamodel;
import com.prashanth.pluralsight.learning.reflection.metamodel.util.PrimaryKeyField;

import java.util.List;

public class MetamodelReflection02 {
    public static void main(String[] args) {

        Metamodel metamodel = Metamodel.of(Person.class);
        PrimaryKeyField primaryKeyField = metamodel.getPrimaryKey();
        List<ColumnField> columnFields = metamodel.getColumns();

        System.out.println("Primary key name: " + primaryKeyField.getName() +
                ", Type: " + primaryKeyField.getType().getSimpleName());

        for (ColumnField columnField : columnFields) {
            System.out.println("Column name: " + columnField.getName() +
                    ", Type: " + columnField.getType().getSimpleName());
        }
    }
}
