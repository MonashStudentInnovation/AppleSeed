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

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;

import java.util.UUID;

public class UUIDTranslatorFactory extends ValueTranslatorFactory<UUID, String> {
    public UUIDTranslatorFactory() {
        super(UUID.class);
    }

    @Override
    protected ValueTranslator<UUID, String> createValueTranslator(TypeKey<UUID> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<UUID, String>(String.class) {
            @Override
            protected UUID loadValue(String value, LoadContext ctx, Path path) throws SkipException {
                return UUID.fromString(value);
            }

            @Override
            protected String saveValue(UUID value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return value.toString();
            }
        };
    }
}
