package org.keycloak.authentication.authenticators.conditional;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_EXCLUDE;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_IP_RANGES;
import static org.keycloak.models.Constants.CFG_DELIMITER_PATTERN;

public class ConditionalClientIpAddressAuthenticator implements ConditionalAuthenticator {

    public static final ConditionalClientIpAddressAuthenticator SINGLETON = new ConditionalClientIpAddressAuthenticator();

    private static final Logger LOG = Logger.getLogger(ConditionalClientIpAddressAuthenticator.class);
    private static final String X_FORWARDED_FOR_HEADER_NAME = "X-Forwarded-For";

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {

        final Map<String, String> config = context.getAuthenticatorConfig().getConfig();
        final boolean exclude = Boolean.parseBoolean(config.get(CONF_EXCLUDE));
        final Stream<IPAddress> ipRanges = getConfiguredIpRanges(config);

        final IPAddress clientIpAddress = getClientIpAddress(context);
        if (exclude) {
            return ipRanges.noneMatch(ipRange -> ipRange.contains(clientIpAddress));
        } else {
            return ipRanges.anyMatch(ipRange -> ipRange.contains(clientIpAddress));
        }
    }

    private Stream<IPAddress> getConfiguredIpRanges(Map<String, String> config) {

        final String ipRangesString = config.get(CONF_IP_RANGES);
        if (ipRangesString == null) {
            throw new IllegalStateException("No IP ranges configured");
        }

        final String[] ipRanges = CFG_DELIMITER_PATTERN.split(ipRangesString);
        if (ipRanges.length == 0) {
            throw new IllegalStateException("No IP ranges configured");
        }

        return Arrays.stream(ipRanges)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::parseIpAddress)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private IPAddress getClientIpAddress(AuthenticationFlowContext context) {

        final List<String> xForwardedForHeaders = context.getHttpRequest()
                .getHttpHeaders()
                .getRequestHeader(X_FORWARDED_FOR_HEADER_NAME);

        final Optional<IPAddress> ipAddressFromForwardedHeader = xForwardedForHeaders
                .stream()
                .map(this::parseIpAddress)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        if (ipAddressFromForwardedHeader.isPresent()) {
            return ipAddressFromForwardedHeader.get();
        }

        final String ipAddressStringFromClientConnection = context.getConnection().getRemoteAddr();
        final Optional<IPAddress> ipAddressFromConnection = parseIpAddress(ipAddressStringFromClientConnection);
        if (ipAddressFromConnection.isPresent()) {
            return ipAddressFromConnection.get();
        }

        throw new IllegalStateException(format("No valid ip address found in {0} header ({1}) or in client connection ({2})",
                X_FORWARDED_FOR_HEADER_NAME, xForwardedForHeaders, ipAddressStringFromClientConnection));
    }

    private Optional<IPAddress> parseIpAddress(String text) {

        IPAddressString ipAddressString = new IPAddressString(text);

        if (!ipAddressString.isValid()) {
            LOG.warn("Ignoring invalid IP address " + ipAddressString);
            return Optional.empty();
        }

        try {
            final IPAddress parsedIpAddress = ipAddressString.toAddress();
            return Optional.of(parsedIpAddress);
        } catch (AddressStringException e) {
            LOG.warn("Ignoring invalid IP address " + ipAddressString, e);
            return Optional.empty();
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Not used
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // Not used
    }

    @Override
    public void close() {
        // Does nothing
    }
}