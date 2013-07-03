/*
 * � Copyright IBM Corp. 2010
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
package sbt;

import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.jscript.types.FBSValue;
import com.ibm.sbt.util.DataNavigator;
import com.ibm.xsp.extlib.util.JSJson;

/**
 * Helper to navigate through a Json object.
 * @author Philippe Riand
 */
public class JsonNavigator extends DataNavigator.Json {

    public JsonNavigator(JsonFactory factory, Object root) {
        super(factory,root);
    }
    public JsonNavigator(Object root) {
        super(root);
    }
    
    @Override
	protected JsonFactory findFactory(Object root) {
		if (root instanceof FBSValue) {
			return JSJson.factory;
		}
		return super.findFactory(root);
	}
}
