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
import com.threewks.gaetools.objectify.repository.test.StringTestEntity;
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

public class StringRepositoryTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(StringTestEntity.class);

    private StringRepository<StringTestEntity> repository;
    private StringRepository<StringTestEntity> noSearchrepository;

    @Before
    public void before() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();
        IndexTypeLookup indexTypeLookup = SearchModule.defaultIndexTypeLookup();
        SearchConfig searchConfig = new SearchConfig(transformerManager, new FieldMediatorSet(), indexTypeLookup);
        repository = new StringRepository<>(StringTestEntity.class, searchConfig);
        noSearchrepository = new StringRepository<>(StringTestEntity.class, null);
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntity() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        AsyncResult<StringTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        StringTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        StringTestEntity load = repository.get(testEntity.getId());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndSearchOfEntity() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        AsyncResult<StringTestEntity> result = repository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        StringTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        List<StringTestEntity> results = repository.search().field("name", Is.EqualTo, "name").run().getResults();
        assertThat(results, hasItem(testEntity));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntities() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        AsyncResult<List<StringTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<StringTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<StringTestEntity> load = repository.get(testEntity.getId(), testEntity2.getId());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldAllowSaveAndSearchOfMultipleEntities() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        AsyncResult<List<StringTestEntity>> result = repository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<StringTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<StringTestEntity> search = repository.search().field("name", list("name", "name2")).run().getResults();
        assertThat(search, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldSearchReturningIds() {
        StringTestEntity testEntity = new StringTestEntity("1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("2", "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        List<String> search = repository.search().field("name", list("name", "name2")).run().getResultIds();
        assertThat(search, hasItems("1", "2"));
    }

    @Test
    public void shouldListGivenCount() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        StringTestEntity testEntity3 = new StringTestEntity("id3", "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<StringTestEntity> list = repository.list(2);
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldLoadByField() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        StringTestEntity testEntity3 = new StringTestEntity("id3", "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<StringTestEntity> list = repository.getByField("name", "name2");
        assertThat(list.size(), is(1));
        assertThat(list, hasItem(testEntity2));
    }

    @Test
    public void shouldLoadByFieldReturningEmptyListWhenNoResults() {
        List<StringTestEntity> list = repository.getByField("name", "none");
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldLoadByFieldCollection() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        StringTestEntity testEntity3 = new StringTestEntity("id3", "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<StringTestEntity> list = repository.getByField("name", Expressive.<Object>list("name2", "name3"));
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity2, testEntity3));
    }

    @Test
    public void shouldLoadByFieldCollectionWhereCollectionIsConcreteType() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        StringTestEntity testEntity3 = new StringTestEntity("id3", "name3");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<StringTestEntity> list = repository.getByField("name", Expressive.list("name2", "name3"));
        assertThat(list.size(), is(2));
        assertThat(list, hasItems(testEntity2, testEntity3));
    }

    @Test
    public void shouldLoadByFieldCollectionReturningEmptyListWhenNoResults() {
        List<StringTestEntity> list = repository.getByField("name", Expressive.<Object>list("none"));
        assertThat(list.size(), is(0));
    }

    @Test
    public void shouldSearchAllowingOrderAndLimit() {
        StringTestEntity testEntity = new StringTestEntity("1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("2", "name");
        StringTestEntity testEntity3 = new StringTestEntity("3", "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        List<StringTestEntity> results = repository.search().field("name", Is.EqualTo, "name").order("id", true).limit(2).run().getResults();
        assertThat(results.size(), is(2));
        assertThat(results, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntityById() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResultIds().isEmpty(), is(false));

        repository.deleteByKeyAsync(testEntity.getId()).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResultIds().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityByEntity() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResultIds().isEmpty(), is(false));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("id", Is.EqualTo, testEntity.getId()).run().getResultIds().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesById() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds(), hasItems("id1", "id2"));

        repository.deleteByKeyAsync(testEntity.getId(), testEntity2.getId()).complete();

        assertThat(repository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds().isEmpty(), is(true));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByEntity() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds(), hasItems("id1", "id2"));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
        assertThat(repository.search().field("id", list(testEntity.getId(), testEntity2.getId())).run().getResultIds().isEmpty(), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfEntityWhenNotSearchIndexing() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        AsyncResult<StringTestEntity> result = noSearchrepository.putAsync(testEntity);
        assertThat(result, is(notNullValue()));
        StringTestEntity complete = result.complete();
        assertThat(complete, is(sameInstance(testEntity)));

        StringTestEntity load = noSearchrepository.get(testEntity.getId());
        assertThat(load.equals(testEntity), is(true));
    }

    @Test
    public void shouldAllowSaveAndLoadOfMultipleEntitiesWhenNotSearchIndexing() {

        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        AsyncResult<List<StringTestEntity>> result = noSearchrepository.putAsync(testEntity, testEntity2);
        assertThat(result, is(notNullValue()));
        List<StringTestEntity> complete = result.complete();
        assertThat(complete.contains(testEntity), is(true));
        assertThat(complete.contains(testEntity2), is(true));

        List<StringTestEntity> load = noSearchrepository.get(testEntity.getId(), testEntity2.getId());
        assertThat(load, hasItems(testEntity, testEntity2));
    }

    @Test
    public void shouldDeleteEntity() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        repository.putAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));

        repository.deleteAsync(testEntity).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResultIds().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntityWhenNotSearchIndexing() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));

        noSearchrepository.deleteAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntities() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id1", "name");
        repository.putAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(testEntity));
        assertThat(repository.get(testEntity2.getId()), is(testEntity2));

        repository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(repository.get(testEntity.getId()), is(nullValue()));
        assertThat(repository.get(testEntity2.getId()), is(nullValue()));
        assertThat(repository.search().field("name", Is.Is, "name").run().getResultIds().isEmpty(), is(true));
    }

    @Test
    public void shouldDeleteEntitiesWhenNotSearchIndexing() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(testEntity2));

        noSearchrepository.deleteAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(nullValue()));
    }

    @Test
    public void shouldDeleteEntityByIdWhenNotSearchIndexing() {

        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        noSearchrepository.putAsync(testEntity).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));

        noSearchrepository.deleteByKeyAsync(testEntity.getId()).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(nullValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDeleteEntitiesByIdWhenNotSearchIndexing() {
        StringTestEntity testEntity = new StringTestEntity("id1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("id2", "name2");
        noSearchrepository.putAsync(testEntity, testEntity2).complete();

        assertThat(noSearchrepository.get(testEntity.getId()), is(testEntity));
        assertThat(noSearchrepository.get(testEntity2.getId()), is(testEntity2));

        noSearchrepository.deleteByKeyAsync(testEntity.getId(), testEntity2.getId()).complete();

        assertThat(noSearchrepository.get(testEntity.getId(), testEntity2.getId()), Matchers.hasItems(nullValue(), nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenSearchWhenNotSearchIndexing() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Unable to search on type StringTestEntity - there is no search service available to this repository");

        noSearchrepository.search();
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearch() {
        StringTestEntity testEntity = new StringTestEntity("1", "original");
        StringTestEntity testEntity2 = new StringTestEntity("2", "original");
        StringTestEntity testEntity3 = new StringTestEntity("3", "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<StringTestEntity> all = ObjectifyService.ofy().load().type(StringTestEntity.class).list();
        for (StringTestEntity e : all) {
            e.setName("other name");
        }
        ObjectifyService.ofy().save().entities(all).now();
        List<String> keys = list(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "original").run().getResultIds().isEmpty(), is(true));
        assertThat(repository.search().field("name", Is.Is, "other name").run().getResultIds().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnSearchAndWriteBackToDatastoreWhenReindexOperationProvided() {
        StringTestEntity testEntity = new StringTestEntity("1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("2", "name");
        StringTestEntity testEntity3 = new StringTestEntity("3", "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        Search<StringTestEntity, String> search = repository.search();
        assertThat(search.run().getResults(), hasItems(testEntity, testEntity2, testEntity3));

        List<String> keys = list(testEntity.getId(), testEntity2.getId(), testEntity3.getId());
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (StringTestEntity entity : batch) {
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
        StringTestEntity testEntity = new StringTestEntity("1", "name");
        StringTestEntity testEntity2 = new StringTestEntity("2", "name");
        StringTestEntity testEntity3 = new StringTestEntity("3", "name");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "name").run().getResults(), hasSize(0));

        List<String> keys = Arrays.asList("1", "2", "3");
        int reindexed = repository.reindex(keys, 10, null);
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "name").run().getResultIds().size(), is(3));
    }

    @Test
    public void shouldReindexEntitiesBasedOnKeysAndWriteBackToDatastoreWhenReindexOperationProvided() {
        StringTestEntity testEntity = new StringTestEntity("1", "original");
        StringTestEntity testEntity2 = new StringTestEntity("2", "original");
        StringTestEntity testEntity3 = new StringTestEntity("3", "original");
        repository.putAsync(testEntity, testEntity2, testEntity3).complete();

        repository.searchService.removeAll();
        assertThat(repository.search().field("name", Is.Is, "original").run().getResults(), hasSize(0));

        List<String> keys = Arrays.asList("1", "2", "3");
        int reindexed = repository.reindex(keys, 10, batch -> {
            for (StringTestEntity entity : batch) {
                entity.setName("different");
            }
            return batch;
        });
        assertThat(reindexed, is(3));

        assertThat(repository.search().field("name", Is.Is, "different").run().getResults(), hasItems(testEntity, testEntity2, testEntity3));
        assertThat(repository.getByField("name", "different"), hasItems(testEntity, testEntity2, testEntity3));
    }
}
