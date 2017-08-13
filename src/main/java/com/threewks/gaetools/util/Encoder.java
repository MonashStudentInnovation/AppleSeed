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
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Encoder {
    private static final String DefaultEncoding = "UTF-8";
    private byte[] data;

    public Encoder(byte[] data) {
        this.data = data;
    }

    public Encoder(String content) {
        this(content, DefaultEncoding);
    }

    public Encoder(String content, String encoding) {
        this(getBytes(content, encoding));
    }

    public byte[] data() {
        return Arrays.copyOf(data, data.length);
    }

    public String string() {
        return getString(data, DefaultEncoding);
    }

    public String string(String encoding) {
        return getString(data, encoding);
    }

    public Encoder hex() {
        data = new Hex().encode(data);
        return this;
    }

    public Encoder unhex() {
        try {
            data = new Hex().decode(data);
            return this;
        } catch (DecoderException e) {
            throw new BaseException(e, "Failed to convert data from hex: %s", e.getMessage());
        }
    }

    public Encoder base32() {
        data = new Base32().encode(data);
        return this;
    }

    public Encoder unbase32() {
        data = new Base32().decode(data);
        return this;
    }

    public Encoder base64() {
        data = Base64.encodeBase64(data);
        return this;
    }

    public Encoder unbase64() {
        data = Base64.decodeBase64(data);
        return this;
    }

    public Encoder md5() {
        return digest("MD5");
    }

    public Encoder sha1() {
        return digest("SHA-1");
    }

    public Encoder sha256() {
        return digest("SHA-256");
    }

    public Encoder crc32() {
        Checksum checksum = new CRC32();
        checksum.update(data, 0, data.length);
        long checksumValue = checksum.getValue();
        data = Long.toHexString(checksumValue).getBytes();
        return unhex();
    }

    public Encoder digest(String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            data = md.digest(data);
            return this;
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(e, "'%s' MessageDigest algorithm unavailable", algorithm);
        }
    }

    private static byte[] getBytes(String content, String encoding) {
        try {
            return content.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new BaseException(e, "The specified encoding '%s' is not supported", encoding);
        }
    }

    private static String getString(byte[] data, String encoding) {
        try {
            return new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new BaseException(e, "The specified encoding '%s' is not supported", encoding);
        }
    }
}
