@echo off

echo build started with %0 %1 %2 %3 %4 %5 %6 %7 %8

if "%1"=="-label" set LABEL=%2

if "%1"=="-tools" set toolsDir="%~f2"

if "%1"=="-workdir" set WORKING_DIR=-DworkingDir="%~f2"

if "%1"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local="%~f2"

if "%1"=="-notes-platform" set NOTES_PLATFORM=-Dnotes-platform="%2"

if "%3"=="-label" set LABEL=%4

if "%3"=="-tools" set toolsDir="%~f4"

if "%3"=="-workdir" set WORKING_DIR=-DworkingDir="%~f4"

if "%3"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local="%~f4"

if "%3"=="-notes-platform" set NOTES_PLATFORM=-Dnotes-platform="%4"

if "%5"=="-label" set LABEL=%6

if "%5"=="-tools" set toolsDir="%~f6"

if "%5"=="-workdir" set WORKING_DIR=-DworkingDir="%~f6"

if "%5"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local="%~f6"

if "%5"=="-notes-platform" set NOTES_PLATFORM=-Dnotes-platform="%6"

if "%7"=="-label" set LABEL=%8

if "%7"=="-tools" set toolsDir="%~f8"

if "%7"=="-workdir" set WORKING_DIR=-DworkingDir="%~f8"

if "%7"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local="%~f8"

if "%7"=="-notes-platform" set NOTES_PLATFORM=-Dnotes-platform="%8"

SHIFT
SHIFT

if "%7"=="-label" set LABEL=%8

if "%7"=="-tools" set toolsDir="%~f8"

if "%7"=="-workdir" set WORKING_DIR=-DworkingDir="%~f8"

if "%7"=="-localRepo" set LOCAL_REPO=-Dmaven.repo.local="%~f8"

if "%7"=="-notes-platform" set NOTES_PLATFORM=-Dnotes-platform="%8"


if (%toolsDir%) == ()  echo Set toolsDir environment variable to the tools directory in your machine and REM this line
if (%toolsDir%) == ()  set toolsDir=..\tools
if (%LABEL%) ==    ()  set LABEL=SNAPSHOT

echo building %LABEL% with tools from %toolsDir%  passed platform: %NOTES_PLATFORM%



set JAVA_HOME=%toolsDir:"=%\ibm-java-sdk-60-win-i386
set M2_HOME=%toolsDir:"=%\apache-maven-3.0.4
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%


if not (%LABEL%) == SNAPSHOT echo changing version to %LABEL%
if not (%LABEL%) == SNAPSHOT call mvn org.eclipse.tycho:tycho-versions-plugin:0.19.0:set-version -DnewVersion=1.0.0.%LABEL%


rem queuing standard and domino builds to aggregate both

mvn clean dependency:purge-local-repository install javadoc:aggregate site -DreResolve=false -DtoolsDir=%toolsDir%  %WORKING_DIR% %LOCAL_REPO% %NOTES_PLATFORM%