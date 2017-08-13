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

public class LongToBigDecimalTest {
    private LongToBigDecimal transformer = new LongToBigDecimal();

    @Test
    public void shouldTransform() {
        assertThat(transformer.from(null), is(nullValue()));
        assertThat(transformer.from(0L), is(new BigDecimal(0)));
        assertThat(transformer.from(1L), is(new BigDecimal(1)));
        assertThat(transformer.from(10L), is(new BigDecimal(10)));
        assertThat(transformer.from(Long.MAX_VALUE), is(new BigDecimal("9223372036854775807")));
        assertThat(transformer.from(Long.MIN_VALUE), is(new BigDecimal("-9223372036854775808")));
    }

}
