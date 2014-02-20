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
								if (options.handleAs === 'json' && !headers['Content-Type']) {
									headers['Content-Type']='application/json';
								}
								
								jargs.setParameters(stringed);
								jargs.setHeaders(headers);
								var resp = ep.xhr(method, jargs, content);
								
								if (resp.getResponse().getStatusLine()
										.getStatusCode() < 300) {
									
									console.log('serializing');
									var entityString = this.serializeEntity(resp);
									console.log('converting to object ' + entityString);
									var data = this.extractEntity(resp);
									console.log('creating response');

									
									var response = {
										url : url,
										options : options,
										data : data,
										text : entityString,
										status : parseInt(''+resp.getResponse()
												.getStatusLine()
												.getStatusCode()),
										getHeader : function(headerName) {
											var h = null;
											if (headerName && resp.getResponse()
													.getFirstHeader(headerName)) {
											var h = ''+resp.getResponse()
													.getFirstHeader(headerName).getValue();
											
											}
											return h;
										},
										xhr : {
											statusText : ''+resp.getResponse().getStatusLine().getReasonPhrase(),
											status : parseInt(''+resp.getResponse()
											.getStatusLine()
											.getStatusCode()),
											response: entityString,
											responseType: '',
											responseText: entityString,
											upload: null,
											withCredentials: false,
											timeout: 0,
											readyState: 4
										},
										_ioargs : null
									};
									
									console.log('creating promise');

									var promise = new Promise(response);
									response.response = promise;
									console.log('returning');
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
						serializeEntity: function(response) {
							var data = response.getData();
							console.log('converting ' + data.getClass().toString());
							
							var type = response.response.getFirstHeader("Content-Type").getValue();
							if (type.indexOf('xml') !== -1) {
								var sw = new Packages.java.io.StringWriter();
								Packages.com.ibm.commons.xml.DOMUtil.serialize(sw,data,true,true);
								sw.flush();
								sw.close();
								
								return '' + sw.toString();
							} else if (type.indexOf('text/html') !== -1) {
							
							return ''+ Packages.org.apache.commons.io.IOUtils.toString(data);
							} else if (type.indexOf('application/json') !== -1) {
								return '' + data.toString();
							}	else {
								
								console.log('ADD PROCESSING FOR ' + type);
								
							}
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
									console.log('converting to xml' + serialized);
									data  = xml.parse(serialized);
									data.evaluate = undefined;
									t = {};
									t.document = data;
									wgxpath.install(t);

									
								} else if (type.indexOf('text/html') !== -1) {
									if (data)
										data = ''+ Packages.org.apache.commons.io.IOUtils.toString(data).toString();
									if(data)
										data = undefined;
									console.log('converting to text' + data);
								} else if (type.indexOf('application/json') !== -1) {
									return eval('(' + data.toString()+')');
								} else {
									console.log('ADD PROCESSING FOR ' + type);
									
								}
								return data;
							}

						
					});
		});