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
package com.threewks.gaetools.search.gae;

import com.atomicleopard.expressive.Expressive;
import com.threewks.gaetools.search.IndexOperation;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.TextSearchService;
import com.threewks.gaetools.search.gae.naming.DefaultIndexNamingStrategy;
import com.threewks.gaetools.search.gae.naming.IndexNamingStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An implementation of {@link TextSearchService} which uses Appengine's Full Text Search Api
 *
 * @param <T>
 * @param <K>
 */
public class GaeSearchService<T, K> extends BaseGaeSearchService<T, K> implements TextSearchService<T, K>, SearchExecutor<T, K, SearchImpl<T, K>> {

    /**
     * Create a {@link GaeSearchService} for the given type using the {@link DefaultIndexNamingStrategy}.
     *
     * @param type         the type this search service will index
     * @param searchConfig
     */
    public GaeSearchService(Class<T> type, SearchConfig searchConfig) {
        this(type, searchConfig, new DefaultIndexNamingStrategy());
    }

    /**
     * Create a {@link GaeSearchService} for the given type using the given {@link IndexNamingStrategy}.
     *
     * @param type
     * @param searchConfig
     * @param indexNamingStrategy
     */
    public GaeSearchService(Class<T> type, SearchConfig searchConfig, IndexNamingStrategy indexNamingStrategy) {
        super(type, searchConfig, indexNamingStrategy);
        if (!metadata.hasIndexableFields()) {
            throw new SearchException("Unable to create %s - the type %s has no indexable fields ", this.getClass().getSimpleName(), type.getSimpleName());
        }
    }

    @Override
    public IndexOperation index(T object) {
        K id = metadata.getId(object);
        return super.index(object, id);
    }

    @Override
    public IndexOperation index(Collection<T> objects) {
        return super.index(objects);
    }

    @Override
    public IndexOperation removeById(K id) {
        return super.removeById(id);
    }

    @Override
    public IndexOperation removeById(Iterable<K> ids) {
        return super.removeById(ids);
    }

    @Override
    public IndexOperation remove(T object) {
        K id = metadata.getId(object);
        return super.removeById(id);
    }

    @Override
    public IndexOperation remove(Iterable<T> objects) {
        if (Expressive.isEmpty(objects)) {
            return new IndexOperation(null);
        }
        List<K> ids = new ArrayList<>();
        for (T object : objects) {
            ids.add(metadata.getId(object));
        }
        return super.removeById(ids);
    }

    @Override
    public int removeAll() {
        return super.removeAll();
    }

    @Override
    public SearchImpl<T, K> search() {
        return super.search();
    }
}
