@echo off

echo Set toolsDir environment variable to the tools directory in your machine and REM this line
set toolsDir=..\..\tools
set JAVA_HOME=%toolsDir%\ibm-java-sdk-60-win-i386
set M2_HOME=%toolsDir%\apache-maven-3.0.4
set PATH=%JAVA_HOME%\bin;%M2_HOME%\bin;%PATH%

mvn clean javadoc:javadoc install -DtoolsDir=%toolsDir% -DbuildLabel=SNAPSHOT