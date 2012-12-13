@echo off

set JAVA_HOME=../tools/ibm-java-sdk-60-win-i386
set M2_HOME=../tools/apache-maven-3.0.4
set PATH=%JAVA_HOME%/bin;%M2_HOME%/bin;%PATH%

mvn clean javadoc:javadoc