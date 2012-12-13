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
package com.ibm.commons.runtime.util;

import java.io.*;
import java.util.BitSet;

/**
 * URLEncoder does not work properly when encoding UTL.
 * This class uses the same strategy than the JSF RI HtmlUtils method 
 * 
 * @author priand
 */
public class URLEncoding {
    
    private static final BitSet DONT_ENCODE_SET = new BitSet(256);

    // See: http://www.ietf.org/rfc/rfc2396.txt
    // We're not fully along for that ride either, but we do encode
    // ' ' as '%20', and don't bother encoding '~' or '/'
    static {
        for (int i = 'a'; i <= 'z'; i++) {
            DONT_ENCODE_SET.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            DONT_ENCODE_SET.set(i);
        }
        for (int i = '0'; i <= '9'; i++) {
            DONT_ENCODE_SET.set(i);
        }
        // Don't encode '%' - we don't want to double encode anything.
        DONT_ENCODE_SET.set('%');
        // Ditto for '+', which is an encoded space
        DONT_ENCODE_SET.set('+');

        DONT_ENCODE_SET.set('#');
        DONT_ENCODE_SET.set('&');
        DONT_ENCODE_SET.set('=');
        DONT_ENCODE_SET.set('-');
        DONT_ENCODE_SET.set('_');
        DONT_ENCODE_SET.set('.');
        DONT_ENCODE_SET.set('*');
        DONT_ENCODE_SET.set('~');
        DONT_ENCODE_SET.set('/');
        DONT_ENCODE_SET.set('\'');
        DONT_ENCODE_SET.set('!');
        DONT_ENCODE_SET.set('(');
        DONT_ENCODE_SET.set(')');
    }
    
    public static String encodeURIString(String text, String encoding, int start, boolean isQueryString) throws IOException, UnsupportedEncodingException {
        StringBuilder b = new StringBuilder();
        char[] cArray = null;
        
        int length = text.length();
        for (int i = start; i < length; i++) {
            char ch = text.charAt(i);
            // PHIL: we should not replace the & by &amp; in this case as we are not 
            // storing the URL within the HTML
            
            if (DONT_ENCODE_SET.get(ch)) {
                b.append(ch);
            } else if(ch==':' && !isQueryString) {
                // Emit the : as it is generally part of the protocol (http://)
                b.append(ch);
            } else if(ch=='?' && !isQueryString) {
                // Emit the start of the query string, as expected
                b.append(ch);
                isQueryString = true;
            } else {
                // Is there a faster way for getting the bytes?
                // It looks like StringCoding class is an internal implementation
                if(cArray==null) {
                    cArray = new char[1];
                }
                cArray[0] = ch;
                String s = new String(cArray);
                byte[] ba = s.getBytes("UTF-8"); // $NON-NLS-1$
                for (int j = 0; j < ba.length; j++) {
                    writeURIDoubleHex(b, ba[j] + 256);
                }
            }
        }
        
        return b.toString();
    }

    static private void writeURIDoubleHex(StringBuilder b, int i) throws IOException {
        b.append('%');
        b.append(intToHex((i >> 4) & 0x0F));
        b.append(intToHex(i & 0x0F));
    }

    static private char intToHex(int i) {
        if (i < 10) {
            return ((char) ('0' + i));
        } else {
            return ((char) ('A' + (i - 10)));
        }
    }
}