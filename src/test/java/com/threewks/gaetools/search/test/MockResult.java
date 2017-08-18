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
package com.threewks.gaetools.search.test;

import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.SearchException;

import java.util.List;

public class MockResult<T, K> implements Result<T, K> {
    private List<T> results;
    private List<K> resultIds;

    public MockResult(List<T> expectedResults, List<K> expectedResultIds) {
        this.results = expectedResults;
        this.resultIds = expectedResultIds;
    }

    @Override
    public List<T> getResults() throws SearchException {
        return results;
    }

    @Override
    public List<K> getResultIds() throws SearchException {
        return resultIds;
    }

    @Override
    public long getMatchingRecordCount() {
        return results == null ? resultIds.size() : results.size();
    }

    @Override
    public long getReturnedRecordCount() {
        return results == null ? resultIds.size() : results.size();
    }

    @Override
    public String cursor() {
        return null;
    }

}
