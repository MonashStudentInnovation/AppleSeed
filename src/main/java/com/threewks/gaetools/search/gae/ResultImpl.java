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
package com.threewks.gaetools.search.gae;

import com.google.appengine.api.search.Cursor;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.SearchException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ResultImpl<T, K> implements Result<T, K> {
    private Future<Results<ScoredDocument>> searchAsync;
    private Results<ScoredDocument> results;
    private Integer offset;
    private List<ScoredDocument> resultsList;
    private SearchExecutor<T, K, ?> searchExecutor;

    public ResultImpl(SearchExecutor<T, K, ?> searchExecutor, Future<Results<ScoredDocument>> searchAsync, Integer offset) {
        this.searchExecutor = searchExecutor;
        this.searchAsync = searchAsync;
        this.offset = offset;
    }

    /**
     * TODO: The implementation of this only works in trivial situations. Further work will be done here
     * in the future to allow the usage of the SearchService to store an entire document.
     */
    @Override
    public List<T> getResults() throws SearchException {
        return searchExecutor.getResults(resultsList());
    }

    @Override
    public List<K> getResultIds() throws SearchException {
        return searchExecutor.getResultsAsIds(resultsList());
    }

    @Override
    public long getMatchingRecordCount() {
        Results<ScoredDocument> results = results();
        return results.getNumberFound();
    }

    @Override
    public long getReturnedRecordCount() {
        Results<ScoredDocument> results = results();
        return Math.max(0, results.getNumberReturned() - offset());
    }

    @Override
    public String cursor() {
        Cursor cursor = results.getCursor();
        return cursor == null ? null : cursor.toWebSafeString();
    }

    /**
     * When applying an offset to a query in the Google Search service, you can no longer order by fields.
     * To get around this, we apply the offset manually to the searched result set.
     *
     * @return
     */
    private List<ScoredDocument> resultsList() {
        if (this.resultsList == null) {
            Results<ScoredDocument> results = results();
            List<ScoredDocument> resultsList = new ArrayList<>(results.getResults());
            int end = resultsList.size();
            int start = Math.min(offset(), end);
            this.resultsList = resultsList.subList(start, end);
        }
        return resultsList;
    }

    private Results<ScoredDocument> results() {
        if (results == null) {
            try {
                results = searchAsync.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new SearchException(e, "Failed to retrieve search results: %s", e.getMessage());
            }
        }
        return results;
    }

    private int offset() {
        return offset == null ? 0 : offset;
    }
}
