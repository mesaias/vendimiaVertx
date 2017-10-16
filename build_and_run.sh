#!/usr/bin/env bash
JMX_OPTIONS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.port=7778 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=vendimia.vertx"
sudo rm -rf src/main/generated
./gradlew clean shadowJar
java $JMX_OPTIONS -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -jar build/libs/laVendimia-1.0-SNAPSHOT-fat.jar -conf src/config/config.json
