/*
 * © Copyright IBM Corp. 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sbt.provisioning.sample.app.model;

/**
 * This class models a subscription and its entitlement state for a subscriber
 * 
 * @author Carlos Manias
 *
 */
public class SubscriptionEntitlement{
  private String partNumber;
  private String subscriptionId;
  private NotesType notesType;

  public static enum NotesType{
    NONE(""), NOTES("dom__emailAddress"), INOTES("yun__emailAddress");
    private String attribute;

    NotesType(String attribute){
      this.attribute = attribute;
    }

    public String getAttribute(){
      return attribute;
    }
  };

  public SubscriptionEntitlement(String partNumber, String subscriptionId, NotesType notesType){
    this.partNumber = partNumber;
    this.subscriptionId = subscriptionId;
    this.notesType = notesType;
  }

  public String getPartNumber(){
    return this.partNumber;
  }

  public String getSubscriptionId(){
    return this.subscriptionId;
  }

  public NotesType getNotesType(){
    return this.notesType;
  }
}
