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
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.OrderComponent;
import com.threewks.gaetools.search.QueryComponent;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.TextSearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a search on the {@link TextSearchService}. Searches are performed by building the request up by invoking
 * fluent methods.
 *
 * @param <T>
 */
public class SearchImpl<T, K> implements Search<T, K> {
    private SearchExecutor<T, K, SearchImpl<T, K>> searchExecutor;
    private List<QueryComponent> queryComponents = new ArrayList<>();
    private List<OrderComponent> sortOrder = new ArrayList<>();
    private Integer limit;
    private Integer offset;
    private Integer accuracy;

    public SearchImpl(SearchExecutor<T, K, SearchImpl<T, K>> searchExecutor) {
        this(searchExecutor, new ArrayList<>(), new ArrayList<>(), null, null, null);
    }

    protected SearchImpl(SearchExecutor<T, K, SearchImpl<T, K>> searchExecutor, List<QueryComponent> queryComponents, List<OrderComponent> sortOrder, Integer limit, Integer offset, Integer accuracy) {
        super();
        this.searchExecutor = searchExecutor;
        this.queryComponents = queryComponents;
        this.sortOrder = sortOrder;
        this.limit = limit;
        this.offset = offset;
        this.accuracy = accuracy;
    }

    /**
     * includes a string query which applies across all fields in the index.
     *
     * @param query
     * @return
     */
    @Override
    public Search<T, K> query(CharSequence query) {
        return createNewInstance(components(QueryComponent.forRawQuery(query)), sortOrder, limit, offset, accuracy);
    }

    /**
     * Defines a search operation on the given field to apply to the current search.
     */
    @Override
    public <V> Search<T, K> field(String field, Is is, V value) {
        if (value == null) {
            return this;
        }
        return createNewInstance(components(QueryComponent.forFieldQuery(field, is, value)), sortOrder, limit, offset, accuracy);
    }

    @Override
    public <V> Search<T, K> field(String field, Collection<V> values) {
        if (Expressive.isEmpty(values)) {
            return this;
        }
        return createNewInstance(components(QueryComponent.forCollection(field, values)), sortOrder, limit, offset, accuracy);
    }

    /**
     * Limits the number of results in the final {@link ResultImpl}
     *
     * @param limit
     * @return
     */
    @Override
    public Search<T, K> limit(Integer limit) {
        return createNewInstance(queryComponents, sortOrder, limit, offset, accuracy);
    }

    /**
     * Adjusts the results in the final {@link ResultImpl} such that the given number of results are skipped over
     * and not included in the results.
     *
     * @param offset
     * @return
     */
    @Override
    public Search<T, K> offset(Integer offset) {
        return createNewInstance(queryComponents, sortOrder, limit, offset, accuracy);
    }

    /**
     * Allows control of the accuracy of the number of matches on the response. If the number of
     * matches is less than the given accuracy, then it is absolutely correct. Above that, it is
     * an estimate based on samples. A high number has performance implications.
     *
     * @return
     * @see ResultImpl#getMatchingRecordCount()
     */
    @Override
    public Search<T, K> accuracy(Integer accuracy) {
        return createNewInstance(queryComponents, sortOrder, limit, offset, accuracy);
    }

    /**
     * Performs the search operation by combining all the previously specified search operations, sort orders, limits and offset.
     *
     * @return
     */
    @Override
    public Result<T, K> run() {
        return searchExecutor.createSearchResult(this);
    }

    /**
     * @return the ordered series of query fragments that were specified on this search request
     */
    @Override
    public List<QueryComponent> query() {
        return queryComponents;
    }

    /**
     * @return the ordered series of sort operations that were specified on this search request
     */
    @Override
    public List<OrderComponent> order() {
        return sortOrder;
    }

    /**
     * @return the limit applied to this search request
     */
    @Override
    public Integer limit() {
        return limit;
    }

    /**
     * @return the offset applied to this search request
     */
    @Override
    public Integer offset() {
        return offset;
    }

    /**
     * @return the accuracy applied to this search request
     */
    @Override
    public Integer accuracy() {
        return accuracy;
    }

    @Override
    public Search<T, K> order(String field, boolean ascending) {
        List<OrderComponent> sortOrder = new ArrayList<>(this.sortOrder);
        sortOrder.add(OrderComponent.forField(field, ascending));
        return createNewInstance(queryComponents, sortOrder, limit, offset, accuracy);
    }

    @Override
    public String toString() {
        return (queryComponents.isEmpty() ? "*" : StringUtils.join(queryComponents, " ")) + (sortOrder.isEmpty() ? "" : " " + sortOrder);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    protected Search<T, K> createNewInstance(List<QueryComponent> queryComponents, List<OrderComponent> sortOrder, Integer limit, Integer offset, Integer accuracy) {
        return new SearchImpl<>(searchExecutor, queryComponents, sortOrder, limit, offset, accuracy);
    }

    private List<QueryComponent> components(QueryComponent queryComponent) {
        List<QueryComponent> components = new ArrayList<>(this.queryComponents);
        components.add(queryComponent);
        return components;
    }
}
