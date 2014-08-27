#!/bin/sh

"$JAVA_HOME/bin/java" -Declipse.home="$ECLIPSE_HOME"  -jar "$PG_JAR" @proguard.pro

cd ..

cp -av target/obfuscated-classes/* target/classes
rm -rf target/obfuscated-classes

for f in lib/*-repackaged.jar
do
  mv $f ${f%-repackaged.jar}.jar
done
