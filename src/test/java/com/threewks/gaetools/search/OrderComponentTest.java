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
package com.threewks.gaetools.search;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderComponentTest {

    @Test
    public void shouldCreateAndRetainFieldAndOrder() {
        assertThat(OrderComponent.forField("fieldName", true).getField(), is("fieldName"));
        assertThat(OrderComponent.forField("fieldName", true).isDescending(), is(false));
        assertThat(OrderComponent.forField("fieldName", true).isAscending(), is(true));
        assertThat(OrderComponent.forField("differentFieldName", false).getField(), is("differentFieldName"));
        assertThat(OrderComponent.forField("differentFieldName", false).isDescending(), is(true));
        assertThat(OrderComponent.forField("differentFieldName", false).isAscending(), is(false));
    }

    @Test
    public void shouldCreateForFieldAscending() {
        assertThat(OrderComponent.forFieldAscending("fieldName").getField(), is("fieldName"));
        assertThat(OrderComponent.forFieldAscending("fieldName").isAscending(), is(true));
        assertThat(OrderComponent.forFieldAscending("fieldName").isDescending(), is(false));
    }

    @Test
    public void shouldCreateForFieldDescending() {
        assertThat(OrderComponent.forFieldDescending("fieldName").getField(), is("fieldName"));
        assertThat(OrderComponent.forFieldDescending("fieldName").isAscending(), is(false));
        assertThat(OrderComponent.forFieldDescending("fieldName").isDescending(), is(true));
    }

    @Test
    public void shouldToStringToHumanReadableString() {
        assertThat(OrderComponent.forField("fieldName", true).toString(), is("fieldName Asc"));
        assertThat(OrderComponent.forField("otherField", false).toString(), is("otherField Desc"));
    }

    @Test
    public void shouldProvideEqualityAndHashcodeOnFieldNameAndDirection() {
        OrderComponent fieldDesc = OrderComponent.forField("Field", true);
        OrderComponent fieldDesc2 = OrderComponent.forField("Field", true);
        OrderComponent fieldAsc = OrderComponent.forField("Field", false);
        OrderComponent field2Desc = OrderComponent.forField("Field2", true);
        OrderComponent field2Asc = OrderComponent.forField("Field2", false);
        OrderComponent fieldNullDesc = OrderComponent.forField(null, true);
        OrderComponent fieldNullAsc = OrderComponent.forField(null, false);

        assertThat(fieldDesc.equals(fieldDesc), is(true));
        assertThat(fieldDesc.equals(fieldDesc2), is(true));
        assertThat(fieldDesc2.equals(fieldDesc), is(true));

        assertThat(fieldDesc.equals(fieldAsc), is(false));
        assertThat(fieldDesc.equals(field2Desc), is(false));
        assertThat(fieldDesc.equals(field2Asc), is(false));
        assertThat(fieldDesc.equals(fieldNullAsc), is(false));

        assertThat(fieldNullAsc.equals(fieldDesc), is(false));
        assertThat(fieldDesc.equals(fieldNullDesc), is(false));

        assertThat(fieldDesc.equals(null), is(false));
        assertThat(fieldDesc.equals("A String"), is(false));

        assertThat(fieldDesc.hashCode() == fieldDesc2.hashCode(), is(true));
        assertThat(fieldDesc.hashCode() == field2Desc.hashCode(), is(false));
        assertThat(fieldDesc.hashCode() == fieldAsc.hashCode(), is(false));

    }
}
