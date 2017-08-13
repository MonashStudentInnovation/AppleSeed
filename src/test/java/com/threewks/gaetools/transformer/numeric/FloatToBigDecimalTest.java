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

public class FloatToBigDecimalTest {
    private FloatToBigDecimal transformer = new FloatToBigDecimal();

    @Test
    public void shouldTransform() {
        assertThat(transformer.from(null), is(nullValue()));
        assertThat(transformer.from(0f), is(new BigDecimal(0)));
        assertThat(transformer.from(1f), is(new BigDecimal(1)));
        assertThat(transformer.from(10f), is(new BigDecimal(10)));
        assertThat(transformer.from(10.01f), is(new BigDecimal("10.0100002288818359375")));
        assertThat(transformer.from(Float.MAX_VALUE), is(new BigDecimal("340282346638528859811704183484516925440")));
        assertThat(transformer.from(Float.MIN_VALUE), is(new BigDecimal("1.40129846432481707092372958328991613128026194187651577175706828388979108268586060148663818836212158203125E-45")));
    }
}
