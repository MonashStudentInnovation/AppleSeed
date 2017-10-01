# SpringBoot Base for Google App Engine API
<img src="https://avatars2.githubusercontent.com/u/22784829?v=4" height="40em"/>

Current Version: `v0.2.0`

This framework is licensed under `MIT`, you can read more [here](https://github.com/MonashUnitPlanner/springboot-base-gae-java8/blob/master/LICENSE)

This is the base for the API which is built on AppEngine Standard with Java 8 using SpringBoot.

## Contributors
Thanks to 3wks for the gae-tool and spring-boot-gae modules

**Built by the [monPlan](https://github.com/monPlan) DevOps and Backend Team:**

| ![lorderikir](https://avatars3.githubusercontent.com/u/5687681?v=4&s=240)  | ![darvid7](https://avatars0.githubusercontent.com/u/11433468?v=4&s=240) | ![callistusystan](https://avatars1.githubusercontent.com/u/18413765?v=4&s=240)|
| --------------| ----|---|
| [@lorderikir](github.com/lorderikir) | [@darvid7](github.com/darvid7) | [@callistusystan](github.com/callistusystan) |

## Available Tools
- Custom Exceptions built on top of Springboot
- GAE Tools by [3wks](https://github.com/3wks)
- Documentation of Routes and Handling of API calls (using SwaggerUI and Swagger Springfox)

You can read more about the tools [here](#tools)

# Getting Started
To get started just fork the [base repository](https://github.com/MonashUnitPlanner/springboot-base-gae-java8)

## Prerequisites
- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- Java IDE, we recommend you use [IntelliJ IDEA](https://www.jetbrains.com/idea/)

!> You will need **JDK 1.8** as it is developed for Java 8 on Google Cloud Platform.


## Getting Started
1. Start a Project on [Google Cloud Platform](https://console.cloud.google.com)
2. Initialise an app on Google App Engine on Java, choose region and wait for initial setup services to finish
3. [Change pom.xml so that the app meets to the app-id given by GCP.](#configuring-mvn-for-gcp)

!> The **Free** Tier of Google App Engine has certain limits, for more _Google DataStore_ transactions perday, please get a paid tier.

# Developing the Backend

## Upgrade to Version 0.2 Notes
?> In order to speed up development and allow faster fixes we have updated our Framework to have predefined Controllers and Services.


## How each class relates to another
![https://i.imgur.com/rtfbJGu.png](https://i.imgur.com/rtfbJGu.png)

| Class | Extends | Description |
|-------|---------|-------------|
| Model | _none_  | How the data model is structured also known as the _schema_ |
| Repository | MonPlanRepository | The interface between the service and model, allows for searching to occur |
| Service | MonPlanService | The difference methods that a 'user' can modify the DataStore (database) |
| Controller | MonPlanController | The RESTful API calls (CRUD) that a _web client_ can call | 


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
_appId-environment_ for example, an app called `david-app`, within the `prod` enviromment it will be called `david-app-prod` within the Google Cloud Project.

## Using MVN for Google App Engine

You can read more at [Using the Maven Google App Engine CLI](#using-the-maven-google-app-engine-cli) below.

## Building a Google DataStore Model

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

# Building and Deploying a Single Paged Web Application
The framework can also be used to build and deploying a Single paged web application, such SPWAs can include a _production build of a ReactJS_ application.

?> This step is really important as if you are using **Declarative Routing** such as [_react-router_](https://github.com/ReactTraining/react-router)

The steps are to building and deploying a production ReactJS SPWA.
1. Run `yarn build` to build the application
2. Copy static files from `/build` directory into `/src/main/resources/WEBAPP`
3. Delete every **controller**, **service**, **repository** and **model** that you are not using.
4. Create a new class called _WebConfig.java_ in `src/main/java/edu/monash/monplan/config`
5. Edit `WebConfig.java` with the following:

```java
package edu.monash.monplan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig  {

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // forward requests to /admin and /user to their index.html
                registry.addViewController("/**").setViewName(
                        "forward:/index.html");
            }
        };
    }
}
```
This will route everything to React-Router, or if you need several routes, just add it to the `ViewControllerRegistry`

You can look at how we did it with our current `development` build [here](https://github.com/MonashUnitPlanner/springboot-spwa-gae-demo)

# Tools
There are several tools bundled up and ready OOB (out-of-the-box) to be deployed.

## Using the Maven Google App Engine CLI

To run and build a localised version of the app run:

```
mvn appengine:devserver
```

To deploy an application to **Google App Engine** execute:

```
mvn appengine:update
```

Sometimes, when you decide to cancel an _update_, you'll need to run:

```
mvn appengine:rollback
```

You should also clean the package after fixing errors
```
mvn clean package
```
## Using Swagger for Documentation
We have included **springfox-swagger2** and **springfox-swagger-ui** as Repos, allowing you to document code automatically.

To access the documentation locally visit: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

When deployed to Google App Engine, your team's frontend developers can use this tool to see and play with how the routes are handled. It is accessible over `https://my-app-name.appspot.com/swagger-ui.html`

# Tips

## Use RESTful API Principles
Really follow REST API standards, to make it easier for developers to develop on, both frontend and backend, afterall your API should make it easier for everyone to develop one,

You can read more about the standards [here](http://www.vinaysahni.com/best-practices-for-a-pragmatic-restful-api)