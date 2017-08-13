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
package com.threewks.gaetools.search;

import java.util.List;

public interface Result<T, K> {
    /**
     * Return the set of results as a hydrated object. The ability to do this is implementation dependent
     * (i.e. whether you get a fully hydrated object, partially hydrated object, or if it just won't work).
     *
     * @return
     * @throws SearchException
     */
    List<T> getResults() throws SearchException;

    /**
     * Return the set of keys for the matching results;
     *
     * @return
     * @throws SearchException
     */
    List<K> getResultIds() throws SearchException;

    /**
     * The number of total records matching the query (potentially an estimated number based on {@link Search#accuracy()})
     *
     * @return
     */
    long getMatchingRecordCount();

    /**
     * The number of records return in this result
     *
     * @return
     */
    long getReturnedRecordCount();

    String cursor();

}
