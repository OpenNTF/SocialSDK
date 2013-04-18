/*
 * © Copyright IBM Corp. 2013
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

package nsf.playground.jsp;

import java.io.IOException;
import java.io.Writer;

import com.ibm.commons.util.StringUtil;



/**
 * JSP Fragment base class.
 * 
 * This class provides the necessary execution methods.
 * 
 * @author priand
 */
public abstract class JspFragment {

	public abstract void exec(Writer w) throws IOException;

	public void emit(Writer w, Object o) throws IOException {
		if(o!=null) {
			String s = o.toString();
			if(StringUtil.isNotEmpty(s)) {
				w.write(s);
			}
		}
	}
}
