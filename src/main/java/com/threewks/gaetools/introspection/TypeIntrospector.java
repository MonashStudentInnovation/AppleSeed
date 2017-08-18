/*
 * This file is a component of thundr, a software library from 3wks.
 * Read more: http://3wks.github.io/thundr/
 * Copyright (C) 2015 3wks, <thundr@3wks.com.au>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threewks.gaetools.introspection;

import com.atomicleopard.expressive.Expressive;
import jodd.util.ClassUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides convenience methods for determining information about {@link Class} and {@link Type} objects.
 *
 * @see ClassIntrospector
 */
public class TypeIntrospector {
    private static Map<Class<?>, Class<?>> primitiveTypes = primitiveTypes();
    private static Map<Class<?>, Class<?>> boxedTypes = boxedTypes(primitiveTypes);
    private static Set<Class<?>> NonJavabeanBasicClasses = nonJavabeanClasses(primitiveTypes);

    private TypeIntrospector() {
    }

    public static boolean isABasicType(Class<?> type) {
        return primitiveTypes.containsKey(type);
    }

    /**
     * @param type
     * @return
     */
    public static boolean isABoxedType(Class<?> type) {
        return boxedTypes.containsKey(type);
    }

    public static List<Class<?>> getPrimitiveTypes() {
        return new ArrayList<>(primitiveTypes.keySet());
    }

    public static List<Class<?>> getBoxedTypes() {
        return new ArrayList<>(boxedTypes.keySet());
    }

    @SuppressWarnings("rawtypes")
    public static boolean isACollection(Type type) {
        Class class1 = asClass(type);
        return Collection.class.isAssignableFrom(class1);
    }

    /**
     * Returns the type of collection, i.e. the generic type.
     * This only works on a Type obtained from field or method signature,
     * not a runtime type.
     *
     * @param type
     * @return the class type of the type in the collection, or null if it cannot be determined.
     */
    public static Class<?> getCollectionType(Type type) {
        return ClassUtil.getComponentType(type, 0);
    }

    /**
     * Converts a boxed type (Integer, Byte etc) to their unboxed counterparts (int, byte etc)
     *
     * @param type
     * @return
     */
    public static Class<?> unbox(Class<?> type) {
        return boxedTypes.get(type);
    }

    /**
     * Converts a unboxed type (int, byte etc) to their boxed counterparts (Integer, Byte etc)
     *
     * @param type
     * @return
     */
    public static Class<?> box(Class<?> type) {
        return primitiveTypes.get(type);
    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name, false, TypeIntrospector.class.getClassLoader());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * A basic to test to see is a type qualifies as a javabean.
     *
     * @param type
     * @return
     */
    public static boolean isAJavabean(Class<?> type) {
        try {
            // TODO - Given this is expensive, a cache would be a good idea.
            // newInstance is the only way to find default ctors if they exist

            // TODO - Should take into account a more extensive set of 'basic' types
            // TODO - Should take into account collection types
            return type != null && !NonJavabeanBasicClasses.contains(type) && !type.isEnum() && !type.isArray() && !type.isAnnotation() && (type.newInstance() != null);
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> asClass(Type type) {
        return ClassUtil.getRawType(type);
    }

    public static boolean isGeneric(Type type) {
        return ClassUtil.getComponentType(type, 0) != null;
    }

    private static Map<Class<?>, Class<?>> boxedTypes(Map<Class<?>, Class<?>> primitiveTypes2) {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveTypes2.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        return map;
    }

    private static Map<Class<?>, Class<?>> primitiveTypes() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        map.put(void.class, Void.class);
        return map;
    }

    private static Set<Class<?>> nonJavabeanClasses(Map<Class<?>, Class<?>> primitiveTypes) {
        Set<Class<?>> set = Expressive.set(Object.class, String.class);
        for (Map.Entry<Class<?>, Class<?>> entry : primitiveTypes.entrySet()) {
            set.add(entry.getKey());
            set.add(entry.getValue());
        }
        return set;
    }

    public static boolean canBoxOrUnbox(Class<?> type1, Class<?> type2) {
        return (primitiveTypes.containsKey(type1) && primitiveTypes.get(type1) == type2) ||
                (boxedTypes.containsKey(type1) && boxedTypes.get(type1) == type2);
    }
}
