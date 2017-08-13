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
package com.threewks.gaetools.transformer.discrete;

import com.atomicleopard.expressive.ETransformer;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ObjectToEnum<From, E extends Enum<E>> implements ETransformer<From, E> {
    private static ConcurrentMap<Class<? extends Enum<?>>, Map<String, Object>> enumValueCache = new ConcurrentHashMap<>();
    private Class<E> type;

    public ObjectToEnum(Class<E> type) {
        this.type = type;
        buildEnumValueCacheForType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E from(From from) {
        if (from == null) {
            return null;
        }
        String fromString = StringUtils.trimToEmpty(StringUtils.lowerCase(from.toString()));
        Map<String, Object> cache = enumValueCache.get(type);
        return (E) cache.get(fromString);
    }

    Map<String, Object> buildEnumValueCacheForType() {
        Map<String, Object> cache = enumValueCache.get(type);
        if (cache == null) {
            cache = new HashMap<>();
            for (E e : type.getEnumConstants()) {
                cache.put(e.name().toLowerCase(), (Object) e);
            }
            enumValueCache.putIfAbsent(type, cache);
        }
        return cache;
    }
}
