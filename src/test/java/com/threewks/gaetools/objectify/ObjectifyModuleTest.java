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
package com.threewks.gaetools.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.TranslatorFactory;
import com.googlecode.objectify.impl.translate.Translators;
import com.googlecode.objectify.impl.translate.opt.joda.DateTimeZoneTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.ReadableInstantTranslatorFactory;
import com.googlecode.objectify.impl.translate.opt.joda.ReadablePartialTranslatorFactory;
import com.threewks.gaetools.objectify.transformers.DatastoreKeyToLongTransformer;
import com.threewks.gaetools.objectify.transformers.DatastoreKeyToStringTransformer;
import com.threewks.gaetools.objectify.transformers.KeyToLongTransformer;
import com.threewks.gaetools.objectify.transformers.KeyToStringTransformer;
import com.threewks.gaetools.objectify.transformers.StringToDatastoreKeyTransformer;
import com.threewks.gaetools.objectify.transformers.StringToKeyTransformer;
import com.threewks.gaetools.test.TestSupport;
import com.threewks.gaetools.transformer.TransformerManager;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ObjectifyModuleTest {

    @Test
    public void addDefaultTranslators() {
        ObjectifyModule.addDefaultTranslators();

        Translators translators = ObjectifyService.factory().getTranslators();
        List<TranslatorFactory<?, ?>> factories = TestSupport.getField(translators, "translatorFactories");
        assertThat(factoriesContain(factories, ReadableInstantTranslatorFactory.class), is(true));
        assertThat(factoriesContain(factories, ReadablePartialTranslatorFactory.class), is(true));
        assertThat(factoriesContain(factories, DateTimeZoneTranslatorFactory.class), is(true));
    }

    @Test
    public void defaultTransformerManager() {
        TransformerManager transformerManager = ObjectifyModule.defaultTransformerManager();

        assertThat(transformerManager.getTransformer(Key.class, String.class), instanceOf(KeyToStringTransformer.class));
        assertThat(transformerManager.getTransformer(Key.class, Long.class), instanceOf(KeyToLongTransformer.class));
        assertThat(transformerManager.getTransformer(String.class, Key.class), instanceOf(StringToKeyTransformer.class));
        assertThat(transformerManager.getTransformer(com.google.appengine.api.datastore.Key.class, String.class), instanceOf(DatastoreKeyToStringTransformer.class));
        assertThat(transformerManager.getTransformer(com.google.appengine.api.datastore.Key.class, Long.class), instanceOf(DatastoreKeyToLongTransformer.class));
        assertThat(transformerManager.getTransformer(String.class, com.google.appengine.api.datastore.Key.class), instanceOf(StringToDatastoreKeyTransformer.class));
    }

    private boolean factoriesContain(List<TranslatorFactory<?, ?>> factories, Class<? extends TranslatorFactory<?, ?>> class1) {
        for (TranslatorFactory<?, ?> factory : factories) {
            if (factory.getClass() == class1) {
                return true;
            }
        }
        return false;
    }
}
