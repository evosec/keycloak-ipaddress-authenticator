package org.keycloak.authentication.authenticators.conditional;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.format.IPAddressRange;
import org.jboss.logging.Logger;
import org.keycloak.models.AuthenticatorConfigModel;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_EXCLUDE;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_FORWARDED_HEADER_NAME;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_FORWARDED_HEADER_NAME_DEFAULT;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_IP_RANGES;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_TRUSTED_PROXIES_COUNT;
import static org.keycloak.authentication.authenticators.conditional.ConditionalClientIpAddressAuthenticatorFactory.CONF_USE_FORWARDED_HEADER;
import static org.keycloak.models.Constants.CFG_DELIMITER_PATTERN;

class ConditionalClientIpAddressAuthenticatorConfig {

    private static final Logger LOG = Logger.getLogger(ConditionalClientIpAddressAuthenticatorConfig.class);

    final Set<IPAddressRange> ipRanges;
    final boolean exclude;
    final boolean useForwardedHeader;
    final String forwardedHeaderName;
    final int trustedProxiesCount;

    public ConditionalClientIpAddressAuthenticatorConfig(AuthenticatorConfigModel configModel) {
        this(configModel.getConfig());
    }

    public ConditionalClientIpAddressAuthenticatorConfig(Map<String, String> configMap) {
        this.ipRanges = getConfiguredIpRanges(configMap);
        this.exclude = Boolean.parseBoolean(configMap.get(CONF_EXCLUDE));
        this.useForwardedHeader = Boolean.parseBoolean(configMap.get(CONF_USE_FORWARDED_HEADER));
        this.forwardedHeaderName = configMap.getOrDefault(CONF_FORWARDED_HEADER_NAME, CONF_FORWARDED_HEADER_NAME_DEFAULT);
        this.trustedProxiesCount = Integer.parseInt(configMap.get(CONF_TRUSTED_PROXIES_COUNT));
    }

    private Set<IPAddressRange> getConfiguredIpRanges(Map<String, String> config) {

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
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<IPAddress> parseIpAddress(String text) {

        final IPAddressString ipAddressString = new IPAddressString(text);

        if (!ipAddressString.isValid()) {
            LOG.warn("Ignoring invalid IP address from config " + ipAddressString);
            return Optional.empty();
        }

        try {
            final IPAddress parsedIpAddress = ipAddressString.toAddress();
            return Optional.of(parsedIpAddress);
        } catch (AddressStringException e) {
            LOG.warn("Ignoring invalid IP address from config " + ipAddressString, e);
            return Optional.empty();
        }
    }

    public Set<IPAddressRange> getIpRanges() {
        return ipRanges;
    }

    public boolean isExclude() {
        return exclude;
    }

    public boolean isUseForwardedHeader() {
        return useForwardedHeader;
    }

    public String getForwardedHeaderName() {
        return forwardedHeaderName;
    }

    public int getTrustedProxiesCount() {
        return trustedProxiesCount;
    }
}
