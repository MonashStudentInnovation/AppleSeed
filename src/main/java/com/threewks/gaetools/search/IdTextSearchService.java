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

import com.threewks.gaetools.search.gae.IdGaeSearchService;

import java.util.Map;

/**
 * The {@link IdTextSearchService} provides a java class based abstraction over a document search service.
 * Implementations of this service use {@link SearchIndex} annotations to determine
 * what fields of the target object to index, and a provided id as the unique identifier.
 * <p>
 * If you want to provide an id from one of the object's internal fields, use the {@link TextSearchService} instead.
 *
 * @see IdGaeSearchService
 */
public interface IdTextSearchService<T, K> {

    /**
     * Index the given object with the given id. This will replace any previous data indexed with the given id
     *
     * @param object the object to index.
     * @param id     to use when indexing the object
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation index(T object, K id);

    /**
     * Index the given objects with the ids provided as keys
     *
     * @param objects
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation index(Map<K, T> objects);

    /**
     * Remove the object previously indexed with the given id
     *
     * @param id
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation removeById(K id);

    /**
     * Remove the objects previously indexed with the given ids
     *
     * @param ids
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation removeById(Iterable<K> ids);

    /**
     * Remove all objects from the index.
     *
     * @return the number of objects removed from the index.
     */
    int removeAll();

    /**
     * Create a {@link Search} for the given type. This will return an object which provides a fluent
     * interface for customising a search
     *
     * @return a {@link Search} that can be used to search.
     */
    Search<T, K> search();
}
