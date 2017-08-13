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
package com.threewks.gaetools.objectify.repository;

import com.googlecode.objectify.Key;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.OrderComponent;
import com.threewks.gaetools.search.QueryComponent;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.SearchExecutor;

import java.util.Collection;
import java.util.List;

public class SearchImpl<E, K> implements Search<E, K> {
    protected com.threewks.gaetools.search.Search<E, Key<E>> searchRequest;
    protected AbstractRepository<E, K> repository;

    protected SearchImpl(AbstractRepository<E, K> repository, com.threewks.gaetools.search.Search<E, Key<E>> searchRequest) {
        this.searchRequest = searchRequest;
        this.repository = repository;
    }

    public com.threewks.gaetools.search.Search<E, Key<E>> getSearchRequest() {
        return searchRequest;
    }

    @Override
    public Search<E, K> query(CharSequence query) {
        return new SearchImpl<>(repository, searchRequest.query(query));
    }

    @Override
    public <V> Search<E, K> field(String field, Is is, V value) {
        return new SearchImpl<>(repository, searchRequest.field(field, is, value));
    }

    @Override
    public <V> Search<E, K> field(String field, Collection<V> values) {
        return new SearchImpl<>(repository, searchRequest.field(field, values));
    }

    @Override
    public Search<E, K> limit(Integer limit) {
        return new SearchImpl<>(repository, searchRequest.limit(limit));
    }

    @Override
    public Search<E, K> offset(Integer offset) {
        return new SearchImpl<>(repository, searchRequest.offset(offset));
    }

    @Override
    public Search<E, K> accuracy(Integer accuracy) {
        return new SearchImpl<>(repository, searchRequest.accuracy(accuracy));
    }

    @Override
    public Search<E, K> order(String field, boolean ascending) {
        return new SearchImpl<>(repository, searchRequest.order(field, ascending));
    }

    @Override
    public Result<E, K> run() {
        SearchExecutor<E, K, SearchImpl<E, K>> searchExecutor = repository.getSearchExecutor();
        return searchExecutor.createSearchResult(this);
    }

    @Override
    public List<QueryComponent> query() {
        return searchRequest.query();
    }

    @Override
    public List<OrderComponent> order() {
        return searchRequest.order();
    }

    @Override
    public Integer limit() {
        return searchRequest.limit();
    }

    @Override
    public Integer offset() {
        return searchRequest.offset();
    }

    @Override
    public Integer accuracy() {
        return searchRequest.accuracy();
    }

}
