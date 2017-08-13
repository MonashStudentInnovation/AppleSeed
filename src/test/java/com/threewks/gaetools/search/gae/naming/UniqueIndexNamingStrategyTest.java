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
package com.threewks.gaetools.search.gae.naming;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UniqueIndexNamingStrategyTest {

    private UniqueIndexNamingStrategy strategy = new UniqueIndexNamingStrategy();

    @Test
    public void shouldReturnFullClassNameAsIndexName() {
        assertThat(strategy.getName(String.class), is("java.lang.String"));
        assertThat(strategy.getName(DateTime.class), is("org.joda.time.DateTime"));
        assertThat(strategy.getName(UniqueIndexNamingStrategy.class), is("com.threewks.gaetools.search.gae.naming.UniqueIndexNamingStrategy"));
        assertThat(strategy.getName(InnerClass.class), is("com.threewks.gaetools.search.gae.naming.UniqueIndexNamingStrategyTest$InnerClass"));
    }

    private static class InnerClass {

    }
}
