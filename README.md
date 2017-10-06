SpringBoot Base for Google App Engine API
<img src="https://avatars2.githubusercontent.com/u/22784829?v=4" height="40em" style="float: right" />
---

Current Version: `v0.2.0`

**Current Build Status**: ![status](https://circleci.com/gh/MonashUnitPlanner/springboot-base-gae-java8.png?circle-token=:circle-token)

This is the base for the API which is built on AppEngine Standard with Java 8 using SpringBoot.

You can also read more about developing on this [here](https://monashunitplanner.github.io/springboot-base-gae-java8/#/)

## Contributors
Thanks to 3wks for the gae-tool and spring-boot-gae modules

### Proudly Built by the [monPlan team](https://monashunitplanner.github.io)

## Available Tools:
- Custom Exceptions built on top of Springboot
- GAE Tools by [3wks](https://github.com/3wks)
- Swagger

## Security Tokens
Security Tokens are automatically generated upon load with the username `user`, for example:
``` 
[INFO]
[INFO] Using default security password: 712c1aa7-d7b6-4364-a4ac-a19c5aa47f3d
[INFO]
```

Therefore you can do a HTTP Request with username:password
for example:
```
user:712c1aa7-d7b6-4364-a4ac-a19c5aa47f3d
```
