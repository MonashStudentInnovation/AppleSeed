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

public class DateToStringTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private DateToString transformer = new DateToString();

    @Test
    public void shouldNotTransformNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() {

        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T12:34:56.123Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T12:34:56.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 0).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T12:34:00.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 0, 0).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T12:00:00.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T00:00:00.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC))), is("2014-06-01T00:00:00.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 1, 1, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC))), is("2014-01-01T00:00:00.000Z"));
    }

    @Test
    public void shouldTransformAllDatesIntoUtc() {
        assertThat(transformer.from(date(new DateTime(2014, 5, 1, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.forOffsetHours(10)))), is("2014-05-01T02:34:56.123Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.forOffsetHours(-10)))), is("2014-06-01T22:34:56.123Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.forOffsetHoursMinutes(10, 30)))), is("2014-06-01T02:04:56.123Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56, 123).withZoneRetainFields(DateTimeZone.forOffsetHours(0)))), is("2014-06-01T12:34:56.123Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 56).withZoneRetainFields(DateTimeZone.forOffsetHours(8)))), is("2014-06-01T04:34:56.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 34, 0).withZoneRetainFields(DateTimeZone.forOffsetHours(8)))), is("2014-06-01T04:34:00.000Z"));
        assertThat(transformer.from(date(new DateTime(2014, 6, 1, 12, 0, 0).withZoneRetainFields(DateTimeZone.forOffsetHours(8)))), is("2014-06-01T04:00:00.000Z"));
    }

    private Date date(DateTime dateTime) {
        return dateTime.toDate();
    }
}
