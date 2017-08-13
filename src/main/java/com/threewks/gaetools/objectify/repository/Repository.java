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

import com.threewks.gaetools.search.Search;

import java.util.List;

/**
 * A base interface for the <a href="http://martinfowler.com/eaaCatalog/repository.html">Repository</a> pattern.
 *
 * @param <E> The entity type
 * @param <K> The key type of the entity
 */
public interface Repository<E, K> {
    /**
     * Save the given entity
     *
     * @param entity
     * @return the entity
     */
    E put(final E entity);

    /**
     * Save the given entities.
     *
     * @param entities
     * @return the list of saved entities
     */
    @SuppressWarnings("unchecked")
    List<E> put(E... entities);

    /**
     * Save the given entities.
     *
     * @param entities
     * @return the list of saved entities
     */
    List<E> put(final List<E> entities);

    /**
     * Load the entity with the given id
     *
     * @param key
     * @return the entity, or null if no entity exists
     */
    E get(K key);

    /**
     * Load the entities with the given ids
     *
     * @param keys
     * @return a list containing an entry for each corresponding id, containing the entity or null if none exists
     */
    @SuppressWarnings("unchecked")
    List<E> get(K... keys);

    /**
     * Load the entities with the given ids
     *
     * @param keys
     * @return a list containing an entry for each corresponding id, containing the entity or null if none exists
     */
    List<E> get(Iterable<K> keys);

    /**
     * List up to count entities.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param count
     * @return
     */
    List<E> list(int count);

    /**
     * Load all entities whose field has the value of the given object.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field
     * @param value
     * @return
     */
    List<E> getByField(String field, Object value);

    /**
     * Load all entities who field has the values of any of the given objects.
     * Note that the given field must be indexed for anything to be returned.
     * This will load all entities into memory, so should only be used where the number of entities is constrained.
     *
     * @param field
     * @param values
     * @return
     */
    List<E> getByField(String field, List<?> values);

    /**
     * @return a builder for a search operation
     */
    Search<E, K> search();

    /**
     * Delete the entity with the given id
     *
     * @param key
     */
    void deleteByKey(K key);

    /**
     * Delete the entities with the given ids
     *
     * @param keys
     */
    @SuppressWarnings("unchecked")
    void deleteByKey(K... keys);

    /**
     * Delete the entities with the given ids
     *
     * @param ids
     */
    void deleteByKey(Iterable<K> ids);

    /**
     * Delete the given entity
     *
     * @param entity
     */
    void delete(E entity);

    /**
     * Delete the given entities
     *
     * @param entities
     */
    @SuppressWarnings("unchecked")
    void delete(E... entities);

    /**
     * Delete the given entities
     *
     * @param entities
     */
    void delete(Iterable<E> entities);

    /**
     * Reindexes all the entities matching the given list of keys. The given {@link ReindexOperation}, if present will
     * be applied to each batch of entities.
     *
     * @param keys
     * @param batchSize
     * @param reindexOperation
     * @return the overall count of re-indexed entities.
     */
    int reindex(List<K> keys, int batchSize, ReindexOperation<E> reindexOperation);
}
