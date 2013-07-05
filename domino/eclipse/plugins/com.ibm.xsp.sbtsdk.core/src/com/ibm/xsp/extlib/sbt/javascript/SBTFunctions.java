/*
 * © Copyright IBM Corp. 2010
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
package com.ibm.xsp.extlib.sbt.javascript;

import javax.faces.context.FacesContext;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.JavaScriptException;
import com.ibm.jscript.engine.IExecutionContext;
import com.ibm.jscript.types.BuiltinFunction;
import com.ibm.jscript.types.FBSDefaultObject;
import com.ibm.jscript.types.FBSGlobalObject;
import com.ibm.jscript.types.FBSObject;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.jscript.types.FBSValueVector;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.service.basic.ProxyService;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.xsp.extlib.social.PeopleService;
import com.ibm.xsp.extlib.social.SocialServicesFactory;

/**
 * Extended Notes/Domino formula language.
 * <p>
 * This class implements a set of new functions available to the JavaScript interpreter. They become available to Domino
 * Designer in the category "@NotesFunctionEx".
 * </p>
 */
public class SBTFunctions extends FBSDefaultObject {

	// Functions IDs
	private static final int FCT_ENDPOINT          = 1;
	private static final int FCT_IDENTITYFROMID    = 3;
    private static final int FCT_IDFROMIDENTITY    = 4;

    private static final int FCT_PROXYURL          = 5;
    

	// ============================= CODE COMPLETION ==========================
	//
	// Even though JavaScript is an untyped language, the XPages JavaScript
	// interpreter can make use of symbolic information defining the
	// objects/functions exposed. This is particularly used by Domino Designer
	// to provide the code completion facility and help the user writing code.
	//
	// Each function expose by a library can then have one or multiple
	// "prototypes", defining its parameters and the returned value type. To
	// make this definition as efficient as possible, the parameter definition
	// is compacted within a string, where all the parameters are defined
	// within parenthesis followed by the returned value type.
	// A parameter is defined by its name, followed by a colon and its type.
	// Generally, the type is defined by a single character (see bellow) or a
	// full Java class name. The returned type is defined right after the
	// closing parameter parenthesis.
	//
	// Here is, for example, the definition of the "@Date" function which can
	// take 3 different set of parameters:
	// "(time:Y):Y",
	// "(years:Imonths:Idays:I):Y",
	// "(years:Imonths:Idays:Ihours:Iminutes:Iseconds:I):Y");
	//
	// List of types
	// V void
	// C char
	// B byte
	// S short
	// I int
	// J long
	// F float
	// D double
	// Z boolean
	// T string
	// Y date/time
	// W any (variant)
	// N multiple (...)
	// L<name>; object
	// ex:
	// (entries:[Lcom.ibm.xsp.extlib.MyClass;):V
	//
	// =========================================================================

	public SBTFunctions(JSContext jsContext) {

		super(jsContext, null, false);

		// Document helpers
		addFunction(FCT_ENDPOINT, "@Endpoint", "(name:T):Lcom.ibm.xsp.extlib.sbt.services.client.Endpoint;"); // $NON-NLS-1$ $NON-NLS-2$
		addFunction(FCT_IDENTITYFROMID, "@IdentityFromId", "(target:Tid:T):T"); // $NON-NLS-1$ $NON-NLS-2$
        addFunction(FCT_IDFROMIDENTITY, "@IdFromIdentity", "(target:Tidentity:T):T"); // $NON-NLS-1$ $NON-NLS-2$
        addFunction(FCT_PROXYURL, "@ProxyUrl", "(proxyName:Tendpoint:Turl:T):T"); // $NON-NLS-1$ $NON-NLS-2$
	}

	private void addFunction(int index, String functionName, String... params) {
		createMethod(functionName, FBSObject.P_NODELETE | FBSObject.P_READONLY, new NotesFunction(getJSContext(),
				index, functionName, params));
	}

	@Override
	public boolean hasInstance(FBSValue v) {
		return v instanceof FBSGlobalObject;
	}

	@Override
	public boolean isJavaNative() {
		return false;
	}

	// =================================================================================
	// Functions implementation
	// For optimization reasons, there is one NotesFunction instance per function,
	// instead of one class (this avoids loading to many classes). To then distinguish
	// the actual function, it uses an index member.
	// =================================================================================

	public static class NotesFunction extends BuiltinFunction {

		private String functionName;
		private int index;
		private String[] params;

		NotesFunction(JSContext jsContext, int index, String functionName, String[] params) {

			super(jsContext);
			this.functionName = functionName;
			this.index = index;
			this.params = params;
		}

