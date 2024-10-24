# Keycloak IP-Address Authenticator

This Keycloak extensions allows implementing conditional authentication flows based on the client's IP address. For example, if you want to show an OTP form only for users connecting from outside your corporate network, you can use this extension to do so.

Supports IPv6 and IPv4. Supports single IP addresses and IP ranges in CIDR as well as netmask notation. Examples: `192.168.1.5`, `a:b:c:d::/64`, `145.251.153.32/255.255.0.0`

![212354312-ce98a2ac-8e03-4af5-864d-b93eb87ea491](https://github.com/evosec/keycloak-ipaddress-authenticator/assets/20876882/6c556343-4876-4b64-945e-053210181a91)

![image](https://github.com/user-attachments/assets/42f47ff9-c2a5-4cd9-a8b4-b505ef0763e3)

## Setup behind proxy servers
A typical Keycloak Setup does not directly expose the Keycloak server, but includes one or more proxy servers for terminating TLS traffic or load balancing. In that case the Keycloak server only sees the IP address of the proxy server, which can be a security risk - For example if you only want to trust IP addresses from your internal network, the Keycloak server only sees the IP address of your proxy server.

In this case, you can use the Parameter "Use a 'forwarded' header", which tells the plugin to use the client IP address from a header (typically "X-Forwarded-For"). To make this secure, you have to be aware how many proxy servers your setup includes, because the first IP addresses in this header can be spoofed by an attacker. For example, if your network setup includes 2 proxies, you should set the parameter "Number of trusted proxies" to 2. This means the second last IP from the forwarded header is used as the client IP address.

For more information see: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For#security_and_privacy_concerns

## Installation
* Download the `keycloak-ipaddress-authenticator-{version}-jar-with-dependencies.jar` from the [Releases Tab](https://github.com/evosec/keycloak-ipaddress-authenticator/releases) and verify the checksum. Alternatively you can build [build from source](#build-from-source).
  * You can also use `keycloak-ipaddress-authenticator-{version}.jar`: This jar only contains the compiled code for this extension itself, so you need to add all dependencies manually (see [`pom.xml`](https://github.com/evosec/keycloak-ipaddress-authenticator/blob/master/pom.xml).
* Place the .jar file in the Keycloak Plugins directory and restart Keycloak.

## Build From Source
* Check out the sources
* Run `mvn clean package`
* This will generate the .jar files in the `target` directory.
