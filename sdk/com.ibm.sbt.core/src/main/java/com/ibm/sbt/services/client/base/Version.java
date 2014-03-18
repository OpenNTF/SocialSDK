/* * © Copyright IBM Corp. 2014 * 
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
package com.ibm.sbt.services.client.base;

import java.io.Serializable;

import com.ibm.commons.util.StringUtil;

/**
 * Version provides an encapsulation for Connections version numbers and allows to compare and sort versions 
 * 
 * @author Phil Riand
 * @author Carlos Manias
 */
public class Version implements Serializable, Comparable<Version> {

	public static final Version EMPTY = new Version(0, 0, 0);
	private static final char POINT = '.';

	/**
	 * Parses a string containing a dot separated version of Connections with the format Major.Minor.Subversion and returns a Version object
	 * @param s
	 * @return
	 */
	public static Version parse(String s) {
		if (StringUtil.isEmpty(s)) {
			return EMPTY;
		}
		String sMajor = "";
		String sMinor = "";
		String sSubversion = "";
		int d1 = s.indexOf(POINT);
		if (d1 >= 0) {
			sMajor = s.substring(0, d1);
			int d2 = s.indexOf(POINT, d1 + 1);
			if (d2 >= 0) {
				sMinor = s.substring(d1 + 1, d2);
				sSubversion = s.substring(d2 + 1);
			} else {
				sSubversion = s.substring(d1 + 1);
			}
		} else {
			sMajor = s;
		}
		try {
			int major = sMajor.isEmpty() ? 0 : Integer.parseInt(sMajor);
			int minor = sMinor.isEmpty() ? 0 : Integer.parseInt(sMinor);
			int subversion = sSubversion.isEmpty() ? 0 : Integer
					.parseInt(sSubversion);
			return new Version(major, minor, subversion);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(StringUtil.format(
					"Invalid version format {0}", s), ex);
		}
	}

	private static final long serialVersionUID = 1L;

	private int major;
	private int minor;
	private int subversion;

	public Version(int major, int minor, int subversion) {
		this.major = major;
		this.minor = minor;
		this.subversion = subversion;
	}

	public Version(int major, int minor) {
		this(major, minor, 0);
	}

	@Override
	public String toString() {
		return StringUtil.format("{0}.{1}.{2}", getMajor(), getMinor(),
				getSubversion());
	}

	/**
	 * Returns major version
	 * @return
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Returns minor version
	 * @return
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Returns subversion
	 * @return
	 */
	public int getSubversion() {
		return subversion;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Version) {
			Version v = (Version) o;
			return (this == v) || (v.major == major && v.minor == minor
					&& v.subversion == subversion);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		int hash = 1;
		hash = hash * prime + getMajor();
		hash = hash * prime + getMinor();
		hash = hash * prime + getSubversion();
		return hash;
	}

	@Override
	public int compareTo(Version v) {
		final int LESSTHAN = -1;
	    final int EQUAL = 0;
	    final int GREATERTHAN = 1;
		if (this.equals(v)) {
			return EQUAL;
		} else if (this.greaterThan(v)) {
			return GREATERTHAN;
		} else {
			return LESSTHAN;
		}
	}

	/**
	 * Returns true if the current version is at least the version in parameter
	 * @param _major
	 * @return
	 */
	public boolean isAtLeast(int _major) {
		return isAtLeast(_major, 0, 0);
	}

	/**
	 * Returns true if the current version is at least the version in parameters
	 * @param _major
	 * @param _minor
	 * @return
	 */
	public boolean isAtLeast(int _major, int _minor) {
		return isAtLeast(_major, _minor, 0);
	}

	/**
	 * Returns true if the current version is at least the version in parameters
	 * @param _major
	 * @param _minor
	 * @param _subversion
	 * @return
	 */
	public boolean isAtLeast(int _major, int _minor, int _subversion) {
		if (this.major > _major) {
			return true;
		}
		if (this.major == _major) {
			if (this.minor > _minor) {
				return true;
			}
			if (this.minor == _minor) {
				if (this.subversion > _subversion) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the current version is greater than the parameter
	 * @param v
	 * @return
	 */
	public boolean greaterThan(Version v) {
		if (major > v.major) {
			return true;
		}
		if (major == v.major) {
			if (minor > v.minor) {
				return true;
			}
			if (minor == v.minor) {
				if (subversion > v.subversion) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the current version is less than the parameter
	 * @param v
	 * @return
	 */
	public boolean lessThan(Version v) {
		if (major < v.major) {
			return true;
		}
		if (major == v.major) {
			if (minor < v.minor) {
				return true;
			}
			if (minor == v.minor) {
				if (subversion < v.subversion) {
					return true;
				}
			}
		}
		return false;
	}
}
