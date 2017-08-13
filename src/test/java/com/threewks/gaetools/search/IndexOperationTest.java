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
package com.threewks.gaetools.search;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexOperationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldCallFutureGetOnComplete() throws InterruptedException, ExecutionException {
        Future<String> future = mockFuture();
        IndexOperation indexOperation = new IndexOperation(future);
        verify(future, never()).get();

        indexOperation.complete();

        verify(future).get();
    }

    @Test
    public void shouldNotFailWithNullFuture() {
        new IndexOperation(null).complete();
    }

    @Test
    public void shouldThrowSearchExceptionIfFutureThrowsException() throws InterruptedException, ExecutionException {
        thrown.expect(SearchException.class);
        thrown.expectMessage("Failed to complete search index operation: Expected");

        Future<String> future = mockFuture();
        when(future.get()).thenThrow(new ExecutionException("Expected", null));

        new IndexOperation(future).complete();
    }

    @SuppressWarnings("unchecked")
    private Future<String> mockFuture() {
        return mock(Future.class);
    }
}
