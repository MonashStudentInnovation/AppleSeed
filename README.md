# monPlan API Java 1.8
This is the base for the API which is built on AppEngine Standard with Java 8 using SpringBoot.

You can also read more about developing on this [here](DOCS.md)

## Running

Currently just running locally through terminal only

```
mvn appengine:devserver
```

You should also clean the package after fixing errors
```
mvn clean package
```

## List of Sample Routes

### Units

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /unit/:unitCode | Gets Unit by UnitCode |
| POST   | /unit    | Adds Unit to Database |
