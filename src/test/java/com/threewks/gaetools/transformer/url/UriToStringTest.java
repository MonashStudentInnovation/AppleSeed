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

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UriToStringTest {
    private UriToString transformer = new UriToString();

    @Test
    public void shouldTransformNullToNull() {
        assertThat(transformer.from(null), is(nullValue()));
    }

    @Test
    public void shouldTransform() throws URISyntaxException {
        assertThat(transformer.from(new URI("http://www.google.com/")), is("http://www.google.com/"));
        assertThat(transformer.from(new URI("http://www.google.com")), is("http://www.google.com"));
        assertThat(transformer.from(new URI("https://www.google.com")), is("https://www.google.com"));
        assertThat(transformer.from(new URI("ftp://www.google.com/ftp/")), is("ftp://www.google.com/ftp/"));
        assertThat(transformer.from(new URI("http://www.google.com/?query=parameter")), is("http://www.google.com/?query=parameter"));
    }
}
