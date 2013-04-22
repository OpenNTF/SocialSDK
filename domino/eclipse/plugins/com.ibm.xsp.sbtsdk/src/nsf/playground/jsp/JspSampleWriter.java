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

import javax.servlet.jsp.JspWriter;

/**
 * JSP Fragment base class.
 * 
 * This class provides the necessary execution methods.
 * 
 * @author priand
 */
public class JspSampleWriter extends JspWriter {

	private Writer w;

	public JspSampleWriter(Writer w) {
		super(0,true);
		this.w=w;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		w.write(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {
		w.flush();
	}

	@Override
	public void close() throws IOException {
		w.close();
	}

	
	
	public void newLine() throws IOException {
		w.write("\n");
	}
	public void print(boolean b) throws IOException {
		w.write(Boolean.toString(b));
	}
	public void print(char c) throws IOException {
		w.write(Character.toString(c));
	}
	public void print(int i) throws IOException {
		w.write(Integer.toString(i));
	}
	public void print(long l) throws IOException {
		w.write(Long.toString(l));
	}
	public void print(float f) throws IOException {
		w.write(Float.toString(f));
	}
	public void print(double d) throws IOException {
		w.write(Double.toString(d));
	}
	public void print(char s[]) throws IOException {
		write(s);
	}
	public void print(String s) throws IOException {
		write(s);
	}
	public void print(Object obj) throws IOException {
		if(obj!=null) {
			write(obj.toString());
		}
	}
	public void println() throws IOException {
		newLine();
	}
	public void println(boolean x) throws IOException {
		print(x); newLine();
	}
	public void println(char x) throws IOException {
		print(x); newLine();
	}
	public void println(int x) throws IOException {
		print(x); newLine();
	}
	public void println(long x) throws IOException {
		print(x); newLine();
	}
	public void println(float x) throws IOException {
		print(x); newLine();
	}
	public void println(double x) throws IOException {
		print(x); newLine();
	}
	public void println(char x[]) throws IOException {
		print(x); newLine();
	}
	public void println(String x) throws IOException {
		print(x); newLine();
	}
	public void println(Object x) throws IOException {
		print(x); newLine();
	}
	public void clear() throws IOException {
	}
	public void clearBuffer() throws IOException {
	}
	public int getRemaining() {
		return 0;
	}
}
