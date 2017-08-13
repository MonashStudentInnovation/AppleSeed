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
package com.threewks.gaetools.search.gae.mediator;

import com.atomicleopard.expressive.ETransformer;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.gae.meta.IndexType;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link FieldMediator} is used to mediate between a java type and a low level search api type.
 * The {@link FieldMediatorSet} groups the full set of {@link FieldMediator} implementations and matches
 * them to the appropriate {@link IndexType}.
 */
public class FieldMediatorSet {
    private Map<IndexType, FieldMediator<?>> fieldMediators = initIndexFieldBuilders();

    @SuppressWarnings("unchecked")
    public <T> FieldMediator<T> get(IndexType indexType) {
        FieldMediator<T> result = (FieldMediator<T>) fieldMediators.get(indexType);
        if (result == null) {
            throw new SearchException("No %s present for %s '%s'", FieldMediator.class.getSimpleName(), IndexType.class.getSimpleName(), indexType);
        }
        return result;
    }

    public <T> void put(IndexType indexType, FieldMediator<T> mediator) {
        this.fieldMediators.put(indexType, mediator);
    }

    protected static Map<IndexType, FieldMediator<?>> initIndexFieldBuilders() {
        Map<IndexType, FieldMediator<?>> map = new HashMap<>();
        map.put(IndexType.Automatic, new TextFieldMediator());
        map.put(IndexType.BigDecimal, new BigDecimalFieldMediator());
        map.put(IndexType.Date, new DateFieldMediator());
        map.put(IndexType.GeoPoint, new GeoPointFieldMediator());
        map.put(IndexType.Html, new HtmlFieldMediator());
        map.put(IndexType.Identifier, new AtomFieldMediator());
        map.put(IndexType.SmallDecimal, new SmallDecimalFieldMediator());
        map.put(IndexType.Text, new TextFieldMediator());
        return map;
    }

    public static final ETransformer<String, String> quote = from -> {
        from = from.replaceAll("\"", "\\\\\"");
        return String.format("\"%s\"", from);
    };
}
