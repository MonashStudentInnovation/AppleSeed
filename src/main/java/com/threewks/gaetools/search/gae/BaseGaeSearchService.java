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

import com.atomicleopard.expressive.Cast;
import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.transform.CollectionTransformer;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Field.FieldType;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutResponse;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.search.IdTextSearchService;
import com.threewks.gaetools.search.IndexOperation;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.OrderComponent;
import com.threewks.gaetools.search.QueryComponent;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.TextSearchService;
import com.threewks.gaetools.search.gae.mediator.FieldMediator;
import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexType;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.search.gae.meta.SearchMetadata;
import com.threewks.gaetools.search.gae.naming.IndexNamingStrategy;
import com.threewks.gaetools.transformer.TransformerManager;
import jodd.bean.BeanUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * A common base class for {@link TextSearchService} and {@link IdTextSearchService} implementations that
 * use the Appengine Full Text Search API.
 *
 * @param <T>
 * @param <K>
 * @see GaeSearchService
 * @see IdGaeSearchService
 */
public abstract class BaseGaeSearchService<T, K> implements SearchExecutor<T, K, SearchImpl<T, K>> {
    private static final int IntLow = Integer.MIN_VALUE + 1;
    private static final int IntHigh = Integer.MAX_VALUE;
    private static final Date DateLow = new Date(0);
    private static final Date DateHigh = new Date(Long.MAX_VALUE);
    private static final String StringLow = "";
    private static final String StringHigh = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
    private static final Map<Is, String> IsSymbols = createIsSymbolMap();

    protected Class<T> type;
    protected SearchMetadata<T, K> metadata;
    protected FieldMediatorSet fieldMediators;
    protected TransformerManager transformerManager;
    protected IndexTypeLookup indexTypeLookup;
    protected IndexNamingStrategy indexNamingStrategy;
    protected String indexName;

    protected BaseGaeSearchService(Class<T> type, SearchConfig searchConfig, IndexNamingStrategy indexNamingStrategy) {
        this(type, new SearchMetadata<>(type, searchConfig.getIndexTypeLookup()), searchConfig, indexNamingStrategy);
    }

    protected BaseGaeSearchService(Class<T> type, Class<K> keyType, SearchConfig searchConfig, IndexNamingStrategy indexNamingStrategy) {
        this(type, new SearchMetadata<>(type, keyType, searchConfig.getIndexTypeLookup()), searchConfig, indexNamingStrategy);
    }

    protected BaseGaeSearchService(Class<T> type, SearchMetadata<T, K> metadata, SearchConfig searchConfig, IndexNamingStrategy indexNamingStrategy) {
        this.type = type;
        this.fieldMediators = searchConfig.getFieldMediators();
        this.transformerManager = searchConfig.getTransformerManager();
        this.indexTypeLookup = searchConfig.getIndexTypeLookup();
        this.indexNamingStrategy = indexNamingStrategy;
        this.indexName = indexNamingStrategy.getName(type);
        this.metadata = metadata;
    }

    public boolean hasIndexableFields() {
        return metadata.hasIndexableFields();
    }

    protected IndexOperation index(T object, K id) {
        Map<String, Object> data = metadata.getData(object);
        Document document = buildDocument(id, data);
        Future<PutResponse> putAsync = getIndex().putAsync(document);
        return new IndexOperation(putAsync);
    }

    protected IndexOperation index(Collection<T> objects) {
        if (Expressive.isEmpty(objects)) {
            return new IndexOperation(null);
        }
        List<Document> documents = new ArrayList<>(objects.size());
        for (T t : objects) {
            Map<String, Object> data = metadata.getData(t);
            K id = metadata.getId(t);
            Document document = buildDocument(id, data);
            documents.add(document);
        }
        return new IndexOperation(getIndex().putAsync(documents));
    }

