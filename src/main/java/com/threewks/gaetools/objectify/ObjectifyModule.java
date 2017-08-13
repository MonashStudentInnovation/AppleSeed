package com.threewks.gaetools.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import com.threewks.gaetools.objectify.transformers.DatastoreKeyToLongTransformer;
import com.threewks.gaetools.objectify.transformers.DatastoreKeyToStringTransformer;
import com.threewks.gaetools.objectify.transformers.KeyToLongTransformer;
import com.threewks.gaetools.objectify.transformers.KeyToStringTransformer;
import com.threewks.gaetools.objectify.transformers.StringToDatastoreKeyTransformer;
import com.threewks.gaetools.objectify.transformers.StringToKeyTransformer;
import com.threewks.gaetools.transformer.TransformerManager;

public final class ObjectifyModule {

    private ObjectifyModule() {
    }

    public static TransformerManager defaultTransformerManager() {
        TransformerManager transformerManager = TransformerManager.createWithDefaults();
        transformerManager.register(Key.class, String.class, new KeyToStringTransformer());
        transformerManager.register(Key.class, Long.class, new KeyToLongTransformer());
        transformerManager.register(String.class, Key.class, new StringToKeyTransformer());
        transformerManager.register(com.google.appengine.api.datastore.Key.class, String.class, new DatastoreKeyToStringTransformer());
        transformerManager.register(com.google.appengine.api.datastore.Key.class, Long.class, new DatastoreKeyToLongTransformer());
        transformerManager.register(String.class, com.google.appengine.api.datastore.Key.class, new StringToDatastoreKeyTransformer());
        return transformerManager;
    }

    public static void addDefaultTranslators() {
        // register Objectify converter to convert between java.uil.Date and org.joda.time.DateTime
        ObjectifyFactory objectifyFactory = ObjectifyService.factory();
        JodaTimeTranslators.add(objectifyFactory);
        objectifyFactory.getTranslators().add(new UUIDTranslatorFactory());
    }

}
