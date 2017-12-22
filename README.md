# toxiproxy-java

This is a client library for the [Toxiproxy](https://github.com/shopify/toxiproxy) - a proxy that simulates network and system conditions. With toxiproxy-java you may use a convenient Java API to create and manage proxies. Before you using this library, please read the [Usage section of the Toxiproxy README](https://github.com/shopify/toxiproxy#usage).

Installation:

```xml
<dependency>
  <groupId>eu.rekawek.toxiproxy</groupId>
  <artifactId>toxiproxy-java</artifactId>
  <version>2.1.2</version>
</dependency>
```

## Usage

By default, the `ToxiproxyClient` tries to connect to the `http://localhost:8474`. This might be changed using the parametrized constructor:

```java
ToxiproxyClient client = new ToxiproxyClient("192.168.1.1", 8474);
```

Following snippet will create a new proxy for the MySQL service:

```java
Proxy mysqlProxy = client.createProxy("mysql", "localhost:21212", "localhost:3306");
```

The proxy will listen on port 21212 on the loopback interface and transfer all the traffic to port 3306. We may also create a [toxic](https://github.com/shopify/toxiproxy#toxics):

```java
mysqlProxy.toxics().latency("my-latency-toxic", DOWNSTREAM, 100).setJitter(15);
```

For a full list of toxics, please visit the [Toxiproxy README](https://github.com/shopify/toxiproxy#toxics).
