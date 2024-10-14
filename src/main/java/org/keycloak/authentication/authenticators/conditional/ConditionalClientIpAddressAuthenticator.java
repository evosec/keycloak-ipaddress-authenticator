package org.keycloak.authentication.authenticators.conditional;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.format.IPAddressRange;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;

public class ConditionalClientIpAddressAuthenticator implements ConditionalAuthenticator {

    public static final ConditionalClientIpAddressAuthenticator SINGLETON = new ConditionalClientIpAddressAuthenticator();

    private static final Logger LOG = Logger.getLogger(ConditionalClientIpAddressAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {

        final ConditionalClientIpAddressAuthenticatorConfig config =
                new ConditionalClientIpAddressAuthenticatorConfig(context.getAuthenticatorConfig());
        final Optional<IPAddress> clientIpAddress = getClientIpAddress(context, config);

        if (clientIpAddress.isPresent()) {
            return doMatchCondition(clientIpAddress.get(), config);
        } else {
            return false;
        }
    }

    private boolean doMatchCondition(IPAddress clientIpAddress, ConditionalClientIpAddressAuthenticatorConfig config) {

        final boolean exclude = config.isExclude();
        final Stream<IPAddressRange> ipRanges = config.getIpRanges().stream();

        if (exclude) {
            return ipRanges.noneMatch(ipRange -> ipRange.contains(clientIpAddress));
        } else {
            return ipRanges.anyMatch(ipRange -> ipRange.contains(clientIpAddress));
        }
    }

    private Optional<IPAddress> getClientIpAddress(AuthenticationFlowContext context, ConditionalClientIpAddressAuthenticatorConfig config) {

        if (config.isUseForwardedHeader()) {
            return getClientIpAddressFromForwardedHeader(context, config);
        } else {
            final String ipAddressStringFromConnection = context.getConnection().getRemoteAddr();
            return parseIpAddress(ipAddressStringFromConnection);
        }
    }

    private Optional<IPAddress> getClientIpAddressFromForwardedHeader(AuthenticationFlowContext context, ConditionalClientIpAddressAuthenticatorConfig config) {

        final List<String> forwardedHeaders = context.getHttpRequest()
                .getHttpHeaders()
                .getRequestHeader(config.getForwardedHeaderName().trim());
        if (forwardedHeaders == null) {
            return Optional.empty();
        }

        final List<String> ipAddressesFromHeader = forwardedHeaders.stream()
                .map(h -> h.split(","))
                .flatMap(Arrays::stream)
                .collect(toList());

        if (ipAddressesFromHeader.isEmpty()) {
            return Optional.empty();
        }

        final int trustedProxiesCount = config.getTrustedProxiesCount();
        final int numberOfIpAddressesInHeader = ipAddressesFromHeader.size();
        if (numberOfIpAddressesInHeader < trustedProxiesCount) {
            LOG.warn(format("Forwarded header contains less addresses than number of trusted proxies. Not possible to securely determine client ip address. Headers: {0}",
                    forwardedHeaders));
            return Optional.empty();
        }

        final int index = numberOfIpAddressesInHeader - trustedProxiesCount;
        final String firstTrustedIpAddress = ipAddressesFromHeader.get(index);
        return parseIpAddress(firstTrustedIpAddress);
    }

    private Optional<IPAddress> parseIpAddress(String text) {

        IPAddressString ipAddressString = new IPAddressString(text.trim());

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
