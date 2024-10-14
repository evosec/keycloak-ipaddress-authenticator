package org.keycloak.authentication.authenticators.conditional.util;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.keycloak.http.FormPartValue;
import org.keycloak.http.HttpRequest;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

class TestHttpRequest implements HttpRequest {

        private final HttpHeaders httpHeaders;

        TestHttpRequest(Map<String, List<String>> headers) {
            this.httpHeaders = new TestHttpHeaders(headers);
        }

        @Override
        public HttpHeaders getHttpHeaders() {
            return httpHeaders;
        }

        @Override
        public String getHttpMethod() {
            throw new IllegalStateException();
        }

        @Override
        public MultivaluedMap<String, String> getDecodedFormParameters() {
            throw new IllegalStateException();
        }

        @Override
        public MultivaluedMap<String, FormPartValue> getMultiPartFormParameters() {
            throw new IllegalStateException();
        }

        @Override
        public X509Certificate[] getClientCertificateChain() {
            throw new IllegalStateException();
        }

        @Override
        public UriInfo getUri() {
            throw new IllegalStateException();
        }
    }
