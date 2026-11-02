#!/bin/bash
cd "$(dirname "$0")"
export JAVA_HOME="${JAVA_HOME:-/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home}"
JARS=$(find lib -name '*.jar' | tr '\n' ':')
CP="build:${JARS}lib/batik-1.7/lib/*:"
exec "$JAVA_HOME/bin/java" -cp "$CP" views.FrameApp



### Lancement de l'application
##cd "$(dirname "$0")"
##
##CP="bin:lib/flanagan/flanagan.jar:lib/jama-1.0.2/Jama-1.0.2.jar:lib/jdom-2.0.5/jdom-2.0.5.jar:lib/javaws.jar:lib/plugin.jar:lib/batik-1.7/batik.jar:lib/batik-1.7/lib/*:lib/junit/junit-4.13.2.jar:lib/junit/hamcrest-core-1.3.jar"
##
##MAIN="${1:-views.FrameApp}"
##
##exec java -cp "$CP" "$MAIN"
##
