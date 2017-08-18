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

import com.google.appengine.api.search.GeoPoint;
import com.threewks.gaetools.search.SearchException;
import com.threewks.gaetools.search.SearchId;
import com.threewks.gaetools.search.SearchIndex;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class SearchMetadataTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private IndexTypeLookup indexTypeLookup;

    @Before
    public void before() {
        indexTypeLookup = new IndexTypeLookup();
    }

    @Test
    public void shouldGetDataRelyingOnAnnotatedFields() {
        SearchMetadata<MetadataTestType, String> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);
        MetadataTestType testType = new MetadataTestType("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        Map<String, Object> data = metadata.getData(testType);
        assertThat(data.size(), is(7));

        assertThat(data, hasEntry("stringVal", (Object) "stringVal"));
        assertThat(data, hasEntry("intVal", (Object) 123));
        assertThat(data, hasEntry("longVal", (Object) 456L));
        assertThat(data, hasEntry("dateVal", (Object) new Date(123456789)));
        assertThat(data, hasEntry("boolVal", (Object) true));
        assertThat(data, hasEntry("bigdecVal", (Object) new BigDecimal("1.23")));
        assertThat(data, hasEntry(is("geoPointVal"), notNullValue()));
    }

    @Test
    public void shouldGetDataRelyingOnAnnotatedGetters() {
        SearchMetadata<MetadataTestJavabeanType, String> metadata = new SearchMetadata<>(MetadataTestJavabeanType.class, indexTypeLookup);
        MetadataTestJavabeanType testType = new MetadataTestJavabeanType("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        Map<String, Object> data = metadata.getData(testType);
        assertThat(data.size(), is(7));

        assertThat(data, hasEntry("stringVal", (Object) "stringVal"));
        assertThat(data, hasEntry("intVal", (Object) 123));
        assertThat(data, hasEntry("longVal", (Object) 456L));
        assertThat(data, hasEntry("dateVal", (Object) new Date(123456789)));
        assertThat(data, hasEntry("boolVal", (Object) true));
        assertThat(data, hasEntry("bigdecVal", (Object) new BigDecimal("1.23")));
        assertThat(data, hasEntry(is("geoPointVal"), notNullValue()));
    }

    @Test
    public void shouldProvideMetadataAboutTargetType() {
        SearchMetadata<MetadataTestType, String> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);
        MetadataTestType testType = new MetadataTestType("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        assertThat(metadata.getType() == MetadataTestType.class, is(true));
        assertThat(metadata.getKeyType() == String.class, is(true));
        assertThat(metadata.getId(testType), is("stringVal"));
        assertThat(metadata.hasIndexableFields(), is(true));

        assertThat(metadata.getFieldType("stringVal") == String.class, is(true));
        assertThat(metadata.getFieldType("intVal") == int.class, is(true));
        assertThat(metadata.getFieldType("longVal") == Long.class, is(true));
        assertThat(metadata.getFieldType("dateVal") == Date.class, is(true));
        assertThat(metadata.getFieldType("boolVal") == boolean.class, is(true));
        assertThat(metadata.getFieldType("bigdecVal") == BigDecimal.class, is(true));
        assertThat(metadata.getFieldType("geoPointVal") == GeoPoint.class, is(true));
    }

    @Test
    public void shouldProvideMetadataAboutTargetJavabeanType() {
        SearchMetadata<MetadataTestJavabeanType, String> metadata = new SearchMetadata<>(MetadataTestJavabeanType.class, indexTypeLookup);
        MetadataTestJavabeanType testType = new MetadataTestJavabeanType("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        assertThat(metadata.getType() == MetadataTestJavabeanType.class, is(true));
        assertThat(metadata.getKeyType() == String.class, is(true));
        assertThat(metadata.getId(testType), is("stringVal"));
        assertThat(metadata.hasIndexableFields(), is(true));

        assertThat(metadata.getFieldType("stringVal") == String.class, is(true));
        assertThat(metadata.getFieldType("intVal") == int.class, is(true));
        assertThat(metadata.getFieldType("longVal") == Long.class, is(true));
        assertThat(metadata.getFieldType("dateVal") == Date.class, is(true));
        assertThat(metadata.getFieldType("boolVal") == boolean.class, is(true));
        assertThat(metadata.getFieldType("bigdecVal") == BigDecimal.class, is(true));
        assertThat(metadata.getFieldType("geoPointVal") == GeoPoint.class, is(true));
    }

    @Test
    public void shouldEncodeAndDecodeFieldNames() {
        SearchMetadata<MetadataTestType, String> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);

        assertThat(metadata.getEncodedFieldName("stringVal"), is("stringVal"));
        assertThat(metadata.getEncodedFieldName("intVal"), is("intVal"));
        assertThat(metadata.getEncodedFieldName("longVal"), is("longVal"));
        assertThat(metadata.getEncodedFieldName("dateVal"), is("dateVal"));
        assertThat(metadata.getEncodedFieldName("boolVal"), is("boolVal"));
        assertThat(metadata.getEncodedFieldName("bigdecVal"), is("bigdecVal"));
        assertThat(metadata.getEncodedFieldName("geoPointVal"), is("geoPointVal"));

        assertThat(metadata.getDecodedFieldName("stringVal"), is("stringVal"));
        assertThat(metadata.getDecodedFieldName("intVal"), is("intVal"));
        assertThat(metadata.getDecodedFieldName("longVal"), is("longVal"));
        assertThat(metadata.getDecodedFieldName("dateVal"), is("dateVal"));
        assertThat(metadata.getDecodedFieldName("boolVal"), is("boolVal"));
        assertThat(metadata.getDecodedFieldName("bigdecVal"), is("bigdecVal"));
        assertThat(metadata.getDecodedFieldName("geoPointVal"), is("geoPointVal"));

        assertThat(metadata.getEncodedFieldName("unknown"), is(nullValue()));
        assertThat(metadata.getDecodedFieldName("unknown"), is(nullValue()));
    }

    @Test
    public void shouldEncodeAndDecodeFieldNameWhenOverriddenByUser() {
        SearchMetadata<MetadataTestTypeWithNamesAndIndexTypes, String> metadata = new SearchMetadata<>(MetadataTestTypeWithNamesAndIndexTypes.class, indexTypeLookup);

        assertThat(metadata.getEncodedFieldName("string value"), is("string-value"));
        assertThat(metadata.getEncodedFieldName("int value"), is("int-value"));
        assertThat(metadata.getEncodedFieldName("long value"), is("long-value"));
        assertThat(metadata.getEncodedFieldName("date value"), is("date-value"));
        assertThat(metadata.getEncodedFieldName("bool $% value"), is("bool-value"));
        assertThat(metadata.getEncodedFieldName("big decimal value"), is("big-decimal-value"));
        assertThat(metadata.getEncodedFieldName("geo point value"), is("geo-point-value"));

        assertThat(metadata.getDecodedFieldName("string-value"), is("string value"));
        assertThat(metadata.getDecodedFieldName("int-value"), is("int value"));
        assertThat(metadata.getDecodedFieldName("long-value"), is("long value"));
        assertThat(metadata.getDecodedFieldName("date-value"), is("date value"));
        assertThat(metadata.getDecodedFieldName("bool-value"), is("bool $% value"));
        assertThat(metadata.getDecodedFieldName("big-decimal-value"), is("big decimal value"));
        assertThat(metadata.getDecodedFieldName("geo-point-value"), is("geo point value"));

        assertThat(metadata.getEncodedFieldName("unknown"), is(nullValue()));
        assertThat(metadata.getDecodedFieldName("unknown"), is(nullValue()));
    }

    @Test
    public void shouldEncodeAndDecodeFieldNamesOnGetters() {
        SearchMetadata<MetadataTestTypeWithNamesAndIndexTypesOnGetters, String> metadata = new SearchMetadata<>(MetadataTestTypeWithNamesAndIndexTypesOnGetters.class, indexTypeLookup);

        assertThat(metadata.getEncodedFieldName("string value"), is("string-value"));
        assertThat(metadata.getEncodedFieldName("int value"), is("int-value"));
        assertThat(metadata.getEncodedFieldName("long value"), is("long-value"));
        assertThat(metadata.getEncodedFieldName("date value"), is("date-value"));
        assertThat(metadata.getEncodedFieldName("bool $% value"), is("bool-value"));
        assertThat(metadata.getEncodedFieldName("big decimal value"), is("big-decimal-value"));
        assertThat(metadata.getEncodedFieldName("geo point value"), is("geo-point-value"));

        assertThat(metadata.getDecodedFieldName("string-value"), is("string value"));
        assertThat(metadata.getDecodedFieldName("int-value"), is("int value"));
        assertThat(metadata.getDecodedFieldName("long-value"), is("long value"));
        assertThat(metadata.getDecodedFieldName("date-value"), is("date value"));
        assertThat(metadata.getDecodedFieldName("bool-value"), is("bool $% value"));
        assertThat(metadata.getDecodedFieldName("big-decimal-value"), is("big decimal value"));
        assertThat(metadata.getDecodedFieldName("geo-point-value"), is("geo point value"));

        assertThat(metadata.getEncodedFieldName("unknown"), is(nullValue()));
        assertThat(metadata.getDecodedFieldName("unknown"), is(nullValue()));
    }

    @Test
    public void shouldThrowExceptionWhenNoAcceptableFieldNameCanBeEncoded() {
        thrown.expect(SearchException.class);
        thrown.expectMessage("The field name '111222' cannot be used - it could not be encoded into an acceptable representation for the text search api. Make sure it has at least one letter.");
        new SearchMetadata<InvalidIndexNameNoAlpha, String>(InvalidIndexNameNoAlpha.class, indexTypeLookup);

    }

    @Test
    public void shouldEncodeInvalidFieldNamesThatStartWithDigits() {
        SearchMetadata<InvalidIndexNameDigitsFirst, String> metadata = new SearchMetadata<>(InvalidIndexNameDigitsFirst.class, indexTypeLookup);
        assertThat(metadata.getEncodedFieldName("1stringvalue"), is("stringvalue"));
    }

    @Test
    public void shouldEncodeInvalidFieldNamesThatUseSymbols() {
        SearchMetadata<InvalidIndexNameSymbols, String> metadata = new SearchMetadata<>(InvalidIndexNameSymbols.class, indexTypeLookup);
        assertThat(metadata.getEncodedFieldName("money$"), is("money-"));
    }

    @Test
    public void shouldEncodeInvalidFieldNamesThatUseWhitespace() {
        SearchMetadata<InvalidIndexNameWhitespace, String> metadata = new SearchMetadata<>(InvalidIndexNameWhitespace.class, indexTypeLookup);
        assertThat(metadata.getEncodedFieldName("string value"), is("string-value"));
    }

    @Test
    public void shouldGuessIndexTypeWhenAutomatic() {
        SearchMetadata<MetadataTestType, String> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);
        assertThat(metadata.getIndexType("stringVal"), is(IndexType.Text));
        assertThat(metadata.getIndexType("intVal"), is(IndexType.SmallDecimal));
        assertThat(metadata.getIndexType("longVal"), is(IndexType.BigDecimal));
        assertThat(metadata.getIndexType("dateVal"), is(IndexType.Date));
        assertThat(metadata.getIndexType("boolVal"), is(IndexType.Identifier));
        assertThat(metadata.getIndexType("bigdecVal"), is(IndexType.BigDecimal));
        assertThat(metadata.getIndexType("geoPointVal"), is(IndexType.GeoPoint));
    }

    @Test
    public void shouldUseAnnotatedIndexTypesWhenPresent() {
        SearchMetadata<MetadataTestTypeWithNamesAndIndexTypes, String> metadata = new SearchMetadata<>(MetadataTestTypeWithNamesAndIndexTypes.class, indexTypeLookup);
        assertThat(metadata.getIndexType("string value"), is(IndexType.Html));
        assertThat(metadata.getIndexType("int value"), is(IndexType.BigDecimal));
        assertThat(metadata.getIndexType("long value"), is(IndexType.SmallDecimal));
        assertThat(metadata.getIndexType("date value"), is(IndexType.Identifier));
        assertThat(metadata.getIndexType("bool $% value"), is(IndexType.Text));
        assertThat(metadata.getIndexType("big decimal value"), is(IndexType.SmallDecimal));
        assertThat(metadata.getIndexType("geo point value"), is(IndexType.GeoPoint));
    }

    @Test
    public void shouldEncodeFieldNameCorrectlyForWeirdEdgeCases() {
        SearchMetadata<?, ?> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);
        assertThat(metadata.encodeFieldName("text"), is("text"));
        assertThat(metadata.encodeFieldName("text more"), is("text-more"));
        assertThat(metadata.encodeFieldName("text  more"), is("text-more"));
        assertThat(metadata.encodeFieldName(" text"), is("text"));
        assertThat(metadata.encodeFieldName("1234 text"), is("text"));
        assertThat(metadata.encodeFieldName("text 1234"), is("text-1234"));
        assertThat(metadata.encodeFieldName("text1234"), is("text1234"));
        assertThat(metadata.encodeFieldName("text_1234"), is("text_1234"));
        assertThat(metadata.encodeFieldName("text!@#!@#1234"), is("text-1234"));
        assertThat(metadata.encodeFieldName("text----____1234"), is("text-____1234"));
    }

    @Test
    public void shouldAllowIndexingOfTypeWithSearchIdWithoutSearchIndex() {
        SearchMetadata<?, ?> metadata = new SearchMetadata<>(MetadataUnindexedIdTestType.class, indexTypeLookup);
        assertThat(metadata.getEncodedFieldName("stringVal"), is("stringVal"));
    }

    @Test
    public void shouldAllowIndexingOfTypeWithoutSearchIdIfKeyTypeSupplied() {
        SearchMetadata<?, ?> metadata = new SearchMetadata<>(MetadataNoIdTestType.class, String.class, indexTypeLookup);
        assertThat(metadata.getType() == MetadataNoIdTestType.class, is(true));
        assertThat(metadata.getKeyType() == String.class, is(true));
        assertThat(metadata.getEncodedFieldName("stringVal"), is("stringVal"));
    }

    @Test
    public void shouldThrowSearchExceptionWhenNoSearchIdAndNoKeyTypeSupplied() {
        thrown.expect(SearchException.class);
        thrown.expectMessage("No id found for MetadataNoIdTestType - make sure @SearchId is on one field or method");
        new SearchMetadata<>(MetadataNoIdTestType.class, indexTypeLookup);
    }

    @Test
    public void shouldGiveUsefulMessageWhenGetIndexTypeOnUnmarkedField() {
        thrown.expect(SearchException.class);
        thrown.expectMessage("There is no field or getter marked with @SearchIndex for MetadataTestType.notstringVal - make sure it is indexed and accessible");
        SearchMetadata<MetadataTestType, String> metadata = new SearchMetadata<>(MetadataTestType.class, indexTypeLookup);
        metadata.getIndexType("notstringVal");
    }

    @Test
    public void shouldReturnHasIndexableFields() {
        assertThat(new SearchMetadata<>(MetadataTestType.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(MetadataNoIdTestType.class, String.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(MetadataTestJavabeanType.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(MetadataTestTypeWithNamesAndIndexTypes.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(MetadataTestTypeWithNamesAndIndexTypesOnGetters.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(MetadataUnindexedIdTestType.class, indexTypeLookup).hasIndexableFields(), is(true));
        assertThat(new SearchMetadata<>(NotIndexable.class, String.class, indexTypeLookup).hasIndexableFields(), is(false));
        assertThat(new SearchMetadata<>(String.class, String.class, indexTypeLookup).hasIndexableFields(), is(false));
    }

    @Test
    public void shouldAllowIndexingOfNonJavabeanMethods() {
        SearchMetadata<IndexedFieldThatIsntAGetter, Object> searchMetadata = new SearchMetadata<>(IndexedFieldThatIsntAGetter.class, indexTypeLookup);
        assertThat(searchMetadata.hasIndexableFields(), is(true));
        assertThat(searchMetadata.getEncodedFieldName("id"), is("id"));
        assertThat(searchMetadata.getEncodedFieldName("indexValue"), is("indexValue"));
    }

    @Test
    public void shouldGetDataRelyingOnAnnotatedFieldsInSuperClass() throws Exception {

        SearchMetadata<ConcreteFieldPojo, String> metadata = new SearchMetadata<>(ConcreteFieldPojo.class, indexTypeLookup);
        ConcreteFieldPojo concreteFieldPojo = new ConcreteFieldPojo("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        Map<String, Object> data = metadata.getData(concreteFieldPojo);
        assertThat(data.size(), is(7));

        assertThat(data, hasEntry("stringVal", (Object) "stringVal"));
        assertThat(data, hasEntry("intVal", (Object) 123));
        assertThat(data, hasEntry("longVal", (Object) 456L));
        assertThat(data, hasEntry("dateVal", (Object) new Date(123456789)));
        assertThat(data, hasEntry("boolVal", (Object) true));
        assertThat(data, hasEntry("bigdecVal", (Object) new BigDecimal("1.23")));
        assertThat(data, hasEntry(is("geoPointVal"), notNullValue()));
    }

    @Test
    public void shouldGetDataRelyingOnAnnotatedGettersInSuperClass() {
        SearchMetadata<ConcreteMethodBean, String> metadata = new SearchMetadata<>(ConcreteMethodBean.class, indexTypeLookup);
        ConcreteMethodBean concreteMethodBean = new ConcreteMethodBean("stringVal", 123, 456L, new Date(123456789), true, new BigDecimal("1.23"), new GeoPoint(1.23, 4.56));
        Map<String, Object> data = metadata.getData(concreteMethodBean);
        assertThat(data.size(), is(7));

        assertThat(data, hasEntry("stringVal", (Object) "stringVal"));
        assertThat(data, hasEntry("intVal", (Object) 123));
        assertThat(data, hasEntry("longVal", (Object) 456L));
        assertThat(data, hasEntry("dateVal", (Object) new Date(123456789)));
        assertThat(data, hasEntry("boolVal", (Object) true));
        assertThat(data, hasEntry("bigdecVal", (Object) new BigDecimal("1.23")));
        assertThat(data, hasEntry(is("geoPointVal"), notNullValue()));
    }

    @SuppressWarnings("unused")
    private class MetadataTestType {
        @SearchId
        @SearchIndex
        private String stringVal;
        @SearchIndex
        private int intVal;
        @SearchIndex
        private Long longVal;
        @SearchIndex
        private Date dateVal;
        @SearchIndex
        private boolean boolVal;
        @SearchIndex
        private BigDecimal bigdecVal;
        @SearchIndex
        private GeoPoint geoPointVal;

        private String notstringVal;
        private int notintVal;
        private Long notlongVal;
        private Date notdateVal;
        private boolean notboolVal;
        private BigDecimal notbigdecVal;
        private GeoPoint notgeoPointVal;

        public MetadataTestType(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super();
            this.stringVal = this.notstringVal = stringVal;
            this.intVal = this.notintVal = intVal;
            this.longVal = this.notlongVal = longVal;
            this.dateVal = this.notdateVal = dateVal;
            this.boolVal = this.notboolVal = boolVal;
            this.bigdecVal = this.notbigdecVal = bigdecVal;
            this.geoPointVal = this.notgeoPointVal = geoPointVal;
        }
    }

    private class MetadataTestJavabeanType {
        private String stringValField;
        private int intValField;
        private Long longValField;
        private Date dateValField;
        private boolean boolValField;
        private BigDecimal bigdecValField;
        private GeoPoint geoPointValField;

        public MetadataTestJavabeanType(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super();
            this.stringValField = stringVal;
            this.intValField = intVal;
            this.longValField = longVal;
            this.dateValField = dateVal;
            this.boolValField = boolVal;
            this.bigdecValField = bigdecVal;
            this.geoPointValField = geoPointVal;
        }

        @SearchId
        @SearchIndex
        public String getStringVal() {
            return stringValField;
        }

        @SearchIndex
        public int getIntVal() {
            return intValField;
        }

        @SearchIndex
        public Long getLongVal() {
            return longValField;
        }

        @SearchIndex
        public Date getDateVal() {
            return dateValField;
        }

        @SearchIndex
        public boolean isBoolVal() {
            return boolValField;
        }

        @SearchIndex
        public BigDecimal getBigdecVal() {
            return bigdecValField;
        }

        @SearchIndex
        public GeoPoint getGeoPointVal() {
            return geoPointValField;
        }
    }

    @SuppressWarnings("unused")
    private class MetadataTestTypeWithNamesAndIndexTypes {
        @SearchId
        @SearchIndex(value = "string value", as = IndexType.Html)
        private String stringVal;
        @SearchIndex(value = "int value", as = IndexType.BigDecimal)
        private int intVal;
        @SearchIndex(value = "long value", as = IndexType.SmallDecimal)
        private Long longVal;
        @SearchIndex(value = "date value", as = IndexType.Identifier)
        private Date dateVal;
        @SearchIndex(value = "bool $% value", as = IndexType.Text)
        private boolean boolVal;
        @SearchIndex(value = "big decimal value", as = IndexType.SmallDecimal)
        private BigDecimal bigdecVal;
        @SearchIndex(value = "geo point value", as = IndexType.GeoPoint)
        private GeoPoint geoPointVal;

        public MetadataTestTypeWithNamesAndIndexTypes(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super();
            this.stringVal = stringVal;
            this.intVal = intVal;
            this.longVal = longVal;
            this.dateVal = dateVal;
            this.boolVal = boolVal;
            this.bigdecVal = bigdecVal;
            this.geoPointVal = geoPointVal;
        }
    }

    @SuppressWarnings("unused")
    private class MetadataTestTypeWithNamesAndIndexTypesOnGetters {
        private String stringVal;
        private int intVal;
        private Long longVal;
        private Date dateVal;
        private boolean boolVal;
        private BigDecimal bigdecVal;
        private GeoPoint geoPointVal;

        public MetadataTestTypeWithNamesAndIndexTypesOnGetters(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super();
            this.stringVal = stringVal;
            this.intVal = intVal;
            this.longVal = longVal;
            this.dateVal = dateVal;
            this.boolVal = boolVal;
            this.bigdecVal = bigdecVal;
            this.geoPointVal = geoPointVal;
        }

        @SearchId
        @SearchIndex(value = "string value", as = IndexType.Html)
        public String getStringVal() {
            return stringVal;
        }

        @SearchIndex(value = "int value", as = IndexType.BigDecimal)
        public int getIntVal() {
            return intVal;
        }

        @SearchIndex(value = "long value", as = IndexType.SmallDecimal)
        public Long getLongVal() {
            return longVal;
        }

        @SearchIndex(value = "date value", as = IndexType.Identifier)
        public Date getDateVal() {
            return dateVal;
        }

        @SearchIndex(value = "bool $% value", as = IndexType.Text)
        public boolean isBoolVal() {
            return boolVal;
        }

        @SearchIndex(value = "big decimal value", as = IndexType.SmallDecimal)
        public BigDecimal getBigdecVal() {
            return bigdecVal;
        }

        @SearchIndex(value = "geo point value", as = IndexType.GeoPoint)
        public GeoPoint getGeoPointVal() {
            return geoPointVal;
        }
    }

    private class InvalidIndexNameNoAlpha {
        @SearchId
        @SearchIndex(value = "111222")
        private String string;
    }

    private class InvalidIndexNameWhitespace {
        @SearchId
        @SearchIndex(value = "string value")
        private String string;
    }

    private class InvalidIndexNameDigitsFirst {
        @SearchId
        @SearchIndex(value = "1stringvalue")
        private String string;
    }

    private class InvalidIndexNameSymbols {
        @SearchId
        @SearchIndex(value = "money$")
        private String string;
    }

    private class MetadataUnindexedIdTestType {
        @SearchId
        private String stringVal;
        @SearchIndex
        private int intVal;
    }

    private class MetadataNoIdTestType {
        @SearchIndex
        private String stringVal;
        @SearchIndex
        private int intVal;
    }

    @SuppressWarnings("unused")
    private class NotIndexable {
        private String hidden;
    }

    private class IndexedFieldThatIsntAGetter {
        @SearchId
        public long id() {
            return 0;
        }

        @SearchIndex
        public String indexValue() {
            return null;
        }
    }

    public abstract class BaseFieldPojo {

        @SearchId
        @SearchIndex
        private String stringVal;

        @SearchIndex
        private int intVal;

        @SearchIndex
        private Long longVal;

        @SearchIndex
        private Date dateVal;

        @SearchIndex
        private boolean boolVal;

        @SearchIndex
        private BigDecimal bigdecVal;

        @SearchIndex
        private GeoPoint geoPointVal;

        protected BaseFieldPojo(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            this.stringVal = stringVal;
            this.intVal = intVal;
            this.longVal = longVal;
            this.dateVal = dateVal;
            this.boolVal = boolVal;
            this.bigdecVal = bigdecVal;
            this.geoPointVal = geoPointVal;
        }
    }

    public class ConcreteFieldPojo extends BaseFieldPojo {

        public ConcreteFieldPojo(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super(stringVal, intVal, longVal, dateVal, boolVal, bigdecVal, geoPointVal);
        }
    }

    private abstract class BaseMethodBean {

        private String stringValField;
        private int intValField;
        private Long longValField;
        private Date dateValField;
        private boolean boolValField;
        private BigDecimal bigdecValField;
        private GeoPoint geoPointValField;

        protected BaseMethodBean(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {

            this.stringValField = stringVal;
            this.intValField = intVal;
            this.longValField = longVal;
            this.dateValField = dateVal;
            this.boolValField = boolVal;
            this.bigdecValField = bigdecVal;
            this.geoPointValField = geoPointVal;
        }

        @SearchId
        @SearchIndex
        public String getStringVal() {
            return stringValField;
        }

        @SearchIndex
        public int getIntVal() {
            return intValField;
        }

        @SearchIndex
        public Long getLongVal() {
            return longValField;
        }

        @SearchIndex
        public Date getDateVal() {
            return dateValField;
        }

        @SearchIndex
        public boolean isBoolVal() {
            return boolValField;
        }

        @SearchIndex
        public BigDecimal getBigdecVal() {
            return bigdecValField;
        }

        @SearchIndex
        public GeoPoint getGeoPointVal() {
            return geoPointValField;
        }
    }

    private class ConcreteMethodBean extends BaseMethodBean {

        public ConcreteMethodBean(String stringVal, int intVal, Long longVal, Date dateVal, boolean boolVal, BigDecimal bigdecVal, GeoPoint geoPointVal) {
            super(stringVal, intVal, longVal, dateVal, boolVal, bigdecVal, geoPointVal);
        }
    }


}
