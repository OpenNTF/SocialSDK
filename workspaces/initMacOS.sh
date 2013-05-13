#!/bin/sh
if [ ! -L /sbtk ]; then
	echo "Mounting /sbtk"
	sudo ln -s `pwd` /sbtk
else
	echo "Found mount point /sbtk"
fi
cd /sbtk
UNZIPPED_TOMCAT=/sbtk/tomcat/apache-tomcat-7.0.30
if [ ! -d "$UNZIPPED_TOMCAT" ]; then
	echo "Unzipped tomcat"
	cd tomcat
	if hash unzip 2>/dev/null; then
		unzip apache-tomcat-7.0.30-sbt.zip
	else
		jar xf apache-tomcat-7.0.30-sbt.zip
	fi
	cd ..
fi
echo "Done!"
