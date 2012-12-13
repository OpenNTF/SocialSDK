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
package com.ibm.sbt.security.authentication.password.consumer.store;

import com.ibm.sbt.security.authentication.password.PasswordException;
import com.ibm.sbt.security.authentication.password.consumer.UserPassword;




/**
 * Gives access to a user password for a particular application.
 * <p>
 * A particular implementation of the store should get some user credential to get access to the password.
 * Note that a password 
 * </p>
 * @author Philippe Riand
 */
public interface PasswordStore {
    
    public UserPassword loadUserPassword(String requestUri) throws PasswordException;

    public void saveUserPassword(String requestUri, String userId, String password) throws PasswordException;

    public void deleteUserPassword(String requestUri) throws PasswordException;
}
