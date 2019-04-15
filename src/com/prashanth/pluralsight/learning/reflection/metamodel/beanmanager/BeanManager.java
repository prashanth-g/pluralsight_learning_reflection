package com.prashanth.pluralsight.learning.reflection.metamodel.beanmanager;

import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Inject;
import com.prashanth.pluralsight.learning.reflection.metamodel.annotation.Provides;
import com.prashanth.pluralsight.learning.reflection.metamodel.provider.H2ConnectionProvider;
import com.sun.tools.javac.util.List;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BeanManager {

    private static BeanManager instance = new BeanManager();

    private Map<Class<?>, Supplier<?>> registry = new HashMap<>();

    public static BeanManager getInstance() {
        return instance;
    }

    public BeanManager() {
        List<Class<?>> classes = List.of(H2ConnectionProvider.class);
        for (Class<?> clss : classes) {
            Method[] methods = clss.getDeclaredMethods();
            for (Method method : methods) {
                Provides provides = method.getAnnotation(Provides.class);
                if(provides != null) {
                    Class<?> returnType = method.getReturnType();
                    Supplier<?> supplier = () -> {
                        try {
                          if(!Modifier.isStatic(method.getModifiers())) {
                              Object obj = clss.getConstructor().newInstance();
                              return method.invoke(obj);
                          } else {
                              return method.invoke(null);
                          }
                        } catch (Exception  e) {
                            throw new RuntimeException(e);
                        }
                    };
                    registry.put(returnType, supplier);
                }
            }
        }
    }

    public <T> T getInstance(Class<T> clss) {

        try {
            T t = clss.getConstructor().newInstance();
            Field[] declaredFields = clss.getDeclaredFields();
            for (Field field: declaredFields) {
                Inject inject = field.getAnnotation(Inject.class);
                if(inject != null) {
                    Class<?> injectedFieldType = field.getType();
                    Supplier<?> supplier = registry.get(injectedFieldType);
                    Object object = supplier.get();
                    field.setAccessible(true);
                    field.set(t, object);
                }
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

