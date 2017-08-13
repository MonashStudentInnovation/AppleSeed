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

import com.atomicleopard.expressive.ETransformer;
import com.google.appengine.api.datastore.Key;
import com.threewks.gaetools.search.gae.SearchConfig;

public class DatastoreKeyRepository<E> extends AbstractRepository<E, Key> {
    public DatastoreKeyRepository(final Class<E> entityType, SearchConfig searchConfig) {
        super(entityType, fromKey(entityType), toKey(entityType), searchConfig);
    }

    static <E> ETransformer<Key, com.googlecode.objectify.Key<E>> fromKey(final Class<E> type) {
        return com.googlecode.objectify.Key::create;
    }

    static <E> ETransformer<com.googlecode.objectify.Key<E>, Key> toKey(final Class<E> type) {
        return com.googlecode.objectify.Key::getRaw;
    }
}
