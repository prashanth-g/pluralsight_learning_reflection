package com.prashanth.pluralsight.learning.reflection.metamodel;

import com.prashanth.pluralsight.learning.reflection.metamodel.model.Person;
import com.prashanth.pluralsight.learning.reflection.metamodel.orm.EntityManager;

import java.sql.SQLException;

public class WritingObjects {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person rabada = new Person("rabada", 23);
        Person marcus = new Person("marcus", 22);
        Person alex = new Person("alex", 24);
        Person azar = new Person("azar", 30);

        System.out.println(rabada);
        System.out.println(marcus);
        System.out.println(alex);
        System.out.println(azar);

        System.out.println("Writing to DB");

        entityManager.persist(rabada);
        entityManager.persist(marcus);
        entityManager.persist(alex);
        entityManager.persist(azar);

        System.out.println(rabada);
        System.out.println(marcus);
        System.out.println(alex);
        System.out.println(azar);
    }
}
