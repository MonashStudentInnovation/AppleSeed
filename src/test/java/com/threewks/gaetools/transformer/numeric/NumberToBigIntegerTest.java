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

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class NumberToBigIntegerTest {
    private NumberToBigInteger transformer = new NumberToBigInteger();

    @Test
    public void shouldTransform() {
        assertThat(transformer.from(null), is(nullValue()));
        assertThat(transformer.from(0), is(new BigInteger("0")));
        assertThat(transformer.from(0d), is(new BigInteger("0")));
        assertThat(transformer.from(0f), is(new BigInteger("0")));
        assertThat(transformer.from(1d), is(new BigInteger("1")));
        assertThat(transformer.from(10d), is(new BigInteger("10")));
        assertThat(transformer.from(10), is(new BigInteger("10")));
        assertThat(transformer.from(Double.MAX_VALUE), is(new BigInteger("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")));
        assertThat(transformer.from(Double.MIN_VALUE), is(new BigInteger("0")));
        assertThat(transformer.from(0.01), is(new BigInteger("0")));
        assertThat(transformer.from(0.01f), is(new BigInteger("0")));
        assertThat(transformer.from(new BigInteger("12")), is(new BigInteger("12")));
        assertThat(transformer.from(new AtomicInteger(54)), is(new BigInteger("54")));
        assertThat(transformer.from((short) 34), is(new BigInteger("34")));
        assertThat(transformer.from(0.49), is(new BigInteger("0")));
        assertThat(transformer.from(0.50), is(new BigInteger("0")));
        assertThat(transformer.from(0.501), is(new BigInteger("0")));
        assertThat(transformer.from(0.999), is(new BigInteger("0")));
        assertThat(transformer.from(1.0), is(new BigInteger("1")));
        assertThat(transformer.from(1.0001), is(new BigInteger("1")));
    }
}
