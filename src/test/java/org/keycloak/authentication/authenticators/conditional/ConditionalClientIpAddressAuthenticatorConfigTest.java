package org.keycloak.authentication.authenticators.conditional;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ConditionalClientIpAddressAuthenticatorConfigTest {

    @Test
    void testMissingOptionalKeysUseDefaults() {

        final Map<String, String> configMap = new HashMap<>();
        configMap.put("ip-ranges", "1.2.3.0/24");

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig(configMap);

        assertFalse(config.isExclude());
        assertFalse(config.isUseForwardedHeader());
        assertEquals("X-Forwarded-For", config.getForwardedHeaderName());
        assertEquals(1, config.getTrustedProxiesCount());
    }

    @Test
    void testConfiguredTrustedProxiesCount() {

        final Map<String, String> configMap = new HashMap<>();
        configMap.put("ip-ranges", "1.2.3.0/24");
        configMap.put("trusted-proxies-count", "2");

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig(configMap);

        assertEquals(2, config.getTrustedProxiesCount());
    }
}
