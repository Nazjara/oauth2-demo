package com.nazjara;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RemoteUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator,
        UserQueryProvider {

    private KeycloakSession keycloakSession;
    private ComponentModel componentModel;

    public RemoteUserStorageProvider(KeycloakSession keycloakSession, ComponentModel componentModel) {
        this.keycloakSession = keycloakSession;
        this.componentModel = componentModel;
    }

    @Override
    public boolean supportsCredentialType(String type) {
        return PasswordCredentialModel.TYPE.equals(type);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String type) {
        if (!supportsCredentialType(type)) {
            return false;
        }

        return keycloakSession.userCredentialManager()
                .getStoredCredentialsByTypeStream(realmModel, userModel, type)
                .findFirst()
                .isPresent();
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!this.supportsCredentialType(credentialInput.getType())) {
            return false;
        }

        var storageId = new StorageId(userModel.getId());
        var username = storageId.getExternalId();

        try (var connection = DbUtil.getConnection(componentModel)) {
            var statement = connection.prepareStatement("select password from users where username = ?");
            statement.setString(1, username);
            statement.execute();
            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var password = resultSet.getString(1);
                return password.equals(credentialInput.getChallengeResponse());
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public void close() {
        // can close some resources in here
    }

    // we need to implement at least one of following methods

    @Override
    public List<UserModel> getUsers(RealmModel realmModel) {
        return null;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realmModel, int i, int i1) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String s, RealmModel realmModel) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String s, RealmModel realmModel, int i, int i1) {
        return null;
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
        try (var connection = DbUtil.getConnection(componentModel)) {
            var statement = connection.prepareStatement(
                    "select " +
                            "  username, firstName, lastName, email, birthDate " +
                            "from users " +
                            "where username like ? " +
                            "order by username limit ? offset ?");

            statement.setString(1, search);
            statement.setInt(2, maxResults);
            statement.setInt(3, firstResult);
            statement.execute();
            var resultSet = statement.getResultSet();

            Stream.Builder<UserModel> stream = Stream.builder();

            while(resultSet.next()) {
                stream.add(mapUser(realm, resultSet));
            }

            return stream.build();
        }
        catch(SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(),ex);
        }
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int i, int i1) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i, int i1) {
        return null;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) {
        return null;
    }

    // we need to implement at least one of following methods

    @Override
    public UserModel getUserById(String s, RealmModel realmModel) {
        return null;
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        try (var connection = DbUtil.getConnection(componentModel)) {
            var statement = connection.prepareStatement(
                    "select " +
                            "  username, firstName, lastName, email " +
                            "from users " +
                            "where username = ?");

            statement.setString(1, username);
            statement.execute();
            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return mapUser(realmModel, resultSet);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public UserModel getUserByEmail(String s, RealmModel realmModel) {
        return null;
    }

    private UserModel mapUser(RealmModel realm, ResultSet rs) throws SQLException {
        var user = new User(keycloakSession, realm, componentModel);
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
