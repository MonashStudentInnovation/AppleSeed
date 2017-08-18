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
import com.google.appengine.api.search.checkers.SearchApiLimits;
import com.threewks.gaetools.transformer.TransformerManager;

public class SmallDecimalFieldMediator implements FieldMediator<Double> {
    private static final double Min = SearchApiLimits.MINIMUM_NUMBER_VALUE;
    private static final double Max = SearchApiLimits.MAXIMUM_NUMBER_VALUE;

    @SuppressWarnings("unchecked")
    @Override
    public <In> Double normalise(TransformerManager transformerManager, In value) {
        Class<In> valueClass = (Class<In>) value.getClass();
        double transform = transformerManager.transform(valueClass, Double.class, value);
        return Math.max(Math.min(Max, transform), Min);
    }

    @Override
    public void setValue(Builder builder, Double value) {
        builder.setNumber(value);
    }

    @Override
    public String stringify(Double value) {
        return value.toString();
    }

    @Override
    public Class<Double> getTargetType() {
        return Double.class;
    }
}
