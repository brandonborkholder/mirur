#!/bin/sh

$JAVA_HOME/bin/java -Declipse.home=$ECLIPSE_HOME  -jar $PG_JAR @build/proguard.pro

rsync -av target/obfuscated-classes target/classes
rm -rf target/obfuscated-classes

