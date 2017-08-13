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

import com.atomicleopard.expressive.Cast;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides information about a method parameter.
 */
public class ParameterDescription {
    private String name;
    private Type type;

    public ParameterDescription(String name, Type type) {
        super();
        this.name = name;
        this.type = type;
    }

    /**
     * Returns true if this parameter is of the given type.
     *
     * @param is
     * @return
     */
    public boolean isA(Class<?> is) {
        Class<?> clazz = classType();
        return clazz != null && clazz.isAssignableFrom(is);
    }

    /**
     * Returns the type of the the generic argument at the given index, or null if this is not a
     * parameter of generic type.
     *
     * @param index
     * @return
     */
    public Type getGenericType(int index) {
        ParameterizedType pt = Cast.as(type, ParameterizedType.class);
        if (pt != null) {
            Type[] generics = pt.getActualTypeArguments();
            if (index < 0) {
                index = generics.length + index;
            }
            if (index < generics.length) {
                return generics[index];
            }
        }
        return null;
    }

    public Type getArrayType() {
        Class<?> clazz = Cast.as(type, Class.class);
        if (clazz != null) {
            return clazz.getComponentType();
        }
        GenericArrayType gat = Cast.as(type, GenericArrayType.class);
        if (gat != null) {
            return gat.getGenericComponentType();
        }
        return null;
    }

    public boolean isGeneric() {
        return TypeIntrospector.isGeneric(type);
    }

    public String name() {
        return name;
    }

    public Class<?> classType() {
        return TypeIntrospector.asClass(type);
    }

    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
