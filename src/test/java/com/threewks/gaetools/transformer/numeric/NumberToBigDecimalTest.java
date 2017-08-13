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
package com.threewks.gaetools.transformer.numeric;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class NumberToBigDecimalTest {
    private NumberToBigDecimal transformer = new NumberToBigDecimal();

    @Test
    public void shouldTransform() {
        assertThat(transformer.from(null), is(nullValue()));
        assertThat(transformer.from(0), is(new BigDecimal(0)));
        assertThat(transformer.from(0d), is(new BigDecimal("0.0")));
        assertThat(transformer.from(0f), is(new BigDecimal("0.0")));
        assertThat(transformer.from(1d), is(new BigDecimal("1.0")));
        assertThat(transformer.from(10d), is(new BigDecimal("10.0")));
        assertThat(transformer.from(10), is(new BigDecimal("10")));
        assertThat(transformer.from(Double.MAX_VALUE), is(new BigDecimal("1.7976931348623157E+308")));
        assertThat(transformer.from(Double.MIN_VALUE), is(new BigDecimal("4.9E-324")));
        assertThat(transformer.from(0.01), is(new BigDecimal("0.01")));
        assertThat(transformer.from(0.01f), is(new BigDecimal("0.01")));
        assertThat(transformer.from(new BigInteger("12")), is(new BigDecimal(12)));
        assertThat(transformer.from(new AtomicInteger(54)), is(new BigDecimal(54)));
        assertThat(transformer.from((short) 34), is(new BigDecimal(34)));
    }
}