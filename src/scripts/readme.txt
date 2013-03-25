 generateSBTProperties.sh
--------------------------
generateSBTProperties.sh grabs the latest sbt.properties file from github and creates a properties file to match your environment. 
The command is hard coded to point to the sbtk client id and sbtk appid, and loop back to the localhost. Once the script completes it puts the final properties file in an easily accessible directory for further use, you can configure the script as you wish. 
Command to generate your properties file is ./generateSBTProperties.sh wasadmin pass  

You should configure the following parameters: 
QSI= the directory you are running the commands from 
SBT= the temporary file you want to store the downloaded sbt.properties from 
ND_HTML= where you want the resulting file to go. 
OAUTHCMD= the path to the createOAuthKeys.sh

 createOAuthKeys.sh
--------------------
createOAuthKeys.sh connects to the local websphere application server deployment manager and creates an oauth keypair based on oauthQSI.py.
You should include username, password, client identifier, application identifier, url on the request, such that you call ./createOAuthKeys.sh wasadmin xxxxx9 qsSample1 MyApp1 http://localhost:8443/callback1/ 
Once done, the command stops the local connections server, stops the node and forces synchronization with the deployment manager. 
Note, you should ensure that the port which the syncNode.sh command uses is open. 
You should also restart connections when the command is done. You'll have to do that manually. 