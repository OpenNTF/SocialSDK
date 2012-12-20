@echo off

echo Set toolsDir environment variable to the tools directory in your machine and REM this line
set toolsDir=..\tools
set NODEJS_HOME=%toolsDir%\node-v0.8.6-x86
set JAVA_HOME=%toolsDir%\ibm-java-sdk-60-win-i386
set M2_HOME=%toolsDir%\apache-maven-3.0.4
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%NODEJS_HOME%;%APPDATA%\npm;%PATH%

echo Set toolsDir variable passed to mvn to the tools directory in your machine and REM this line
mvn compile -pl js -DtoolsDir=..\tools
