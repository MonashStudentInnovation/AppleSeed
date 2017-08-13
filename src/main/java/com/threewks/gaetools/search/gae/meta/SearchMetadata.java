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

import com.threewks.gaetools.introspection.ClassIntrospector;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.SearchId;
import com.threewks.gaetools.search.SearchIndex;
import com.threewks.gaetools.search.gae.GaeSearchService;
import com.threewks.gaetools.search.gae.IdGaeSearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Determines meta data used for the {@link GaeSearchService} and {@link IdGaeSearchService} for a particular class type.
 *
 * @param <T>
 * @param <K>
 */
public class SearchMetadata<T, K> {
    private static final Pattern FieldNamePattern = Pattern.compile("[a-zA-Z][\\w-]*");
    private Class<T> type;
    private Class<K> keyType;
    private Accessor<T, K> idAccessor;
    private Map<String, Accessor<T, ?>> accessors = new LinkedHashMap<>();
    private Map<String, String> encodedFieldNames = new HashMap<>();
    private IndexTypeLookup indexTypeLookup;
    private ClassIntrospector classIntrospector = new ClassIntrospector();

    public SearchMetadata(Class<T> type, IndexTypeLookup indexTypeLookup) {
        this.indexTypeLookup = indexTypeLookup;
        this.type = type;
        findMethodAccessors(type);
        findFieldAccessors(type);
        if (idAccessor == null) {
            throw new SearchException("No id found for %s - make sure @%s is on one field or method", type.getSimpleName(), SearchId.class.getSimpleName());
        }
        this.keyType = idAccessor.getType();
    }

    public SearchMetadata(Class<T> type, Class<K> keyType, IndexTypeLookup indexTypeLookup) {
        this.indexTypeLookup = indexTypeLookup;
        this.type = type;
        this.keyType = keyType;
        findMethodAccessors(type);
        findFieldAccessors(type);
    }

    public Class<K> getKeyType() {
        return keyType;
    }

    public Class<T> getType() {
        return type;
    }

    public K getId(T t) {
        return idAccessor.get(t);
    }

    public Map<String, Object> getData(T t) {
        Map<String, Object> result = new HashMap<>();
        for (Accessor<T, ?> accessor : accessors.values()) {
            result.put(accessor.getEncodedName(), accessor.get(t));
        }
        return result;
    }

    public String getEncodedFieldName(String field) {
        Accessor<T, ?> accessor = accessors.get(field);
        return accessor == null ? null : accessor.getEncodedName();
    }

    public IndexType getIndexType(String field) {
        Accessor<T, ?> accessor = accessors.get(field);
        if (accessor == null) {
            throw new SearchException("There is no field or getter marked with @%s for %s.%s - make sure it is indexed and accessible", SearchIndex.class.getSimpleName(), type.getSimpleName(), field);
        }
        return accessor.getIndexType();
    }

    public String getDecodedFieldName(String encodedFieldName) {
        return encodedFieldNames.get(encodedFieldName);
    }

    public Class<?> getFieldType(String field) {
        Accessor<T, ?> accessor = accessors.get(field);
        return accessor == null ? null : accessor.getType();
    }

    private String determineName(Method method) {
        SearchIndex annotation = method.getAnnotation(SearchIndex.class);
        String name = annotation == null ? null : annotation.value();
        if (StringUtils.isBlank(name)) {
            name = method.getName();
            if (name.startsWith("is")) {
                name = WordUtils.uncapitalize(name.substring(2));
            } else if (name.startsWith("get")) {
                name = WordUtils.uncapitalize(name.substring(3));
            }
        }
        return name;
    }

    private String determineName(Field field) {
        SearchIndex annotation = field.getAnnotation(SearchIndex.class);
        return annotation == null || StringUtils.isBlank(annotation.value()) ? field.getName() : annotation.value();
    }

    public boolean hasIndexableFields() {
        return !accessors.isEmpty();
    }

    /**
     * Ensure the field name matches the requirements of the search interface
     *
     * @param fieldName
     * @return
     */
    protected String encodeFieldName(String fieldName) {
        // From the docs - They must start with a letter and can contain letters, digits, or underscore
        String encoded = fieldName.replaceAll("[^\\w-]", "-");
        encoded = encoded.replaceAll("-+", "-");
        encoded = encoded.replaceAll("[^a-zA-Z]*([a-zA-Z]+)([\\w-]*)", "$1$2");
        if (!FieldNamePattern.matcher(encoded).matches()) {
            throw new SearchException(
                    "The field name '%s' cannot be used - it could not be encoded into an acceptable representation for the text search api. Make sure it has at least one letter. The encoded result was '%s'",
                    fieldName, encoded);
        }
        return encoded;
    }

    private void findFieldAccessors(Class<T> type) {
        for (Field field : classIntrospector.listFields(type)) {
            if (field.isAnnotationPresent(SearchId.class)) {
                String name = determineName(field);
                String encodedName = encodeFieldName(name);
                idAccessor = new FieldAccessor<>(type, field, name, encodedName, IndexType.Identifier);
                encodedFieldNames.put(encodedName, name);
                accessors.put(name, idAccessor);
            }
            if (field.isAnnotationPresent(SearchIndex.class)) {
                String name = determineName(field);
                String encodedNamed = encodeFieldName(name);
                IndexType indexType = field.getAnnotation(SearchIndex.class).as();
                if (indexType == IndexType.Automatic) {
                    indexType = indexTypeLookup.inferIndexType(field.getGenericType());
                }
                accessors.put(name, new FieldAccessor<>(type, field, name, encodedNamed, indexType));
                encodedFieldNames.put(encodedNamed, name);
            }
        }
    }

    private void findMethodAccessors(Class<T> type) {
        for (Method method : classIntrospector.listMethods(type)) {
            if (method.isAnnotationPresent(SearchId.class)) {
                String name = determineName(method);
                String encodedName = encodeFieldName(name);
                idAccessor = new MethodAccessor<>(type, method, name, encodedName, IndexType.Identifier);
                encodedFieldNames.put(encodedName, name);
                accessors.put(name, idAccessor);
            }
            if (method.isAnnotationPresent(SearchIndex.class)) {
                String name = determineName(method);
                String encodedName = encodeFieldName(name);
                IndexType indexType = method.getAnnotation(SearchIndex.class).as();
                if (indexType == IndexType.Automatic) {
                    indexType = indexTypeLookup.inferIndexType(method.getReturnType());
                }
                accessors.put(name, new MethodAccessor<>(type, method, name, encodedName, indexType));
                encodedFieldNames.put(encodedName, name);
            }
        }
    }
}
