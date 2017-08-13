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
package com.threewks.gaetools.search.gae.transformers;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.search.GeoPoint;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class GeoPtToGeoPointTransformerTest {

    private GeoPtToGeoPointTransformer transformer = new GeoPtToGeoPointTransformer();

    @Test
    public void shouldCreateGeoPointFromGeoPt() {
        GeoPt geoPt = new GeoPt(1.23f, 4.56f);
        GeoPoint result = transformer.from(geoPt);
        assertThat(result.getLatitude(), is((double) 1.23f));
        assertThat(result.getLongitude(), is((double) 4.56f));
    }

    @Test
    public void shouldReturnNullOnNullInput() {
        assertThat(transformer.from(null), is(nullValue()));
    }
}
