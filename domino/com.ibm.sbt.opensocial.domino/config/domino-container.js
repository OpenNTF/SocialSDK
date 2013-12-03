{
  "gadgets.container" : ["domino"],
  "parent" : "default",
  //Do to a bug in the Shindig js code we need to make sure we set this property to always
  //include security tokens in the iframe URLs for gadgets.  If not if the gadget makes a makeRequest
  //null will be sent to the servlet and that is not a valid security token
  "gadgets.uri.iframe.alwaysAppendSecurityToken" : "true",
  "gadgets.securityTokenType" : "secure",
  "gadgets.securityTokenTTL" : 3600
}