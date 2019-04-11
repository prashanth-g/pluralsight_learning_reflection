package com.prashanth.pluralsight.learning.reflection.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Reflection01 {

    public static void main(String[] args) throws ClassNotFoundException {

        String personClassName = "com.prashanth.pluralsight.learning.reflection.core.Person";
        Class<?> personClass = Class.forName(personClassName);

        System.out.println("Person class :" + personClass);

        final Field[] personClassFields = personClass.getFields();
        System.out.println("Fields : ");
        System.out.println(Arrays.toString(personClassFields));

        final Field[] personClassDeclaredFields = personClass.getDeclaredFields();
        System.out.println("Declared Fields : ");
        System.out.println(Arrays.toString(personClassDeclaredFields));

        final Method[] methods = personClass.getMethods();
        System.out.println("Methods : ");
        for (Method method: methods) {
            System.out.println(method);
        }

        final Method[] declaredMethods = personClass.getDeclaredMethods();
        System.out.println("Declared Methods : ");
        for (Method method: declaredMethods) {
            System.out.println(method);
        }

        System.out.println("Static Declared Methods : ");
        Arrays.stream(declaredMethods)
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .forEach(System.out::println);

    }
}
