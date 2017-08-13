# Spring boot GAE project for Java 8

Test project used to test Spring boot on AppEngine standard with Java 8. See  [Trello Board](https://trello.com/b/R4VCZVW9/spring-boot-gae). 

## Running

Currently just running locally through terminal only

```
mvn appengine:devserver
```

Didn't seem to work with the IntelliJ runner. A TODO task.

## Proofs

List of concepts proven

* **Objectify integration:** root url calls repository.
* **Task queues:** `/admin/queue-message` `POST` endpoint. Queues a task that eventually just writes a log message.
* **Annotation field validation:** Above request includes validation that `message` is not blank. (currently just returns 400 page)

TODO: 
* **Exception handler and error responses**: Some basic exception mapping to REST responses and 400 error handler to return JSON instead of 400 page.

