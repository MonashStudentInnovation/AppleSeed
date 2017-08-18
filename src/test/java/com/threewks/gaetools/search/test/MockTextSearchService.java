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
package com.threewks.gaetools.search.test;

import com.atomicleopard.expressive.Expressive;
import com.google.appengine.api.search.ScoredDocument;
import com.threewks.gaetools.search.IndexOperation;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.TextSearchService;
import com.threewks.gaetools.search.gae.SearchExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MockTextSearchService<T, K> implements TextSearchService<T, K>, SearchExecutor<T, K, MockSearch<T, K>> {
    private List<T> indexed = new ArrayList<>();
    private List<T> removed = new ArrayList<>();
    private List<K> removedIds = new ArrayList<>();
    private boolean removedAll = false;
    private List<K> expectedResultIds = new ArrayList<>();
    private List<T> expectedResults = new ArrayList<>();

    public MockTextSearchService() {
    }

    public List<T> getIndexed() {
        return indexed;
    }

    public boolean hasRemovedAll() {
        return removedAll;
    }

    @Override
    public IndexOperation index(final T object) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                indexed.add(object);
                return null;
            }
        });
    }

    @Override
    public IndexOperation index(final Collection<T> objects) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                indexed.addAll(objects);
                return null;
            }
        });
    }

    @Override
    public IndexOperation removeById(final K id) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                removedIds.add(id);
                return null;
            }
        });
    }

    @Override
    public IndexOperation remove(final T object) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                removed.add(object);
                return null;
            }
        });
    }

    @Override
    public IndexOperation removeById(final Iterable<K> ids) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                removedIds.addAll(Expressive.list(ids));
                return null;
            }
        });
    }

    @Override
    public IndexOperation remove(final Iterable<T> objects) {
        return new IndexOperation(new DefaultFuture() {
            @Override
            public Void get() throws InterruptedException, ExecutionException {
                removed.addAll(Expressive.list(objects));
                return null;
            }
        });
    }

    @Override
    public int removeAll() {
        removedAll = true;
        return 1234;
    }

    @Override
    public Search<T, K> search() {
        return new MockSearch<>(expectedResults, expectedResultIds);
    }

    @Override
    public List<K> getResultsAsIds(List<ScoredDocument> results) {
        return expectedResultIds;
    }

    @Override
    public List<T> getResults(List<ScoredDocument> results) {
        return expectedResults;
    }

    @Override
    public Result<T, K> createSearchResult(MockSearch<T, K> searchRequest) {
        return new MockResult<>(expectedResults, expectedResultIds);
    }

    private abstract class DefaultFuture implements Future<Void> {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCancelled() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            throw new UnsupportedOperationException();
        }
    }
}
