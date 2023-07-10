Reducing amount of http requests in a given time
-
(NO THROTTLING THIRD PARTY LIB USED - SPRING-BOOT ONLY)

Service classes in this app allows to reduce http request amount in given time by client IP address simply adding annotation above controller/service method.

```
@ControlRequestCount(<'method key value'>)
```

Code implements awareness of multithreading environment and nature of such tools.

By default, request amount reduced by 2 requests in 5 seconds from one IP address. (Can be configured in app property file)