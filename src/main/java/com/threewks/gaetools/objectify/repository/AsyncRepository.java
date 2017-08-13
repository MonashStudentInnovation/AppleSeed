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

import java.util.List;

/**
 * An interface extension for {@link Repository} which enables asynchronous interfaces for
 * save and delete operations.
 *
 * @param <E> The entity type
 * @param <K> The key type of the entity
 */
public interface AsyncRepository<E, K> extends Repository<E, K> {
    AsyncResult<E> putAsync(final E entity);

    /**
     * Save the given entities.
     *
     * @param entities
     * @return an async result to complete the save operation
     */
    @SuppressWarnings("unchecked")
    AsyncResult<List<E>> putAsync(E... entities);

    /**
     * Save the given entities.
     *
     * @param entities
     * @return an async result to complete the save operation
     */
    AsyncResult<List<E>> putAsync(final List<E> entities);

    /**
     * Delete the entity with the given id
     *
     * @param key
     * @return an async operation used to complete the delete operation
     */
    AsyncResult<Void> deleteByKeyAsync(K key);

    /**
     * Delete the entities with the given ids
     *
     * @param keys
     * @return an async operation used to complete the delete operation
     */
    @SuppressWarnings("unchecked")
    AsyncResult<Void> deleteByKeyAsync(K... keys);

    /**
     * Delete the entities with the given ids
     *
     * @param ids
     * @return an async operation used to complete the delete operation
     */
    AsyncResult<Void> deleteByKeyAsync(Iterable<K> ids);

    /**
     * Delete the given entity
     *
     * @param entity
     * @return an async operation used to complete the delete operation
     */
    AsyncResult<Void> deleteAsync(E entity);

    /**
     * Delete the given entities
     *
     * @param entities
     * @return an async operation used to complete the delete operation
     */
    @SuppressWarnings("unchecked")
    AsyncResult<Void> deleteAsync(E... entities);

    /**
     * Delete the given entities
     *
     * @param entities
     * @return an async operation used to complete the delete operation
     */
    AsyncResult<Void> deleteAsync(Iterable<E> entities);
}
