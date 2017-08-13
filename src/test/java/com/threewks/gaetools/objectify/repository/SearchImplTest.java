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

import com.googlecode.objectify.Key;
import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.objectify.repository.test.LongTestEntity;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.OrderComponent;
import com.threewks.gaetools.search.QueryComponent;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.GaeSearchService;
import com.threewks.gaetools.search.gae.SearchConfig;
import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.transformer.TransformerManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SearchImplTest {
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();

    private SearchImpl<LongTestEntity, Long> searchImpl;
    private Search<LongTestEntity, Long> search;

    @SuppressWarnings("unchecked")
    @Before
    public void before() {
        LongRepository<LongTestEntity> repository = mock(LongRepository.class);
        GaeSearchService<LongTestEntity, Key<LongTestEntity>> searchService = new GaeSearchService<>(LongTestEntity.class, new SearchConfig(TransformerManager.createWithDefaults(), new FieldMediatorSet(),
                new IndexTypeLookup()));
        com.threewks.gaetools.search.gae.SearchImpl<LongTestEntity, Key<LongTestEntity>> searchDelegate = searchService.search();
        searchImpl = new SearchImpl<>(repository, searchDelegate);
        search = searchImpl;
    }

    @Test
    public void shouldApplyAndRetainLimit() {
        Search<LongTestEntity, Long> next = searchImpl.limit(87);
        assertThat(next, not(sameInstance(search)));
        assertThat(next.limit(), is(87));
        assertThat(delegate(next).limit(), is(87));
        assertThat(searchImpl.limit(), is(nullValue()));
        assertThat(delegate(searchImpl).limit(), is(nullValue()));
    }

    @Test
    public void shouldApplyAndRetainOffset() {
        Search<LongTestEntity, Long> next = searchImpl.offset(87);
        assertThat(next, not(sameInstance(search)));
        assertThat(next.offset(), is(87));
        assertThat(delegate(next).offset(), is(87));
    }

    @Test
    public void shouldApplyAndRetainQuery() {
        Search<LongTestEntity, Long> next = searchImpl.query("Text");
        assertThat(next, not(sameInstance(search)));
        assertThat(next.query(), hasItems(QueryComponent.forRawQuery("Text")));
        assertThat(delegate(next).query(), hasItems(QueryComponent.forRawQuery(("Text"))));
        assertThat(searchImpl.query().isEmpty(), is(true));
        assertThat(delegate(searchImpl).query().isEmpty(), is(true));
    }

    @Test
    public void shouldApplyFieldOperation() {
        Search<LongTestEntity, Long> next = searchImpl.field("name", Is.EqualTo, "value");
        assertThat(next, not(sameInstance(search)));
        assertThat(next.query(), hasItem(QueryComponent.forFieldQuery("name", Is.EqualTo, "value")));
        assertThat(searchImpl.query().isEmpty(), is(true));
        assertThat(delegate(searchImpl).query().isEmpty(), is(true));
        assertThat(delegate(next).query(), hasItem(QueryComponent.forFieldQuery("name", Is.EqualTo, "value")));
    }

    @Test
    public void shouldApplyOrderOperation() {
        Search<LongTestEntity, Long> next = searchImpl.order("name", false);
        assertThat(next, not(sameInstance(search)));
        assertThat(searchImpl.order().isEmpty(), is(true));
        assertThat(delegate(searchImpl).order().isEmpty(), is(true));
        assertThat(next.order(), hasItems(OrderComponent.forField("name", false)));
        assertThat(delegate(next).order(), hasItems(OrderComponent.forField("name", false)));
    }


    @Test
    public void shouldApplyAndRetainAccuracy() {
        Search<LongTestEntity, Long> next = searchImpl.accuracy(200);
        assertThat(next, not(sameInstance(search)));
        assertThat(next.accuracy(), is(200));
        assertThat(delegate(next).accuracy(), is(200));
    }

    private Search<LongTestEntity, Key<LongTestEntity>> delegate(Search<LongTestEntity, Long> next) {
        return ((SearchImpl<LongTestEntity, Long>) next).getSearchRequest();
    }
}
