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

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FieldAccessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldProvideAccessToField() throws NoSuchFieldException {

        Field field = FieldPojo.class.getDeclaredField("field");

        FieldAccessor<FieldPojo, String> methodAccessor = new FieldAccessor<>(FieldPojo.class, field, "field", "encoded-name", IndexType.Text);
        assertThat(methodAccessor.getName(), is("field"));
        assertThat(methodAccessor.getEncodedName(), is("encoded-name"));
        assertThat(methodAccessor.getIndexType(), is(IndexType.Text));
        assertThat(methodAccessor.getType() == String.class, is(true));
        assertThat(methodAccessor.get(new FieldPojo()), is("field value"));
    }

    @Test
    public void shouldProvideAccessToFieldWhenFoundInBaseClass() throws NoSuchFieldException {

        Field field = BaseFieldPojo.class.getDeclaredField("abstractField");

        FieldAccessor<ConcreteFieldPojo, String> methodAccessor = new FieldAccessor<>(ConcreteFieldPojo.class, field, "abstractField", "encoded-name", IndexType.Text);
        assertThat(methodAccessor.getName(), is("abstractField"));
        assertThat(methodAccessor.getEncodedName(), is("encoded-name"));
        assertThat(methodAccessor.getIndexType(), is(IndexType.Text));
        assertThat(methodAccessor.getType() == String.class, is(true));
        assertThat(methodAccessor.get(new ConcreteFieldPojo()), is("abstract field value"));
    }

    @Test
    public void shouldThrowSearchExceptionWhenCannotGetFieldValue() throws NoSuchFieldException {
        thrown.expect(SearchException.class);
        thrown.expectMessage("Failed to access field 'FieldAccessorTest.field': ");

        Field field = FieldPojo.class.getDeclaredField("field");

        FieldAccessor<FieldAccessorTest, String> fieldAccessor = new FieldAccessor<>(FieldAccessorTest.class, field, "encoded-name", "notAField", IndexType.Text);
        fieldAccessor.get(this);
    }

    public static class FieldPojo {
        @SuppressWarnings("unused")
        private String field = "field value";
    }

    public static abstract class BaseFieldPojo {
        @SuppressWarnings("unused")
        private String abstractField = "abstract field value";
    }

    public static class ConcreteFieldPojo extends BaseFieldPojo {
    }
}
