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

import com.google.appengine.api.search.GeoPoint;
import com.threewks.gaetools.introspection.TypeIntrospector;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.SearchIndex;
import org.joda.time.ReadableInstant;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * When an {@link IndexType} is not explicitly set on a field using {@link SearchIndex#as()}, this class is
 * used to infer the {@link IndexType} that should be used based on the field/method type.
 * <p>
 * You can add additional mappings (which control how types are stored and searchable) by calling {@link #addMapping(Class, IndexType)}.
 * <p>
 * To remove an existing mapping, you can call addMapping with a null indexType.
 */
public class IndexTypeLookup {
    private static Map<Class<?>, IndexType> indexTypeMappingCache;

    public IndexTypeLookup() {
        indexTypeMappingCache = createBasicMappings();
    }

    public void addMapping(Class<?> type, IndexType indexType) {
        indexTypeMappingCache.put(type, indexType);
    }

    public boolean hasMapping(Class<?> type, IndexType indexType) {
        return indexTypeMappingCache.get(type) == indexType;
    }

    /**
     * Given a type, infer the {@link IndexType} that is used to store and query a field.
     *
     * @param type
     * @return
     */
    protected IndexType inferIndexType(Type type) {
        // we go a direct get first
        IndexType indexType = indexTypeMappingCache.get(type);
        if (indexType == null) {
            if (TypeIntrospector.isACollection(type)) {
                type = TypeIntrospector.getCollectionType(type);
            }
            Class<?> classType = TypeIntrospector.asClass(type);
            // if not present, we find the first match in the cache
            for (Map.Entry<Class<?>, IndexType> entry : indexTypeMappingCache.entrySet()) {
                if (entry.getKey().isAssignableFrom(classType)) {
                    indexType = entry.getValue();
                    // put the matching type back into the cache to speed up subsequent inference
                    indexTypeMappingCache.put(classType, indexType);
                    break;
                }
            }
        }
        if (indexType == null) {
            throw new SearchException("Unable to infer an %s for the type %s - you should add a mapping using the %s", IndexTypeLookup.class.getSimpleName(), type,
                    IndexTypeLookup.class.getSimpleName());
        }
        return indexType;
    }

    protected static Map<Class<?>, IndexType> createBasicMappings() {
        Map<Class<?>, IndexType> map = new LinkedHashMap<>();
        map.put(Short.class, IndexType.SmallDecimal);
        map.put(Integer.class, IndexType.SmallDecimal);
        map.put(short.class, IndexType.SmallDecimal);
        map.put(int.class, IndexType.SmallDecimal);
        map.put(AtomicInteger.class, IndexType.SmallDecimal);
        map.put(AtomicBoolean.class, IndexType.Identifier);
        map.put(boolean.class, IndexType.Identifier);
        map.put(Boolean.class, IndexType.Identifier);
        map.put(Enum.class, IndexType.Identifier);
        map.put(UUID.class, IndexType.Identifier);
        map.put(Number.class, IndexType.BigDecimal);
        map.put(long.class, IndexType.BigDecimal);
        map.put(float.class, IndexType.BigDecimal);
        map.put(double.class, IndexType.BigDecimal);
        map.put(CharSequence.class, IndexType.Text);
        map.put(ReadableInstant.class, IndexType.Date);
        map.put(Date.class, IndexType.Date);
        map.put(GeoPoint.class, IndexType.GeoPoint);
        return map;
    }
}
