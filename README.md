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

## Proofs

List of concepts proven

* **Objectify integration:** root url calls repository.
* **Task queues:** `/admin/queue-message` `POST` endpoint. Queues a task that eventually just writes a log message.
* **Annotation field validation:** Above request includes validation that `message` is not blank. (currently just returns 400 page)

TODO: 
* **Exception handler and error responses**: Some basic exception mapping to REST responses and 400 error handler to return JSON instead of 400 page.

