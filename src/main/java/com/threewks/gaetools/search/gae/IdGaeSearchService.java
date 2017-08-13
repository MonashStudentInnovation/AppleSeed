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

import com.threewks.gaetools.search.IdTextSearchService;
import com.threewks.gaetools.search.IndexOperation;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.gae.naming.DefaultIndexNamingStrategy;
import com.threewks.gaetools.search.gae.naming.IndexNamingStrategy;

import java.util.Map;

/**
 * An implementation of the {@link IdTextSearchService} which uses Appengine's Full Text Search API.
 *
 * @param <T>
 * @param <K>
 * @author nick
 */
public class IdGaeSearchService<T, K> extends BaseGaeSearchService<T, K> implements IdTextSearchService<T, K>, SearchExecutor<T, K, SearchImpl<T, K>> {
    /**
     * Create an {@link IdGaeSearchService} for the given type and the given id type. This will use the {@link DefaultIndexNamingStrategy}.
     *
     * @param type
     * @param keyType
     * @param searchConfig
     */
    public IdGaeSearchService(Class<T> type, Class<K> keyType, SearchConfig searchConfig) {
        this(type, keyType, searchConfig, new DefaultIndexNamingStrategy());
    }

    /**
     * Create an {@link IdGaeSearchService} for the given type and the given id type using the given {@link IndexNamingStrategy}/
     *
     * @param type
     * @param keyType
     * @param searchConfig
     * @param indexNamingStrategy
     */
    public IdGaeSearchService(Class<T> type, Class<K> keyType, SearchConfig searchConfig, IndexNamingStrategy indexNamingStrategy) {
        super(type, keyType, searchConfig, indexNamingStrategy);
    }

    @Override
    public IndexOperation index(T object, K id) {
        return super.index(object, id);
    }

    @Override
    public IndexOperation index(Map<K, T> objects) {
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
    public int removeAll() {
        return super.removeAll();
    }

    @Override
    public SearchImpl<T, K> search() {
        return super.search();
    }

    @Override
    public Result<T, K> createSearchResult(SearchImpl<T, K> searchRequest) {
        return super.createSearchResult(searchRequest);
    }
}
