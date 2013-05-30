/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

/**
 * Provides functionality to send emails.
 * 
 * @module sbt.emailService
 */
define(['./declare', './lang', './config', './_bridge/Transport', './json'], function(declare, lang, config, Transport, sbtJson) {
    var transport = new Transport();
    return {
        /**
         * Sends an email.
         * @method send
         * @static
         * @param {Object || Array} email The JSON object representing the email to send.
         * @param {Object} [callbacks] An object of callback functions.  The possible properties for callback functions are load,
         * error, and handle.
         * 
         * @example
         *     var emails = 
         *         [
         *             {
         *                 "from" : "sdary@renovations.com",
         *                 "to" : ["fadams@renovations.com", "tamado@renovations.com"],
         *                 "cc" : ["pclemmons@renovations.com"],
         *                 "bcc" : [],
         *                 "subject" : "This is a test email",
         *                 "mimeParts" : 
         *                     [
         *                         {
         *                             "mimeType" : "text/plain",
         *                             "content" : "This is plain text",
         *                             "headers" : 
         *                                 {
         *                                     "header1":"value1", 
         *                                     "header2":"value2"
         *                                 }
         *                         },
         *                         {
         *                             "mimeType" : "text/html",
         *                             "content" : "<b>This is html</b>"
         *                         },
         *                         {
         *                             "mimeType" : "application/embed+json",
         *                             "content" : {
         *                                 "gadget" : "http://renovations.com/gadget.xml",
         *                                 "context" : {
         *                                     "foo" : "bar"
         *                                 }
         *                             }
         *                         }
         *                     ]
         *              },
         *              {
         *                     "from" : "sdaryn@renovations.com",
         *                     "to" : ["fadams@renovations.com", "tamado@renovations.com"],
         *                     "subject": "This is a test email",
         *                     "mimeParts" : 
         *                         [
         *                             {
         *                                 "mimeType" : "text/plain",
         *                                 "content" : "This is plain text"
         *                             },
         *                             {
         *                                 "mimeType" : "text/html",
         *                                 "content" : "<b>This is html</b>"
         *                             }
         *                         ]
         *              }
         *         ];
         *     var loadCallback = function(response) {
         *         //If you send multiple emails, for example emails is an array of email objects,
         *         //than it is possible that some emails succeeded being sent while others may have
         *         //failed.  It is good practice to check for any emails that had errors being sent.
         *         if(response.error && response.error.length != 0) {
         *             //There was one of more errors with emails sent, handle them
         *         }
         *        
         *         if(response.successful  && response.successful.length > 0) {
         *             //Some or all of your emails were successfully sent
         *         }
         *     };
         *  
         *     var errorCallback = function(response) {
         *         //This callback will only be called if there was an error in the request
         *         //being made to the server.  It will NOT be called if there are errors
         *         //with any of the emails being sent.
         *         if(response.message) {
         *             //The request failed handle it.
         *         }
         *     };
         *  
         *     var handleCallback = function(response) {
         *         //This callback is called no matter whether the request succeeded or failed.
         *         errorCallback(response);
         *         loadCallback(response);
         *     };
         *  
         *     email.send(emails, {load: loadCallback, error: errorCallback, handle: handleCallback});
         */
        send : function(emails) {
            var postUrl = config.Properties.serviceUrl + '/mailer';
            
            var options = {
                method: "POST",
                data: sbtJson.stringify(emails),
                headers: {"Content-Type" : "application/json"},
                handleAs: "json"
            };
            
            return transport.request(postUrl, options);
        }
    };
});