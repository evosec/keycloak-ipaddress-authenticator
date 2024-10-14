package org.keycloak.authentication.authenticators.conditional;

import org.junit.jupiter.api.Test;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.util.TestAuthenticationFlowContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalClientIpAddressAuthenticatorTest {

    final ConditionalClientIpAddressAuthenticator conditionalClientIpAddressAuthenticator = ConditionalClientIpAddressAuthenticator.SINGLETON;

    @Test
    void testAccessGrantedWithoutForwardedHeader() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(false);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(1);

        testAccessGranted(config, "1.2.3.4", "X-Forwarded-For", "7.8.9.0");
    }

    @Test
    void testAccessDeniedWithoutForwardedHeader() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(false);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(1);

        testAccessDenied(config, "7.8.9.0", "X-Forwarded-For", "1.2.3.4");
    }

    @Test
    void testAccessGrantedWithForwardedHeader() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(true);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(1);

        testAccessGranted(config, "7.8.9.0", "X-Forwarded-For", "1.2.3.4");
    }

    @Test
    void testAccessDeniedWithForwardedHeader() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(true);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(1);

        testAccessDenied(config, "1.2.3.4", "X-Forwarded-For", "7.8.9.0");
    }

    @Test
    void testUntrustedIpsInForwardedHeaderIgnored() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(true);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(1);

        testAccessDenied(config, "1.2.3.4", "X-Forwarded-For", "1.2.3.4, 7.8.9.0");
    }

    @Test
    void testUntrustedIpsInForwardedHeaderIgnoredMultipleHeaders() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(true);
        config.setForwardedHeaderName("X-Forwarded-For");
        config.setTrustedProxiesCount(2);

        testAccessGranted(config, "1.2.3.4", "X-Forwarded-For", "5.6.7.0", "1.2.3.4, 7.8.9.0");
    }

    @Test
    void testAccessDeniedCustomHeaderMissing() {

        final ConditionalClientIpAddressAuthenticatorConfig config = new ConditionalClientIpAddressAuthenticatorConfig();
        config.setIpRanges("1.2.3.0/24", "4.5.0.0/16");
        config.setExclude(false);

        config.setUseForwardedHeader(true);
        config.setForwardedHeaderName("X-Forwarded-Custom");
        config.setTrustedProxiesCount(1);

        testAccessDenied(config, "4.5.6.7", "X-Forwarded-For", "1.2.3.4");
    }

    private void testAccessGranted(ConditionalClientIpAddressAuthenticatorConfig config, String clientIp, String forwardedHeaderName, String... forwardedHeaderValues) {

        final boolean accessGranted = test(config, clientIp, forwardedHeaderName, forwardedHeaderValues);
        assertTrue(accessGranted);
    }

    private void testAccessDenied(ConditionalClientIpAddressAuthenticatorConfig config, String clientIp, String forwardedHeaderName, String... forwardedHeaderValues) {

        final boolean accessGranted = test(config, clientIp, forwardedHeaderName, forwardedHeaderValues);
        assertFalse(accessGranted);
    }

    private boolean test(ConditionalClientIpAddressAuthenticatorConfig config, String clientIp, String forwardedHeaderName, String... forwardedHeaderValues) {
        final Map<String, List<String>> headers = new HashMap<>();
        headers.put(forwardedHeaderName, asList(forwardedHeaderValues));

        final AuthenticationFlowContext context = new TestAuthenticationFlowContext(clientIp, headers);
        return conditionalClientIpAddressAuthenticator.matchCondition(context, config);
    }

}
