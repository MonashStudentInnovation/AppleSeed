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


import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.objectify.ObjectifyModule;
import com.threewks.gaetools.objectify.SetupObjectify;
import com.threewks.gaetools.objectify.repository.test.LongTestEntity;
import com.threewks.gaetools.search.SearchModule;
import com.threewks.gaetools.search.gae.SearchConfig;
import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.transformer.TransformerManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BaseRepositoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(LongTestEntity.class);

    private SearchConfig searchConfig;

    @Before
    public void before() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();
        IndexTypeLookup indexTypeLookup = SearchModule.defaultIndexTypeLookup();
        searchConfig = new SearchConfig(transformerManager, new FieldMediatorSet(), indexTypeLookup);
    }

    @Test
    public void shouldHandleWhenIdFieldIsOnParentEntityClass() {
        new LongRepository<>(LongTestEntitySubclass.class, searchConfig);
    }

    private static class LongTestEntitySubclass extends LongTestEntity {

    }

}
