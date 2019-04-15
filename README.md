# Scheduler [![Build Status](https://travis-ci.org/kovalevivan/scheduler.svg?branch=master)](https://travis-ci.org/kovalevivan/scheduler)
Test application for scheduling conference room

## Technology stack

* Spring Boot 2.1.4
* Java 11
* Joda time
* H2 in memory database

## What was implemented
* REST API with one method that receives a request in JSON format and returns all successfully submitted reservations in JSON format (including all previously submitted reservations)

## Request scheme
```javascript
{
	"officeHours": "HHmmHHmm",
	"requestList": [{
			"submissionTime": "yyyy-MM-dd HH:mm:ss",
			"employeeId": "",
			"meetingStartTime": "yyyy-MM-dd HH:mm:ss",
			"meetingDurationHours": 1
		}]
}
```

## Response scheme
```javascript
[ {
  "date" : "yyyy-MM-dd",
  "schedule" : [ {
    "startTime" : "HH:mm",
    "endTime" : "HH:mm",
    "employeeId" : ""
  }, {
    "startTime" : "HH:mm",
    "endTime" : "HH:mm",
    "employeeId" : ""
  } ]
} ]
```
## Build
```cmd
mvn clean install
```
