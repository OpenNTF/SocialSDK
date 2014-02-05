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
 * Social Business Toolkit SDK.
 * 
 * Implementation of a transport using the Dojo XHR API.
 */
define(
		[ 'dojo/_base/declare', 'dojo/_base/xhr', 'dojo/_base/lang',
				'dojox/xml/parser', '../util', '../Promise', "sbt/json",  "../xml", "../xpath" ],
		function(declare, xhr, lang, parser, util, Promise, json, xml, xpath) {
			return declare(
					null,
					{

						request : function(url, options) {
							try {
								var response = {};

								var epf = Packages.com.ibm.sbt.services.endpoints.EndpointFactory;
								var ep = epf.getEndpointFromEnvironment(
										options.name || null, null);
								var jargs = new Packages.com.ibm.sbt.services.client.ClientService.Args();
								jargs.setServiceUrl(url);

								var content = options.data || null;
								var headers = options.headers || {};
								var method = options.method || 'GET'
								var query = options.query || {};
								var stringed = {};
								
								for (var key in query) {
									 stringed[key] = query[key].toString(); 
								}
								
								jargs.setParameters(stringed);

								var resp = ep.xhr(method, jargs, content);
								
								if (resp.getResponse().getStatusLine()
										.getStatusCode() < 300) {
									var data = this.extractEntity(resp);
									
									
									var response = {
										url : url,
										options : options,
										data : data,
										text : null,
										status : resp.getResponse()
												.getStatusLine()
												.getStatusCode(),
										getHeader : function(headerName) {
											return resp.getResponse()
													.getFirstHeader(headerName);
										},
										xhr : null,
										_ioargs : null
									};
									var promise = new Promise(response);
									response.response = promise;
									return response;
								} else {
								}

							} catch (e) {

								var code = 400;
								var message = '' + e;
								
								if (!(e.javaException === undefined)) {
									var jex = e.javaException;
									var propList = "";
									jex.printStackTrace();
									for ( var propName in jex) {
										if (typeof (jex[propName]) != "undefined") {
											propList += (propName + ", ");
										}
									}
									if (!(jex.getResponseStatusCode === undefined)) {
										code = jex.getResponseStatusCode();
									}
									if (!(jex.getReasonPhrase === undefined)) {
										message = jex.getReasonPhrase();
									}
								}

								var error = new Error();
								error.message = message;
								error.response = {};
								error.response.status = code;

								var promise = new Promise(error);
								response.response = promise;
								
								return response;

							}

							var error = new Error();
							error.message = 'unimplemented';
							error.response = {};
							error.response.status = 400;

							var promise = new Promise(error);
							response.response = promise;
							return response;
						},
						extractEntity : function(response) {
								var data = response.getData();

								var type = response.response.getFirstHeader("Content-Type").getValue();
								if (type.indexOf('xml') !== -1) {
									var sw = new Packages.java.io.StringWriter();
									Packages.com.ibm.commons.xml.DOMUtil.serialize(sw,data,true,true);
									sw.flush();
									sw.close();
									
									var serialized = '' + sw.toString();
									data  = xml.parse(serialized);
									data.evaluate = undefined;
									t = {};
									t.document = data;
									wgxpath.install(t);


								} else {
									console.log('ADD PROCESSING FOR ' + type);
									
								}
								return data;
							}

						
					});
		});