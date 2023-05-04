package org.keycloak.authentication.authenticators.conditional;

import org.keycloak.Config;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.MULTIVALUED_STRING_TYPE;

public class ConditionalClientIpAddressAuthenticatorFactory implements ConditionalAuthenticatorFactory {

    public static final String PROVIDER_ID = "conditional-client-ip-address";

    public static final String CONF_IP_RANGES = "ip-ranges";
    public static final String CONF_EXCLUDE = "exclude";

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = new AuthenticationExecutionModel.Requirement[]{
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

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
        ipRanges.setLabel("IP ranges / subnets");
        ipRanges.setHelpText("A list of IP ranges. Supports IPv6 and IPv4, supports CIDR and netmask notation. Examples: a:b:c:d::/64, a.b.c.d/255.255.0.0");

        final ProviderConfigProperty exclude = new ProviderConfigProperty();
        exclude.setType(BOOLEAN_TYPE);
        exclude.setName(CONF_EXCLUDE);
        exclude.setLabel("Exclude specified ranges");
        exclude.setHelpText("Match if client IP address is NOT in specified ranges");

        return Arrays.asList(ipRanges, exclude);
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalClientIpAddressAuthenticator.SINGLETON;
    }
}