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

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class CompleteAllTest {
    @Test
    public void shouldCompleteAsync() {
        AsyncResult<String> async1 = new MockAsyncResult<>("first");
        assertThat(CompleteAll.async(async1), is(Arrays.asList("first")));
    }

    @Test
    public void shouldCompleteAllAsync() {
        AsyncResult<String> async1 = new MockAsyncResult<>("first");
        AsyncResult<String> async2 = new MockAsyncResult<>("second");
        assertThat(CompleteAll.async(async1, async2), is(Arrays.asList("first", "second")));
    }

    @Test
    public void shouldCompleteAllAsyncWithMixedTypes() {
        AsyncResult<String> async1 = new MockAsyncResult<>("first");
        AsyncResult<Long> async2 = new MockAsyncResult<>(1L);
        assertThat(CompleteAll.async(async1, async2), is(Arrays.asList("first", 1L)));
    }

    @Test
    public void shouldCompleteAllAsyncWhenNoAsyncsGiven() {
        assertThat(CompleteAll.async(), is(Collections.emptyList()));
    }
}
