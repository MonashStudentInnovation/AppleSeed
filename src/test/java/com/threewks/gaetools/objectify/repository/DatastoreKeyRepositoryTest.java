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

import com.atomicleopard.expressive.Expressive;
import com.google.appengine.api.datastore.Key;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.exception.BaseException;
import com.threewks.gaetools.objectify.ObjectifyModule;
import com.threewks.gaetools.objectify.SetupObjectify;
import com.threewks.gaetools.objectify.repository.test.DatastoreKeyTestEntity;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.SearchModule;
import com.threewks.gaetools.search.gae.SearchConfig;
import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.transformer.TransformerManager;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class DatastoreKeyRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(DatastoreKeyTestEntity.class);

    private DatastoreKeyRepository<DatastoreKeyTestEntity> repository;
    private DatastoreKeyRepository<DatastoreKeyTestEntity> noSearchrepository;

    @Before
    public void before() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();
        IndexTypeLookup indexTypeLookup = SearchModule.defaultIndexTypeLookup();
        SearchConfig searchConfig = new SearchConfig(transformerManager, new FieldMediatorSet(), indexTypeLookup);
        repository = new DatastoreKeyRepository<>(DatastoreKeyTestEntity.class, searchConfig);
        noSearchrepository = new DatastoreKeyRepository<>(DatastoreKeyTestEntity.class, null);
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntity() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        AsyncResult<DatastoreKeyTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        DatastoreKeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        DatastoreKeyTestEntity load = repository.get(testEntity.getKey());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndSearchOfEntity() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        AsyncResult<DatastoreKeyTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        DatastoreKeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        List<DatastoreKeyTestEntity> results = repository.search().field("name", Is.EqualTo, "name").run().getResults();
        assertThat(results, hasItem(testEntity));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntities() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        AsyncResult<List<DatastoreKeyTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<DatastoreKeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<DatastoreKeyTestEntity> load = repository.get(testEntity.getKey(), testEntity2.getKey());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldAllowSaveAndSearchOfMultipleEntities() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        AsyncResult<List<DatastoreKeyTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<DatastoreKeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<DatastoreKeyTestEntity> search = repository.search().field("name", list("name", "name2")).run().getResults();
        assertThat(search, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchReturningIds() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        List<Key> search = repository.search().field("name", list("name", "name2")).run().getResultIds();
        assertThat(search, hasItems(testEntity.getKey(), testEntity2.getKey()));
    }

    @Test
    public void shouldListGivenCount() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<DatastoreKeyTestEntity> list = repository.list(2);
        assertThat(list, hasItems(testEntity, testEntity2));
        assertThat(list.size(), is(2));
    }

    @Test
    public void shouldLoadByField() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<DatastoreKeyTestEntity> list = repository.getByField("name", "name2");
        assertThat(list.size(), is(1));
        assertThat(list, hasItem(testEntity2));
    }

    @Test
    public void shouldLoadByFieldReturningEmptyListWhenNoResults() {
        List<DatastoreKeyTestEntity> list = repository.getByField("name", "none");
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldLoadByFieldCollection() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<DatastoreKeyTestEntity> list = repository.getByField("name", Expressive.<Object>list("name2", "name3"));
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity2, testEntity3));
    }

    @Test
    public void shouldLoadByFieldCollectionReturningEmptyListWhenNoResults() {
        List<DatastoreKeyTestEntity> list = repository.getByField("name", Expressive.<Object>list("none"));
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldSearchAllowingOrderAndLimit() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<DatastoreKeyTestEntity> results = repository.search().field("name", Is.EqualTo, "name").order("id", true).limit(2).run().getResults();
        assertThat(results.size(), is(2));
        assertThat(results, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntityById() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(false));

        repository.deleteByKeyAsync(testEntity.getKey()).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityByEntity() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(false));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesById() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.get(testEntity2.getKey()), is(testEntity2));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().size(), is(2));

        repository.deleteByKeyAsync(testEntity.getKey(), testEntity2.getKey()).complete();

        assertThat(repository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByEntity() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.get(testEntity2.getKey()), is(testEntity2));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().size(), is(2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntityWhenNotSearchIndexing() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        AsyncResult<DatastoreKeyTestEntity> result = noSearchrepository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        DatastoreKeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        DatastoreKeyTestEntity load = noSearchrepository.get(testEntity.getKey());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntitiesWhenNotSearchIndexing() {

        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        AsyncResult<List<DatastoreKeyTestEntity>> result = noSearchrepository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<DatastoreKeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<DatastoreKeyTestEntity> load = noSearchrepository.get(testEntity.getKey(), testEntity2.getKey());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntity() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityWhenNotSearchIndexing() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));

        noSearchrepository.deleteAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntities() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.get(testEntity2.getKey()), is(testEntity2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.get(testEntity2.getKey()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntitiesWhenNotSearchIndexing() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(testEntity2));

        noSearchrepository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntityByIdWhenNotSearchIndexing() {

        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));

        noSearchrepository.deleteByKeyAsync(testEntity.getKey()).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByIdWhenNotSearchIndexing() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name2");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(testEntity2));

        noSearchrepository.deleteByKeyAsync(testEntity.getKey(), testEntity2.getKey()).complete();

        assertThat(noSearchrepository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenSearchWhenNotSearchIndexing() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Unable to search on type DatastoreKeyTestEntity - there is no search service available to this repository");

        noSearchrepository.search();
    }

    @Test
    public void shouldReindexEntities() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "original");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "original");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<DatastoreKeyTestEntity> all = ObjectifyService.ofy().load().type(DatastoreKeyTestEntity.class).list();
        for (DatastoreKeyTestEntity e : all) {
            e.setName("other name");
        }
        ObjectifyService.ofy().save().entities(all).now();

        List<Key> keys = list(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "original").run().getResults().isEmpty(), is(true));
        assertThat(repository.search().field("name", Is.Is, "other name").run().getResults().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesAndWriteBackToDatastoreWhenReindexOperationProvided() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("id", list(1, 2, 3)).run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<Key> keys = list(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (DatastoreKeyTestEntity entity : batch) {
                entity.setName("different");
            }
            return batch;
        });
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "different").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));
        assertThat(repository.getByField("name", "different"), hasItems(testEntity, testEntity2, testEntity3));
    }

    @Test
    public void shouldSearchGreaterThanLong() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.search().field("id", Is.GreaterThan, 0).run().getResults(), hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchGreaterThanEqualsLong() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.search().field("id", Is.GreaterThanOrEqualTo, 1).run().getResults(), hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchGreaterThanEqualsLongs() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(Long.MAX_VALUE, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(Integer.MAX_VALUE, "name");
        DatastoreKeyTestEntity testEntity3 = new DatastoreKeyTestEntity((long) Float.MAX_VALUE, "name");
        DatastoreKeyTestEntity testEntity4 = new DatastoreKeyTestEntity((long) Double.MAX_VALUE, "name");
        repository.putAsync(testEntity).complete();
        repository.putAsync(testEntity2).complete();
        repository.putAsync(testEntity3).complete();
        repository.putAsync(testEntity4).complete();

        assertThat(repository.search().field("id", Is.GreaterThanOrEqualTo, 1).run().getResults(), hasItems(testEntity, testEntity2, testEntity3, testEntity4));
    }

    @Test
    public void shouldSearchLessThanLong() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.search().field("id", Is.LessThan, 10).run().getResults(), hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchLessThanEqualsLong() {
        DatastoreKeyTestEntity testEntity = new DatastoreKeyTestEntity(1, "name");
        DatastoreKeyTestEntity testEntity2 = new DatastoreKeyTestEntity(2, "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.search().field("id", Is.LessThanOrEqualTo, 2).run().getResults(), hasItems(testEntity, testEntity2));
    }
}
