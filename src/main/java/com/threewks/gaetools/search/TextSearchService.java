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

import com.threewks.gaetools.search.gae.GaeSearchService;

import java.util.Collection;

/**
 * The {@link TextSearchService} provides a java class based abstraction over a document search service.
 * Implementations of this service use {@link SearchId} and {@link SearchIndex} annotations to determine
 * what fields of the target object to index. If you need to provide an external id, rather than
 * relying on internal object data use the {@link IdTextSearchService} instead.
 *
 * @see GaeSearchService
 */
public interface TextSearchService<T, K> {

    /**
     * Index the given object using the fields annotated with {@link SearchId} and {@link SearchIndex}, replacing
     * any previously indexed data
     *
     * @param object the object to index.
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation index(T object);

    /**
     * Index the given objects using the fields annotated with {@link SearchId} and {@link SearchIndex}.
     *
     * @param objects
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation index(Collection<T> objects);

    /**
     * Remove the object with the given id from the index
     *
     * @param id
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation removeById(K id);

    /**
     * Remove the given object from the index
     *
     * @param object
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation remove(T object);

    /**
     * Remove the objects with the given ids from the index.
     *
     * @param ids
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation removeById(Iterable<K> ids);

    /**
     * Remove the given objects from the index
     *
     * @param objects
     * @return an asynchronous result wrapper. Call {@link IndexOperation#complete()} to complete the operation.
     */
    IndexOperation remove(Iterable<T> objects);

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
