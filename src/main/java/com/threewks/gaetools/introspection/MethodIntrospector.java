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
import jodd.paramo.MethodParameter;
import jodd.paramo.Paramo;
import jodd.paramo.ParamoException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodIntrospector {
    protected AccessibleObject methodOrCtor;
    protected List<ParameterDescription> parameterDescriptions;
    protected List<String> names;
    protected List<Type> types;
    protected List<Class<?>> classes;

    public MethodIntrospector(AccessibleObject methodOrCtor) {
        super();
        this.methodOrCtor = methodOrCtor;
        this.classes = getClasses(methodOrCtor);
        this.types = getGenericParameterTypes(methodOrCtor);
        this.names = getParameterNames(methodOrCtor);
        this.parameterDescriptions = mergeToParameters(names, types);

    }

    public List<String> getNames() {
        return names;
    }

    public String getName(int index) {
        return names.get(index);
    }

    public List<Type> getTypes() {
        return types;
    }

    public Type getType(int index) {
        return types.get(index);
    }

    public List<Class<?>> getClassTypes() {
        return classes;
    }

    public Class<?> getClassType(int index) {
        return classes.get(index);
    }

    public List<ParameterDescription> getParameterDescriptions() {
        return parameterDescriptions;
    }

    private List<ParameterDescription> mergeToParameters(List<String> names, List<Type> types) {
        List<ParameterDescription> parameters = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            parameters.add(new ParameterDescription(names.get(i), types.get(i)));
        }
        return parameters;
    }

    private List<Type> getGenericParameterTypes(AccessibleObject methodOrCtor) {
        Method method = Cast.as(methodOrCtor, Method.class);
        Constructor<?> ctor = Cast.as(methodOrCtor, Constructor.class);
        return Arrays.asList(method != null ? method.getGenericParameterTypes() : ctor.getGenericParameterTypes());
    }

    private List<Class<?>> getClasses(AccessibleObject methodOrCtor) {
        Method method = Cast.as(methodOrCtor, Method.class);
        Constructor<?> ctor = Cast.as(methodOrCtor, Constructor.class);
        Class<?>[] parameters = method != null ? method.getParameterTypes() : ctor.getParameterTypes();
        return Arrays.asList(parameters);
    }

    /**
     * Paramo fails if there are no parameters (for example, a default ctor)
     *
     * @param methodOrCtor
     * @return
     */
    private List<String> getParameterNames(AccessibleObject methodOrCtor) {
        // TODO - Under JDK 8 we should be able to do this without paramo.
        try {
            MethodParameter[] parameterNames = Paramo.resolveParameters(methodOrCtor);
            List<String> names = new ArrayList<>(parameterNames.length);
            for (MethodParameter parameterName : parameterNames) {
                names.add(parameterName.getName());
            }
            return names;
        } catch (ParamoException e) {
            // parameter names are only available when the classes were debug compiled - so if you use a library or JDK class, Paramo cannot help
            Method method = Cast.as(methodOrCtor, Method.class);
            Constructor<?> constructor = Cast.as(methodOrCtor, Constructor.class);
            int parameterCount = method == null ? constructor.getParameterTypes().length : method.getParameterTypes().length;
            List<String> names = new ArrayList<>(parameterCount);
            for (int i = 0; i < parameterCount; i++) {
                names.add("Unknown");
            }
            return names;
        }
    }
}
