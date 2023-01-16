# Keycloak custom event listener

We need a custom event listener for Keycloak / RH-SSO to print to stdout events
of interest such as login, logout, and administrative changes.

We could just set the logging level of `org.keycloak.events` to DEBUG to see
all the events, but it prints a lot of events which are not required also.

# Building
```bash
$ mvn clean install
```

# Deploying
1. Transfer the `target/keycloak-custom-listener.jar` to the `deployments`
   folder of your EAP server
2. From the admin console, select your realm, then click on the 'Events' tab
   and add 'keycloak-custom-listener' as a listener. Then click 'Save'

# Logging format
The events we are interested are of type:
- Login, Logout, errors
- admin events

For login, logout, and errors, the log will have string 'pncType=LOGIN\_LOGOUT'
and 'pncType=LOGIN\_LOGOUT\_ERROR'. For admin events, the log will have string
'pncType=ADMIN\_EVENT'.

This should help run specific queries on your logs for these events.

# Acknowledgements
This codebase is *heavily* inspired from
[aboutbits/keycloak-extensions](https://github.com/aboutbits/keycloak-extensions) and its [blog
post](https://aboutbits.it/blog/2020-11-23-keycloak-custom-event-listener).
