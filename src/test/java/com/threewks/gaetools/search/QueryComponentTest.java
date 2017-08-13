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

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class QueryComponentTest {

    @Test
    public void shouldCreateRawQueryComponent() {
        QueryComponent queryComponent = QueryComponent.forRawQuery("raw query");
        assertThat(queryComponent.getQuery(), is("raw query"));
        assertThat(queryComponent.isFieldedQuery(), is(false));
        assertThat(queryComponent.getField(), is(nullValue()));
        assertThat(queryComponent.getCollectionValue(), is(nullValue()));
        assertThat(queryComponent.getValue(), is(nullValue()));
    }

    @Test
    public void shouldCreateFieldQueryComponent() {
        QueryComponent queryComponent = QueryComponent.forFieldQuery("field", Is.Is, "Value");
        assertThat(queryComponent.getQuery(), is(nullValue()));
        assertThat(queryComponent.isFieldedQuery(), is(true));
        assertThat(queryComponent.getField(), is("field"));
        assertThat(queryComponent.getCollectionValue(), is(nullValue()));
        assertThat(queryComponent.getValue(), is((Object) "Value"));
        assertThat(queryComponent.getIs(), is(Is.Is));
    }

    @Test
    public void shouldCreateFieldCollectionQueryComponent() {
        QueryComponent queryComponent = QueryComponent.forCollection("field", list("1", "2"));
        assertThat(queryComponent.getQuery(), is(nullValue()));
        assertThat(queryComponent.isFieldedQuery(), is(true));
        assertThat(queryComponent.getField(), is("field"));
        assertThat(queryComponent.getCollectionValue(), Matchers.hasItems("1", "2"));
        assertThat(queryComponent.getValue(), is(nullValue()));
        assertThat(queryComponent.getIs(), is(nullValue()));
    }

    @Test
    public void shouldHaveAToString() {
        assertThat(QueryComponent.forRawQuery("raw query").toString(), is("raw query"));
        assertThat(QueryComponent.forFieldQuery("field", Is.Is, "Value").toString(), is("field is Value"));
        assertThat(QueryComponent.forCollection("field", list("1", "2")).toString(), is("field [1, 2]"));
    }

    @Test
    public void shouldHaveEqualityAndHashcode() {
        QueryComponent rawQuery1 = QueryComponent.forRawQuery("raw query");
        QueryComponent rawQuery2 = QueryComponent.forRawQuery("raw query");
        QueryComponent rawQuery3 = QueryComponent.forRawQuery("Raw Query");

        QueryComponent collectionQuery1 = QueryComponent.forCollection("field", list("1", "2"));
        QueryComponent collectionQuery2 = QueryComponent.forCollection("field", list("1", "2"));
        QueryComponent collectionQuery3 = QueryComponent.forCollection("field", list("1", "2", "3"));

        QueryComponent fieldQuery1 = QueryComponent.forFieldQuery("field", Is.Is, "Value");
        QueryComponent fieldQuery2 = QueryComponent.forFieldQuery("field", Is.Is, "Value");
        QueryComponent fieldQuery3 = QueryComponent.forFieldQuery("field2", Is.Is, "Value");
        QueryComponent fieldQuery4 = QueryComponent.forFieldQuery("field", Is.GreaterThan, "Value");
        QueryComponent fieldQuery5 = QueryComponent.forFieldQuery("field", Is.Is, "Value2");

        assertThat(rawQuery1.equals(rawQuery1), is(true));
        assertThat(rawQuery1.equals(rawQuery2), is(true));
        assertThat(rawQuery1.equals(rawQuery3), is(false));
        assertThat(collectionQuery1.equals(collectionQuery1), is(true));
        assertThat(collectionQuery1.equals(collectionQuery2), is(true));
        assertThat(collectionQuery1.equals(collectionQuery3), is(false));
        assertThat(fieldQuery1.equals(fieldQuery1), is(true));
        assertThat(fieldQuery1.equals(fieldQuery2), is(true));
        assertThat(fieldQuery1.equals(fieldQuery3), is(false));
        assertThat(fieldQuery1.equals(fieldQuery4), is(false));
        assertThat(fieldQuery1.equals(fieldQuery5), is(false));

        assertThat(rawQuery1.equals(collectionQuery1), is(false));
        assertThat(collectionQuery1.equals(fieldQuery1), is(false));
        assertThat(fieldQuery1.equals(collectionQuery2), is(false));
        assertThat(fieldQuery1.equals(rawQuery1), is(false));

        assertThat(rawQuery1.hashCode() == rawQuery2.hashCode(), is(true));
        assertThat(collectionQuery1.hashCode() == collectionQuery2.hashCode(), is(true));
        assertThat(fieldQuery1.hashCode() == fieldQuery2.hashCode(), is(true));

        assertThat(rawQuery1.hashCode() == rawQuery3.hashCode(), is(false));
        assertThat(collectionQuery1.hashCode() == collectionQuery3.hashCode(), is(false));
        assertThat(fieldQuery1.hashCode() == fieldQuery3.hashCode(), is(false));

    }
}
