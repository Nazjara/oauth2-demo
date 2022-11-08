package com.nazjara;

import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

public class RemoteUserStorageProviderFactory implements UserStorageProviderFactory<RemoteUserStorageProvider> {

    private static final String PROVIDER_NAME = "remote_user_storage_provider_factory";
    protected final List<ProviderConfigProperty> configMetadata;

    public RemoteUserStorageProviderFactory() {
        configMetadata = ProviderConfigurationBuilder.create()
                .property()
                .name("CONFIG_KEY_JDBC_DRIVER")
                .label("JDBC Driver Class")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("org.h2.Driver")
                .helpText("Fully qualified class name of the JDBC driver")
                .add()
                // ... repeat this for every property (omitted)
                .build();
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configMetadata;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config)
            throws ComponentValidationException {
        try (var connection = DbUtil.getConnection(config)) {
            connection.createStatement().execute(config.get("CONFIG_KEY_VALIDATION_QUERY"));
        }
        catch(Exception ex) {
            throw new ComponentValidationException("Unable to validate database connection",ex);
        }
    }

    @Override
    public RemoteUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new RemoteUserStorageProvider(keycloakSession, componentModel);
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }
}
