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

/**
 * Represents a component of a {@link Search} which controls the order of results.
 * You can create an {@link OrderComponent} by using one of the static factory methods:
 * <ul>
 * <li>{@link #forField(String, boolean)}</li>
 * <li>{@link #forFieldAscending(String)}</li>
 * <li>{@link #forFieldDescending(String)}</li>
 * </ul>
 */
public class OrderComponent {
    private String field;
    private boolean ascending;

    /**
     * Create an {@link OrderComponent} for the given field.
     *
     * @param field
     * @param ascending true if the order is 'Ascending', false if it is 'Descending'
     * @return
     */
    public static OrderComponent forField(String field, boolean ascending) {
        return new OrderComponent(field, ascending);
    }

    /**
     * Create an ascending {@link OrderComponent} for the given field
     *
     * @param field
     * @return
     */
    public static OrderComponent forFieldAscending(String field) {
        return new OrderComponent(field, true);
    }

    /**
     * Create an descending {@link OrderComponent} for the given field
     *
     * @param field
     * @return
     */
    public static OrderComponent forFieldDescending(String field) {
        return new OrderComponent(field, false);
    }

    private OrderComponent(String field, boolean ascending) {
        super();
        this.field = field;
        this.ascending = ascending;
    }

    public String getField() {
        return field;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isDescending() {
        return !ascending;
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
        return String.format("%s %s", field, ascending ? "Asc" : "Desc");
    }
}
