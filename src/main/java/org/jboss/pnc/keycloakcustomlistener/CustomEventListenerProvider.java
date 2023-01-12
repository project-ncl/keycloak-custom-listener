package org.jboss.pnc.keycloakcustomlistener;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

/**
 * Custom ListenerProvider to print to stdout events required for events of interest efforts
 * The #{onEvent} method contains all the logic
 */
public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {

        if (EventType.LOGIN.equals(event.getType())) {
            log.infof("## NEW %s EVENT", event.getType());
            log.info("-----------------------------------------------------------");

            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(realm, event.getUserId());

            String emailPlainContent = "New user registration\n\n" +
                    "Email: " + newRegisteredUser.getEmail() + "\n" +
                    "Username: " + newRegisteredUser.getUsername() + "\n" +
                    "Client: " + event.getClientId();

           log.info(emailPlainContent);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
