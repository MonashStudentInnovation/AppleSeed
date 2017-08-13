package com.threewks.gaetools.search;

import com.google.appengine.api.datastore.GeoPt;
import com.googlecode.objectify.Key;
import com.threewks.gaetools.search.gae.mediator.FieldMediatorSet;
import com.threewks.gaetools.search.gae.meta.IndexType;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;

public final class SearchModule {

    private SearchModule() {
    }

    public static IndexTypeLookup defaultIndexTypeLookup() {
        IndexTypeLookup indexTypeLookup = new IndexTypeLookup();
        indexTypeLookup.addMapping(Key.class, IndexType.Identifier);
        indexTypeLookup.addMapping(com.google.appengine.api.datastore.Key.class, IndexType.Identifier);
        indexTypeLookup.addMapping(GeoPt.class, IndexType.GeoPoint);
        return indexTypeLookup;
    }

    public static FieldMediatorSet defaultFieldMediatorSet() {
        return new FieldMediatorSet();
    }

}
