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
package nsf.playground.extension;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsf.playground.playground.PreviewJavaHandler.Renderer;
import nsf.playground.playground.PreviewJavaHandler.RequestParams;


/**
 * Java preview extension for the Playground.
 * 
 * @author priand
 */
public abstract class JavaPreviewExtension {
	
	protected JavaPreviewExtension() {
	}

	public Renderer findRenderer(HttpServletRequest req, HttpServletResponse resp, RequestParams requestParams, boolean createDefault) throws IOException {
		return null;
	}
}
