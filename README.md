# Keycloak IP-Address Authenticator

This Keycloak extensions allows implementing conditional authentication flows based on the client's IP address. For example, if you want to show an OTP form only for users connecting from outside your corporate network, you can use this extension to do so.

Supports IPv6 and IPv4. Supports single IP addresses and IP ranges in CIDR as well as netmask notation. Examples: `192.168.1.5`, `a:b:c:d::/64`, `145.251.153.32/255.255.0.0`

![212354312-ce98a2ac-8e03-4af5-864d-b93eb87ea491](https://github.com/evosec/keycloak-ipaddress-authenticator/assets/20876882/6c556343-4876-4b64-945e-053210181a91)

![227958012-18a803c0-956e-4195-895f-0913dd01e434](https://github.com/evosec/keycloak-ipaddress-authenticator/assets/20876882/d2297a79-5b05-4082-94d3-72bda1487395)


## Installation
* Download the `keycloak-ipaddress-authenticator-{version}-jar-with-dependencies.jar` from the [Releases Tab](https://github.com/evosec/keycloak-ipaddress-authenticator/releases) and verify the checksum. Alternatively you can build [build from source](#build-from-source).
  * You can also use `keycloak-ipaddress-authenticator-{version}.jar`: This jar only contains the compiled code for this extension itself, so you need to add all dependencies manually (see [`pom.xml`](https://github.com/evosec/keycloak-ipaddress-authenticator/blob/master/pom.xml).
* Place the .jar file in the Keycloak Plugins directory and restart Keycloak.

## Build From Source
* Check out the sources
* Run `mvn clean package`
* This will generate the .jar files in the `target` directory.