		/**
		 * Index of the function.
		 * <p>
		 * There must be one instanceof this class per index.
		 * </p>
		 */
		public int getIndex() {
			return this.index;
		}

		/**
		 * Return the list of the function parameters.
		 * <p>
		 * Note that this list is not used at runtime, at least for now, but consumed by Designer code completion.<br>
		 * A function can expose multiple parameter sets.
		 * </p>
		 */
		@Override
		protected String[] getCallParameters() {
			return this.params;
		}

		/**
		 * Function name, as exposed by Designer and use at runtime.
		 * <p>
		 * This function is exposed in the JavaScript global namespace, so you should be careful to avoid any name
		 * conflict.
		 * </p>
		 */
		@Override
		public String getFunctionName() {
			return this.functionName;
		}

		/**
		 * Actual code execution.
		 * <p>
		 * The JS runtime calls this method when the method is executed within a JavaScript formula.
		 * </p>
		 * 
		 * @param context
		 *            the JavaScript execution context (global variables, function...)
		 * @param args
		 *            the arguments passed to the function
		 * @params _this the "this" object when the method is called as a "this" member
		 */
		@Override
		public FBSValue call(IExecutionContext context, FBSValueVector args, FBSObject _this)
				throws JavaScriptException {

			try {
				// Else execute the formulas
				switch (index) {

					// ////////////////////////////////////////////////////////////////////////////////////////
					// Document IDs
					// ////////////////////////////////////////////////////////////////////////////////////////

					case FCT_ENDPOINT: {
						if (args.size() >= 1) {
						    int type = 0;
	                        if (args.size() >= 2) {
	                            type = args.get(1).intValue();
	                        }
						    String name = args.get(0).stringValue();
						    switch(type) {
						        case 0:
		                            return FBSUtility.wrap(context.getJSContext(),EndpointFactory.getEndpoint(name));
                                case 1:
                                    return FBSUtility.wrap(context.getJSContext(),EndpointFactory.getEndpointName(name));
                                case 2:
                                    return FBSUtility.wrap(context.getJSContext(),EndpointFactory.getEndpointLabel(name));
						    }
						}
					}
						break;
					case FCT_IDENTITYFROMID: {
                        if (args.size() >= 2) {
                            String target = args.get(0).stringValue();
                            String id = args.get(1).stringValue();
                            PeopleService svc = SocialServicesFactory.getInstance().getPeopleService();
                            return FBSUtility.wrap(context.getJSContext(),svc.getUserIdentityFromId(target, id));
                        }
					}
					    break;
                    case FCT_IDFROMIDENTITY: {
                        if (args.size() >= 2) {
                            String target = args.get(0).stringValue();
                            String id = args.get(1).stringValue();
                            PeopleService svc = SocialServicesFactory.getInstance().getPeopleService();
                            return FBSUtility.wrap(context.getJSContext(),svc.getUserIdFromIdentity(target, id));
                        }
                    }
                        break;
                    case FCT_PROXYURL: {
                        if (args.size() >= 2) {
                            String proxyName = args.get(0).stringValue();
                            String endpoint = args.get(1).stringValue();
                            String url = null;
                            if (args.size() >= 3) {
                                url = args.get(2).stringValue();
                            }
                            //TODO Padraic - shoudl this be facesContext?
                            Context ctx = Context.getUnchecked();

//                            String proxiedUrl = ProxyEndpointService.getProxyUrlForEndpoint(FacesContext.getCurrentInstance(), proxyName, endpoint, url);
                            String proxiedUrl = ProxyEndpointService.getProxyUrlForEndpoint(ctx, proxyName, endpoint, url);
                            
                            
                            return FBSUtility.wrap(proxiedUrl);
                        }
                    }
                        break;

					default: {
						throw new InterpretException(null, StringUtil.format(
								"Internal error: unknown function \'{0}\'", functionName));
					}

				}

				// } catch (InterpretException e) {
				// throw e;
				// } catch (NotesException e) {
				// // This case covers where a call to session.evaluate() throws a NotesException
				// // We want to continue rendering the page but allow @IsError to pick up on this issue
				// // so we return @Error (NaN / FBSUndefined.undefinedValue)
				// return FBSUndefined.undefinedValue;
			} catch (Exception e) {
				throw new InterpretException(e, StringUtil.format("Error while executing function \'{0}\'",
						functionName));
			}
			throw new InterpretException(null, StringUtil.format("Cannot evaluate function \'{0}\'", functionName));
		}
	}
}