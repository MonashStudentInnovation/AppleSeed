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
import org.apache.commons.codec.binary.Hex;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class EncoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldEncodeHex() {
        assertThat(new Encoder("0").hex().string(), is("30"));
        assertThat(new Encoder("0").hex().unhex().string(), is("0"));
    }

    @Test
    public void shouldEncodeBase32() {
        assertThat(new Encoder("0").base32().string(), is("GA======"));
        assertThat(new Encoder("0").base32().unbase32().string(), is("0"));
    }

    @Test
    public void shouldEncodeBase64() {
        assertThat(new Encoder("0").base64().string(), is("MA=="));
        assertThat(new Encoder("0").base64().unbase64().string(), is("0"));
    }

    @Test
    public void shouldMd5() {
        assertThat(new Encoder("0").md5().hex().string(), is("cfcd208495d565ef66e7dff9f98764da"));
    }

    @Test
    public void shouldSha1() {
        assertThat(new Encoder("0").sha1().hex().string(), is("b6589fc6ab0dc82cf12099d1c2d40ab994e8410c"));
    }

    @Test
    public void shouldSha256() {
        assertThat(new Encoder("0").sha256().hex().string(), is("5feceb66ffc86f38d952786c6d696c79c2dbc239dd4e91b46729d73a27fb57e9"));
    }

    @Test
    public void shouldCrc32() {
        assertThat(new Encoder("0").crc32().hex().string(), is("f4dbdf21"));
    }

    @Test
    public void shouldRespectDifferentEncodings() {
        assertThat(new Encoder("0", "UTF-16BE").hex().string("UTF-16BE"), is(not("30")));
        assertThat(new Encoder("0", "UTF-16BE").hex().unhex().string("UTF-16BE"), is("0"));
    }

    @Test
    public void shouldOperateOnByteArray() throws UnsupportedEncodingException {
        assertThat(new Encoder("something".getBytes("UTF-16BE")).hex().unhex().string("UTF-16BE"), is("something"));
    }

    @Test
    public void shouldReturnByteArray() throws UnsupportedEncodingException {
        assertThat(new Encoder("something").hex().unhex().data(), is("something".getBytes()));
    }

    @Test
    public void shouldEncodeWithMd5ThenHex() throws NoSuchAlgorithmException {
        String input = "My input string";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String expected = new String(Hex.encodeHex(md5.digest(input.getBytes())));
        assertThat(new Encoder(input).md5().hex().string(), is(expected));
    }

    @Test
    public void shouldThrowBaseExceptionWhenTryingToRunUnknownDigest() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("'made-up' MessageDigest algorithm unavailable");
        new Encoder("content").digest("made-up");
    }

    @Test
    public void shouldThrowBaseExceptionWhenTryingToConvertToStringUsingUnknownStringEncoding() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("The specified encoding 'MADE_UP' is not supported");
        new Encoder("content").string("MADE_UP");
    }

    @Test
    public void shouldThrowBaseExceptionWhenTryingToConvertToBytesUsingUnknownStringEncoding() {
        thrown.expect(BaseException.class);
        thrown.expectMessage("The specified encoding 'MADE_UP' is not supported");
        new Encoder("content", "MADE_UP");
    }
}
