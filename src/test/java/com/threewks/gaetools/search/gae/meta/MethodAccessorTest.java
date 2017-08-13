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
package com.threewks.gaetools.search.gae.meta;

import com.threewks.gaetools.search.SearchException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MethodAccessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldProvideAccessToMethod() throws NoSuchMethodException {

        Method method = MethodPojo.class.getDeclaredMethod("getMethod");

        MethodAccessor<MethodPojo, String> methodAccessor = new MethodAccessor<>(MethodPojo.class, method, "method", "encoded-name", IndexType.Text);
        assertThat(methodAccessor.getName(), is("method"));
        assertThat(methodAccessor.getEncodedName(), is("encoded-name"));
        assertThat(methodAccessor.getIndexType(), is(IndexType.Text));
        assertThat(methodAccessor.getType() == String.class, is(true));
        assertThat(methodAccessor.get(new MethodPojo()), is("return value"));
    }

    @Test
    public void shouldProvideAccessToMethodWhenFoundInBaseClass() throws NoSuchMethodException {

        Method method = BaseMethodPojo.class.getDeclaredMethod("getConcreteAbstractMethod");

        MethodAccessor<ConcreteMethodPojo, String> methodAccessor = new MethodAccessor<>(ConcreteMethodPojo.class, method, "method", "encoded-name", IndexType.Text);
        assertThat(methodAccessor.getName(), is("method"));
        assertThat(methodAccessor.getEncodedName(), is("encoded-name"));
        assertThat(methodAccessor.getIndexType(), is(IndexType.Text));
        assertThat(methodAccessor.getType() == String.class, is(true));
        assertThat(methodAccessor.get(new ConcreteMethodPojo()), is("abstract method value"));
    }

    @Test
    public void shouldProvideAccessToMethodWhenFoundInConcreteClass() throws NoSuchMethodException {

        Method method = BaseMethodPojo.class.getDeclaredMethod("getAbstractMethod");

        MethodAccessor<ConcreteMethodPojo, String> methodAccessor = new MethodAccessor<>(ConcreteMethodPojo.class, method, "method", "encoded-name", IndexType.Text);
        assertThat(methodAccessor.getName(), is("method"));
        assertThat(methodAccessor.getEncodedName(), is("encoded-name"));
        assertThat(methodAccessor.getIndexType(), is(IndexType.Text));
        assertThat(methodAccessor.getType() == String.class, is(true));
        assertThat(methodAccessor.get(new ConcreteMethodPojo()), is("concrete method value"));
    }

    @Test
    public void shouldThrowSearchExceptionWhenCannotGetFieldValue() throws NoSuchMethodException {

        thrown.expect(SearchException.class);
        thrown.expectMessage("Failed to call method 'MethodAccessorTest.getAbstractMethod': object is not an instance of declaring class");

        Method method = BaseMethodPojo.class.getDeclaredMethod("getAbstractMethod");

        MethodAccessor<MethodAccessorTest, String> methodAccessor = new MethodAccessor<>(MethodAccessorTest.class, method, "method", "encoded-name", IndexType.Text);
        methodAccessor.get(this);
    }

    public static class MethodPojo {

        public String getMethod() {
            return "return value";
        }
    }


    public static abstract class BaseMethodPojo {

        public abstract String getAbstractMethod();

        public String getConcreteAbstractMethod() {
            return "abstract method value";
        }
    }

    public static class ConcreteMethodPojo extends BaseMethodPojo {

        @Override
        public String getAbstractMethod() {
            return "concrete method value";
        }
    }

}
