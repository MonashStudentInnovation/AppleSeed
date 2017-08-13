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

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.transform.CollectionTransformer;
import com.google.appengine.api.search.ScoredDocument;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.threewks.gaetools.exception.BaseException;
import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.search.IndexOperation;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.gae.IdGaeSearchService;
import com.threewks.gaetools.search.gae.SearchConfig;
import com.threewks.gaetools.search.gae.SearchExecutor;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

public abstract class AbstractRepository<E, K> implements AsyncRepository<E, K> {
    protected IdGaeSearchService<E, Key<E>> searchService;
    protected Class<E> entityType;
    protected Field idField;
    protected boolean isSearchable;
    protected ETransformer<Iterable<E>, Map<Key<E>, E>> toKeyLookup;
    protected ETransformer<com.google.appengine.api.datastore.Key, Key<E>> toOfyKey;
    protected CollectionTransformer<com.google.appengine.api.datastore.Key, Key<E>> toOfyKeys;
    protected ETransformer<E, Object> toId;
    protected CollectionTransformer<E, Object> toIds;
    protected ETransformer<K, Key<E>> toKey;
    protected CollectionTransformer<K, Key<E>> toKeys;
    protected ETransformer<E, Key<E>> toKeyFromEntity;
    protected CollectionTransformer<E, Key<E>> toKeysFromEntities;
    protected ETransformer<Key<E>, K> fromKey;
    protected CollectionTransformer<Key<E>, K> fromKeys;

    public AbstractRepository(Class<E> entityType, ETransformer<K, Key<E>> toKey, ETransformer<Key<E>, K> fromKey, SearchConfig searchConfig) {
        this.entityType = entityType;
        this.searchService = createIdGaeSearchService(searchConfig);
        this.isSearchable = searchService != null && searchService.hasIndexableFields();
        this.idField = idField(entityType);
        this.toKey = toKey;
        this.toKeys = new CollectionTransformer<>(toKey);
        this.fromKey = fromKey;
        this.fromKeys = new CollectionTransformer<>(fromKey);

        this.toId = from -> {
            try {
                return idField.get(from);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RepositoryException(e, "Unable to access '%s.%s' - cannot extract an id: %s", AbstractRepository.this.entityType.getSimpleName(), idField.getName(), e.getMessage());
            }
        };
        this.toIds = Expressive.Transformers.transformAllUsing(toId);
        this.toKeyLookup = from -> {
            Map<Key<E>, E> lookup = new LinkedHashMap<>();
            for (E e : from) {
                lookup.put(key(e), e);
            }
            return lookup;
        };
        this.toKeyFromEntity = Key::create;
        this.toKeysFromEntities = Expressive.Transformers.transformAllUsing(toKeyFromEntity);
    }

    @Override
    public E put(E entity) {
        return putAsync(entity).complete();
    }

    @Override
    public AsyncResult<E> putAsync(final E entity) {
        boolean hasId = hasId(entity);
        final Result<Key<E>> ofyFuture = ofy().save().entity(entity);
        if (!hasId) {
            // if no id exists - we need objectify to complete so that the id can be used in indexing the record.
            ofyFuture.now();
        }
        final IndexOperation searchFuture = shouldSearch() ? index(entity) : null;
        return () -> {
            ofyFuture.now();
            if (searchFuture != null) {
                searchFuture.complete();
            }
            return entity;
        };
    }

    @Override
    public List<E> put(@SuppressWarnings("unchecked") E... entities) {
        return putAsync(entities).complete();
    }

    @Override
    @SuppressWarnings("unchecked")
    public AsyncResult<List<E>> putAsync(E... entities) {
        return putAsync(Arrays.asList(entities));
    }

    @Override
    public List<E> put(List<E> entities) {
        return putAsync(entities).complete();
    }

    @Override
    public AsyncResult<List<E>> putAsync(final List<E> entities) {
        List<Object> ids = toIds.from(entities);
        final Result<Map<Key<E>, E>> ofyFuture = ofy().save().entities(entities);
        if (ids.contains(null)) {
            ofyFuture.now(); // force sync save
        }
        final IndexOperation searchFuture = shouldSearch() ? index(entities) : null;
        return () -> {
            ofyFuture.now();
            if (searchFuture != null) {
                searchFuture.complete();
            }
            return entities;
        };
    }

