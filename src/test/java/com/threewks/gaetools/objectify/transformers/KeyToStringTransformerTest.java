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

import com.googlecode.objectify.Key;
import com.threewks.gaetools.SetupAppengine;
import com.threewks.gaetools.objectify.SetupObjectify;
import com.threewks.gaetools.objectify.repository.test.LongTestEntity;
import com.threewks.gaetools.objectify.repository.test.StringTestEntity;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class KeyToStringTransformerTest {
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(LongTestEntity.class);
    private KeyToStringTransformer transformer = new KeyToStringTransformer();

    @Test
    public void shouldTransformNullToNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() {
        Key<StringTestEntity> key = Key.create(StringTestEntity.class, "dcba");
        Key<StringTestEntity> childKey = Key.create(key, StringTestEntity.class, "abcd");
        Key<LongTestEntity> key2 = Key.create(LongTestEntity.class, 4321);
        Key<LongTestEntity> childKey2 = Key.create(key, LongTestEntity.class, 1234);
        assertThat(transformer.from(key), is(key.getString()));
        assertThat(transformer.from(childKey), is(childKey.getString()));
        assertThat(transformer.from(key2), is(key2.getString()));
        assertThat(transformer.from(childKey2), is(childKey2.getString()));
    }
}
