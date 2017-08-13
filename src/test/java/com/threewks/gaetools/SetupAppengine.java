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
package com.threewks.gaetools;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalSearchServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SetupAppengine extends ExternalResource {
    private LocalServiceTestHelper helper;

    @Override
    protected void before() throws Throwable {
        List<LocalServiceTestConfig> testConfigs = createTestConfigs();
        helper = new LocalServiceTestHelper(testConfigs.toArray(new LocalServiceTestConfig[0]));
        helper.setTimeZone(TimeZone.getDefault());
        helper.setUp();
    }

    /**
     * Override to customise the test configs required.
     *
     * @return
     */
    protected List<LocalServiceTestConfig> createTestConfigs() {
        List<LocalServiceTestConfig> results = new ArrayList<>();
        LocalTaskQueueTestConfig queueConfig = new LocalTaskQueueTestConfig();
        queueConfig.setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml");

        results.add(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());
        results.add(queueConfig);
        results.add(new LocalMemcacheServiceTestConfig());
        results.add(new LocalSearchServiceTestConfig());
        return results;
    }

    @Override
    protected void after() {
        helper.tearDown();
    }
}
