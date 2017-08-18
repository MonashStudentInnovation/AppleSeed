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

import com.threewks.gaetools.search.SearchId;
import com.threewks.gaetools.search.SearchIndex;

import java.math.BigDecimal;
import java.util.Date;

class TestType {
    @SearchId
    @SearchIndex
    private int intType;
    @SearchIndex
    private long longType;
    @SearchIndex
    private BigDecimal bigDecType;
    @SearchIndex
    private String stringType;
    @SearchIndex
    private Date dateType;
    @SearchIndex
    private boolean boolType;

    public TestType(int intType, long longType, BigDecimal bigDecType, String stringType, Date dateType, boolean boolType) {
        super();
        this.intType = intType;
        this.longType = longType;
        this.bigDecType = bigDecType;
        this.stringType = stringType;
        this.dateType = dateType;
        this.boolType = boolType;
    }

    public int getGetIntType() {
        return intType;
    }

    public BigDecimal getGetBigDecType() {
        return bigDecType;
    }

    public long getGetLongType() {
        return longType;
    }

    public String getGetStringType() {
        return stringType;
    }

    public Date getGetDateType() {
        return dateType;
    }

    public boolean isIsBoolType() {
        return boolType;
    }
}
