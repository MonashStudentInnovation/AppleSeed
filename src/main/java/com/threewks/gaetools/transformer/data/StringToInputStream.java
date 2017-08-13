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
package com.threewks.gaetools.transformer.data;

import com.atomicleopard.expressive.ETransformer;
import com.threewks.gaetools.exception.BaseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StringToInputStream implements ETransformer<String, InputStream> {

	@Override
    public InputStream from(String from) {
        if (from == null) {
            return null;
        }
        try {
			String encoding = "UTF-8";
			byte[] bytes = from.getBytes(encoding);
            return new ByteArrayInputStream(bytes);
        } catch (UnsupportedEncodingException e) {
            throw new BaseException(e, "Failed to get byte data from string: %s", e.getMessage());
        }
    }

}
