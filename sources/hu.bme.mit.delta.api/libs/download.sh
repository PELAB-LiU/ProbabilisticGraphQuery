#!/bin/bash

cd "$(dirname "$0")"

wget https://repo1.maven.org/maven2/com/koloboke/koloboke-api-jdk8/1.0.0/koloboke-api-jdk8-1.0.0.jar -O koloboke-api-jdk8-1.0.0.jar
wget https://repo1.maven.org/maven2/com/koloboke/koloboke-impl-jdk8/1.0.0/koloboke-impl-jdk8-1.0.0.jar -O koloboke-impl-jdk8-1.0.0.jar
wget https://repo1.maven.org/maven2/com/koloboke/koloboke-impl-common-jdk8/1.0.0/koloboke-impl-common-jdk8-1.0.0.jar -O koloboke-impl-common-jdk8-1.0.0.jar
wget https://repo1.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar -O jsr305-3.0.2.jar

