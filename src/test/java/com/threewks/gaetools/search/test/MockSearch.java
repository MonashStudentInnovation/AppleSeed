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
package com.threewks.gaetools.search.test;

import com.google.appengine.api.search.ScoredDocument;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.OrderComponent;
import com.threewks.gaetools.search.QueryComponent;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.SearchExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockSearch<T, K> implements Search<T, K> {
    private SearchExecutor<T, K, MockSearch<T, K>> executor;
    private List<QueryComponent> queryComponents = new ArrayList<>();
    private List<OrderComponent> sort = new ArrayList<>();
    private Integer accuracy;
    private Integer limit;
    private Integer offset;

    public MockSearch(final List<T> expectedResults, final List<K> expectedResultIds) {
        super();
        this.executor = new SearchExecutor<T, K, MockSearch<T, K>>() {
            @Override
            public List<K> getResultsAsIds(List<ScoredDocument> results) {
                return expectedResultIds;
            }

            @Override
            public List<T> getResults(List<ScoredDocument> results) {
                return expectedResults;
            }

            @Override
            public Result<T, K> createSearchResult(MockSearch<T, K> searchRequest) {
                return new MockResult<>(expectedResults, expectedResultIds);
            }

        };
    }

    protected MockSearch(SearchExecutor<T, K, MockSearch<T, K>> service, List<QueryComponent> queryComponents, List<OrderComponent> sort, Integer accuracy, Integer limit, Integer offset) {
        super();
        this.executor = service;
        this.queryComponents = queryComponents;
        this.sort = sort;
        this.accuracy = accuracy;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public Search<T, K> query(CharSequence query) {
        List<QueryComponent> queryComponents = new ArrayList<>(this.queryComponents);
        queryComponents.add(QueryComponent.forRawQuery(query));
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public <V> Search<T, K> field(String field, Collection<V> values) {
        List<QueryComponent> queryComponents = new ArrayList<>(this.queryComponents);
        queryComponents.add(QueryComponent.forCollection(field, values));
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public <V> Search<T, K> field(String field, Is is, V value) {
        List<QueryComponent> queryComponents = new ArrayList<>(this.queryComponents);
        queryComponents.add(QueryComponent.forFieldQuery(field, is, value));
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public Search<T, K> order(String field, boolean ascending) {
        List<OrderComponent> sort = new ArrayList<>(this.sort);
        sort.add(OrderComponent.forField(field, ascending));
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public Search<T, K> limit(Integer limit) {
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public Search<T, K> offset(Integer offset) {
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public Search<T, K> accuracy(Integer accuracy) {
        return new MockSearch<>(executor, queryComponents, sort, accuracy, limit, offset);
    }

    @Override
    public Result<T, K> run() {
        return executor.createSearchResult(this);
    }

    @Override
    public List<QueryComponent> query() {
        return queryComponents;
    }

    @Override
    public List<OrderComponent> order() {
        return sort;
    }

    @Override
    public Integer limit() {
        return limit;
    }

    @Override
    public Integer offset() {
        return offset;
    }

    @Override
    public Integer accuracy() {
        return accuracy;
    }

}