    protected IndexOperation index(Map<K, T> objects) {
        if (Expressive.isEmpty(objects)) {
            return new IndexOperation(null);
        }
        List<Document> documents = new ArrayList<>(objects.size());
        for (Map.Entry<K, T> entry : objects.entrySet()) {
            Map<String, Object> data = metadata.getData(entry.getValue());
            Document document = buildDocument(entry.getKey(), data);
            documents.add(document);
        }
        return new IndexOperation(getIndex().putAsync(documents));
    }

    protected IndexOperation removeById(K id) {
        String stringId = convert(id, String.class);
        Future<Void> deleteAsync = getIndex().deleteAsync(stringId);
        return new IndexOperation(deleteAsync);
    }

    protected IndexOperation removeById(Iterable<K> ids) {
        List<String> stringIds = convert(ids, String.class);
        Future<Void> deleteAsync = getIndex().deleteAsync(stringIds);
        return new IndexOperation(deleteAsync);
    }

    protected int removeAll() {
        int count = 0;
        Index index = getIndex();
        GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).setLimit(200).build();
        GetResponse<Document> response = index.getRange(request);

        // can only delete documents in blocks of 200 so we need to iterate until they're all gone
        while (!response.getResults().isEmpty()) {
            List<String> ids = new ArrayList<>();
            for (Document document : response) {
                ids.add(document.getId());
            }
            index.delete(ids);
            count += ids.size();
            response = index.getRange(request);
        }
        return count;
    }

    protected SearchImpl<T, K> search() {
        return new SearchImpl<>(this);
    }

    @Override
    public Result<T, K> createSearchResult(SearchImpl<T, K> searchRequest) {
        String queryString = buildQueryString(searchRequest.query());
        SortOptions.Builder sortOptions = buildSortOptions(searchRequest.order());

        QueryOptions.Builder queryOptions = QueryOptions.newBuilder();
        Integer limit = searchRequest.limit();
        if (limit != null) {
            int offset = searchRequest.offset() == null ? 0 : searchRequest.offset();
            int effectiveLimit = limit + offset;
            if (effectiveLimit > 1000) {
                Logger.warn("Currently the Google Search API does not support queries with a limit over 1000. With an offset of %d and a limit of %d, you have an effective limit of %d", offset, limit, effectiveLimit);
            }
            limit = effectiveLimit;
            /* Note, this can't be more than 1000 (Crashes) */
            queryOptions = queryOptions.setLimit(limit);
        }
        if (searchRequest.accuracy() != null) {
            queryOptions.setNumberFoundAccuracy(searchRequest.accuracy());
        }
        queryOptions.setSortOptions(sortOptions);
        Query query = Query.newBuilder().setOptions(queryOptions).build(queryString);
        Future<Results<ScoredDocument>> searchAsync = getIndex().searchAsync(query);
        return new ResultImpl<>(this, searchAsync, searchRequest.offset());
    }

    protected SortOptions.Builder buildSortOptions(List<OrderComponent> order) {
        SortOptions.Builder sortOptions = SortOptions.newBuilder();
        for (OrderComponent sort : order) {
            String fieldName = getEncodedFieldName(sort.getField());
            SortExpression.Builder expression = SortExpression.newBuilder().setExpression(fieldName);
            expression = expression.setDirection(sort.isAscending() ? SortExpression.SortDirection.ASCENDING : SortExpression.SortDirection.DESCENDING);
            IndexType indexType = metadata.getIndexType(sort.getField());
            if (IndexType.SmallDecimal == indexType || IndexType.BigDecimal == indexType) {
                expression = expression.setDefaultValueNumeric(sort.isDescending() ? IntLow : IntHigh);
            } else if (IndexType.Date == indexType) {
                expression = expression.setDefaultValueDate(sort.isDescending() ? DateLow : DateHigh);
            } else {
                expression = expression.setDefaultValue(sort.isDescending() ? StringLow : StringHigh);
            }
            sortOptions = sortOptions.addSortExpression(expression);
        }
        return sortOptions;
    }

    protected String buildQueryString(List<QueryComponent> queryComponents) {
        List<String> stringQueryComponents = new ArrayList<>();
        for (QueryComponent queryComponent : queryComponents) {
            String fragmentString = convertQueryComponentToQueryFragment(queryComponent);
            stringQueryComponents.add(fragmentString);
        }
        return StringUtils.join(stringQueryComponents, " ");
    }

    protected String convertQueryComponentToQueryFragment(QueryComponent queryComponent) {
        if (!queryComponent.isFieldedQuery()) {
            return queryComponent.getQuery();
        }

        String field = this.getEncodedFieldName(queryComponent.getField());
        if (field == null) {
            throw new SearchException("Unable to build query string - there is no field named '%s' on %s", queryComponent.getField(), type.getSimpleName());
        }
        String operation = IsSymbols.get(queryComponent.getIs());
        if (queryComponent.isCollectionQuery()) {
            List<String> values = convertValuesToString(field, queryComponent.getCollectionValue());
            String stringValue = StringUtils.join(values, " OR ");
            return String.format("%s:(%s)", field, stringValue);
        } else {
            String value = convertValueToString(field, queryComponent.getValue());
            return String.format("%s%s%s", field, operation, value);
        }
    }

    protected Document buildDocument(K id, Map<String, Object> fields) {
        String stringId = convert(id, String.class);
        Builder documentBuilder = Document.newBuilder();
        documentBuilder.setId(stringId);
        for (Map.Entry<String, Object> fieldData : fields.entrySet()) {
            Object value = fieldData.getValue();
            String fieldName = fieldData.getKey();
            for (Object object : getCollectionValues(value)) {
                try {
                    Field field = buildField(metadata, fieldName, object);
                    documentBuilder.addField(field);
                } catch (Exception e) {
                    throw new SearchException(e, "Failed to add field '%s' with value '%s' to document with id '%s': %s", fieldName, value.toString(), id, e.getMessage());
                }
            }
        }

        return documentBuilder.build();
    }

    <F> Field buildField(SearchMetadata<T, K> metadata, String field, Object value) {
        com.google.appengine.api.search.Field.Builder fieldBuilder = Field.newBuilder().setName(metadata.getEncodedFieldName(field));
        IndexType indexType = metadata.getIndexType(field);
        FieldMediator<F> fieldMediator = fieldMediators.get(indexType);
        F normalised = fieldMediator.normalise(transformerManager, value);
        fieldMediator.setValue(fieldBuilder, normalised);
        return fieldBuilder.build();
    }

    @SuppressWarnings("unchecked")
    <O, R> R convert(O input, Class<R> type) {
        Class<O> inputType = (Class<O>) input.getClass();
        if (inputType == type) {
            return (R) input;
        }
        ETransformer<O, R> transformer = transformerManager.getTransformerSafe(inputType, type);
        return transformer.from(input);
    }

    @SuppressWarnings("unchecked")
    <O, R> List<R> convert(Iterable<O> inputs, Class<R> type) {
        Iterator<O> iterator = inputs.iterator();
        if (!iterator.hasNext()) {
            return Collections.emptyList();
        }

        List<R> results = new ArrayList<>();
        ETransformer<O, R> transformer = null;
        for (O t : inputs) {
            if (transformer == null) {
                Class<O> inputType = (Class<O>) t.getClass();
                transformer = transformerManager.getTransformerSafe(inputType, type);
            }
            results.add(transformer.from(t));
        }

        return results;
    }

    /**
     * We treat all values as collections.
     * Nulls are treated as an empty collection,
     * Non-collections are treated as a collection of length 1
     *
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    private Collection<Object> getCollectionValues(Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        Collection<Object> collection = Cast.as(value, Collection.class);
        return collection == null ? Collections.singleton(value) : collection;
    }

    @Override
    public List<K> getResultsAsIds(List<ScoredDocument> results) {
        EList<String> stringIds = Transformers.ToIds.from(results);
        return transformerManager.transformAll(String.class, metadata.getKeyType(), stringIds);
    }

    @Override
    public List<T> getResults(List<ScoredDocument> results) {
        EList<Map<String, Object>> data = toMap.from(results);
        EList<T> objects = toBean.from(data);
        return objects;
    }

    protected String convertValueToString(String field, Object value) {
        IndexType indexType = metadata.getIndexType(field);
        FieldMediator<?> indexTypeFieldBuilder = fieldMediators.get(indexType);
        return convertSingleValueToString(field, value, metadata, indexTypeFieldBuilder);
    }

    protected List<String> convertValuesToString(String field, Collection<Object> values) {
        IndexType indexType = metadata.getIndexType(field);
        FieldMediator<?> indexTypeFieldBuilder = fieldMediators.get(indexType);
        List<String> stringValues = new ArrayList<>();
        for (Object value : values) {
            stringValues.add(convertSingleValueToString(field, value, metadata, indexTypeFieldBuilder));
        }
        return stringValues;
    }

    protected String getEncodedFieldName(String field) {
        return metadata.getEncodedFieldName(field);
    }

    /**
     * Extension point allowing the Index implementation to be modified
     */
    protected Index getIndex() {
        SearchService searchService = SearchServiceFactory.getSearchService();
        return searchService.getIndex(IndexSpec.newBuilder().setName(indexName));
    }

    private <V> String convertSingleValueToString(String field, Object value, SearchMetadata<T, ?> metadata, FieldMediator<V> fieldMediator) {
        try {
            V normalised = fieldMediator.normalise(transformerManager, value);
            return fieldMediator.stringify(normalised);
        } catch (Exception e) {
            throw new SearchException("Cannot query the field %s %s - cannot convert the query value %s %s to a %s. You can register extra conversions using the %s", metadata.getFieldType(field)
                    .getSimpleName(), field, value.getClass().getSimpleName(), value, fieldMediator.getTargetType().getSimpleName(), TransformerManager.class.getSimpleName());
        }
    }

    private static Map<Is, String> createIsSymbolMap() {
        Map<Is, String> map = new HashMap<>();
        map.put(Is.EqualTo, "=");
        map.put(Is.GreaterThan, ">");
        map.put(Is.GreaterThanOrEqualTo, ">=");
        map.put(Is.Is, ":");
        map.put(Is.LessThan, "<");
        map.put(Is.LessThanOrEqualTo, "<=");
        map.put(Is.Like, ":~");
        return map;
    }

    public static class Transformers {
        public static CollectionTransformer<ScoredDocument, String> ToIds = Expressive.Transformers.transformAllUsing(Document::getId);
    }

    // TODO - NAO - This is a very naive implementation, a wider more flexible strategy would work well here
    private CollectionTransformer<Map<String, Object>, T> toBean = Expressive.Transformers.transformAllUsing(new ETransformer<Map<String, Object>, T>() {
        @Override
        public T from(Map<String, Object> from) {
            try {
                T instance = type.newInstance();
                for (Map.Entry<String, Object> entry : from.entrySet()) {
                    String field = metadata.getDecodedFieldName(entry.getKey());
                    BeanUtil.declaredForcedSilent.setProperty(instance, field, entry.getValue());
                }
                return instance;
            } catch (Exception e) {
                throw new SearchException(e, "Failed to create a new instance of %s for search results: %s", type.getName(), e.getMessage());
            }
        }
    });
    private CollectionTransformer<ScoredDocument, Map<String, Object>> toMap = Expressive.Transformers.transformAllUsing(from -> {
        Map<String, Object> results = new HashMap<>();
        results.put("___id___", from.getId());
        for (Field field : from.getFields()) {
            FieldType fieldType = field.getType();
            Object value = null;
            if (FieldType.TEXT.equals(fieldType)) {
                value = field.getText();
            } else if (FieldType.NUMBER.equals(fieldType)) {
                value = field.getNumber();
            } else if (FieldType.DATE.equals(fieldType)) {
                value = field.getDate();
            } else if (FieldType.ATOM.equals(fieldType)) {
                value = field.getAtom();
            } else if (FieldType.HTML.equals(fieldType)) {
                value = field.getHTML();
            } else if (FieldType.GEO_POINT.equals(fieldType)) {
                value = field.getGeoPoint();
            }
            results.put(field.getName(), value);
        }
        return results;
    });
}
