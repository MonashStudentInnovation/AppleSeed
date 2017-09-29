# Developing for SpringBoot Java 8 on SpringBoot-Base-GAE-Java8

## Initial Comment
> In order to speed up development and allow faster fixes we have updated our Framework to have predefined Controllers and Services.

```
Model < - > Repository < - > Service < - > Controller < - > SpringBoot < - > Frontend
```


## Configuring MVN for GCP
You will neded to edit `pom.xml`

```xml
    <groupId>springboot-base-gae-java8</groupId>
    <artifactId>${groupId}</artifactId>
    <packaging>war</packaging>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <!-- [START]: CONFIGURATION Your Application Here-->
        <appId>david-app</appId> <!-- CHANGE appID to appID-->
        <environmentID>prod</environmentID>
        <appVersion>1</appVersion>


        <!-- {END] CONFIGURATION-->


        <!-- [DO NOT MODIFY BEGIN]: Auto Configuration -->

        <!-- AUTOMATIC GAE CONFIGURATION -->

        <!--        project ID is automatically set to: 
                            {appId}-{environmentID}
                    so in GCP, the project is named the same
                    for example monplan-frontend-dev
                    is monPlan frontend Environment
                                                                -->
        <projectID>${appId}-${environmentID}</projectID>
        <appVersion>1</appVersion>
        <!-- TO BE CONTINUED -->
    </properties>
```

**appID** needs to be changed to the main appengine project name <br/>
**envrionmentID** is the environment type <br/>

This is designed for an enterprise grade application which follows the convention: 
_appId-environment_ for example, an app called `david-app`, within the prod enviromment it will be called `david-app-prod` within the Google Cloud Project.

## Using MVN for Google App Engine

To test locally you can run `mvn appengine:devserver` <br>
To run a deployment run `mvn appengine:deploy` <br>
If you cancel, the deployment using `Ctrl+C` you will have to _rollback_ the current changes before deploying a new update, this can be done using `mvn appengine:update`

## New Models

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
    register(DavidHello.class);
+   register(David.class);
}
```

So we can wire this up.

### Making the David.class Model
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
    private String fullName;

    private List<String> memes;
    
    public String getId(){
        return id;
    }
    
    public String setId(String id){
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getMemes() {
        return memes;
    }

    public void setMemes(List<String> memes) {
        this.memes = memes;
    }
    
    
    
    public void init() {
        // Protects us from accidentally re-initialising an object that's retrieved from db
        this.setId(UUID.randomUUID().toString());
    }
}
```

### Making the Repository
```java
package edu.monash.monplan.repository;

import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.David;
import org.springframework.stereotype.Repository;

@Repository
public class DavidRepository extends MonPlanRepository<David> {

    public DavidRepository(SearchConfig searchConfig) {
        super(David.class, searchConfig, David.codeField, David.nameField);
    }
}

```

### Making the Service 

```java
package edu.monash.monplan.service;

import edu.monash.monplan.model.David;
import edu.monash.monplan.repository.MonPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class DavidService extends MonPlanService<David> {
   
    public DavidService(MonPlanRepository<David> repository) {
        super(repository);
    }
}
```

### Making The Controller
```java
package edu.monash.monplan.controller;

import edu.monash.monplan.model.David;
import edu.monash.monplan.service.MonPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/davids/")
public class DavidController extends MonPlanController<David> {

    @Autowired
    public DavidController(MonPlanService<David> service) {
        super(service);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createDavid(@RequestBody David david) {
        return this.create(david);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getDavidById(@PathVariable(value="id") String id) {
        return this.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getDavidsByParams(@RequestParam(value="code", required=false) String[] codes,
                                           @RequestParam(value="name", required=false) String[] names) {
        return this.getByParams(codes, names);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateDavidById(@PathVariable(value="id") String id,
                                         @RequestBody David David) {
        return this.updateById(id, David);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity updateDavidById(@PathVariable(value="id") String id) {
        return this.deleteById(id);
    }
}

```
