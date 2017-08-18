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

import com.atomicleopard.expressive.EList;
import com.atomicleopard.expressive.ETransformer;
import com.atomicleopard.expressive.Expressive;
import com.atomicleopard.expressive.collection.Pair;
import com.atomicleopard.expressive.collection.Triplets;
import com.threewks.gaetools.introspection.ClassIntrospector;
import com.threewks.gaetools.introspection.TypeIntrospector;
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
import com.threewks.gaetools.transformer.discrete.ObjectToEnum;
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
import org.joda.time.ReadableInstant;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransformerManager {
    private Triplets<Class<?>, Class<?>, ETransformer<?, ?>> transformers = new Triplets<>(new ConcurrentHashMap<>());
    private Triplets<Class<?>, Class<?>, ETransformer<?, ?>> transformerCache = new Triplets<>(
            new ConcurrentHashMap<>());
    private ClassIntrospector classIntrospector = new ClassIntrospector();

    private TransformerManager() {
    }

    public <From, To> To transform(Class<From> fromType, Class<To> toType, From from) {
        ETransformer<? super From, ? extends To> transformer = getBestTransformer(fromType, toType);
        if (transformer == null) {
            throw new TransformerException("No transformation available from '%s' to '%s'", fromType.getName(), toType.getName());
        }
        return transformer.from(from);
    }

    @SuppressWarnings("unchecked")
    public <From, To> EList<To> transformAll(Class<From> fromType, Class<To> toType, Iterable<From> from) {
        ETransformer<From, To> transformer = (ETransformer<From, To>) getBestTransformer(fromType, toType);
        if (transformer == null) {
            throw new TransformerException("No transformation available from '%s' to '%s'", fromType.getName(), toType.getName());
        }
        return Expressive.Transformers.transformAllUsing(transformer).from(from);
    }

    public <From, To> void register(Class<From> fromType, Class<To> toType, ETransformer<From, To> transformer) {
        this.transformers.put(fromType, toType, transformer);
        // There is a race condition between clearing the cache and registration of the unboxed types
        // Generally this should not be an issue
        clearCache();
        Class<?> unboxedFrom = TypeIntrospector.unbox(fromType);
        Class<?> unboxedTo = TypeIntrospector.unbox(toType);
        if (unboxedFrom != null) {
            this.transformers.put(unboxedFrom, toType, transformer);
        }
        if (unboxedTo != null) {
            this.transformers.put(fromType, unboxedTo, transformer);
        }
        if (unboxedTo != null && unboxedFrom != null) {
            this.transformers.put(unboxedFrom, unboxedTo, transformer);
        }
    }

    public <From, To> void unregister(Class<From> fromType, Class<To> toType) {
        this.transformers.remove(fromType, toType);
        clearCache();
    }

    @SuppressWarnings("unchecked")
    public <From, To> ETransformer<From, To> getTransformer(Class<From> fromType, Class<To> toType) {
        if (TypeIntrospector.isABasicType(fromType)) {
            fromType = (Class<From>) TypeIntrospector.box(fromType);
        }
        if (TypeIntrospector.isABasicType(toType)) {
            toType = (Class<To>) TypeIntrospector.box(toType);
        }
        if (fromType == toType) {
            return NoopTransformerInstance;
        }

        ETransformer<From, To> convertor = (ETransformer<From, To>) transformers.get(fromType, toType);
        // We prefer registered converters over a noop transformer for super types
        if (convertor == null && toType.isAssignableFrom(fromType)) {
            return NoopTransformerInstance;
        }
        if (convertor == null && toType.isEnum()) {
            convertor = createEnumTransformer(toType);
        }
        return convertor;
    }

    @SuppressWarnings("unchecked")
    public <From, To> ETransformer<? super From, ? extends To> getBestTransformer(Class<From> fromType, Class<To> toType) {
        if (TypeIntrospector.isABasicType(fromType)) {
            fromType = (Class<From>) TypeIntrospector.box(fromType);
        }
        if (TypeIntrospector.isABasicType(toType)) {
            toType = (Class<To>) TypeIntrospector.box(toType);
        }
        ETransformer<? super From, ? extends To> transformer = getTransformer(fromType, toType);
        if (transformer != null) {
            return transformer;
        }
        transformer = getFromCache(fromType, toType);
        if (transformer != null) {
            return transformer;
        }

        List<Class<?>> fromTypes = classIntrospector.listImplementedTypes(fromType);
        for (Class<?> f : fromTypes) {
            for (Pair<Class<?>, Class<?>> entry : transformers.keySet()) {
                Class<?> t = entry.getB();
                if (toType.isAssignableFrom(t)) {
                    transformer = (ETransformer<? super From, ? extends To>) getTransformer(f, t);
                    if (transformer != null) {
                        addToCache(fromType, toType, transformer);
                        return transformer;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Return a transformer which can transform from the given 'from' type to the given 'to' type.
     * If no transformer can be found, throws a {@link TransformerException}
     *
     * @param fromType
     * @param toType
     * @return
     */
    public <From, To> ETransformer<From, To> getTransformerSafe(Class<From> fromType, Class<To> toType) {
        ETransformer<From, To> transformer = getTransformer(fromType, toType);
        if (transformer == null) {
            throw new TransformerException("No ETransformer<%s, %s> registered in this %s", fromType.getName(), toType.getName(), this.getClass().getSimpleName());
        }
        return transformer;
    }

    public TransformerManager copy() {
        TransformerManager transformerManager = new TransformerManager();
        transformerManager.transformers.putAll(this.transformers);
        return transformerManager;
    }

    protected <From, To> void addToCache(Class<From> fromType, Class<To> toType, ETransformer<? super From, ? extends To> transformer) {
        transformerCache.put(fromType, toType, transformer);
    }

    protected <From, To> void clearCache() {
        // TODO - this is very heavy handed - removing just those relevant entries
        // would be more effective
        transformerCache.clear();
    }

    @SuppressWarnings("unchecked")
    protected <From, To> ETransformer<? super From, ? extends To> getFromCache(Class<From> fromType, Class<To> toType) {
        return (ETransformer<? super From, ? extends To>) transformerCache.get(fromType, toType);
    }

    public static TransformerManager createEmpty() {
        return new TransformerManager();
    }

    public static TransformerManager createWithDefaults() {
        TransformerManager transformerManager = createEmpty();

        // numeric types
        transformerManager.register(String.class, Byte.class, new StringToByte());
        transformerManager.register(String.class, Integer.class, new StringToInteger());
        transformerManager.register(String.class, Long.class, new StringToLong());
        transformerManager.register(String.class, Short.class, new StringToShort());
        transformerManager.register(String.class, Float.class, new StringToFloat());
        transformerManager.register(String.class, Double.class, new StringToDouble());
        transformerManager.register(String.class, BigDecimal.class, new StringToBigDecimal());
        transformerManager.register(String.class, BigInteger.class, new StringToBigInteger());
        transformerManager.register(String.class, Number.class, new StringToNumber());
        transformerManager.register(Byte.class, String.class, new ByteToString());
        transformerManager.register(Long.class, String.class, new LongToString());
        transformerManager.register(Long.class, BigDecimal.class, new LongToBigDecimal());
        transformerManager.register(Integer.class, String.class, new IntegerToString());
        transformerManager.register(Integer.class, BigDecimal.class, new IntegerToBigDecimal());
        transformerManager.register(Short.class, String.class, new ShortToString());
        transformerManager.register(Double.class, String.class, new DoubleToString());
        transformerManager.register(Double.class, BigDecimal.class, new DoubleToBigDecimal());
        transformerManager.register(Float.class, String.class, new FloatToString());
        transformerManager.register(Float.class, BigDecimal.class, new FloatToBigDecimal());
        transformerManager.register(BigDecimal.class, String.class, new BigDecimalToString());
        transformerManager.register(BigInteger.class, String.class, new BigIntegerToString());
        transformerManager.register(Number.class, String.class, new NumberToString());
        transformerManager.register(Number.class, Float.class, new NumberToFloat());
        transformerManager.register(Number.class, Double.class, new NumberToDouble());
        transformerManager.register(Number.class, Integer.class, new NumberToInteger());
        transformerManager.register(Number.class, Long.class, new NumberToLong());
        transformerManager.register(Number.class, Short.class, new NumberToShort());
        transformerManager.register(Number.class, BigInteger.class, new NumberToBigInteger());
        transformerManager.register(Number.class, BigDecimal.class, new NumberToBigDecimal());
        transformerManager.register(Number.class, AtomicInteger.class, new NumberToAtomicInteger());
        transformerManager.register(Number.class, AtomicLong.class, new NumberToAtomicLong());

        // string/character types
        transformerManager.register(Character.class, String.class, new CharToString());
        transformerManager.register(String.class, Character.class, new StringToChar());

        // date types
        transformerManager.register(String.class, DateTime.class, new StringToDateTime());
        transformerManager.register(String.class, Date.class, new StringToDate());
        transformerManager.register(String.class, ReadableInstant.class, new StringToReadableInstant());
        transformerManager.register(DateTime.class, String.class, new DateTimeToString());
        transformerManager.register(DateTime.class, Long.class, new DateTimeToLong());
        transformerManager.register(DateTime.class, BigDecimal.class, new DateTimeToBigDecimal());
        transformerManager.register(DateTime.class, Date.class, new DateTimeToDate());
        transformerManager.register(Date.class, String.class, new DateToString());
        transformerManager.register(Date.class, DateTime.class, new DateToDateTime());
        transformerManager.register(Date.class, Long.class, new DateToLong());
        transformerManager.register(ReadableInstant.class, String.class, new ReadableInstantToString());
        transformerManager.register(ReadableInstant.class, Long.class, new ReadableInstantToLong());
        transformerManager.register(ReadableInstant.class, Date.class, new ReadableInstantToDate());
        transformerManager.register(Object.class, DateTime.class, new ObjectToDateTime());
        transformerManager.register(Long.class, DateTime.class, new LongToDateTime());
        transformerManager.register(BigDecimal.class, DateTime.class, new BigDecimalToDateTime());
        transformerManager.register(Long.class, Date.class, new LongToDate());

        // discrete types
        transformerManager.register(Boolean.class, String.class, new BooleanToString());
        transformerManager.register(Enum.class, String.class, new EnumToString());
        transformerManager.register(String.class, Boolean.class, new StringToBoolean());

        // url types
        transformerManager.register(URL.class, String.class, new UrlToString());
        transformerManager.register(URI.class, String.class, new UriToString());
        transformerManager.register(String.class, URL.class, new StringToUrl());
        transformerManager.register(String.class, URI.class, new StringToUri());

        // uuids
        transformerManager.register(UUID.class, String.class, new UUIDToString());
        transformerManager.register(String.class, UUID.class, new StringToUUID());

        // data types
        transformerManager.register(byte[].class, InputStream.class, new ByteArrayToInputStream());
        transformerManager.register(String.class, InputStream.class, new StringToInputStream());
        transformerManager.register(InputStream.class, byte[].class, new InputStreamToByteArray());

        return transformerManager;
    }

    /**
     * We handle enums as a special case transformation, a more flexible strategy using TransformerFactories might be a better
     * idea
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <From, To> ETransformer<From, To> createEnumTransformer(Class<To> toType) {
        return new ObjectToEnum(toType);
    }

    @SuppressWarnings("rawtypes")
    protected static final NoopTransformer NoopTransformerInstance = new NoopTransformer();

    @SuppressWarnings("unchecked")
    protected static final class NoopTransformer<From, To> implements ETransformer<From, To> {
        @Override
        public To from(From from) {
            return (To) from;
        }
    }

}
