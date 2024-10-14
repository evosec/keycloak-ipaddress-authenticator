package org.keycloak.authentication.authenticators.conditional.util;

import org.keycloak.common.ClientConnection;

class TestClientConnection implements ClientConnection {

    private final String remoteAddr;

    TestClientConnection(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    @Override
    public String getRemoteHost() {
        throw new IllegalStateException();
    }

    @Override
    public int getRemotePort() {
        throw new IllegalStateException();
    }

    @Override
    public String getLocalAddr() {
        throw new IllegalStateException();
    }

    @Override
    public int getLocalPort() {
        throw new IllegalStateException();
    }
}

