[![Build Status](https://travis-ci.org/matek2305/spring-data-loader.svg?branch=master)](https://travis-ci.org/matek2305/spring-data-loader)
[![codecov.io](https://codecov.io/github/matek2305/spring-data-loader/coverage.svg?branch=master)](https://codecov.io/github/matek2305/spring-data-loader?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56b647c70a0ff5003b975aac/badge.svg)](https://www.versioneye.com/user/projects/56b647c70a0ff5003b975aac)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.matek2305/spring-data-loader/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.matek2305/spring-data-loader)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/191dbbecd2f3428e9552a90d03bdb8c2)](https://www.codacy.com/app/matek2305/spring-data-loader)
# spring-data-loader

Simple util to help loading presentation/development data for your Spring based application.

### Maven

```xml
<dependency>
    <groupId>com.github.matek2305</groupId>
    <artifactId>spring-data-loader</artifactId>
    <version>0.9.0</version>
</dependency>
```

### Usage

After adding dependency to your pom.xml, build.gradle etc. you must mark your configuration (or main class when using Spring Boot) with ```@EnableDataLoader``` annotation like in example below (```@Profile("dev")``` is not needed):

```java
@Profile("dev")
@Configuration
@EnableDataLoader
public class DevDataLoaderConfiguration {
}
```

After that all your beans implementing ```DataLoader``` interface will be executed during application startup so you can place your data there. If there are more than one bean and one depends on another there is ```@LoadDataAfter``` annotation to mark dependencies.

```java
@Component
@LoadDataAfter(RoleDataLoader.class)
public class UserDataLoader implements DataLoader {

    @Override
    public void load() {
      // this will be exected after RoleDataLoader#load
    }
}

@Component
public class RoleDataLoader implements DataLoader {

    @Override
    public void load() {
      // load your roles data here
    }
}
```

### Contribute

This is my first public utility library and I would be grateful for your improvement ideas, so feel free to open an [issue](https://github.com/matek2305/spring-data-loader/issues) or contribute.
