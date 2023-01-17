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

import java.util.HashSet;
import java.util.Set;

/**
 * Custom ListenerProvider to print to stdout events required for events of interest efforts
 * The #{onEvent} method contains all the logic
 */
public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    private static final Set<EventType> TYPES_FOR_LOGIN_LOGOUT = new HashSet<>();
    private static final Set<EventType> TYPES_FOR_LOGIN_LOGOUT_ERRORS = new HashSet<>();
    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();

        TYPES_FOR_LOGIN_LOGOUT.add(EventType.LOGIN);
        TYPES_FOR_LOGIN_LOGOUT.add(EventType.REGISTER);
        TYPES_FOR_LOGIN_LOGOUT.add(EventType.LOGOUT);
        TYPES_FOR_LOGIN_LOGOUT_ERRORS.add(EventType.REGISTER_ERROR);
        TYPES_FOR_LOGIN_LOGOUT_ERRORS.add(EventType.LOGIN_ERROR);
        TYPES_FOR_LOGIN_LOGOUT_ERRORS.add(EventType.LOGOUT_ERROR);
        TYPES_FOR_LOGIN_LOGOUT.add(EventType.CLIENT_LOGIN);
        TYPES_FOR_LOGIN_LOGOUT_ERRORS.add(EventType.CLIENT_LOGIN_ERROR);
    }

    @Override
    public void onEvent(Event event) {

        if (TYPES_FOR_LOGIN_LOGOUT.contains(event.getType())) {
           log.info("pncType=LOGIN_LOGOUT " + toStringEvent(event));
        } else if (TYPES_FOR_LOGIN_LOGOUT_ERRORS.contains(event.getType())) {
            log.info("pncType=LOGIN_LOGOUT_ERROR " + toStringEvent(event));
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.info("pncType=ADMIN_EVENT " + toStringAdminEvent(adminEvent));
    }

    @Override
    public void close() {

    }

    private String toStringEvent(Event event) {
        RealmModel realm = this.model.getRealm(event.getRealmId());
        String user = event.getUserId();

        if (realm != null && user != null) {
            UserModel userModel = this.session.users().getUserById(realm, event.getUserId());
            if (userModel != null) {
                user = userModel.getUsername();
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("type=").append(event.getType()).append(" ");
        builder.append("realmId=").append(event.getRealmId()).append(" ");
        builder.append("clientId=").append(event.getClientId()).append(" ");
        builder.append("userId=").append(user).append(" ");
        builder.append("ipAddress=").append(event.getIpAddress()).append(" ");
        if (event.getError() != null) {
            builder.append("error=").append(event.getError());
        }

        return builder.toString();
    }

    private String toStringAdminEvent(AdminEvent event) {


        StringBuilder builder = new StringBuilder();
        builder.append("operationType=").append(event.getOperationType()).append(" ");
        builder.append("realmId=").append(event.getRealmId()).append(" ");
        if (event.getAuthDetails() != null) {
            builder.append("clientId=").append(event.getAuthDetails().getClientId()).append(" ");
            builder.append("userId=").append(event.getAuthDetails().getUserId()).append(" ");
            builder.append("ipAddress=").append(event.getAuthDetails().getIpAddress()).append(" ");
        }
        builder.append("resourceType=").append(event.getResourceType()).append(" ");
        builder.append("resourcePath=").append(event.getResourcePath()).append(" ");

        if (event.getError() != null) {
            builder.append("error=").append(event.getError());
        }

        return builder.toString();
    }
}
