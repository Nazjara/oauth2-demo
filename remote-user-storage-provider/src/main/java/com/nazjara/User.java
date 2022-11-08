package com.nazjara;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;

@Getter
@Setter
public class User extends AbstractUserAdapter {

    private String firstName;
    private String lastName;
    private String email;
    private String username;

    public User(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel) {
        super(session, realm, storageProviderModel);
    }
}
