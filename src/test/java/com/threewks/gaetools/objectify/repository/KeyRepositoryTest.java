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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.exception.BaseException;
import com.threewks.gaetools.objectify.ObjectifyModule;
import com.threewks.gaetools.objectify.SetupObjectify;
import com.threewks.gaetools.objectify.repository.test.KeyTestEntity;
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

import java.util.Arrays;
import java.util.List;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class KeyRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(KeyTestEntity.class);

    private KeyRepository<KeyTestEntity> repository;
    private KeyRepository<KeyTestEntity> noSearchrepository;

    @Before
    public void before() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();
        IndexTypeLookup indexTypeLookup = SearchModule.defaultIndexTypeLookup();
        SearchConfig searchConfig = new SearchConfig(transformerManager, new FieldMediatorSet(), indexTypeLookup);
        repository = new KeyRepository<>(KeyTestEntity.class, searchConfig);
        noSearchrepository = new KeyRepository<>(KeyTestEntity.class, null);
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntity() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        AsyncResult<KeyTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        KeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        KeyTestEntity load = repository.get(testEntity.getKey());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndSearchOfEntity() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        AsyncResult<KeyTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        KeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        List<KeyTestEntity> results = repository.search().field("name", Is.EqualTo, "name").run().getResults();
        assertThat(results, hasItem(testEntity));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntities() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        AsyncResult<List<KeyTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<KeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<KeyTestEntity> load = repository.get(testEntity.getKey(), testEntity2.getKey());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldAllowSaveAndSearchOfMultipleEntities() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        AsyncResult<List<KeyTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<KeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<KeyTestEntity> search = repository.search().field("name", list("name", "name2")).run().getResults();
        assertThat(search, hasItems(testEntity, testEntity2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSearchReturningIds() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        List<Key<KeyTestEntity>> search = repository.search().field("name", list("name", "name2")).run().getResultIds();
        assertThat(search, hasItems(testEntity.getKey(), testEntity2.getKey()));
    }

    @Test
    public void shouldListGivenCount() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<KeyTestEntity> list = repository.list(2);
        assertThat(list, hasItems(testEntity, testEntity2));
        assertThat(list.size(), is(2));
    }

    @Test
    public void shouldLoadByField() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<KeyTestEntity> list = repository.getByField("name", "name2");
        assertThat(list.size(), is(1));
        assertThat(list, hasItem(testEntity2));
    }

    @Test
    public void shouldLoadByFieldReturningEmptyListWhenNoResults() {
        List<KeyTestEntity> list = repository.getByField("name", "none");
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldLoadByFieldCollection() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<KeyTestEntity> list = repository.getByField("name", Expressive.<Object>list("name2", "name3"));
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity2, testEntity3));
    }

    @Test
    public void shouldLoadByFieldCollectionReturningEmptyListWhenNoResults() {
        List<KeyTestEntity> list = repository.getByField("name", Expressive.<Object>list("none"));
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldSearchAllowingOrderAndLimit() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<KeyTestEntity> results = repository.search().field("name", Is.EqualTo, "name").order("id", true).limit(2).run().getResults();
        assertThat(results.size(), is(2));
        assertThat(results, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntityById() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(false));

        repository.deleteByKeyAsync(testEntity.getKey()).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.search().field("key", Is.EqualTo, testEntity.getKey()).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityByEntity() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
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
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.get(testEntity2.getKey()), is(testEntity2));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResultIds().size(), is(2));

        repository.deleteByKeyAsync(testEntity.getKey(), testEntity2.getKey()).complete();

        assertThat(repository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByEntity() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));
        assertThat(repository.get(testEntity2.getKey()), is(testEntity2));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResultIds().size(), is(2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("key", list(testEntity.getKey(), testEntity2.getKey())).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntityWhenNotSearchIndexing() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        AsyncResult<KeyTestEntity> result = noSearchrepository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        KeyTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        KeyTestEntity load = noSearchrepository.get(testEntity.getKey());
        assertThat(load.equals(testEntity), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntitiesWhenNotSearchIndexing() {

        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        AsyncResult<List<KeyTestEntity>> result = noSearchrepository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<KeyTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<KeyTestEntity> load = noSearchrepository.get(testEntity.getKey(), testEntity2.getKey());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntity() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(testEntity));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getKey()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityWhenNotSearchIndexing() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));

        noSearchrepository.deleteAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntities() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name");
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
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(testEntity2));

        noSearchrepository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntityByIdWhenNotSearchIndexing() {

        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));

        noSearchrepository.deleteByKeyAsync(testEntity.getKey()).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(nullValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByIdWhenNotSearchIndexing() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name2");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getKey()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getKey()), is(testEntity2));

        noSearchrepository.deleteByKeyAsync(testEntity.getKey(), testEntity2.getKey()).complete();

        assertThat(noSearchrepository.get(testEntity.getKey(), testEntity2.getKey()), Matchers.hasItems(nullValue(), nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenSearchWhenNotSearchIndexing() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Unable to search on type KeyTestEntity - there is no search service available to this repository");

        noSearchrepository.search();
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearch() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "original");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "original");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<KeyTestEntity> all = ObjectifyService.ofy().load().type(KeyTestEntity.class).list();
        for (KeyTestEntity e : all) {
            e.setName("other name");
        }
        ObjectifyService.ofy().save().entities(all).now();

        List<Key<KeyTestEntity>> keys = Arrays.asList(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "original").run().getResults().isEmpty(), is(true));
        assertThat(repository.search().field("name", Is.Is, "other name").run().getResults().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearchAndWriteBackToDatastoreWhenReindexOperationProvided() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("id", Is.GreaterThan, 0).run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<Key<KeyTestEntity>> keys = Arrays.asList(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (KeyTestEntity entity : batch) {
                entity.setName("different");
            }
            return batch;
        });
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "different").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));
        assertThat(repository.getByField("name", "different"), hasItems(testEntity, testEntity2, testEntity3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnKeys() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "name");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "name");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults(), hasSize(0));

        List<Key<KeyTestEntity>> keys = Arrays.asList(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "name").run().getResultIds().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnKeysAndWriteBackToDatastoreWhenReindexOperationProvided() {
        KeyTestEntity testEntity = new KeyTestEntity(1, "original");
        KeyTestEntity testEntity2 = new KeyTestEntity(2, "original");
        KeyTestEntity testEntity3 = new KeyTestEntity(3, "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasSize(0));

        List<Key<KeyTestEntity>> keys = Arrays.asList(testEntity.getKey(), testEntity2.getKey(), testEntity3.getKey());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (KeyTestEntity entity : batch) {
                entity.setName("different");
            }
            return batch;
        });
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "different").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));
        assertThat(repository.getByField("name", "different"), hasItems(testEntity, testEntity2, testEntity3));
    }
}
