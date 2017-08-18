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
package com.threewks.gaetools.util;

import com.threewks.gaetools.exception.BaseException;
import jodd.io.FastByteArrayOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Streams {
    private static final String DefaultEncoding = "UTF-8";
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String readString(InputStream inputStream) {
        return readString(inputStream, DefaultEncoding);
    }

    public static String readString(InputStream inputStream, String encoding) {
        try {
            byte[] data = readBytes(inputStream);
            return new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new BaseException(e, "Could not create string with encoding %s: %s", encoding, e.getMessage());
        }
    }

    public static byte[] readBytes(InputStream inputStream) {
        try {
            FastByteArrayOutputStream byteArrayOutputStream = new FastByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new BaseException(e, "Failed to read from InputStream: %s", e.getMessage());
        }
    }

    public static byte[] getResourceAsBytes(String resource) {
        try {
            InputStream resourceAsStream = getResourceAsStream(resource);
            return readBytes(resourceAsStream);
        } catch (Exception e) {
            throw new BaseException(e, "Could not load resource %s: %s", resource, e.getMessage());
        }
    }

    public static String getResourceAsString(String resource) {
        InputStream resourceAsStream = getResourceAsStream(resource);
        return readString(resourceAsStream);
    }

    public static InputStream getResourceAsStream(String resource) {
        try {
            InputStream resourceAsStream = Streams.class.getClassLoader().getResourceAsStream(resource);
            if (resourceAsStream == null) {
                throw new BaseException("Could not load resource %s: resource not found", resource);
            }
            return new BufferedInputStream(resourceAsStream);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(e, "Could not load resource %s: %s", resource, e.getMessage());
        }
    }

    /**
     * Copy from the given input stream to the given output stream using a temporary buffer.
     *
     * @param input
     * @param output
     * @return
     */
    public static long copy(InputStream input, OutputStream output) {
        return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * Copy from the given input stream to the given output stream using the given byte[] as a buffer.
     *
     * @param input
     * @param output
     * @param buffer
     * @return
     */
    public static long copy(InputStream input, OutputStream output, byte[] buffer) {
        try {
            long count = 0;
            int n;
            while ((n = input.read(buffer)) > -1) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (IOException e) {
            throw new BaseException(e, "Could not copy input stream to output stream: %s", e.getMessage());
        }
    }
}
