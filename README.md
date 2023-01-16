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
   and add 'keycloak-custom-listenere' as a listener. Then click 'Save'

# Acknowledgements
This codebase is *heavily* inspired from
[aboutbits/keycloak-extensions](https://github.com/aboutbits/keycloak-extensions) and its [blog
post](https://aboutbits.it/blog/2020-11-23-keycloak-custom-event-listener).
