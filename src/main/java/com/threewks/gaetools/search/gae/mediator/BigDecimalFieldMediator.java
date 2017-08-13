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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalFieldMediator implements FieldMediator<BigDecimal> {
    private static final BigDecimal Min = new BigDecimal(SearchApiLimits.MINIMUM_NUMBER_VALUE);
    private static final BigDecimal Max = new BigDecimal(SearchApiLimits.MAXIMUM_NUMBER_VALUE);

    private int shift;
    private int scale;

    public BigDecimalFieldMediator() {
        this(3, 3);
    }

    public BigDecimalFieldMediator(int shift, int scale) {
        this.shift = shift;
        this.scale = scale;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <In> BigDecimal normalise(TransformerManager transformerManager, In value) {
        Class<In> valueClass = (Class<In>) value.getClass();
        BigDecimal bigDecimalValue = transformerManager.transform(valueClass, BigDecimal.class, value);
        bigDecimalValue = bigDecimalValue.movePointLeft(shift).setScale(shift + scale, RoundingMode.DOWN);
        bigDecimalValue = bigDecimalValue.min(Max).max(Min);
        return bigDecimalValue;
    }

    @Override
    public void setValue(Builder builder, BigDecimal value) {
        builder.setNumber(value.doubleValue());
    }

    @Override
    public String stringify(BigDecimal value) {
        return Double.valueOf(value.doubleValue()).toString();
    }

    @Override
    public Class<BigDecimal> getTargetType() {
        return BigDecimal.class;
    }

}
