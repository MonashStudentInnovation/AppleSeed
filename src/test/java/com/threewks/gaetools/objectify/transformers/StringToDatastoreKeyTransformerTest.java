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
package com.threewks.gaetools.objectify.transformers;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.threewks.gaetools.SetupAppengine;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class StringToDatastoreKeyTransformerTest {
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    private StringToDatastoreKeyTransformer transformer = new StringToDatastoreKeyTransformer();

    @Test
    public void shouldTransformNullToNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() {
        Key key = KeyFactory.createKey("kind", "dcba");
        Key childKey = KeyFactory.createKey(key, "other-kind", 1234);
        assertThat(transformer.from(KeyFactory.keyToString(key)), is(key));
        assertThat(transformer.from(KeyFactory.keyToString(childKey)), is(childKey));
    }
}
