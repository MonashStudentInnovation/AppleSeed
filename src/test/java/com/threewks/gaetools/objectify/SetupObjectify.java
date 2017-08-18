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

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.junit.rules.ExternalResource;

import java.util.Arrays;
import java.util.List;

public class SetupObjectify extends ExternalResource {

    private ObjectifyFactory original = null;
    private List<Class<?>> register;
    private Closeable closable;

    public SetupObjectify(Class<?>... register) {
        this.register = Arrays.asList(register);
    }

    @Override
    protected void before() throws Throwable {
        original = ObjectifyService.factory();
        ObjectifyFactory factory = newObjectifyFactory();
        ObjectifyModule.addDefaultTranslators();
        addTranslators(factory);
        for (Class<?> type : register) {
            factory.register(type);
        }
        ObjectifyService.setFactory(factory);
        closable = ObjectifyService.begin();

    }

    @Override
    protected void after() {
        ObjectifyService.setFactory(original);
        closable.close();
    }

    protected void addTranslators(ObjectifyFactory factory) {
    }

    protected ObjectifyFactory newObjectifyFactory() {
        return new ObjectifyFactory();
    }
}
