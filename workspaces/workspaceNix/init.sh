#!/bin/sh
if grep '/sbtk' /proc/mounts; then
	echo "Found mount point /sbtk"
else
	echo "Mounting /sbtk"
	sudo mkdir -p /sbtk
	sudo mount --bind . /sbtk
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
