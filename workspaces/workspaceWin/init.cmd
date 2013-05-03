@ECHO OFF
IF EXIST C:\sbtk\ GOTO DRIVE_EXISTS
echo Setting junction C:\sbtk\ to %~dp0
mklink /j c:\sbtk ..
:DRIVE_EXISTS
cd \sbtk
IF EXIST C:\sbtk\tomcat\apache-tomcat-7.0.30\ GOTO TOMCAT_UNZIPPED
echo Unzipping Tomcat
cd tomcat
jar xf apache-tomcat-7.0.30-sbt.zip
if %errorlevel%==9009 echo Please unzip Tomcat in z:/tomcat
cd ..
:TOMCAT_UNZIPPED