    protected E loadInternal(Key<E> keys) {
        return ofy().load().key(keys).now();
    }

    protected List<E> loadInternal(Iterable<Key<E>> keys) {
        if (Expressive.isEmpty(keys)) {
            return Collections.<E>emptyList();
        }
        Map<Key<E>, E> results = ofy().load().keys(keys);
        return Expressive.Transformers.transformAllUsing(Expressive.Transformers.usingLookup(results)).from(keys);
    }

    @Override
    public E get(K key) {
        Key<E> ofyKey = toKey.from(key);
        return loadInternal(ofyKey);
    }

    @Override
    public List<E> get(Iterable<K> keys) {
        EList<Key<E>> ofyKeys = toKeys.from(keys);
        return loadInternal(ofyKeys);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> get(K... keys) {
        return get(Arrays.asList(keys));
    }

    @Override
    public List<E> list(int count) {
        return ofy().load().type(entityType).limit(count).list();
    }

    @Override
    public List<E> getByField(String field, Object value) {
        return ofy().load().type(entityType).filter(field, value).list();
    }

    @Override
    public List<E> getByField(String field, List<?> values) {
        return ofy().load().type(entityType).filter(field + " in", values).list();
    }

    @Override
    public void deleteByKey(K key) {
        deleteByKeyAsync(key).complete();
    }

    @Override
    public AsyncResult<Void> deleteByKeyAsync(K key) {
        Key<E> ofyKey = toKey.from(key);
        final Result<Void> ofyDelete = deleteInternal(ofyKey);
        final IndexOperation searchDelete = shouldSearch() ? searchService.removeById(ofyKey) : null;
        return () -> {
            ofyDelete.now();
            if (searchDelete != null) {
                searchDelete.complete();
            }
            return null;
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteByKey(K... keys) {
        deleteByKeyAsync(keys).complete();
    }

    @SuppressWarnings("unchecked")
    @Override
    public AsyncResult<Void> deleteByKeyAsync(K... keys) {
        return deleteByKeyAsync(Arrays.asList(keys));
    }

    @Override
    public void deleteByKey(Iterable<K> ids) {
        deleteByKeyAsync(ids).complete();
    }

    @Override
    public AsyncResult<Void> deleteByKeyAsync(Iterable<K> keys) {
        EList<Key<E>> ofyKeys = toKeys.from(keys);
        final Result<Void> ofyDelete = deleteInternal(ofyKeys);
        final IndexOperation searchDelete = shouldSearch() ? searchService.removeById(ofyKeys) : null;
        return () -> {
            ofyDelete.now();
            if (searchDelete != null) {
                searchDelete.complete();
            }
            return null;
        };
    }

    @Override
    public void delete(E entity) {
        deleteAsync(entity).complete();
    }

    @Override
    public AsyncResult<Void> deleteAsync(E e) {
        final Result<Void> ofyDelete = ofy().delete().entity(e);
        final IndexOperation searchDelete = shouldSearch() ? searchService.removeById(Key.create(e)) : null;
        return () -> {
            ofyDelete.now();
            if (searchDelete != null) {
                searchDelete.complete();
            }
            return null;
        };
    }

    @Override
    public void delete(Iterable<E> entities) {
        deleteAsync(entities).complete();
    }

    @Override
    public AsyncResult<Void> deleteAsync(Iterable<E> entities) {
        final Result<Void> ofyDelete = ofy().delete().entities(entities);
        final IndexOperation searchDelete = shouldSearch() ? searchService.removeById(toKeysFromEntities.from(entities)) : null;
        return () -> {
            ofyDelete.now();
            if (searchDelete != null) {
                searchDelete.complete();
            }
            return null;
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(E... entities) {
        deleteAsync(entities).complete();

    }

    @SuppressWarnings("unchecked")
    @Override
    public AsyncResult<Void> deleteAsync(E... entities) {
        return deleteAsync(Arrays.asList(entities));
    }

    @Override
    public Search<E, K> search() {
        if (!isSearchable) {
            throw new BaseException("Unable to search on type %s - there is no search service available to this repository", entityType.getSimpleName());
        }
        return new SearchImpl<>(this, searchService.search());
    }

    @Override
    public int reindex(List<K> keys, int batchSize, ReindexOperation<E> reindexOperation) {
        int count = 0;
        List<List<K>> batches = Lists.partition(keys, batchSize);
        for (List<K> batchKeys : batches) {
            List<E> batch = get(batchKeys);
            batch = reindexOperation == null ? batch : reindexOperation.apply(batch);
            if (reindexOperation != null) {
                // we only re-save the batch when a re-index op is supplied, otherwise the data can't have changed.
                ofy().save().entities(batch).now();
            }
            if (shouldSearch()) {
                index(batch).complete();
            }
            count += batch.size();
            ofy().clear(); // Clear the Objectify cache to free memory for next batch
            Logger.info("Reindexed %d entities of type %s, %d of %d", batch.size(), entityType.getSimpleName(), count, keys.size());
        }
        return count;
    }

    protected IndexOperation index(final E entity) {
        return searchService.index(entity, key(entity));
    }

    protected IndexOperation index(List<E> batch) {
        Map<Key<E>, E> keyedLookup = toKeyLookup.from(batch);
        return searchService.index(keyedLookup);
    }

    protected Result<Void> deleteInternal(Key<E> key) {
        return ofy().delete().key(key);
    }

    protected Result<Void> deleteInternal(Iterable<Key<E>> keys) {
        return ofy().delete().keys(keys);
    }

    protected Key<E> key(E entity) {
        return Key.create(entity);
    }

    protected boolean hasId(E entity) {
        try {
            return idField.get(entity) != null;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RepositoryException(e, "Unable to determine if an id exists for a %s - %s: %s", entityType.getSimpleName(), entity, e.getMessage());
        }
    }

    protected boolean shouldSearch() {
        return isSearchable;
    }

    protected Field idField(Class<E> entityType) {
        try {
            String idFieldName = ObjectifyService.factory().getMetadata(entityType).getKeyMetadata().getIdFieldName();
            return FieldUtils.getField(entityType, idFieldName, true);
        } catch (IllegalArgumentException | SecurityException e) {
            throw new RepositoryException(e, "Unable to determine id field for type %s: %s", entityType.getClass().getName(), e.getMessage());
        }
    }

    public SearchExecutor<E, K, SearchImpl<E, K>> getSearchExecutor() {
        return this.searchExecutor;
    }

    protected SearchExecutor<E, K, SearchImpl<E, K>> searchExecutor = new SearchExecutor<E, K, SearchImpl<E, K>>() {
        @Override
        public List<K> getResultsAsIds(List<ScoredDocument> results) {
            return fromKeys.from(searchService.getResultsAsIds(results));
        }

        @Override
        public List<E> getResults(java.util.List<ScoredDocument> results) {
            return loadInternal(searchService.getResultsAsIds(results));
        }

        @Override
        public com.threewks.gaetools.search.Result<E, K> createSearchResult(SearchImpl<E, K> searchRequest) {
            Search<E, Key<E>> delegate = searchRequest.getSearchRequest();
            final com.threewks.gaetools.search.Result<E, Key<E>> delegateResults = delegate.run();
            return new com.threewks.gaetools.search.Result<E, K>() {
                @Override
                public List<E> getResults() throws SearchException {
                    return loadInternal(delegateResults.getResultIds());
                }

                @Override
                public List<K> getResultIds() throws SearchException {
                    return fromKeys.from(delegateResults.getResultIds());
                }

                @Override
                public long getMatchingRecordCount() {
                    return delegateResults.getMatchingRecordCount();
                }

                @Override
                public long getReturnedRecordCount() {
                    return delegateResults.getReturnedRecordCount();
                }

                @Override
                public String cursor() {
                    return delegateResults.cursor();
                }

            };
        }

    };

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<Key<T>> keyClass() {
        Class keyClass = Key.class;
        return (Class<Key<T>>) keyClass;
    }

    /**
     * Extension point allowing the IdGaeSearchService implementation to be modified
     */
    protected IdGaeSearchService<E, Key<E>> createIdGaeSearchService(SearchConfig searchConfig) {
        return searchConfig == null ? null : new IdGaeSearchService<>(entityType, AbstractRepository.<E>keyClass(), searchConfig);
    }

}
