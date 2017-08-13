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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class RepositoryExceptionTest {

    @Test
    public void shouldHaveAMessageCtor() {
        RepositoryException e = new RepositoryException("Message: %s", "expected");
        assertThat(e.getCause(), is(nullValue()));
        assertThat(e.getMessage(), is("Message: expected"));
    }

    @Test
    public void shouldHaveCauseAndMessageCtor() {
        Exception cause = new RuntimeException();
        RepositoryException e = new RepositoryException(cause, "Message: %s", "expected");
        assertThat(e.getCause(), is((Throwable) cause));
        assertThat(e.getMessage(), is("Message: expected"));
    }
}
