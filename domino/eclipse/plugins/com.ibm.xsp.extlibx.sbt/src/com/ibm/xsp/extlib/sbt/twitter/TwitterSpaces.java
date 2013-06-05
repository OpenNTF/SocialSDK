/*
 * © Copyright Twitter
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

/**
 * @author doconnor
 *
 */
public class TwitterSpaces {
    private static final String[] UNICODE_SPACE_RANGES = {
        "\\u0009-\\u000d", // # White_Space # Cc [5] <control-0009>..<control-000D>
        "\\u0020", // White_Space # Zs SPACE
        "\\u0085", // White_Space # Cc <control-0085>
        "\\u00a0", // White_Space # Zs NO-BREAK SPACE
        "\\u1680", // White_Space # Zs OGHAM SPACE MARK
        "\\u180E", // White_Space # Zs MONGOLIAN VOWEL SEPARATOR
        "\\u2000-\\u200a", // # White_Space # Zs [11] EN QUAD..HAIR SPACE
        "\\u2028", // White_Space # Zl LINE SEPARATOR
        "\\u2029", // White_Space # Zp PARAGRAPH SEPARATOR
        "\\u202F", // White_Space # Zs NARROW NO-BREAK SPACE
        "\\u205F", // White_Space # Zs MEDIUM MATHEMATICAL SPACE
        "\\u3000", // White_Space # Zs IDEOGRAPHIC SPACE
      };
      private static String characterClass = null;

      static {
        StringBuilder sb = new StringBuilder(UNICODE_SPACE_RANGES.length+1);
        for (int i=0; i < UNICODE_SPACE_RANGES.length; i++) {
          sb.append(UNICODE_SPACE_RANGES[i]);
        }
        characterClass = sb.toString();
      }

      public static String getCharacterClass() {
        return characterClass;
      }
}
