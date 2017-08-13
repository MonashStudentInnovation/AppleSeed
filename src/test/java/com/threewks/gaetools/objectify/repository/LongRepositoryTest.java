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
import com.googlecode.objectify.ObjectifyService;
import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.exception.BaseException;
import com.threewks.gaetools.objectify.ObjectifyModule;
import com.threewks.gaetools.objectify.SetupObjectify;
import com.threewks.gaetools.objectify.repository.test.LongTestEntity;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.Search;
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

public class LongRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(LongTestEntity.class);

    private LongRepository<LongTestEntity> repository;
    private LongRepository<LongTestEntity> noSearchrepository;

    @Before
    public void before() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();
        IndexTypeLookup indexTypeLookup = SearchModule.defaultIndexTypeLookup();
        SearchConfig searchConfig = new SearchConfig(transformerManager, new FieldMediatorSet(), indexTypeLookup);
        repository = new LongRepository<>(LongTestEntity.class, searchConfig);
        noSearchrepository = new LongRepository<>(LongTestEntity.class, null);
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntity() {
        LongTestEntity testEntity = new LongTestEntity("name");
        AsyncResult<LongTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        LongTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        LongTestEntity load = repository.get(testEntity.getId());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndSearchOfEntity() {
        LongTestEntity testEntity = new LongTestEntity("name");
        AsyncResult<LongTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        LongTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        List<LongTestEntity> results = repository.search().field("name", Is.EqualTo, "name").run().getResults();
        assertThat(results, hasItem(testEntity));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntities() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        AsyncResult<List<LongTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<LongTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<LongTestEntity> load = repository.get(testEntity.getId(), testEntity2.getId());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldAllowSaveAndSearchOfMultipleEntities() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        AsyncResult<List<LongTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<LongTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<LongTestEntity> search = repository.search().field("name", list("name", "name2")).run().getResults();
        assertThat(search, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchReturningIds() {
        LongTestEntity testEntity = new LongTestEntity(1, "name");
        LongTestEntity testEntity2 = new LongTestEntity(2, "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        List<Long> search = repository.search().field("name", list("name", "name2")).run().getResultIds();
        assertThat(search, hasItems(1L, 2L));
    }

    @Test
    public void shouldListGivenCount() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        LongTestEntity testEntity3 = new LongTestEntity("name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<LongTestEntity> list = repository.list(2);
        assertThat(list, hasItems(testEntity, testEntity2));
        assertThat(list.size(), is(2));
    }

    @Test
    public void shouldLoadByField() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        LongTestEntity testEntity3 = new LongTestEntity("name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<LongTestEntity> list = repository.getByField("name", "name2");
        assertThat(list.size(), is(1));
        assertThat(list, hasItem(testEntity2));
    }

    @Test
    public void shouldLoadByFieldReturningEmptyListWhenNoResults() {
        List<LongTestEntity> list = repository.getByField("name", "none");
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldLoadByFieldCollection() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        LongTestEntity testEntity3 = new LongTestEntity("name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<LongTestEntity> list = repository.getByField("name", Expressive.<Object>list("name2", "name3"));
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity2, testEntity3));
    }

    @Test
    public void shouldLoadByFieldCollectionReturningEmptyListWhenNoResults() {
        List<LongTestEntity> list = repository.getByField("name", Expressive.<Object>list("none"));
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldSearchAllowingOrderAndLimit() {
        LongTestEntity testEntity = new LongTestEntity(1, "name");
        LongTestEntity testEntity2 = new LongTestEntity(2, "name");
        LongTestEntity testEntity3 = new LongTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<LongTestEntity> results = repository.search().field("name", Is.EqualTo, "name").order("id", true).limit(2).run().getResults();
        assertThat(results.size(), is(2));
        assertThat(results, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntityById() {
        LongTestEntity testEntity = new LongTestEntity("name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResults().isEmpty(), is(false));

        repository.deleteByKeyAsync(testEntity.getId()).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityByEntity() {
        LongTestEntity testEntity = new LongTestEntity("name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResults().isEmpty(), is(false));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResults().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesById() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds().size(), is(2));

        repository.deleteByKeyAsync(testEntity.getId(), testEntity2.getId()).complete();

        assertThat(repository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResults().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByEntity() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds().size(), is(2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntityWhenNotSearchIndexing() {
        LongTestEntity testEntity = new LongTestEntity("name");
        AsyncResult<LongTestEntity> result = noSearchrepository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        LongTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        LongTestEntity load = noSearchrepository.get(testEntity.getId());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntitiesWhenNotSearchIndexing() {

        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        AsyncResult<List<LongTestEntity>> result = noSearchrepository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<LongTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<LongTestEntity> load = noSearchrepository.get(testEntity.getId(), testEntity2.getId());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntity() {
        LongTestEntity testEntity = new LongTestEntity("name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityWhenNotSearchIndexing() {
        LongTestEntity testEntity = new LongTestEntity("name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));

        noSearchrepository.deleteAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntities() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.get(testEntity2.getId()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntitiesWhenNotSearchIndexing() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(testEntity2));

        noSearchrepository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntityByIdWhenNotSearchIndexing() {

        LongTestEntity testEntity = new LongTestEntity("name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));

        noSearchrepository.deleteByKeyAsync(testEntity.getId()).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByIdWhenNotSearchIndexing() {
        LongTestEntity testEntity = new LongTestEntity("name");
        LongTestEntity testEntity2 = new LongTestEntity("name2");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(testEntity2));

        noSearchrepository.deleteByKeyAsync(testEntity.getId(), testEntity2.getId()).complete();

        assertThat(noSearchrepository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenSearchWhenNotSearchIndexing() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Unable to search on type LongTestEntity - there is no search service available to this repository");

        noSearchrepository.search();
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearch() {
        LongTestEntity testEntity = new LongTestEntity(1, "original");
        LongTestEntity testEntity2 = new LongTestEntity(2, "original");
        LongTestEntity testEntity3 = new LongTestEntity(3, "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        Search<LongTestEntity, Long> search = repository.search().field("name", Is.Is, "original");
        assertThat(search.run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<LongTestEntity> all = ObjectifyService.ofy().load().type(LongTestEntity.class).list();
        for (LongTestEntity e : all) {
            e.setName("other name");
        }
        ObjectifyService.ofy().save().entities(all).now();

        List<Long> keys = list(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(search.run().getResults().isEmpty(), is(true));
        assertThat(repository.search().field("name", Is.Is, "other name").run().getResultIds().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearchAndWriteBackToDatastoreWhenReindexOperationProvided() {
        LongTestEntity testEntity = new LongTestEntity(1, "name");
        LongTestEntity testEntity2 = new LongTestEntity(2, "name");
        LongTestEntity testEntity3 = new LongTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("id", Is.GreaterThan, 0).run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<Long> keys = list(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (LongTestEntity entity : batch) {
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
        LongTestEntity testEntity = new LongTestEntity(1, "name");
        LongTestEntity testEntity2 = new LongTestEntity(2, "name");
        LongTestEntity testEntity3 = new LongTestEntity(3, "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults(), hasSize(0));

        List<Long> keys = Arrays.asList(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "name").run().getResultIds().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnKeysAndWriteBackToDatastoreWhenReindexOperationProvided() {
        LongTestEntity testEntity = new LongTestEntity(1, "original");
        LongTestEntity testEntity2 = new LongTestEntity(2, "original");
        LongTestEntity testEntity3 = new LongTestEntity(3, "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasSize(0));

        List<Long> keys = Arrays.asList(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (LongTestEntity entity : batch) {
                entity.setName("different");
            }
            return batch;
        });
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "different").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));
        assertThat(repository.getByField("name", "different"), hasItems(testEntity, testEntity2, testEntity3));
    }
}
