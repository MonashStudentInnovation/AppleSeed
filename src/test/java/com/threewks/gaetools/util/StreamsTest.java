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
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StreamsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldReadStringFromBytes() {
        String string = "Expected string";
        ByteArrayInputStream is = new ByteArrayInputStream(string.getBytes());
        String result = Streams.readString(is);
        assertThat(result, is(string));
    }

    @Test
    public void shouldReadStringFromBytesWithSpecifiedEncoding() throws UnsupportedEncodingException {
        String string = "Expected string";
        ByteArrayInputStream is = new ByteArrayInputStream(string.getBytes("UTF-16"));
        String result = Streams.readString(is, "UTF-16");
        assertThat(result, is(string));
    }

    @Test
    public void shouldThrowBaseExceptionIfReadStringFromBytesWithUnsupportedEncoding() throws UnsupportedEncodingException {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not create string with encoding BOOM: ");
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[]{});
        Streams.readString(is, "BOOM");
    }

    @Test
    public void shouldReadBytesFromInputStream() {
        byte[] randomBytes = RandomStringUtils.random(8001).getBytes();
        InputStream inputStream = new ByteArrayInputStream(randomBytes);
        byte[] result = Streams.readBytes(inputStream);
        assertThat(result, is(randomBytes));
    }

    @Test
    public void shouldReadBytesFromInputStreamWrappingExceptionsInBaseException() throws IOException {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Failed to read from InputStream: BOOM");

        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(Mockito.any(byte[].class))).thenThrow(new IOException("BOOM"));

        Streams.readBytes(inputStream);
    }

    @Test
    public void shouldCopyFromInputStreamToOutputStream() {
        byte[] randomBytes = RandomStringUtils.random(8001).getBytes();
        InputStream inputStream = new ByteArrayInputStream(randomBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long bytes = Streams.copy(inputStream, outputStream);

        assertThat(bytes, is((long) randomBytes.length));
        assertThat(outputStream.toByteArray(), is(randomBytes));
    }

    @Test
    public void shouldCopyFromInputStreamToOutputStreamWrappingIOExceptionInBaseException() throws IOException {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not copy input stream to output stream: BOOM");

        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(Mockito.any(byte[].class))).thenThrow(new IOException("BOOM"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Streams.copy(inputStream, outputStream);
    }

    @Test
    public void shouldCopyFromInputStreamToOutputStreamUsingGivenBuffer() {
        byte[] randomBytes = RandomStringUtils.random(8001).getBytes();
        InputStream inputStream = new ByteArrayInputStream(randomBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long bytes = Streams.copy(inputStream, outputStream, new byte[10]);

        assertThat(bytes, is((long) randomBytes.length));
        assertThat(outputStream.toByteArray(), is(randomBytes));
    }

    @Test
    public void shouldCopyFromInputStreamToOutputStreamufferWrappingIOExceptionInBaseException() throws IOException {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not copy input stream to output stream: BOOM");

        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(Mockito.any(byte[].class))).thenThrow(new IOException("BOOM"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Streams.copy(inputStream, outputStream, new byte[10]);
    }

    @Test
    public void shouldGetResourceAsByteArray() {
        String fileContents = "Test data";
        assertThat(Streams.getResourceAsBytes("streams.txt"), is(fileContents.getBytes()));
    }

    @Test
    public void shouldGetResourceAsString() {
        String fileContents = "Test data";
        assertThat(Streams.getResourceAsString("streams.txt"), is(fileContents));
    }

    @Test
    public void shouldGetResourceAsInputStream() {
        String fileContents = "Test data";
        InputStream stream = Streams.getResourceAsStream("streams.txt");
        String actual = Streams.readString(stream);
        assertThat(actual, is(fileContents));
    }

    @Test
    public void shouldThrowBaseExceptionWhenFailedToGetResourceAsByteArray() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not load resource streams-dont-exist.txt:");

        Streams.getResourceAsBytes("streams-dont-exist.txt");
    }

    @Test
    public void shouldThrowBaseExceptionWhenFailedToGetResourceAsString() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not load resource streams-dont-exist.txt:");

        Streams.getResourceAsString("streams-dont-exist.txt");
    }

    @Test
    public void shouldThrowBaseExceptionWhenFailedToGetResourceAsInputStream() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("Could not load resource streams-dont-exist.txt:");

        Streams.getResourceAsStream("streams-dont-exist.txt");
    }
}
