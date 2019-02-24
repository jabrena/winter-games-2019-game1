# winter-games-2019-game1
Implementation of first challenge

```
./mvnw clean test
./mvnw clean site
./mvnw surefire-report:report 
```

## Features

``` guerkin
Feature: Cloud Foundry Version Control

  Scenario: Check PCF
    Given the endpoint /api/version
    Then verify if https://api.run.pivotal.io/v2/info has the version 2.131.0
    And return true in that case
    Then verify if https://api.ng.bluemix.net/v2/info has the version 2.106.0
    And return true in that case
     
```
