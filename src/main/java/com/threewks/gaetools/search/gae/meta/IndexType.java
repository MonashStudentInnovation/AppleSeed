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
package com.threewks.gaetools.search.gae.meta;

/**
 * Controls how fields are mapping into the full text search index.
 */
public enum IndexType {
    Automatic,
    Identifier,
    Text,
    /**
     * Html or html fragment content
     */
    Html,
    /**
     * Big decimals are numbers which may be outside the range of an int
     *
     * @see Integer#MIN_VALUE
     * @see Integer#MAX_VALUE
     */
    BigDecimal,
    /**
     * Small decimals are numbers guaranteed to be inside the range of an int.
     * They can be any numeric amount (int, float, BigDecimal etc) as long as they
     * can be constrained between the min and max of an int
     *
     * @see Integer#MIN_VALUE
     * @see Integer#MAX_VALUE
     */
    SmallDecimal,
    Date,
    GeoPoint
}
