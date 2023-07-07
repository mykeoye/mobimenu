# Mobimenu

Mobimenu is a platform that provides a digital operating system for food businesses. It does so through the
use of Qr code technology to efficiently manage interactions with customers, providing businesses with digitized,
and intuitive menus, inventory management, customer-engagement, accounting and a host of other tools they need to succeed.

This is the core backend service which drives the entire platform. It is built with Java and the Quarkus framework,
and leverages the reactive toolkit `small-rye mutiny`[https://smallrye.io/smallrye-mutiny/2.3.1/] which is built on the principles of Reactive streams.

## How to build

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```
The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

You can also run the application in dev mode which enables live coding using:
```shell script
./gradlew quarkusDev
```
The service can be accessed in dev mode using this url http://localhost:8080/q/dev/
