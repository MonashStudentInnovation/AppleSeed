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
package com.threewks.gaetools.objectify;

import com.atomicleopard.expressive.Expressive;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class Refs {
    public static <T> T unref(Ref<T> ref) {
        return ref == null ? null : ref.get();
    }

    public static <T> Collection<T> unref(Collection<Ref<T>> refs) {
        return Expressive.isEmpty(refs) ? Collections.emptyList() : ofy().load().refs(refs).values();
    }

    public static <T> Ref<T> ref(T instance) {
        return instance == null ? null : Ref.create(instance);
    }

    public static <T> List<Ref<T>> ref(Iterable<T> instances) {
        List<Ref<T>> result = new ArrayList<>();
        if (instances != null) {
            for (T t : instances) {
                result.add(Refs.ref(t));
            }
        }
        return result;
    }

    public static <T> Ref<T> ref(Key<T> key) {
        return key == null ? null : Ref.create(key);
    }

    public static <T> Key<T> key(T instance) {
        return instance == null ? null : Key.create(instance);
    }

    public static <T> Key<T> key(com.google.appengine.api.datastore.Key key) {
        return key == null ? null : Key.create(key);
    }

    public static <T> T unkey(Key<T> key) {
        return key == null ? null : ofy().load().key(key).now();
    }

    public static <T> Collection<T> unkey(Iterable<Key<T>> keys) {
        return Expressive.isEmpty(keys) ? Collections.emptyList() : ofy().load().keys(keys).values();
    }

}
