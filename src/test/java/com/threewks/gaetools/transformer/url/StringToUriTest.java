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
package com.threewks.gaetools.transformer.url;

import com.threewks.gaetools.transformer.TransformerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class StringToUriTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private StringToUri transformer = new StringToUri();

    @Test
    public void shouldTransformNullToNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() throws URISyntaxException {
        assertThat(transformer.from("http://www.google.com/"), is(new URI("http://www.google.com/")));
        assertThat(transformer.from("http://www.google.com"), is(new URI("http://www.google.com")));
        assertThat(transformer.from("https://www.google.com"), is(new URI("https://www.google.com")));
        assertThat(transformer.from("ftp://www.google.com/ftp/"), is(new URI("ftp://www.google.com/ftp/")));
        assertThat(transformer.from("http://www.google.com/?query=parameter"), is(new URI("http://www.google.com/?query=parameter")));
        assertThat(transformer.from("www.google.com/?query=parameter"), is(new URI("www.google.com/?query=parameter")));
    }

    @Test
    public void shouldThrowTransformerExceptionWhenCannotCreateUri() {
        thrown.expect(TransformerException.class);
        thrown.expectMessage("Could not transform from 'ht tp://www google com' to a URI: Illegal character");
        transformer.from("ht tp://www google com");
    }
}
