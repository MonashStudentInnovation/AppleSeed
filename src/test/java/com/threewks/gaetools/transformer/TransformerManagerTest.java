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
package com.threewks.gaetools.transformer;

import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.collection.Triplets;
import com.threewks.gaetools.search.gae.meta.IndexType;
import com.threewks.gaetools.test.TestSupport;
import com.threewks.gaetools.transformer.data.ByteArrayToInputStream;
import com.threewks.gaetools.transformer.data.InputStreamToByteArray;
import com.threewks.gaetools.transformer.data.StringToInputStream;
import com.threewks.gaetools.transformer.date.BigDecimalToDateTime;
import com.threewks.gaetools.transformer.date.DateTimeToBigDecimal;
import com.threewks.gaetools.transformer.date.DateTimeToDate;
import com.threewks.gaetools.transformer.date.DateTimeToLong;
import com.threewks.gaetools.transformer.date.DateTimeToString;
import com.threewks.gaetools.transformer.date.DateToDateTime;
import com.threewks.gaetools.transformer.date.DateToLong;
import com.threewks.gaetools.transformer.date.DateToString;
import com.threewks.gaetools.transformer.date.LongToDate;
import com.threewks.gaetools.transformer.date.LongToDateTime;
import com.threewks.gaetools.transformer.date.ObjectToDateTime;
import com.threewks.gaetools.transformer.date.ReadableInstantToDate;
import com.threewks.gaetools.transformer.date.ReadableInstantToLong;
import com.threewks.gaetools.transformer.date.ReadableInstantToString;
import com.threewks.gaetools.transformer.date.StringToDate;
import com.threewks.gaetools.transformer.date.StringToDateTime;
import com.threewks.gaetools.transformer.date.StringToReadableInstant;
import com.threewks.gaetools.transformer.discrete.BooleanToString;
import com.threewks.gaetools.transformer.discrete.EnumToString;
import com.threewks.gaetools.transformer.discrete.StringToBoolean;
import com.threewks.gaetools.transformer.numeric.BigDecimalToString;
import com.threewks.gaetools.transformer.numeric.BigIntegerToString;
import com.threewks.gaetools.transformer.numeric.ByteToString;
import com.threewks.gaetools.transformer.numeric.DoubleToBigDecimal;
import com.threewks.gaetools.transformer.numeric.DoubleToString;
import com.threewks.gaetools.transformer.numeric.FloatToBigDecimal;
import com.threewks.gaetools.transformer.numeric.FloatToString;
import com.threewks.gaetools.transformer.numeric.IntegerToBigDecimal;
import com.threewks.gaetools.transformer.numeric.IntegerToString;
import com.threewks.gaetools.transformer.numeric.LongToBigDecimal;
import com.threewks.gaetools.transformer.numeric.LongToString;
import com.threewks.gaetools.transformer.numeric.NumberToAtomicInteger;
import com.threewks.gaetools.transformer.numeric.NumberToAtomicLong;
import com.threewks.gaetools.transformer.numeric.NumberToBigDecimal;
import com.threewks.gaetools.transformer.numeric.NumberToBigInteger;
import com.threewks.gaetools.transformer.numeric.NumberToDouble;
import com.threewks.gaetools.transformer.numeric.NumberToFloat;
import com.threewks.gaetools.transformer.numeric.NumberToInteger;
import com.threewks.gaetools.transformer.numeric.NumberToLong;
import com.threewks.gaetools.transformer.numeric.NumberToShort;
import com.threewks.gaetools.transformer.numeric.NumberToString;
import com.threewks.gaetools.transformer.numeric.ShortToString;
import com.threewks.gaetools.transformer.numeric.StringToBigDecimal;
import com.threewks.gaetools.transformer.numeric.StringToBigInteger;
import com.threewks.gaetools.transformer.numeric.StringToByte;
import com.threewks.gaetools.transformer.numeric.StringToDouble;
import com.threewks.gaetools.transformer.numeric.StringToFloat;
import com.threewks.gaetools.transformer.numeric.StringToInteger;
import com.threewks.gaetools.transformer.numeric.StringToLong;
import com.threewks.gaetools.transformer.numeric.StringToNumber;
import com.threewks.gaetools.transformer.numeric.StringToShort;
import com.threewks.gaetools.transformer.text.CharToString;
import com.threewks.gaetools.transformer.text.StringToChar;
import com.threewks.gaetools.transformer.url.StringToUri;
import com.threewks.gaetools.transformer.url.StringToUrl;
import com.threewks.gaetools.transformer.url.UriToString;
import com.threewks.gaetools.transformer.url.UrlToString;
import com.threewks.gaetools.transformer.uuid.StringToUUID;
import com.threewks.gaetools.transformer.uuid.UUIDToString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.atomicleopard.expressive.Expressive.list;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class TransformerManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private TransformerManager transformerManager = TransformerManager.createEmpty();

    @Test
    public void shouldGetTransformForRegisteredType() {
        StringToLong transformer = new StringToLong();

        assertThat(transformerManager.getTransformer(String.class, Long.class), is(nullValue()));

        transformerManager.register(String.class, Long.class, transformer);

        assertThat(transformerManager.getTransformer(String.class, Long.class), is((ETransformer<String, Long>) transformer));
    }

    @Test
    public void shouldGetTransformerSafeForRegisteredType() {
        StringToLong transformer = new StringToLong();

        transformerManager.register(String.class, Long.class, transformer);

        assertThat(transformerManager.getTransformerSafe(String.class, Long.class), is((ETransformer<String, Long>) transformer));
    }

    @Test
    public void shouldReturnANoopTransformerForSameTypes() {
        ETransformer<String, String> noop = transformerManager.getTransformer(String.class, String.class);
        assertThat(noop, is(notNullValue()));
        assertThat(noop.from("input"), is("input"));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void shouldReturnANoopTransformerForAssignableTypes() {
        ETransformer<ArrayList, List> noop = transformerManager.getTransformer(ArrayList.class, List.class);
        assertThat(noop, is(notNullValue()));
        ArrayList<String> original = new ArrayList<>();
        original.add("entry");
        assertThat(noop.from(original), sameInstance((List) original));
    }

    @Test
    public void shouldReturnANoopBestTransformerForSameTypes() {
        ETransformer<? super String, ? extends String> noop = transformerManager.getBestTransformer(String.class, String.class);
        assertThat(noop, is(notNullValue()));
        assertThat(noop.from("input"), is("input"));
    }

    @Test
    public void shouldReturnTransformerForBasicTypeIfBoxedTypeTransformerRegistered() {
        assertThat(transformerManager.getTransformer(String.class, Long.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(String.class, long.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(Long.class, String.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(long.class, String.class), is(nullValue()));

        transformerManager.register(String.class, Long.class, new StringToLong());

        assertThat(transformerManager.getTransformer(String.class, Long.class), is(notNullValue()));
        assertThat(transformerManager.getTransformer(String.class, long.class), is(notNullValue()));

        transformerManager.register(Long.class, String.class, new LongToString());

        assertThat(transformerManager.getTransformer(Long.class, String.class), is(notNullValue()));
        assertThat(transformerManager.getTransformer(long.class, String.class), is(notNullValue()));
    }

    @Test
    public void shouldRegisterBasicTypeAndBoxedTypeForToAndFromTransformer() {
        ETransformer<Integer, Long> expected = from -> null;

        assertThat(transformerManager.getTransformer(Integer.class, Long.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(int.class, long.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(Integer.class, long.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(int.class, Long.class), is(nullValue()));

        transformerManager.register(Integer.class, Long.class, expected);

        assertThat(transformerManager.getTransformer(Integer.class, Long.class), is(expected));
        assertThat(transformerManager.getTransformer(int.class, long.class), is(expected));
        assertThat(transformerManager.getTransformer(Integer.class, long.class), is(expected));
        assertThat(transformerManager.getTransformer(int.class, Long.class), is(expected));
    }

    @Test
    public void shouldTakeTypeInheritenceIntoAccountWhenGettingBestTransformerConsideringSource() {
        ObjectToDateTime transformer = new ObjectToDateTime();
        transformerManager.register(Object.class, DateTime.class, transformer);

        assertThat(transformerManager.getTransformer(String.class, DateTime.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(Long.class, DateTime.class), is(nullValue()));

        ETransformer<? super String, ? extends DateTime> stringToDateTime = transformerManager.getBestTransformer(String.class, DateTime.class);
        assertThat(stringToDateTime, is(notNullValue()));
        assertThat(stringToDateTime.from("2014-06-05T00:00:00.000Z").compareTo(new DateTime(2014, 6, 5, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC)), is(0));

        ETransformer<? super Long, ? extends DateTime> longToDateTime = transformerManager.getBestTransformer(Long.class, DateTime.class);
        assertThat(longToDateTime, is(notNullValue()));
        assertThat(longToDateTime.from(123456L).compareTo(new DateTime(123456)), is(0));
    }

    @Test
    public void shouldTakeTypeInheritenceIntoAccountWhenGettingBestTransformerWhenConsideringDestination() {
        ObjectToDateTime transformer = new ObjectToDateTime();
        transformerManager.register(Object.class, DateTime.class, transformer);

        assertThat(transformerManager.getTransformer(String.class, ReadableInstant.class), is(nullValue()));
        assertThat(transformerManager.getTransformer(Long.class, ReadableInstant.class), is(nullValue()));

        ETransformer<? super String, ? extends ReadableInstant> stringToDateTime = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        assertThat(stringToDateTime, is(notNullValue()));
        assertThat(stringToDateTime.from("2014-06-05T00:00:00.000Z").compareTo(new DateTime(2014, 6, 5, 0, 0, 0).withZoneRetainFields(DateTimeZone.UTC)), is(0));

        ETransformer<? super Long, ? extends ReadableInstant> longToDateTime = transformerManager.getBestTransformer(Long.class, ReadableInstant.class);
        assertThat(longToDateTime, is(notNullValue()));
        assertThat(longToDateTime.from(123456L).compareTo(new DateTime(123456)), is(0));
    }

    @Test
    public void shouldCacheBestTransformerForFasterLaterLookups() {
        ObjectToDateTime registered = new ObjectToDateTime();
        transformerManager.register(Object.class, DateTime.class, registered);

        ETransformer<? super String, ? extends ReadableInstant> found = transformerManager.getBestTransformer(String.class, ReadableInstant.class);

        ETransformer<? super String, ? extends ReadableInstant> cached = transformerManager.getFromCache(String.class, ReadableInstant.class);
        assertThat(cached, is(notNullValue()));
        assertThat(cached, is(sameInstance((Object) registered)));
        assertThat(cached, is(sameInstance((Object) found)));

        // inject another instance into the cache
        ObjectToDateTime newInstance = new ObjectToDateTime();
        transformerManager.addToCache(String.class, ReadableInstant.class, newInstance);

        ETransformer<? super String, ? extends ReadableInstant> found2 = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        assertThat(found2, sameInstance((Object) newInstance));
    }

    @Test
    public void shouldTransformEnumFromStringUsingBestTransformer() {
        ETransformer<? super String, ? extends IndexType> transformer = transformerManager.getBestTransformer(String.class, IndexType.class);
        assertThat(transformer, is(notNullValue()));

        assertThat(transformer.from("Automatic"), is(IndexType.Automatic));
        assertThat(transformer.from("automatic"), is(IndexType.Automatic));
        assertThat(transformer.from("AUTOMATIC"), is(IndexType.Automatic));

    }

    @Test
    public void shouldTransformObjectToEnum() {
        ETransformer<? super Object, ? extends IndexType> transformer = transformerManager.getTransformer(Object.class, IndexType.class);
        assertThat(transformer, is(notNullValue()));

        assertThat(transformer.from("Automatic"), is(IndexType.Automatic));
        assertThat(transformer.from("automatic"), is(IndexType.Automatic));
        assertThat(transformer.from("AUTOMATIC"), is(IndexType.Automatic));
    }

    @Test
    public void shouldClearConvertorCacheWhenNewTransformersRegistered() {
        ObjectToDateTime registered = new ObjectToDateTime();
        transformerManager.register(Object.class, DateTime.class, registered);

        ETransformer<? super String, ? extends ReadableInstant> found = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        ETransformer<? super String, ? extends ReadableInstant> cached = transformerManager.getFromCache(String.class, ReadableInstant.class);
        assertThat(found, is((Object) registered));
        assertThat(cached, is((Object) registered));

        StringToDateTime registered2 = new StringToDateTime();
        transformerManager.register(String.class, DateTime.class, registered2);

        ETransformer<? super String, ? extends ReadableInstant> found2 = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        ETransformer<? super String, ? extends ReadableInstant> cached2 = transformerManager.getFromCache(String.class, ReadableInstant.class);
        assertThat(found2, is((Object) registered2));
        assertThat(cached2, is((Object) registered2));
    }

    @Test
    public void shouldUnregisterPreviouslyRegisteredTransformerAndClearCache() {
        ObjectToDateTime registered = new ObjectToDateTime();
        transformerManager.register(Object.class, DateTime.class, registered);

        ETransformer<? super String, ? extends ReadableInstant> found = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        ETransformer<? super String, ? extends ReadableInstant> cached = transformerManager.getFromCache(String.class, ReadableInstant.class);
        assertThat(found, is((Object) registered));
        assertThat(cached, is((Object) registered));

        transformerManager.unregister(Object.class, DateTime.class);

        ETransformer<? super String, ? extends ReadableInstant> found2 = transformerManager.getBestTransformer(String.class, ReadableInstant.class);
        ETransformer<? super String, ? extends ReadableInstant> cached2 = transformerManager.getFromCache(String.class, ReadableInstant.class);
        assertThat(found2, is(nullValue()));
        assertThat(cached2, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenNoBestTransformerFound() {
        assertThat(transformerManager.getBestTransformer(String.class, Integer.class), is(nullValue()));
    }

    @Test
    public void shouldTransformGivenObject() {
        StringToLong registered = new StringToLong();
        transformerManager.register(String.class, Long.class, registered);

        assertThat(transformerManager.transform(String.class, Long.class, "1"), is(1L));
        assertThat(transformerManager.transform(String.class, Number.class, "1"), is((Number) 1L));
    }

    @Test
    public void shouldTransformAllGivenObjects() {
        StringToLong registered = new StringToLong();
        transformerManager.register(String.class, Long.class, registered);

        assertThat(transformerManager.transformAll(String.class, Long.class, list("1", "2")), is(list(1L, 2L)));
        assertThat(transformerManager.transformAll(String.class, Number.class, list("1", "2")), is(list((Number) 1L, (Number) 2L)));
    }

    @Test
    public void shouldTransformAllGivenObjects2() {
        NumberToString registered = new NumberToString();
        transformerManager.register(Number.class, String.class, registered);

        assertThat(transformerManager.transformAll(Number.class, String.class, Expressive.list(1L, 2)), is(list("1", "2")));
        assertThat(transformerManager.transformAll(Integer.class, String.class, list(1, 2)), is(list("1", "2")));
    }

    @Test
    public void shouldThrowTransformerExceptionWhenNoTransformerAvailable() {
        thrown.expect(TransformerException.class);
        thrown.expectMessage("No ETransformer<java.lang.String, java.lang.Long> registered in this TransformerManager");

        transformerManager.getTransformerSafe(String.class, Long.class);
    }

    @Test
    public void shouldThrowTransformerExceptionWhenNoBestTransformerFoundForTransform() {
        thrown.expect(TransformerException.class);
        thrown.expectMessage("No transformation available from 'java.lang.String' to 'java.lang.Long'");
        transformerManager.transform(String.class, Long.class, "1");
    }

    @Test
    public void shouldThrowTransformerExceptionWhenNoBestTransformerFoundForTransformAll() {
        thrown.expect(TransformerException.class);
        thrown.expectMessage("No transformation available from 'java.lang.String' to 'java.lang.Long'");
        transformerManager.transformAll(String.class, Long.class, list("1", "2"));
    }

    @Test
    public void shouldCopyTransformerRetainingRegisteredTransformers() {
        StringToLong transformer = new StringToLong();
        LongToString transformer2 = new LongToString();

        transformerManager.register(String.class, Long.class, transformer);
        transformerManager.register(Long.class, String.class, transformer2);

        TransformerManager copy = transformerManager.copy();
        assertThat(copy, is(not(sameInstance(transformerManager))));

        assertThat(copy.getTransformer(String.class, Long.class), is((ETransformer<String, Long>) transformer));
        assertThat(copy.getTransformer(Long.class, String.class), is((ETransformer<Long, String>) transformer2));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldRegisterDefaultTransformers() {
        transformerManager = TransformerManager.createWithDefaults();

        // String transformers
        assertThat(transformerManager.getTransformer(Character.class, String.class) instanceof CharToString, is(true));
        assertThat(transformerManager.getTransformer(String.class, Character.class) instanceof StringToChar, is(true));

        // discrete types
        assertThat(transformerManager.getTransformer(Boolean.class, String.class) instanceof BooleanToString, is(true));
        assertThat(transformerManager.getTransformer(String.class, Boolean.class) instanceof StringToBoolean, is(true));
        assertThat(transformerManager.getTransformer(Enum.class, String.class) instanceof EnumToString, is(true));

        // numeric types
        assertThat(transformerManager.getTransformer(Enum.class, String.class) instanceof EnumToString, is(true));
        assertThat(transformerManager.getTransformer(String.class, Byte.class) instanceof StringToByte, is(true));
        assertThat(transformerManager.getTransformer(String.class, Integer.class) instanceof StringToInteger, is(true));
        assertThat(transformerManager.getTransformer(String.class, Long.class) instanceof StringToLong, is(true));
        assertThat(transformerManager.getTransformer(String.class, Short.class) instanceof StringToShort, is(true));
        assertThat(transformerManager.getTransformer(String.class, Float.class) instanceof StringToFloat, is(true));
        assertThat(transformerManager.getTransformer(String.class, Double.class) instanceof StringToDouble, is(true));
        assertThat(transformerManager.getTransformer(String.class, BigDecimal.class) instanceof StringToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(String.class, BigInteger.class) instanceof StringToBigInteger, is(true));
        assertThat(transformerManager.getTransformer(String.class, Number.class) instanceof StringToNumber, is(true));
        assertThat(transformerManager.getTransformer(Byte.class, String.class) instanceof ByteToString, is(true));
        assertThat(transformerManager.getTransformer(Long.class, String.class) instanceof LongToString, is(true));
        assertThat(transformerManager.getTransformer(Long.class, BigDecimal.class) instanceof LongToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(Integer.class, String.class) instanceof IntegerToString, is(true));
        assertThat(transformerManager.getTransformer(Integer.class, BigDecimal.class) instanceof IntegerToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(Short.class, String.class) instanceof ShortToString, is(true));
        assertThat(transformerManager.getTransformer(Double.class, String.class) instanceof DoubleToString, is(true));
        assertThat(transformerManager.getTransformer(Double.class, BigDecimal.class) instanceof DoubleToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(Float.class, String.class) instanceof FloatToString, is(true));
        assertThat(transformerManager.getTransformer(Float.class, BigDecimal.class) instanceof FloatToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(BigDecimal.class, String.class) instanceof BigDecimalToString, is(true));
        assertThat(transformerManager.getTransformer(BigInteger.class, String.class) instanceof BigIntegerToString, is(true));
        assertThat(transformerManager.getTransformer(Number.class, String.class) instanceof NumberToString, is(true));
        assertThat(transformerManager.getTransformer(Number.class, Float.class) instanceof NumberToFloat, is(true));
        assertThat(transformerManager.getTransformer(Number.class, Double.class) instanceof NumberToDouble, is(true));
        assertThat(transformerManager.getTransformer(Number.class, Integer.class) instanceof NumberToInteger, is(true));
        assertThat(transformerManager.getTransformer(Number.class, Long.class) instanceof NumberToLong, is(true));
        assertThat(transformerManager.getTransformer(Number.class, Short.class) instanceof NumberToShort, is(true));
        assertThat(transformerManager.getTransformer(Number.class, BigInteger.class) instanceof NumberToBigInteger, is(true));
        assertThat(transformerManager.getTransformer(Number.class, BigDecimal.class) instanceof NumberToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(Number.class, AtomicInteger.class) instanceof NumberToAtomicInteger, is(true));
        assertThat(transformerManager.getTransformer(Number.class, AtomicLong.class) instanceof NumberToAtomicLong, is(true));

        // date types
        assertThat(transformerManager.getTransformer(String.class, DateTime.class) instanceof StringToDateTime, is(true));
        assertThat(transformerManager.getTransformer(String.class, Date.class) instanceof StringToDate, is(true));
        assertThat(transformerManager.getTransformer(String.class, ReadableInstant.class) instanceof StringToReadableInstant, is(true));
        assertThat(transformerManager.getTransformer(DateTime.class, String.class) instanceof DateTimeToString, is(true));
        assertThat(transformerManager.getTransformer(DateTime.class, Long.class) instanceof DateTimeToLong, is(true));
        assertThat(transformerManager.getTransformer(DateTime.class, BigDecimal.class) instanceof DateTimeToBigDecimal, is(true));
        assertThat(transformerManager.getTransformer(DateTime.class, Date.class) instanceof DateTimeToDate, is(true));
        assertThat(transformerManager.getTransformer(Date.class, String.class) instanceof DateToString, is(true));
        assertThat(transformerManager.getTransformer(Date.class, DateTime.class) instanceof DateToDateTime, is(true));
        assertThat(transformerManager.getTransformer(Date.class, Long.class) instanceof DateToLong, is(true));
        assertThat(transformerManager.getTransformer(ReadableInstant.class, String.class) instanceof ReadableInstantToString, is(true));
        assertThat(transformerManager.getTransformer(ReadableInstant.class, Long.class) instanceof ReadableInstantToLong, is(true));
        assertThat(transformerManager.getTransformer(ReadableInstant.class, Date.class) instanceof ReadableInstantToDate, is(true));
        assertThat(transformerManager.getTransformer(Object.class, DateTime.class) instanceof ObjectToDateTime, is(true));
        assertThat(transformerManager.getTransformer(Long.class, DateTime.class) instanceof LongToDateTime, is(true));
        assertThat(transformerManager.getTransformer(BigDecimal.class, DateTime.class) instanceof BigDecimalToDateTime, is(true));
        assertThat(transformerManager.getTransformer(Long.class, Date.class) instanceof LongToDate, is(true));

        // url types
        assertThat(transformerManager.getTransformer(URL.class, String.class) instanceof UrlToString, is(true));
        assertThat(transformerManager.getTransformer(URI.class, String.class) instanceof UriToString, is(true));
        assertThat(transformerManager.getTransformer(String.class, URL.class) instanceof StringToUrl, is(true));
        assertThat(transformerManager.getTransformer(String.class, URI.class) instanceof StringToUri, is(true));

        // uuids
        assertThat(transformerManager.getTransformer(UUID.class, String.class) instanceof UUIDToString, is(true));
        assertThat(transformerManager.getTransformer(String.class, UUID.class) instanceof StringToUUID, is(true));

        // data
        assertThat(transformerManager.getTransformer(byte[].class, InputStream.class), instanceOf(ByteArrayToInputStream.class));
        assertThat(transformerManager.getTransformer(InputStream.class, byte[].class), instanceOf(InputStreamToByteArray.class));
        assertThat(transformerManager.getTransformer(String.class, InputStream.class), instanceOf(StringToInputStream.class));

        // This is just a simple check to remind this test to update when more defaults are added
        Triplets<Class<?>, Class<?>, ETransformer<?, ?>> transformers = TestSupport.getField(transformerManager, "transformers");
        assertThat(transformers.size(), is(92));
    }
}
