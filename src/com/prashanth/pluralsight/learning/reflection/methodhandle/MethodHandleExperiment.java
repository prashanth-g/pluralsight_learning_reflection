package com.prashanth.pluralsight.learning.reflection.methodhandle;


import com.prashanth.pluralsight.learning.reflection.methodhandle.model.Person;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

public class MethodHandleExperiment {
    public static void main(String[] args) throws Throwable {

        Lookup lookup = MethodHandles.lookup();

        MethodType emptyConstructorMethodType = MethodType.methodType(void.class);
        MethodHandle emptyConstructor = lookup.findConstructor(Person.class, emptyConstructorMethodType);

        Person p1 = (Person) emptyConstructor.invoke();
        System.out.println(p1);

        MethodType cnstructorMethodType = MethodType.methodType(void.class, String.class, int.class);
        MethodHandle constructor = lookup.findConstructor(Person.class, cnstructorMethodType);

        Person p2 = (Person) constructor.invoke("Xee", 21);
        System.out.println(p2);

        MethodType nameGetterMethodType = MethodType.methodType(String.class);
        MethodHandle getName = lookup.findVirtual(Person.class, "getName", nameGetterMethodType);

        String name = (String) getName.invoke(p2);
        System.out.println(name);

        MethodType nameSetterMethodType = MethodType.methodType(void.class, String.class);
        MethodHandle setName = lookup.findVirtual(Person.class, "setName", nameSetterMethodType);

        setName.invoke(p2, "Ping");
        System.out.println(p2);

    }
}
