/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;


public class TextOutputStream extends OutputStream {

	private boolean newLine;
	private int indentLevel;
	private int indentLength;
	private OutputStream os;
	private String lineSeparator;

    public TextOutputStream(OutputStream os) {
    	this.os = os;
    	this.newLine = true;
    	this.indentLength = 2;
    	this.lineSeparator = System.getProperty("line.separator");
    }

    public void incIndent() {
    	indentLevel++;
    }

    public void decIndent() {
    	indentLevel--;
    }

    @Override
	public void flush() throws IOException {
    	os.flush();
    }

    public void flushNoException() {
    	try {
    		os.flush();
    	} catch(IOException ex) {
    		Platform.getInstance().log(ex);
    	}
    }

    @Override
	public void close() throws IOException {
    	os.close();
    }

    @Override
	public void write(byte[] b, int off, int len) throws IOException {
    	for(int i=off; i<off+len; i++) {
    		outchar((char)b[i]);
    	}
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b,0,b.length);
	}

	@Override
	public void write(int b) throws IOException {
		outchar((char)b);
	}

	public void write(char cbuf[], int off, int len) throws IOException {
    	for(int i=off; i<off+len; i++) {
    		outchar(cbuf[i]);
    	}
    }

    public void println() throws IOException {
    	outchar('\n');
    }

    public void println(char c) throws IOException {
    	outchar(c);
    	outchar('\n');
    }

    public void println(String msg) throws IOException {
    	outstr(msg);
    	outchar('\n');
    }

    public void println(String fmt, Object... parameters) throws IOException {
    	String msg = StringUtil.format(fmt, parameters);
    	outstr(msg);
    	outchar('\n');
    }

    public void print(char c) throws IOException {
    	outchar(c);
    }

    public void print(String msg) throws IOException {
    	outstr(msg);
    }

    public void print(String fmt, Object... parameters) throws IOException {
    	String msg = StringUtil.format(fmt, parameters);
    	outstr(msg);
    }


    public void trace(String fmt, Object... parameters) {
    	try {
	    	String msg = StringUtil.format(fmt, parameters);
	    	outstr(msg);
	    	outchar('\n');
    	} catch(IOException ex) {
    		Platform.getInstance().log(ex);
    	}
    }

    public void exception(Throwable ex) {
		PrintWriter pw = new PrintWriter(this);
		ex.printStackTrace(pw);
		pw.flush();
    }

    private void outstr(CharSequence s) throws IOException {
    	int count = s.length();
    	for(int i=0; i<count; i++) {
    		char c = s.charAt(i);
    		outchar(c);
    	}
    }

    private void outchar(char c) throws IOException {
    	if(c=='\n') {
    		newLine = true;
    		_writestr(lineSeparator);
    	} else {
        	if(newLine) {
        		if(indentLevel>0) {
        			_writestr(spaces(indentLength*indentLevel));
        		}
        		newLine = false;
        	}
    		os.write(c);
    	}
    }
    private void _writestr(String s) throws IOException {
    	int count = s.length();
    	for(int i=0; i<count; i++) {
    		os.write(s.charAt(i));
    	}
    }
    
	/**
	 * Generates a sequence of spaces
	 * @param count
	 * @return
	 */
	public static String spaces(int count) {
		switch(count) {
			case 0:		return "";
			case 1:		return " ";
			case 2:		return "  ";
			case 3:		return "   ";
			case 4:		return "    ";
			case 5:		return "     ";
			case 6:		return "      ";
			case 7:		return "       ";
			case 8:		return "        ";
		}
		return StringUtil.repeat(' ', count);
	}
}