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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.FacesException;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TextUtil;
import com.ibm.xsp.FacesExceptionEx;


/**
 * Tiny JSP process that replaces basic JSP directives, <% %> <%=...%> and generate a Java class.
 * 
 * @author priand
 */
public class JspCompiler {
	
	public static final String START_TAG = "<%"; //$NON-NLS-1$
	public static final String END_TAG 	 = "%>";  //$NON-NLS-1$

	private static final String IMPORTPATTERN = "<%@[ \\t\\n\\r]*page[ \\t\\n\\r]+import[ \\t\\n\\r]*=[ \\t\\n\\r]*[\\\'\\\"]([^\\\'\\\"]+)[\\\'\\\"][ \\t\\n\\r]*%>";
	private static Pattern importPattern = Pattern.compile(IMPORTPATTERN);
	
	public JspCompiler() {
	}
	
	// Generate the JavaScript code that then output the final text
	public String compileJsp(String source, String className) throws FacesException {
		StringBuilder javaSource = new StringBuilder(2048);
		
		generateHeader(javaSource, source, className);
		
		source = extractBody(source);
		int slen = source.length();
		
		for(int pos=0; pos<slen; ) {
			int exp = source.indexOf(START_TAG,pos);
			int end = exp>=0 ? exp : slen;
			if(end>pos) {
				javaSource.append("    emit(out,");
				String emit = TextUtil.toJavaString(source.substring(pos,end),true);
				javaSource.append(emit);
				pos = end;
				javaSource.append(");\n");
			}
			if(exp>=0) {
				int stend = source.indexOf(END_TAG,exp+2); 
				if(stend<0) {
					throw new FacesExceptionEx(null,"<% without a closing %>");
				}
				
				if(source.charAt(exp+2)=='=') { 
					String sub = source.substring(exp+3,stend).trim();
					javaSource.append("    emit(out,");
					String emit = sub;
					javaSource.append(emit);
					javaSource.append(");\n");
				} else {
					String sub = source.substring(exp+2,stend).trim();
					javaSource.append(sub);
					if(!sub.endsWith(";")) {
						javaSource.append(";");
					}
					javaSource.append("\n"); 
				}
				pos = stend+2;
			}
		}

		generateFooter(javaSource, className);

		return javaSource.toString();
	}

	public static String extractBody(String source) throws FacesException {
		int start = StringUtil.indexOfIgnoreCase(source, "<body>");
		int end = StringUtil.indexOfIgnoreCase(source, "</body>");
		if(start>=0 && end>=start+6) {
			return source.substring(start+6,end).trim();
		}
		return source;
	}
	
	protected void generateHeader(StringBuilder b, String source, String className) {
		int pos = className.lastIndexOf('.');
		String genPackage = pos>=0 ? className.substring(0,pos) : null;
		String genClass = pos>=0 ? className.substring(pos+1) :className;
		if(StringUtil.isNotEmpty(genPackage)) {
			b.append("package ").append(genPackage).append(";\n");
			b.append("\n");
		}
		generateImports(b, source);
		b.append("public class ").append(genClass).append(" extends nsf.playground.jsp.JspFragment {\n");
		b.append("  public void exec(JspWriter out, HttpServletRequest request, HttpServletResponse response) throws Exception {\n");
	}

	protected void generateImports(StringBuilder b, String source) throws FacesException {
		// Std imports
		b.append("import javax.servlet.http.HttpServletRequest;\n");
		b.append("import javax.servlet.http.HttpServletResponse;\n");
		b.append("import javax.servlet.jsp.JspWriter;\n");
		
		// Then import from the JSP page
		Matcher matcher = importPattern.matcher(source);
		while(matcher.find()) {
			String imp = matcher.group(1); 
			b.append("import ").append(imp).append(";\n");
		}
		b.append("\n");
	}
	
	
	protected void generateFooter(StringBuilder b, String className) {
		b.append("  } // exec\n");
		b.append("} // class\n");
	}
	
/*	
	public static void main(String[] args) {
//		test("<body>Simple JSP</body>"); 
//		test("<html><head><%@page import=\"mypack1\"%></head><body>Simple JSP</body></html>"); 
//		test("<html><head><%@page import=\"mypack1\"%></head><body>Simple JSP<span><%=myfunc()%></span></body></html>"); 
//		test("<html><head><%@page import=\"mypack1\"%></head><body><%Object o = factory;%>Simple JSP<span><%=myfunc()%></span></body></html>"); 
//		test("<html><head><%@page import=\"mypack1\"%></head><body><%Object o = factory%>Simple JSP<span><%=myfunc()%></span></body></html>"); 
		test("<html><head><%@page import=\"mypack1\"%><%@page import=\"mypack2\"%></head><body><%Object o = factory%>Simple JSP<span><%=myfunc()%></span></body></html>"); 
	}
	private static void test(String s) {
		try {
			System.out.println(StringUtil.format("=======================================================")); 
			System.out.println(StringUtil.format("Pattern: {0}",IMPORTPATTERN)); 
			System.out.println(StringUtil.format(">>>>Source:\n{0}\n",s)); 
			JspCompiler p = new JspCompiler();
			String javaCode = p.compileJsp(s,"myPackage.MyClass");
			System.out.println(StringUtil.format(">>>>Generated Java class:\n{0}\n",javaCode)); 
		} catch(Exception e) {
			System.out.println(StringUtil.format("Error while processing text")); 
			e.printStackTrace();
		}
	}
*/	
}
