# winter-games-2019-game1

[![Build Status](https://travis-ci.org/jabrena/winter-games-2019-game1.svg?branch=master)](https://travis-ci.org/jabrena/winter-games-2019-game1)
[![codecov](https://codecov.io/gh/jabrena/winter-games-2019-game1/branch/master/graph/badge.svg)](https://codecov.io/gh/jabrena/winter-games-2019-game1)

Implementation of first challenge

```
./mvnw clean test jacoco:report surefire-report:report
```

## Features

``` guerkin
Feature: Cloud Foundry Version Control

  Scenario: Check PCF
    Given the endpoint /api/version
    Then verify if https://api.run.pivotal.io/v2/info has the version 2.131.0
    Then verify if https://api.ng.bluemix.net/v2/info has the version 2.106.0
    And return true if both cases are true, in other case return false
     
```

## Checklist

- [x] Call PCF & Bluemix in a sequential way
- [x] Call PCF & Bluemix in parallel
- [ ] Manage 404 Errors
- [ ] Improve error handling at webclient level
- [ ] Error handling at functional level
- [ ] Manage Error handling at global level
 
