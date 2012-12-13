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

package com.ibm.sbt.services.client;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import com.ibm.commons.util.AbstractException;

/**
 * REST services related exception.
 * 
 * @Philippe Riand
 */
public class ClientServicesException extends AbstractException {

	private static final long	serialVersionUID	= 1L;
	private int					responseStatusCode;

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setResponseStatusCode(int responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	public void setRequestURI(URI requestURI) {
		this.requestURI = requestURI;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public int getResponseStatusCode() {
		return responseStatusCode;
	}

	public URI getRequestURI() {
		return requestURI;
	}

	private String	reasonPhrase;
	private URI		requestURI;

	public ClientServicesException(Throwable nextException) {
		super(nextException);
	}

	public ClientServicesException(Throwable nextException, String msg, Object... params) {
		super(nextException, msg, params);
	}

	public ClientServicesException(HttpResponse response, HttpRequestBase request) {
		this(null,
				"HTTP Status {0}, {1}. HTTP error response code received in response to request to url: {2}",
				response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), request
						.getURI());
		this.setResponseStatusCode(response.getStatusLine().getStatusCode());
		this.setReasonPhrase(response.getStatusLine().getReasonPhrase());
		this.setRequestURI(request.getURI());
	}

	public boolean isInformational() {
		return responseStatusCode > 99 && responseStatusCode < 200;
	}

	public boolean isSuccesful() {
		return responseStatusCode > 199 && responseStatusCode < 300;
	}

	public boolean isRedirection() {
		return responseStatusCode > 299 && responseStatusCode < 400;
	}

	public boolean isClientError() {
		return responseStatusCode > 399 && responseStatusCode < 500;
	}

	public boolean isServerError() {
		return responseStatusCode > 499 && responseStatusCode < 600;
	}

	// TODO fill more response code from rfc2616, section 10
	public static final int	BAD_REQUEST						= 400;
	public static final int	UNAUTHORIZED					= 401;
	public static final int	PAYMENT_REQURED					= 402;
	public static final int	FORBIDDEN						= 403;
	public static final int	NOT_FOUND						= 404;
	public static final int	METHOD_NOT_ALLOWED				= 405;
	public static final int	NOT_ACCEPTABLE					= 406;
	public static final int	PROXY_AUTHENTICATION_REQUIRED	= 407;
	public static final int	REQUEST_TIMEOUT					= 408;
	public static final int	CONFLICT						= 409;
	public static final int	GONE							= 410;
	public static final int	LENGTH_REQUIRED					= 411;
	public static final int	PRECONDITION_FAILED				= 412;
	public static final int	REQUEST_URI_TOO_LONG			= 413;
	public static final int	REQUEST_ENTITY_TOO_LARGE		= 414;
	public static final int	UNSUPPORTED_MEDIA_TYPE			= 415;
	public static final int	REQUEST_RANGE_NOT_SATISFIABLE	= 416;
	public static final int	EXPECTATION_FAILED				= 416;

}
