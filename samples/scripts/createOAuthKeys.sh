#!/bin/sh
#Create OAuth Keys 
#sudo ./createOAuthKeys.sh wasadmin xxxxx9 qsSample1 MyApp1 http://localhost:8443/callback1/ 

#Edits the OAuth
if [ "$3" = "" ]; then 
	echo "Insufficient Arguments: Include username, password, client identifier, application identifier, url"
	echo
	exit 1
fi

#Starts Node and Server and Manager
/local/con/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/startNode.sh 
/local/con/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/startServer.sh conServer
/local/con/opt/IBM/WebSphere/AppServer/profiles/Dmgr01/bin/startManager.sh


#Parameters
USER=$1
PASSWORD=$2
CLIENTID=$3
APPID=$4
URL=$5

#Starts WSAdmin
cd /local/con/opt/IBM/WebSphere/AppServer/profiles/Dmgr01/bin/
/local/con/opt/IBM/WebSphere/AppServer/profiles/Dmgr01/bin/wsadmin.sh -user $USER -password $PASSWORD -lang jython -port 8883 -f /local/qsi/bin/con/oauthQSI.py $CLIENTID $APPID $URL

#Synchronize and Stop Deployment Manager
/local/con/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/stopServer.sh conServer
/local/con/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/stopNode.sh 
/local/con/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/bin/syncNode.sh localhost 8879 -conntype SOAP -username $USER -password $PASSWORD
/local/con/opt/IBM/WebSphere/AppServer/profiles/Dmgr01/bin/stopManager.sh
