package org.keycloak.authentication.authenticators.conditional;

import org.keycloak.Config;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

import static java.text.MessageFormat.format;
import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.MULTIVALUED_STRING_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

public class ConditionalClientIpAddressAuthenticatorFactory implements ConditionalAuthenticatorFactory {

    public static final String PROVIDER_ID = "conditional-client-ip-address";

    static final String CONF_IP_RANGES = "ip-ranges";
    static final String CONF_EXCLUDE = "exclude";
    static final String CONF_USE_FORWARDED_HEADER = "use-forwarded-header";
    static final String CONF_FORWARDED_HEADER_NAME = "forwarded-header-name";
    static final String CONF_FORWARDED_HEADER_NAME_DEFAULT = "X-Forwarded-For";
    static final String CONF_TRUSTED_PROXIES_COUNT = "trusted-proxies-count";


    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = new AuthenticationExecutionModel.Requirement[]{AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED};

    @Override
    public void init(Config.Scope config) {
        // no-op
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Condition - IP range";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow is executed only if the client ip address is in specified ip ranges / subnets";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        final ProviderConfigProperty ipRanges = new ProviderConfigProperty();
        ipRanges.setType(MULTIVALUED_STRING_TYPE);
        ipRanges.setName(CONF_IP_RANGES);
        ipRanges.setDefaultValue("a:b:c:d::/64");
        ipRanges.setLabel("IP ranges / subnets");
        ipRanges.setHelpText("A list of IP ranges. Supports IPv6 and IPv4, supports CIDR and netmask notation. Examples: a:b:c:d::/64, a.b.c.d/255.255.0.0");

        final ProviderConfigProperty exclude = new ProviderConfigProperty();
        exclude.setType(BOOLEAN_TYPE);
        exclude.setName(CONF_EXCLUDE);
        exclude.setLabel("Exclude specified ranges");
        exclude.setHelpText("Match if client IP address is NOT in specified ranges");

        final ProviderConfigProperty useForwardedHeader = new ProviderConfigProperty();
        useForwardedHeader.setType(BOOLEAN_TYPE);
        useForwardedHeader.setName(CONF_USE_FORWARDED_HEADER);
        useForwardedHeader.setLabel("Use a 'forwarded' header");
        useForwardedHeader.setHelpText(format("Use IP addresses from a header added by reverse proxies (e.g. {0}). Be aware of security concerns when using this header and set 'Number of trusted proxies' accordingly (https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For#security_and_privacy_concerns)",
                CONF_FORWARDED_HEADER_NAME_DEFAULT));

        final ProviderConfigProperty forwardedHeaderName = new ProviderConfigProperty();
        forwardedHeaderName.setType(STRING_TYPE);
        forwardedHeaderName.setName(CONF_FORWARDED_HEADER_NAME);
        forwardedHeaderName.setLabel("Forwarded header name");
        forwardedHeaderName.setDefaultValue(CONF_FORWARDED_HEADER_NAME_DEFAULT);
        forwardedHeaderName.setRequired(false);
        forwardedHeaderName.setHelpText(format("Optional: Name of the forwarded header (Default: {0})", CONF_FORWARDED_HEADER_NAME_DEFAULT));

        final ProviderConfigProperty trustedProxiesCount = new ProviderConfigProperty();
        trustedProxiesCount.setType(STRING_TYPE);
        trustedProxiesCount.setName(CONF_TRUSTED_PROXIES_COUNT);
        trustedProxiesCount.setLabel("Number of trusted proxies");
        trustedProxiesCount.setDefaultValue("1");
        trustedProxiesCount.setHelpText("Number of trusted proxies. Only the last n ip addresses from the forwarded header will be used");

        return Arrays.asList(ipRanges, exclude, useForwardedHeader, forwardedHeaderName, trustedProxiesCount);
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalClientIpAddressAuthenticator.SINGLETON;
    }
}
