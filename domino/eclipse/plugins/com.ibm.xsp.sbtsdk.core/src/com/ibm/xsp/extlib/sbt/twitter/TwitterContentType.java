/*
 * © Copyright IBM Corp. 2011
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
package com.ibm.xsp.extlib.sbt.twitter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.renderkit.ContentTypeRenderer;

/**
 * @author doconnor
 *
 */
public class TwitterContentType implements ContentTypeRenderer {
    private final static String urlPatt = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
    private final static String hashTagsPattern = "(" + TwitterRegEx.AUTO_LINK_HASHTAGS.pattern() + ")";
    private final static String userNamesPattern = "(" + TwitterRegEx.AUTO_LINK_USERNAMES_OR_LISTS.pattern() + ")";
    private final static Pattern linkPattern = Pattern.compile(userNamesPattern + "|" + urlPatt + "|" + hashTagsPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    
    private final String CONTENT = "twitterContent";
    /**
     * 
     */
    public TwitterContentType() {
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.renderkit.ContentTypeRenderer#getContentTypes()
     */
    public String[] getContentTypes() {
        return new String[]{CONTENT};
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.renderkit.ContentTypeRenderer#render(javax.faces.context.FacesContext, javax.faces.component.UIComponent, javax.faces.context.ResponseWriter, java.lang.String, java.lang.String)
     */
    public boolean render(FacesContext context, UIComponent component, ResponseWriter writer, String contentType, String value) throws IOException {
        if(StringUtil.equals(CONTENT, contentType)){
            renderContent(context, writer, component, value);
            return true;
        }
        return false;
    }
    
    private void renderContent(FacesContext context, ResponseWriter writer, UIComponent component, String value) throws IOException {
        Matcher matcher = linkPattern.matcher(value);
        writer.startElement("span", component);
        int lastEnd = 0;
        boolean foundLink = false;
        while (matcher.find()) {
            foundLink = true;
            int matchStart = matcher.start(0);
            if(matchStart > 0){
                matchStart++;
            }
            int matchEnd = matcher.end();
            String url = value.substring(matchStart, matchEnd);
            String plainText = value.substring(lastEnd, matchStart);
            writer.writeText(plainText, null);
            if(StringUtil.isNotEmpty(url)){
                String href = url;
                if(url.trim().startsWith("#")){
                    href = "http://twitter.com/#!/search?q=%23" + url.trim().substring(1);
                }
                else if(url.trim().startsWith("@")){
                    href = "http://twitter.com/#!/" + url.trim().substring(1);
                }
                writer.startElement("a", component);
                writer.writeURIAttribute("href", context.getExternalContext().encodeResourceURL(href), null);
                writer.writeAttribute("target", "_blank", null);
                writer.writeText(url, null);
                writer.endElement("a");
            }
            lastEnd = matchEnd;
        }
        if(foundLink){
            if(lastEnd < value.length()){
                String plainText = value.substring(lastEnd);
                writer.writeText(plainText, null);
            }
        }
        else{
            writer.writeText(value, null);
        }
        writer.endElement("span");
    }
}
