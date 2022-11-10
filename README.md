# oauth2-demo
Demo project to show capabilities of OAuth2 implementation in Spring Framework.<br />
Project contains the following modules:<br />
1. **web-client** - Spring-based OAuth2 client. Receives a request from the user, communicates with an Authorization Server to perform a login, sends a request with a JWT token to the API Gateway, returns a response.
2. **js-client** - JS-based OAuth2 client.
3. **spring-authorization-server** - Spring authorization server implementation. Used for authenticating the users and issuing access tokens containing the user data and proper access policies.
4. **api-gateway** - API gateway service based on Spring Cloud Gateway. Used for loadbalancing and routing requests from OAuth2 clients to resource servers.
5. **resource-server-1**, **resource-server-2** - Spring-based OAuth2 resource servers. Used for receiving a request from an API Gateway, communicating with an Authorization Server to validate a JWT token, returning a resource if user has an authority to access it. 
6. **discovery-server** - Eureka discovery service implementation. Used for discovering resource servers and notifying API Gateway about them.
7. **remote-user-storage-provider** - custom implementation of user storage provider for a Keycloak authorization server. Can be installed on the Keycloak server to integrate it with a remote User Federation (like an SQL database).

## Examples
### Get authorization code
#### Keycloak
```
curl --location --request GET 'localhost:8080/realms/custom-realm/protocol/openid-connect/auth?client_id=auth-app&response_type=code&scope=openid profile&state=state&redirect_uri=http://localhost:9999/callback'
```
#### Spring Authorization Server
```
curl --location --request GET 'localhost:8087/oauth2/authorize?response_type=code&client_id=clientId&redirect_uri=http://localhost:8087/authorized&scope=openid read'
```
### Get JWT token
#### Keycloak
```
curl --location --request POST 'localhost:8080/realms/custom-realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=auth-app' \
--data-urlencode 'scope=open_id profile' \
--data-urlencode 'code=c7dbd9f9-e3fb-440c-b076-9490cff40975.ef85f31f-2e31-49b0-860f-f8c2e0627b69.a1f04b8a-dbbe-4be0-b2e9-60451df62fc0' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_secret=z6YK8SFpMZ7c8QuFcRxD6MUmTeeU07lY' \
--data-urlencode 'redirect_uri=http://localhost:9999/callback'
```
#### Spring Authorization Server
```
curl --location --request POST 'localhost:8087/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=clientId' \
--data-urlencode 'client_secret=clientSecret' \
--data-urlencode 'code=cCQI4PIoWyGaMjJCbgTTFGK0ntCOZbQSeP5IRiX-m4CEuvJytu9WilsctBw6JTkBGoqeTYotamRO2uL29tRnzp-n-XKf_LkIf331ZB_KRqjFCuBxgt3PacDnWjSrGA0D' \
--data-urlencode 'redirect_uri=http://127.0.0.1:8087/authorized'
```
