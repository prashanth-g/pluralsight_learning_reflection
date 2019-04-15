package com.prashanth.pluralsight.learning.reflection.metamodel;

import com.prashanth.pluralsight.learning.reflection.metamodel.beanmanager.BeanManager;
import com.prashanth.pluralsight.learning.reflection.metamodel.model.Person;
import com.prashanth.pluralsight.learning.reflection.metamodel.orm.EntityManager;
import com.prashanth.pluralsight.learning.reflection.metamodel.orm.ManagedEntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ReadingObjects {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        BeanManager beanManager = BeanManager.getInstance();
        EntityManager<Person> entityManager = beanManager.getInstance(ManagedEntityManager.class);

        Person rabada = entityManager.find(Person.class, 1L);
        Person marcus = entityManager.find(Person.class, 2L);
        Person alex = entityManager.find(Person.class, 3L);
        Person azar = entityManager.find(Person.class, 4L);

        System.out.println(rabada);
        System.out.println(marcus);
        System.out.println(alex);
        System.out.println(azar);
    }
}
