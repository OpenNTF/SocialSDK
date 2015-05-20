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
package com.ibm.sbt.provisioning.sample.app.weightedBSSCall.subscriber;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.model.Rest;
import com.ibm.sbt.provisioning.sample.app.model.SubscriptionEntitlement.NotesType;
import com.ibm.sbt.provisioning.sample.app.services.Subscriber;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.weightedBSSCall.WeightedBSSCall;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;

public class UpdateNotesSubscriber extends WeightedBSSCall<Boolean>{

  private static final Logger logger = Logger.getLogger(UpdateNotesSubscriber.class.getName());
  private JsonJavaObject subscriberJson;
  private NotesType notesType;

  public UpdateNotesSubscriber(JsonJavaObject subscriberJson, NotesType notesType){
    this.subscriberJson = subscriberJson;
    this.notesType = notesType;
  }

  private JsonJavaObject getSubscriberByEmail(String subscriberEmail) throws BssException{
    EntityList<JsonEntity> subscriberList =
        Subscriber.getInstance().getService().getSubscribersByEmail(subscriberEmail);
    return subscriberList.get(0).getJsonObject();
  }

  private JsonJavaObject createAttribute(JsonJavaObject subscriber){
    int subscriberId = subscriber.getAsInt("Id");
    int ownerId = subscriber.getAsInt("Owner");
    String email = subscriber.getAsObject("Person").getAsString("EmailAddress");
    Map<String, Object> props = new HashMap<String, Object>();
    props.put("StringValue", email);
    props.put("SubscriberId", subscriberId);
    props.put("Owner", ownerId);
    props.put("BooleanValue", false);
    props.put("Name", notesType.getAttribute());
    props.put("Type", "STRING");
    props.put("IntegerValue", 1);
    props.put("Deleted", false);
    JsonJavaObject attribute = new JsonJavaObject(props);
    return attribute;
  }

  private JsonJavaObject addMailDomAttribute(JsonJavaObject subscriber){
    JsonJavaObject attribute = createAttribute(subscriber);
    JsonJavaArray attributesSet = subscriber.getAsArray("SubscriberAttributeSet");
    attributesSet.add(attribute);
    subscriber.put("SubscriberAttributeSet", attributesSet);
    return subscriber;
  }

  private boolean updateAttributeFullObject(JsonJavaObject subscriber){
    boolean result = false;
    try{
      JsonJavaObject updatedSubscriber = addMailDomAttribute(subscriber);
      Map<String, Object> props = new HashMap<String, Object>();
      props.put("Subscriber", updatedSubscriber);
      JsonJavaObject subscriberObject = new JsonJavaObject(props);

      Subscriber.getInstance().getService().updateSubscribeProfile(subscriberObject);
      result = true;
    }catch(BssException e){
      logger.severe("Error updating subscriber with new attribute");
      logger.severe(e.getMessage());
    }
    return result;
  }

  /**
   * Method used for triggering the proper HTTP call for adding a subscriber to an organization ,
   * towards the BSS endpoint designed for managing subscribers
   */
  @Override
  protected Boolean doCall() throws Exception{
    String email = this.subscriberJson.getAsObject("Subscriber").getAsObject("Person").getAsString("EmailAddress");
    boolean updated = false;
    try{
      JsonJavaObject subscriberFromServer = getSubscriberByEmail(email);
      if(notesType != NotesType.NONE){
        logger.fine("triggering call : " + this.getUrl() + " " + getMethod());
        updated = updateAttributeFullObject(subscriberFromServer);
        BSSProvisioning.getStateTransitionReport().get(email)[1] =
            new SimpleDateFormat(BSSProvisioning.DATE_FORMAT).format(new Date());
        BSSProvisioning.getSubscriberWeightReport().get(email)[0]++;
      }else{
        updated = true;
      }
    }catch(BssException be){
      logger.severe(" BssException : " + be.getMessage());
    }
    return updated;
  }

  @Override
  public String getUrl(){
    return BSSEndpoints.RES_SUBSCRIBER.getEndpointString();
  }

  @Override
  public Rest getMethod(){
    return Rest.POST;
  }

}
