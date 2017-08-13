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
package com.threewks.gaetools.transformer.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class ObjectToDateTimeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ObjectToDateTime transformer = new ObjectToDateTime();

    @Test
    public void shouldNotTransformNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() {
        long now = System.currentTimeMillis();
        assertThat(transformer.from(1234L), is(new DateTime(1234)));
        assertThat(transformer.from(new DateTime(now)), is(new DateTime(now)));
        assertThat(transformer.from(new Date(now)), is(new DateTime(now)));
        assertThat(transformer.from("2014-06-14T12:34:56.123+10:00").compareTo(new DateTime(2014, 6, 14, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.forOffsetHours(10))), is(0));
    }
}
