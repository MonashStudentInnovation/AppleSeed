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
package com.threewks.gaetools.search;

import java.util.Collection;
import java.util.List;

/**
 * Represents a search on the {@link TextSearchService}. Searches are performed by building the request up by invoking
 * fluent methods.
 *
 * @param <T>
 */
public interface Search<T, K> {

    /**
     * Includes a string query which applies across all fields in the index.
     *
     * @param query
     * @return
     */
    Search<T, K> query(CharSequence query);

    /**
     * Defines a search operation on the given field to apply to the current search.
     *
     * @param field
     * @return
     */
    <V> Search<T, K> field(String field, Is is, V value);

    /**
     * Defines a search operation on the given field to match one of the given values
     *
     * @param field
     * @return
     */
    <V> Search<T, K> field(String field, Collection<V> values);

    /**
     * Limits the number of results in the final {@link Result}
     *
     * @param limit
     * @return
     */
    Search<T, K> limit(Integer limit);

    /**
     * Adjusts the results in the final {@link Result} such that the given number of results are skipped over
     * and not included in the results.
     *
     * @param offset
     * @return
     */
    Search<T, K> offset(Integer offset);

    /**
     * Allows control of the accuracy of the number of matches on the response. If the number of
     * matches is less than the given accuracy, then it is absolutely correct. Above that, it is
     * an estimate based on samples. A high number has performance implications.
     *
     * @return
     * @see Result#getMatchingRecordCount()
     */
    Search<T, K> accuracy(Integer accuracy);

    /**
     * Defines a sort order on the given field to apply to the current search.
     *
     * @param field
     * @return
     */
    Search<T, K> order(String field, boolean ascending);

    /**
     * Performs the search operation by combining all the previously specified search operations, sort orders, limits and offset.
     *
     * @return
     */
    Result<T, K> run();

    /**
     * @return the ordered series of query fragments that were specified on this search request
     */
    List<QueryComponent> query();

    /**
     * @return the ordered series of sort operations that were specified on this search request
     */
    List<OrderComponent> order();

    /**
     * @return the limit applied to this search request
     */
    Integer limit();

    /**
     * @return the offset applied to this search request
     */
    Integer offset();

    /**
     * @return the accuracy applied to this search request
     */
    Integer accuracy();

}
