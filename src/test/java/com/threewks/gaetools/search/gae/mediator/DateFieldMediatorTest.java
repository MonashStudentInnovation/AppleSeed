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
package com.threewks.gaetools.search.gae.mediator;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Field.Builder;
import com.threewks.gaetools.test.TestSupport;
import com.threewks.gaetools.transformer.TransformerManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateFieldMediatorTest {
    private DateFieldMediator mediator = new DateFieldMediator();
    private TransformerManager transformerManager = TransformerManager.createWithDefaults();
    private DateTime dt = new DateTime();

    @Test
    public void shouldNormaliseToDateTime() {
        assertThat(mediator.normalise(transformerManager, dt), is(dt));
        assertThat(mediator.normalise(transformerManager, dt.toDate()), is(dt));
        assertThat(mediator.normalise(transformerManager, dt.getMillis()), is(dt));
    }

    @Test
    public void shouldSetValueInUTC() {
        Date expected = dt.withZone(DateTimeZone.UTC).toDate();
        Builder builder = Field.newBuilder();
        mediator.setValue(builder, dt);
        assertThat(TestSupport.getField(builder, "date"), is(expected));
    }

    @Test
    public void shouldStringifyDateIntoUTC() {
        assertThat(mediator.stringify(dt), is(dt.withZone(DateTimeZone.UTC).toString("yyyy-MM-dd")));
    }
}
