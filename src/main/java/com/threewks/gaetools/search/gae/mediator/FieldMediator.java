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
import com.threewks.gaetools.transformer.TransformerManager;

/**
 * A {@link FieldMediator} is used to transform between a java type and field type in the low level full text search api.
 *
 * @param <T>
 */
public interface FieldMediator<T> {

    Class<T> getTargetType();

    /**
     * Normalise the given object to the type this {@link FieldMediator} operates on
     *
     * @param transformerManager
     * @param value
     * @return
     */
    <In> T normalise(TransformerManager transformerManager, In value);

    /**
     * Set the normalised value into the given field
     *
     * @param builder
     * @param value
     */
    void setValue(Field.Builder builder, T value);

    /**
     * Convert the given noramlised object to a string suitable for querying.
     *
     * @param value
     * @return
     */
    String stringify(T value);

}
