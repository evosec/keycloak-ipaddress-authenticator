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

    private Set<IPAddressRange> ipRanges;
    private boolean exclude;
    private boolean useForwardedHeader;
    private String forwardedHeaderName;
    private int trustedProxiesCount;

    ConditionalClientIpAddressAuthenticatorConfig() {
    }

    ConditionalClientIpAddressAuthenticatorConfig(AuthenticatorConfigModel configModel) {
        this(configModel.getConfig());
    }

    ConditionalClientIpAddressAuthenticatorConfig(Map<String, String> configMap) {
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

    public void setIpRanges(Set<IPAddressRange> ipRanges) {
        this.ipRanges = ipRanges;
    }

    public void setIpRanges(String... ipRanges) {
        this.ipRanges = Arrays.stream(ipRanges)
                .map(this::parseIpAddress)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public boolean isExclude() {
        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public boolean isUseForwardedHeader() {
        return useForwardedHeader;
    }

    public void setUseForwardedHeader(boolean useForwardedHeader) {
        this.useForwardedHeader = useForwardedHeader;
    }

    public String getForwardedHeaderName() {
        return forwardedHeaderName;
    }

    public void setForwardedHeaderName(String forwardedHeaderName) {
        this.forwardedHeaderName = forwardedHeaderName;
    }

    public int getTrustedProxiesCount() {
        return trustedProxiesCount;
    }

    public void setTrustedProxiesCount(int trustedProxiesCount) {
        this.trustedProxiesCount = trustedProxiesCount;
    }
}
