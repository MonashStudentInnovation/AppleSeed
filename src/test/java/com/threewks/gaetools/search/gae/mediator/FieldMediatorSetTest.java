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

import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.gae.meta.IndexType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FieldMediatorSetTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldHaveAMediatorForEachIndexTypeByDefault() {
        FieldMediatorSet fieldMediatorSet = new FieldMediatorSet();
        for (IndexType indexType : IndexType.values()) {
            assertThat(fieldMediatorSet.get(indexType), is(notNullValue()));
        }
    }

    @Test
    public void shouldFailIfAFieldMediatorIsMissing() {
        thrown.expect(SearchException.class);
        thrown.expectMessage("No FieldMediator present for IndexType 'Automatic'");

        FieldMediatorSet fieldMediatorSet = new FieldMediatorSet();
        FieldMediator<?> fieldMediator = fieldMediatorSet.get(IndexType.Automatic);
        assertThat(fieldMediator, is(notNullValue()));

        fieldMediatorSet.put(IndexType.Automatic, null);

        fieldMediator = fieldMediatorSet.get(IndexType.Automatic);
        assertThat(fieldMediator, is(nullValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldAllowReplacementOfFieldMediator() {
        FieldMediatorSet fieldMediatorSet = new FieldMediatorSet();
        FieldMediator<Object> mediator = mock(FieldMediator.class);
        fieldMediatorSet.put(IndexType.BigDecimal, mediator);

        assertThat(fieldMediatorSet.get(IndexType.BigDecimal), is(mediator));
    }

    @Test
    public void shouldQuoteTerm() {
        assertThat(FieldMediatorSet.quote.from(""), is("\"\""));
        assertThat(FieldMediatorSet.quote.from(" "), is("\" \""));
        assertThat(FieldMediatorSet.quote.from("text"), is("\"text\""));
        assertThat(FieldMediatorSet.quote.from("two words"), is("\"two words\""));
        assertThat(FieldMediatorSet.quote.from("two\"words"), is("\"two\\\"words\""));
        assertThat(FieldMediatorSet.quote.from("O'Brian"), is("\"O'Brian\""));
    }
}
