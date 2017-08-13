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

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.Expressive;
import jodd.introspector.ClassDescriptor;
import jodd.introspector.CtorDescriptor;
import jodd.util.ClassUtil;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//import javax.inject.Inject;

/**
 * Provides reflective information on a {@link Class}.
 *
 * @see TypeIntrospector
 */
public class ClassIntrospector {
    public static final boolean supportsInjection = TypeIntrospector.classExists("javax.inject.Inject");

    @SuppressWarnings("unchecked")
    public <T> List<Constructor<T>> listConstructors(Class<T> type) {
        ClassDescriptor classDescriptor = new ClassDescriptor(type, false, false, true, null);
        List<Constructor<T>> ctors = new ArrayList<>();
        for (CtorDescriptor desc : classDescriptor.getAllCtorDescriptors()) {
            ctors.add(desc.getConstructor());
        }
        ctors.sort(new ConstructorComparator());
        return ctors;
    }

    public <T> List<Method> listSetters(Class<T> type) {
        Method[] methods = ClassUtil.getSupportedMethods(type);
        List<Method> setters = new ArrayList<>();
        for (Method method : methods) {
            if (ClassUtil.getBeanPropertySetterName(method) != null) {
                setters.add(method);
            }
        }
        return setters;
    }

    public <T> List<Field> listFields(Class<T> type) {
        return Arrays.asList(ClassUtil.getSupportedFields(type));
    }

    public List<Class<?>> listImplementedTypes(Class<?> type) {
        EList<Class<?>> types = Expressive.list(type);
        types.addItems(ClassUtils.getAllSuperclasses(type));
        types.addItems(ClassUtils.getAllInterfaces(type));
        return types;
    }

    public Method getMethod(Class<?> type, String methodName) {
        for (Method method : listMethods(type)) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    // TODO - NAO - Isolate dependencies on ReflectUtil and other introspection magic to
    // just this package.
    public List<Method> listMethods(Class<?> type) {
        return Arrays.asList(ClassUtil.getSupportedMethods(type));
    }

    @SuppressWarnings("rawtypes")
    static class ConstructorComparator implements Comparator<Constructor> {
        @Override
        public int compare(Constructor o1, Constructor o2) {
            Class<?>[] types1 = o1.getParameterTypes();
            Class<?>[] types2 = o2.getParameterTypes();
            int compare = new Integer(types1.length).compareTo(types2.length);
            if (compare == 0) {
                // to keep the outcome consistent, we want to deterministically sort
                for (int i = 0; compare == 0 && i < types1.length; i++) {
                    compare = types1[i].getName().compareTo(types2[i].getName());
                }
            }
            return compare;
        }
    }
}
