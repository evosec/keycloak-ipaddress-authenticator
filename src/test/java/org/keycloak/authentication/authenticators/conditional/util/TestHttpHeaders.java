package org.keycloak.authentication.authenticators.conditional.util;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class TestHttpHeaders implements HttpHeaders {

    private final Map<String, List<String>> headers;

    TestHttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public List<String> getRequestHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getHeaderString(String name) {
        throw new IllegalStateException();
    }

    @Override
    public MultivaluedMap<String, String> getRequestHeaders() {
        throw new IllegalStateException();
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        throw new IllegalStateException();
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        throw new IllegalStateException();
    }

    @Override
    public MediaType getMediaType() {
        throw new IllegalStateException();
    }

    @Override
    public Locale getLanguage() {
        throw new IllegalStateException();
    }

    @Override
    public Map<String, Cookie> getCookies() {
        throw new IllegalStateException();
    }

    @Override
    public Date getDate() {
        throw new IllegalStateException();
    }

    @Override
    public int getLength() {
        throw new IllegalStateException();
    }
}
