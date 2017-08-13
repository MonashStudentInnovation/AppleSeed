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

import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.transformer.TransformerManager;

/**
 * This DTO wraps the configuration required for the {@link GaeSearchService}, {@link IdGaeSearchService} and implementations.
 * Each of the parameters are injection points that allow the addition of extra types and behaviours.
 */
public class SearchConfig {
    private TransformerManager transformerManager;
    private FieldMediatorSet fieldMediators;
    private IndexTypeLookup indexTypeLookup;

    public SearchConfig(TransformerManager transformerManager, FieldMediatorSet fieldMediators, IndexTypeLookup indexTypeLookup) {
        this.transformerManager = transformerManager;
        this.fieldMediators = fieldMediators;
        this.indexTypeLookup = indexTypeLookup;
    }

    public TransformerManager getTransformerManager() {
        return transformerManager;
    }

    public FieldMediatorSet getFieldMediators() {
        return fieldMediators;
    }

    public IndexTypeLookup getIndexTypeLookup() {
        return indexTypeLookup;
    }
}
