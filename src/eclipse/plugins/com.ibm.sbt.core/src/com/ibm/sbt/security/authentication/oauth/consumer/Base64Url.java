/****************************************************************************************
 * Copyright 2012 IBM Corp.                                                                   *
 *                                                                                      *
 * Licensed under the Apache License, Version 2.0 (the "License");                      *
 * you may not use this file except in compliance with the License.                     *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0   *
 *                                                                                      *
 * Unless required by applicable law or agreed to in writing, software                  *
 * distributed under the License is distributed on an "AS IS" BASIS,                    *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.             *
 * See the License for the specific language governing permissions and                  *
 * limitations under the License.                                                       *
 ****************************************************************************************/

package com.ibm.sbt.security.authentication.oauth.consumer;
public final class Base64Url {

   // private static final String CLASS_NAME = Base64Url.class.getName();

    // Tokens used in Base64-encoded data, indexed by their corresponding byte value.
    private static final char[] TOKENS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '-', '_' };

    // The minimum token character value considered for decoding.
    private static final int TOKEN_MIN = 0;

    // The maximum token character value considered for decoding.
    private static final int TOKEN_MAX = Byte.MAX_VALUE;

    // Value associated with characters that are not valid Base64 token.
    private static final byte INVALID_TOKEN_VALUE = -1;

    // Array indexed by token character and containing decoded values.
    // INVALID_TOKEN_VALUE is used for characters that are not valid Base64
    // tokens.
    private static final byte[] TOKEN_VALUES = new byte[TOKEN_MAX - TOKEN_MIN + 1];

    static {
        // Initialize the token values array to the invalid token value.
        for (int i = 0; i < TOKEN_VALUES.length; i++) {
            TOKEN_VALUES[i] = INVALID_TOKEN_VALUE;
        }
        // Populate the valid token entries in the token values array.
        for (int i = 0; i < TOKENS.length; i++) {
            TOKEN_VALUES[TOKENS[i]] = (byte) i;
        }
    }

    // Private constructor to prevent instantiation.
    private Base64Url() {
        // Never called.
    }

    /**
     * Encode a byte array into a Base64-encoded string. The string is not
     * broken into 72 character lines.
     * 
     * @param data
     *            Byte data to be encoded.
     * @return Base64-encoded string.
     */
    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    /**
     * Encode a byte array into a Base64-encoded String. The String is not
     * broken into 72 character lines.
     * 
     * @param data
     *            Byte data to be encoded
     * @param offset
     *            Starting index within the byte data to be encoded
     * @param length
     *            Number of bytes to encode
     * @return Base64-encoded string.
     */
    public static String encode(byte[] data, int offset, int length) {
        // overestimating the resultSize is ok
        final int remainder = length % 3;
        final int resultSize = ((remainder == 0 ? length : length + 3 - remainder) / 3 * 4);
        final StringBuilder resultBuff = new StringBuilder(resultSize);

        // Indicates the current position within the three byte encoding input group.
        int groupIndex = 0;
        byte previousByte = 0;
        byte currentByte;

        // Iterate through and encode all the input data.
        for (int i = offset; i < length+offset; i++) {
            currentByte = data[i];
            switch (groupIndex) {
            case 0: {
                // Encode the first 6-bit output token using the first byte of
                // the input group.
                resultBuff.append(TOKENS[(currentByte & 0xfc) >> 2]);
                groupIndex = 1;
                break;
            }
            case 1: {
                // Encode the second 6-bit output token using the first and
                // second byte of the input group.
                resultBuff.append(TOKENS[((previousByte & 0x3) << 4)
                        | ((currentByte & 0xf0) >> 4)]);
                groupIndex = 2;
                break;
            }
            case 2: {
                // Encode the third and fourth 6-bit output token using the
                // second and third byte of the input group.
                resultBuff.append(TOKENS[((previousByte & 0xf) << 2)
                        | ((currentByte & 0xc0) >> 6)]);
                resultBuff.append(TOKENS[currentByte & 0x3f]);
                groupIndex = 0;
                break;
            }
            }
            // Keep hold of the current byte as it may contain data for the next
            // 6-bit output.
            previousByte = currentByte;
        }
        // Encode any remaining data using zero as the next byte
        switch (groupIndex) {
        case 1: {
            resultBuff.append(TOKENS[(previousByte & 0x3) << 4]);
            break;
        }
        case 2: {
            resultBuff.append(TOKENS[(previousByte & 0xf) << 2]);
            break;
        }
        }
        return  resultBuff.toString();
    }

    /**
     * Decode a Base64-encoded string
     * 
     * @param data
     *            Base64-encoded String
     * @return Decoded data
     * @throws IllegalArgumentException
     *             Thrown if the supplied string is not a valid Base64-encoded
     *             string.
     */
    public static byte[] decode(String data) throws IllegalArgumentException {
        return decode(data.toCharArray(), 0, data.length());
    }

    /**
     * Decode the portion of the char array of Base64-encoded data identified by
     * offset and length
     * 
     * @param data
     *            Base64-encoded char array
     * @param offset
     *            The starting point in the char array of the data to be decoded
     * @param length
     *            The length of the data in the char array to be decoded
     * @return Decoded data
     * @throws IllegalArgumentException
     *             Thrown if the supplied char array does not contain valid
     *             Base64-encoded data.
     */
    public static byte[] decode(char[] data, int offset, int length) {
        int numberOfCompleteGroups = length/4;
        int incompleteGroupLength = length%4;
        int resultLength = numberOfCompleteGroups*3;
        switch (incompleteGroupLength) {
        case 1: {
        	throw new IllegalArgumentException();
        }
        case 2: {
        	resultLength += 1;
        	break;
        }
        case 3: {
        	resultLength += 2;
        	break;
        }
        }
        final byte[] result = new byte[resultLength];
        int index = 0;

        // Current index in the four character input group.
        int groupIndex = 0;

        int previousTokenValue = 0;
        int currentTokenValue;

        // Read and decode all the input data.
        for (int i = offset; i < length+offset; i++) {
            char token = data[i];
            // Ignore any tokens that are not valid Base64 characters.
            if (token < TOKEN_MIN
                    || token > TOKEN_MAX
                    || (currentTokenValue = TOKEN_VALUES[token]) == INVALID_TOKEN_VALUE) {
                continue;
            }

            switch (groupIndex) {
            case 0: {
                groupIndex = 1;
                break;
            }
            case 1: {
                // Calculate the first byte of the output group from the first
                // and second characters of the input.
                result[index++] = (byte) ((previousTokenValue << 2) | ((currentTokenValue & 0x30) >> 4));
                groupIndex = 2;
                break;
            }
            case 2: {
                // Calculate the second byte of the output group from the second
                // and third characters of the input.
                result[index++] = (byte) (((previousTokenValue & 0xf) << 4) | ((currentTokenValue & 0x3c) >> 2));
                groupIndex = 3;
                break;
            }
            case 3: {
                // Calculate the third byte of the output group from the third
                // and fourth characters of the input.
                result[index++] = (byte) (((previousTokenValue & 0x3) << 6) | currentTokenValue);
                groupIndex = 0;
                break;
            }
            }
            previousTokenValue = currentTokenValue;
        }
        final byte[] decoded;
        if (index < result.length) { // must have ignored some non Base64 characters
        	throw new IllegalArgumentException();
        } else {
            decoded = result;
        }
        return decoded;
    }
    
    public static void main(String[] args) {
    	byte[] toEncode;
    	String encoded, toEncodeStr, decodedStr;
    	byte[] decoded;
    	java.util.Random rand = new java.util.Random();
    	for (int i=1; i<100; i++) {
    		toEncode = new byte[i];
    		rand.nextBytes(toEncode);
    		toEncodeStr = new java.math.BigInteger(toEncode).toString();
    		encoded = Base64Url.encode(toEncode);
    		decoded = Base64Url.decode(encoded);
    		decodedStr = new java.math.BigInteger(decoded).toString();
//    		System.out.println(encoded + "," + toEncodeStr + "," + decodedStr);
    		for (int j=0; j<i; j++) {
    			if (toEncode[j] != decoded[j]) {
//    				System.out.println("failed at length=" + i + " toEncode=" + toEncode + " decoded=" + decoded);
    			}
    		}
    	}
    }
}
