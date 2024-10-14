package org.keycloak.authentication.authenticators.conditional.util;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationSelectionOption;
import org.keycloak.authentication.FlowStatus;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationFlowModel;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.services.managers.BruteForceProtector;
import org.keycloak.sessions.AuthenticationSessionModel;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class TestAuthenticationFlowContext implements AuthenticationFlowContext {

        private final ClientConnection clientConnection;
        private final HttpRequest httpRequest;

        public TestAuthenticationFlowContext(String clientIp, Map<String, List<String>> headers) {

            this.clientConnection = new TestClientConnection(clientIp);
            this.httpRequest = new TestHttpRequest(headers);
        }

        @Override
        public ClientConnection getConnection() {
            return clientConnection;
        }

        @Override
        public HttpRequest getHttpRequest() {
           return httpRequest;
        }

        @Override
        public AuthenticatorConfigModel getAuthenticatorConfig() {
            throw new IllegalStateException();
        }

        @Override
        public UserModel getUser() {
            throw new IllegalStateException();
        }

        @Override
        public void setUser(UserModel user) {
            throw new IllegalStateException();
        }

        @Override
        public List<AuthenticationSelectionOption> getAuthenticationSelections() {
            throw new IllegalStateException();
        }

        @Override
        public void setAuthenticationSelections(List<AuthenticationSelectionOption> credentialAuthExecMap) {
            throw new IllegalStateException();
        }

        @Override
        public void clearUser() {
            throw new IllegalStateException();
        }

        @Override
        public void attachUserSession(UserSessionModel userSession) {
            throw new IllegalStateException();
        }

        @Override
        public AuthenticationSessionModel getAuthenticationSession() {
            throw new IllegalStateException();
        }

        @Override
        public String getFlowPath() {
            throw new IllegalStateException();
        }

        @Override
        public LoginFormsProvider form() {
            throw new IllegalStateException();
        }

        @Override
        public URI getActionUrl(String code) {
            throw new IllegalStateException();
        }

        @Override
        public URI getActionTokenUrl(String tokenString) {
            throw new IllegalStateException();
        }

        @Override
        public URI getRefreshExecutionUrl() {
            throw new IllegalStateException();
        }

        @Override
        public URI getRefreshUrl(boolean authSessionIdParam) {
            throw new IllegalStateException();
        }

        @Override
        public void cancelLogin() {
            throw new IllegalStateException();
        }

        @Override
        public void resetFlow() {
            throw new IllegalStateException();
        }

        @Override
        public void resetFlow(Runnable afterResetListener) {
            throw new IllegalStateException();
        }

        @Override
        public void fork() {
            throw new IllegalStateException();
        }

        @Override
        public void forkWithSuccessMessage(FormMessage message) {
            throw new IllegalStateException();
        }

        @Override
        public void forkWithErrorMessage(FormMessage message) {
            throw new IllegalStateException();
        }

        @Override
        public EventBuilder getEvent() {
            throw new IllegalStateException();
        }

        @Override
        public EventBuilder newEvent() {
            throw new IllegalStateException();
        }

        @Override
        public AuthenticationExecutionModel getExecution() {
            throw new IllegalStateException();
        }

        @Override
        public AuthenticationFlowModel getTopLevelFlow() {
            throw new IllegalStateException();
        }

        @Override
        public RealmModel getRealm() {
            throw new IllegalStateException();
        }

        @Override
        public UriInfo getUriInfo() {
            throw new IllegalStateException();
        }

        @Override
        public KeycloakSession getSession() {
            throw new IllegalStateException();
        }

        @Override
        public BruteForceProtector getProtector() {
            throw new IllegalStateException();
        }

        @Override
        public FormMessage getForwardedErrorMessage() {
            throw new IllegalStateException();
        }

        @Override
        public FormMessage getForwardedSuccessMessage() {
            throw new IllegalStateException();
        }

        @Override
        public FormMessage getForwardedInfoMessage() {
            throw new IllegalStateException();
        }

        @Override
        public void setForwardedInfoMessage(String message, Object... parameters) {
            throw new IllegalStateException();
        }

        @Override
        public String generateAccessCode() {
            throw new IllegalStateException();
        }

        @Override
        public AuthenticationExecutionModel.Requirement getCategoryRequirementFromCurrentFlow(String authenticatorCategory) {
            throw new IllegalStateException();
        }

        @Override
        public void success() {
            throw new IllegalStateException();
        }

        @Override
        public void failure(AuthenticationFlowError error) {
            throw new IllegalStateException();
        }

        @Override
        public void failure(AuthenticationFlowError error, Response response) {
            throw new IllegalStateException();
        }

        @Override
        public void failure(AuthenticationFlowError error, Response response, String eventDetails, String userErrorMessage) {
            throw new IllegalStateException();
        }

        @Override
        public void challenge(Response challenge) {
            throw new IllegalStateException();
        }

        @Override
        public void forceChallenge(Response challenge) {
            throw new IllegalStateException();
        }

        @Override
        public void failureChallenge(AuthenticationFlowError error, Response challenge) {
            throw new IllegalStateException();
        }

        @Override
        public void attempted() {
            throw new IllegalStateException();
        }

        @Override
        public FlowStatus getStatus() {
            throw new IllegalStateException();
        }

        @Override
        public AuthenticationFlowError getError() {
            throw new IllegalStateException();
        }

        @Override
        public String getEventDetails() {
            throw new IllegalStateException();
        }

        @Override
        public String getUserErrorMessage() {
            throw new IllegalStateException();
        }
    }
