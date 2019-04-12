package com.prashanth.pluralsight.learning.reflection.metamodel.model;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Column;
import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.PrimaryKey;

public class Person {

    @PrimaryKey
    private long id;

    @Column
    private int age;

    @Column
    private String name;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(Long id, int age, String name) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
