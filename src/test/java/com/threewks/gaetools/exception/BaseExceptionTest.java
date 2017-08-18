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
package com.threewks.gaetools.exception;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class BaseExceptionTest {

    @Test
    public void shouldHaveDefaultCtorForLazyPeople() throws InstantiationException, IllegalAccessException {
        assertThat(BaseException.class.newInstance(), is(notNullValue()));
        assertThat(BaseException.class.newInstance().getMessage(), is(nullValue()));
        assertThat(BaseException.class.newInstance().getCause(), is(nullValue()));
    }

    @Test
    public void shouldRetainFormattedMessage() {
        assertThat(new BaseException("%s", "message").getMessage(), is("message"));
        assertThat(new BaseException("%s", "message").getCause(), is(nullValue()));
    }

    @Test
    public void shouldRetainFormattedMessageAndCause() {
        Throwable cause = new Throwable();
        assertThat(new BaseException(cause, "%s", "message").getMessage(), is("message"));
        assertThat(new BaseException(cause, "%s", "message").getCause(), is(cause));
    }

    @Test
    public void shouldRetainCause() {
        Throwable cause = new Throwable();
        assertThat(new BaseException(cause).getCause(), is(cause));
        assertThat(new BaseException(cause).getMessage(), is("java.lang.Throwable"));
    }

    @Test
    public void shouldNotFailedtoConstructWhenMessageIsAFormatStringWithNoArgs() {
        assertThat(new BaseException("Message with % in it").getMessage(), is("Message with % in it"));
        Throwable cause = new Throwable();
        assertThat(new BaseException(cause, "Message with % in it").getMessage(), is("Message with % in it"));
    }
}
