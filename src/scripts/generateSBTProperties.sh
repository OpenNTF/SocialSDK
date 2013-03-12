#!/bin/sh 
# generates the toolkits sbt.properties

QSI=/local/qsi/bin
SBT=${QSI}/temp/sbt.properties
ND_HTML=/local/nd/notesdata/domino/html
HOSTNAME=`hostname --long`

OAUTHCMD=${QSI}/con/createOAuthKey.sh
USER=$1
PASSWORD=$2
CLIENTID=SBTK
APPID=SBTK
URL=http://localhost:8443/callback1

echo "Beginning to grab the current sbt.properties"
echo 

cd ${QSI}/temp
wget https://raw.github.com/OpenNTF/SocialSDK/master/src/config/sbt.properties

echo "Getting the OAUTH Key" 
${OAUTHCMD} ${USER} ${PASSWORD} ${CLIENTID} ${APPID} ${URL} > ${QSI}/temp/SECRET
tail -n 1 ${QSI}/temp/SECRET

CS=`cat ${QSI}/temp/SECRET` 

echo "Modifying the SBT Properties"
sed -i "s/qs.renovations.com/${HOSTNAME}/g" ${ND_HTML}/sbt.properties
sed -i "/connectionsOA2.consumerSecret=/c \connectionsOA2.consumerSecret=${CS}" ${ND_HTML}/sbt.properties

echo 
echo "Copying the sbt.properties"
cp ${SBT} ${ND_HTML}/sbt.properties
