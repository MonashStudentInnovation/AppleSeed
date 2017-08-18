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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;

/**
 * Represents a component of a {@link Search}. There are three types of query component,
 * raw queries (just a string passed down to the search implementation),
 * field queries (a query for a specific value on a specific field, like 'field-name' greater than 10)
 * and field collection queries (like 'field-name' in value1, value2, value3)
 * <p>
 * There is a static factory method for each type of use case.
 * <p>{@link #forRawQuery(CharSequence)}</p>
 * <p>{@link #forFieldQuery(String, Is, Object)}</p>
 * <p>{@link #forCollection(String, Collection)}</p>
 */
public class QueryComponent {
    private String fieldOrQuery;
    private Is is;
    private Object value;

    /**
     * Create a {@link QueryComponent} for a raw query string.
     *
     * @param query
     * @return
     */
    public static QueryComponent forRawQuery(CharSequence query) {
        return new QueryComponent(query.toString(), null, null);
    }

    /**
     * Create a {@link QueryComponent} for matching a particular field against a discrete set of values
     *
     * @param field
     * @param values
     * @return
     */
    public static <V> QueryComponent forCollection(String field, Collection<V> values) {
        return new QueryComponent(field, null, values);
    }

    /**
     * Create a {@link QueryComponent} for querying on a particular field using the given relation ({@link Is}) with the given value.
     *
     * @param field
     * @param is
     * @param value
     * @return
     */

    public static <V> QueryComponent forFieldQuery(String field, Is is, V value) {
        return new QueryComponent(field, is, value);
    }

    private QueryComponent(String fieldOrQuery, Is is, Object value) {
        super();
        this.fieldOrQuery = fieldOrQuery;
        this.is = is;
        this.value = value;
    }

    /**
     * Get the field for a field query, or null if not a field query
     *
     * @return
     * @see #isFieldedQuery()
     */
    public String getField() {
        return value == null ? null : fieldOrQuery;
    }

    /**
     * Get the query for a non-field query, or null if is a field query.
     *
     * @return
     * @see #isFieldedQuery()
     */
    public String getQuery() {
        return value == null ? fieldOrQuery : null;
    }

    /**
     * Get the {@link Is} for a fielded query
     *
     * @return
     */
    public Is getIs() {
        return is;
    }

    /**
     * Get the value for a fielded query, returns null if a collection query
     *
     * @return
     * @see #isCollectionQuery()
     */
    public Object getValue() {
        return isCollectionQuery() ? null : value;
    }

    /**
     * Return true if this is a fielded query
     *
     * @return
     */
    public boolean isFieldedQuery() {
        return value != null;
    }

    /**
     * Return true if this is a collection query
     *
     * @return
     */
    public boolean isCollectionQuery() {
        return value instanceof Collection;
    }

    /**
     * Return the collection for a collection query, or null if not a colleciton query
     *
     * @return
     * @see #isCollectionQuery()
     */
    @SuppressWarnings("unchecked")
    public Collection<Object> getCollectionValue() {
        return isCollectionQuery() ? (Collection<Object>) value : null;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return String.format("%s%s%s", fieldOrQuery, is == null ? "" : " " + is, value == null ? "" : " " + value);
    }
}
