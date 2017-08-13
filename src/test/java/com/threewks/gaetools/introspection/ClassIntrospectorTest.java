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

import org.junit.Test;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ClassIntrospectorTest {

    @Test
    public void shouldListClassContructorsAndOrderByNumberOfParamersFollowedByName() {
        List<Constructor<TestC>> constructors = new ClassIntrospector().listConstructors(TestC.class);
        assertThat(constructors.size(), is(4));
        assertThat(constructors.get(0).toString(), is("public com.threewks.gaetools.introspection.ClassIntrospectorTest$TestC()"));
        assertThat(constructors.get(1).toString(), is("public com.threewks.gaetools.introspection.ClassIntrospectorTest$TestC(java.lang.String)"));
        assertThat(constructors.get(2).toString(), is("public com.threewks.gaetools.introspection.ClassIntrospectorTest$TestC(java.lang.String,int)"));
        assertThat(constructors.get(3).toString(), is("public com.threewks.gaetools.introspection.ClassIntrospectorTest$TestC(java.lang.String,java.lang.String)"));
    }

    @Test
    public void shouldListSetters() {
        List<Method> setters = new ClassIntrospector().listSetters(TestCA.class);
        assertThat(setters.size(), is(3));
        List<String> setterNames = new ArrayList<>();
        for (Method method : setters) {
            setterNames.add(method.getName());
        }
        assertThat(setterNames, hasItems("setA", "setB", "setC"));
    }

    @Test
    public void shouldReturnClassesAndInterfacesInPriorityOrder() {
        List<Class<?>> types = new ClassIntrospector().listImplementedTypes(TestCA.class);
        assertThat(types, is(Arrays.asList(TestCA.class, TestC.class, Object.class, TestBA.class, TestB.class, TestAA.class, TestA.class)));
    }

    interface TestA {

    }

    interface TestB {

    }

    interface TestAA extends TestA {

    }

    interface TestBA extends TestB {

    }

    static class TestC implements TestAA {
        @SuppressWarnings("unused")
        @Inject
        private String fieldA;
        @SuppressWarnings("unused")
        private String fieldB;

        public TestC() {
        }

        public TestC(String a) {
        }

        public TestC(String a, String b) {
        }

        public TestC(String a, int b) {
        }

        public void setC(String c) {
        }
    }

    static class TestCA extends TestC implements TestBA {
        @SuppressWarnings("unused")
        @Inject
        private String fieldC;
        @SuppressWarnings("unused")
        private String fieldD;

        public void setA(String a) {

        }

        public void setB(int b) {

        }
    }

    public static class DefaultCtor {
    }

    public static class NoDefaultCtor {
        public NoDefaultCtor(String something) {
        }
    }

    public static class PrivateCtor {
        private PrivateCtor() {

        }
    }
}
