@echo off

echo build started with %0 %1 %2 %3 %4 %5 %6 %7 %8

if "%1"=="-label" set LABEL=%2

if "%1"=="-tools" set toolsDir=%~f2

if "%1"=="-workdir" set WORKING_DIR=-DworkingDir=%~f2

if "%1"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local=%~f2

if "%3"=="-label" set LABEL=%4

if "%3"=="-tools" set toolsDir=%~f4

if "%3"=="-workdir" set WORKING_DIR=-DworkingDir=%~f4

if "%3"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local=%~f4

if "%5"=="-label" set LABEL=%6

if "%5"=="-tools" set toolsDir=%~f6

if "%5"=="-workdir" set WORKING_DIR=-DworkingDir=%~f6

if "%5"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local=%~f6

if "%7"=="-label" set LABEL=%8

if "%7"=="-tools" set toolsDir=%~f8

if "%7"=="-workdir" set WORKING_DIR=-DworkingDir=%~f8

if "%7"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local=%~f8


if (%toolsDir%) == ()  echo Set toolsDir environment variable to the tools directory in your machine and REM this line
if (%toolsDir%) == ()  set toolsDir=..\tools
if (%LABEL%) ==    ()  set LABEL=SNAPSHOT

echo building %LABEL% with tools from %toolsDir% 



if (%JAVA_HOME%) == () set JAVA_HOME=%toolsDir:"=%\ibm-java-sdk-60-win-i386
if (%M2_HOME%) ==   () set M2_HOME=%toolsDir:"=%\apache-maven-3.0.4
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%

mvn clean install  -DtoolsDir=%toolsDir%  -DbuildLabel=%LABEL% %WORKING_DIR% %LOCAL_REPO%
