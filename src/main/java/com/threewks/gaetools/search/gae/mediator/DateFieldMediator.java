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

import com.google.appengine.api.search.Field.Builder;
import com.threewks.gaetools.transformer.TransformerManager;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFieldMediator implements FieldMediator<DateTime> {
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @SuppressWarnings("unchecked")
    @Override
    public <In> DateTime normalise(TransformerManager transformerManager, In value) {
        Class<In> valueClass = (Class<In>) value.getClass();
        return transformerManager.transform(valueClass, DateTime.class, value);
    }

    @Override
    public void setValue(Builder builder, DateTime value) {
        builder.setDate(value.withZone(DateTimeZone.UTC).toDate());
    }

    @Override
    public String stringify(DateTime value) {
        return formatter.print(value.withZone(DateTimeZone.UTC));
    }

    @Override
    public Class<DateTime> getTargetType() {
        return DateTime.class;
    }
}
