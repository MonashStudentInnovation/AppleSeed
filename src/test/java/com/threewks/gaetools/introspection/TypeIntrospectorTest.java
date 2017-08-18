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

import com.threewks.gaetools.introspection.ClassIntrospectorTest.NoDefaultCtor;
import com.threewks.gaetools.introspection.ClassIntrospectorTest.PrivateCtor;
import com.threewks.gaetools.introspection.ClassIntrospectorTest.TestC;
import com.threewks.gaetools.introspection.ClassIntrospectorTest.TestCA;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class TypeIntrospectorTest {
    @Test
    public void shouldReturnTrueForIsACollection() {
        assertThat(TypeIntrospector.isACollection(Collection.class), is(true));
        assertThat(TypeIntrospector.isACollection(List.class), is(true));
        assertThat(TypeIntrospector.isACollection(Set.class), is(true));

        ArrayList<String> s = new ArrayList<>();
        List<String> s2 = s;
        Collection<String> s3 = s;
        assertThat(TypeIntrospector.isACollection(s.getClass()), is(true));
        assertThat(TypeIntrospector.isACollection(s2.getClass()), is(true));
        assertThat(TypeIntrospector.isACollection(s3.getClass()), is(true));
    }

    @Test
    public void shouldGetGenericTypeForCollectionOrNull() {
        assertThat(TypeIntrospector.getCollectionType(Collection.class), is(nullValue()));
        assertThat(TypeIntrospector.getCollectionType(List.class), is(nullValue()));
        assertThat(TypeIntrospector.getCollectionType(Set.class), is(nullValue()));
        assertThat(TypeIntrospector.getCollectionType(new ArrayList<String>().getClass()), is(nullValue()));

        assertThat(TypeIntrospector.getCollectionType(getType("listOfStrings")), is((Object) String.class));
        assertThat(TypeIntrospector.getCollectionType(getType("setOfStrings")), is((Object) String.class));
        assertThat(TypeIntrospector.getCollectionType(getType("collectionOfStrings")), is((Object) String.class));
        assertThat(TypeIntrospector.getCollectionType(getType("iterableOfStrings")), is((Object) String.class));
    }

    @Test
    public void shouldReturnTrueIfClassExistsFalseOtherwise() {
        assertThat(TypeIntrospector.classExists(null), is(false));
        assertThat(TypeIntrospector.classExists("doesnt.Exist"), is(false));
        assertThat(TypeIntrospector.classExists("invalid class name"), is(false));
        assertThat(TypeIntrospector.classExists("com.threewks.gaetools.introspection.TypeIntrospector"), is(true));
        assertThat(TypeIntrospector.classExists("TypeIntrospector"), is(false));
    }

    @Test
    public void shouldReturnTrueForIsAJavabeanIfIsANormalObjectWithANoArgsCtor() {
        assertThat(TypeIntrospector.isAJavabean(null), is(false));
        assertThat(TypeIntrospector.isAJavabean(Object.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(Void.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(int.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(Integer.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(String[].class), is(false));
        assertThat(TypeIntrospector.isAJavabean(String.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(NoDefaultCtor.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(PrivateCtor.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(Method.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(NoDefaultCtor.class), is(false));
        assertThat(TypeIntrospector.isAJavabean(Test.class), is(false));

        assertThat(TypeIntrospector.isAJavabean(Date.class), is(true));
        assertThat(TypeIntrospector.isAJavabean(TestC.class), is(true));
        assertThat(TypeIntrospector.isAJavabean(TestCA.class), is(true));
    }

    @Test
    public void shouldReturnTrueIfBoxedType() {
        assertThat(TypeIntrospector.isABoxedType(Long.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Integer.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Character.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Byte.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Float.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Double.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Short.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Boolean.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(Void.class), is(true));
        assertThat(TypeIntrospector.isABoxedType(long.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(int.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(char.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(byte.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(float.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(double.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(short.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(boolean.class), is(false));
        assertThat(TypeIntrospector.isABoxedType(void.class), is(false));
    }

    @Test
    public void shouldReturnTrueIfPrimitiveType() {
        assertThat(TypeIntrospector.isABasicType(Long.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Integer.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Character.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Byte.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Float.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Double.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Short.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Boolean.class), is(false));
        assertThat(TypeIntrospector.isABasicType(Void.class), is(false));
        assertThat(TypeIntrospector.isABasicType(long.class), is(true));
        assertThat(TypeIntrospector.isABasicType(int.class), is(true));
        assertThat(TypeIntrospector.isABasicType(char.class), is(true));
        assertThat(TypeIntrospector.isABasicType(byte.class), is(true));
        assertThat(TypeIntrospector.isABasicType(float.class), is(true));
        assertThat(TypeIntrospector.isABasicType(double.class), is(true));
        assertThat(TypeIntrospector.isABasicType(short.class), is(true));
        assertThat(TypeIntrospector.isABasicType(boolean.class), is(true));
        assertThat(TypeIntrospector.isABasicType(void.class), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnPrimitiveTypes() {
        assertThat(TypeIntrospector.getPrimitiveTypes(), hasItems(long.class, int.class, short.class, char.class, void.class, float.class, double.class, byte.class, boolean.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnBoxedTypes() {
        assertThat(TypeIntrospector.getBoxedTypes(), hasItems(Long.class, Integer.class, Short.class, Character.class, Void.class, Float.class, Double.class, Byte.class, Boolean.class));
    }

    @Test
    public void shouldBoxTypes() {
        assertThat(TypeIntrospector.box(void.class).equals(Void.class), is(true));
        assertThat(TypeIntrospector.box(byte.class).equals(Byte.class), is(true));
        assertThat(TypeIntrospector.box(boolean.class).equals(Boolean.class), is(true));
        assertThat(TypeIntrospector.box(char.class).equals(Character.class), is(true));
        assertThat(TypeIntrospector.box(short.class).equals(Short.class), is(true));
        assertThat(TypeIntrospector.box(int.class).equals(Integer.class), is(true));
        assertThat(TypeIntrospector.box(long.class).equals(Long.class), is(true));
        assertThat(TypeIntrospector.box(float.class).equals(Float.class), is(true));
        assertThat(TypeIntrospector.box(double.class).equals(Double.class), is(true));
        assertThat(TypeIntrospector.box(String.class), is(nullValue()));
        assertThat(TypeIntrospector.box(null), is(nullValue()));

    }

    @Test
    public void shouldUnboxTypes() {
        assertThat(TypeIntrospector.unbox(Void.class).equals(void.class), is(true));
        assertThat(TypeIntrospector.unbox(Byte.class).equals(byte.class), is(true));
        assertThat(TypeIntrospector.unbox(Boolean.class).equals(boolean.class), is(true));
        assertThat(TypeIntrospector.unbox(Character.class).equals(char.class), is(true));
        assertThat(TypeIntrospector.unbox(Short.class).equals(short.class), is(true));
        assertThat(TypeIntrospector.unbox(Integer.class).equals(int.class), is(true));
        assertThat(TypeIntrospector.unbox(Long.class).equals(long.class), is(true));
        assertThat(TypeIntrospector.unbox(Float.class).equals(float.class), is(true));
        assertThat(TypeIntrospector.unbox(Double.class).equals(double.class), is(true));
        assertThat(TypeIntrospector.unbox(String.class), is(nullValue()));
        assertThat(TypeIntrospector.unbox(null), is(nullValue()));

    }

    private Type getType(String methodName) {
        try {
            return this.getClass().getDeclaredMethod(methodName).getGenericReturnType();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    private List<String> listOfStrings() {
        return null;
    }

    @SuppressWarnings("unused")
    private Collection<String> collectionOfStrings() {
        return null;
    }

    @SuppressWarnings("unused")
    private Set<String> setOfStrings() {
        return null;
    }

    @SuppressWarnings("unused")
    private Iterable<String> iterableOfStrings() {
        return null;
    }

}
