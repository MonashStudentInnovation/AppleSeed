# monPlan API Java 1.8
Spring boot on AppEngine standard with Java 8.


## Developing

Currently just running locally through terminal only

```
mvn appengine:devserver
```

You should also clean the package after fixing errors
```
mvn clean package
```

**NOTE:** This currently does **not** work using the IntellIJ IDEA debugger.

### New Models

Each new **kind** for entity must follow the following model:

For example the creation of a new class called **David** will follow:
```
 |
 \____ controller
          \_____ DavidController.java
 |
 \____ model
          \_____ David.java
 |
 \____ service
          \_____ DavidService.java
 |
 \____ repository
          \_____ DavidRepository.java
```

and within `edu.monash.monplan.config` we have the `ObjectifyConfig` class, we add the following
```
private void registerObjectifyEntities() {
    register(Diesel.class);
    register(Unit.class);
+   register(David.class);
}
```

So we can wire this up.

#### Making the David.class Model
```java
package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.threewks.gaetools.search.SearchIndex;

@Entity
public class David {

    @Id
    private String id;

    @Index
    private String FullName;

    private List<String> memes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexedThing() {
        return indexedThing;
    }

    public void setIndexedThing(String indexedThing) {
        this.indexedThing = indexedThing;
    }

    public List<String> getSearchIndexedThing() {
        return searchIndexedThing;
    }

    public void List<String>(List<String> searchIndexedThing) {
        this.searchIndexedThing = searchIndexedThing;
    }

    
    public void init() {
        // Protects us from accidentally re-initialising an object that's retrieved from db
        this.setId(UUID.randomUUID().toString());
    }
}
```

## Proofs

List of concepts proven

* **Objectify integration:** root url calls repository.
* **Task queues:** `/admin/queue-message` `POST` endpoint. Queues a task that eventually just writes a log message.
* **Annotation field validation:** Above request includes validation that `message` is not blank. (currently just returns 400 page)

TODO: 
* **Exception handler and error responses**: Some basic exception mapping to REST responses and 400 error handler to return JSON instead of 400 page.

