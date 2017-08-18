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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class NumberToIntegerTest {
    private NumberToInteger transformer = new NumberToInteger();

    @Test
    public void shouldTransform() {
        assertThat(transformer.from(null), is(nullValue()));
        assertThat(transformer.from(BigDecimal.ZERO), is(0));
        assertThat(transformer.from(BigDecimal.ONE), is(1));
        assertThat(transformer.from(BigDecimal.TEN), is(10));
        assertThat(transformer.from(new BigDecimal(1234)), is(1234));
        assertThat(transformer.from(new BigDecimal("1234.0000001")), is(1234));
        assertThat(transformer.from(new BigDecimal("0.000000000000000000000000000000000012")), is(0));
        assertThat(transformer.from(new BigDecimal("-0.000000000000000000000000000000000012")), is(0));
        assertThat(transformer.from(0), is(0));
        assertThat(transformer.from(1), is(1));
        assertThat(transformer.from(10), is(10));
        assertThat(transformer.from(1234.0), is(1234));
        assertThat(transformer.from(1234.0f), is(1234));
        assertThat(transformer.from(10), is(10));
        assertThat(transformer.from(10L), is(10));
        assertThat(transformer.from((int) 11), is(11));
    }
}
