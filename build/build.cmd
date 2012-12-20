@echo off

echo build started with %0 %1 %2 %3 %4

if "%1"=="-label" set LABEL=%2

if "%1"=="-tools" set toolsDir=%2

if "%3"=="-label" set LABEL=%4

if "%3"=="-tools" set toolsDir=%4

echo building %LABEL% with tools from %toolsDir%

if (%toolsDir%) == () echo Set toolsDir environment variable to the tools directory in your machine and REM this line
if (%toolsDir%) == () set toolsDir=..\tools
if (%LABEL%) == ()   set LABEL=SNAPSHOT

set JAVA_HOME=%toolsDir:"=%\ibm-java-sdk-60-win-i386
set M2_HOME=%toolsDir:"=%\apache-maven-3.0.4
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%

mvn clean javadoc:javadoc surefire-report:report install -DtoolsDir=%toolsDir%  -DbuildLabel=%LABEL%
