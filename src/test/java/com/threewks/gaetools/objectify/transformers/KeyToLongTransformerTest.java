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
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class KeyToLongTransformerTest {
    @Rule
    public SetupAppengine setupAppengine = new SetupAppengine();
    @Rule
    public SetupObjectify setupObjectify = new SetupObjectify(LongTestEntity.class);
    private KeyToLongTransformer transformer = new KeyToLongTransformer();

    @Test
    public void shouldTransformNullToNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() {
        Key<LongTestEntity> key = Key.create(LongTestEntity.class, 4321L);
        Key<LongTestEntity> childKey = Key.create(key, LongTestEntity.class, 1234L);
        assertThat(transformer.from(key), is(4321L));
        assertThat(transformer.from(childKey), is(1234L));
    }
}
