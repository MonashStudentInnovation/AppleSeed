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

import java.lang.reflect.Field;

public class FieldAccessor<T, V> implements Accessor<T, V> {
    private Class<T> type;
    private String name;
    private String encodedName;
    private String fieldName;
    private Class<V> fieldType;
    private Field field;
    private IndexType indexType;

    @SuppressWarnings("unchecked")
    public FieldAccessor(Class<T> type, Field field, String name, String encodedName, IndexType indexType) {
        try {
            this.type = type;
            this.field = field;
            this.field.setAccessible(true);
            this.fieldType = (Class<V>) this.field.getType();
            this.name = name;
            this.encodedName = encodedName;
            this.fieldName = field.getName();
            this.indexType = indexType;
        } catch (SecurityException e) {
            throw new SearchException(e, "Unable to access field '%s.%s': %s", type.getSimpleName(), fieldName, e.getMessage());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEncodedName() {
        return encodedName;
    }

    @Override
    public Class<V> getType() {
        return fieldType;
    }

    @Override
    public IndexType getIndexType() {
        return indexType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(T t) {
        try {
            return (V) field.get(t);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new SearchException(e, "Failed to access field '%s.%s': %s", type.getSimpleName(), fieldName, e.getMessage());
        }
    }

}
