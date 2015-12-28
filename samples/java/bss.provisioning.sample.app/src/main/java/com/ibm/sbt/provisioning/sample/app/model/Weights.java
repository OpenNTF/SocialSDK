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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaArray;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.provisioning.sample.app.task.BSSProvisioning;
import com.ibm.sbt.provisioning.sample.app.util.BSSEndpoints;
import com.ibm.sbt.provisioning.sample.app.util.Util;
import com.ibm.sbt.services.client.base.datahandlers.JsonDataHandler;

/**
 * This class represents the full json object containing the information about the weight calls
 * 
 * @author Carlos Manias
 *
 */
public class Weights{

  private static final Logger logger = Logger.getLogger(Weights.class.getName());

  private long maxSystemWeight;
  private long resetDuration;
  private WeightSet defaultSet;

  public Weights(){
    loadWeightsFromFile();
  }

  public Weights(String weightsFile){
    loadWeightsFromFile(weightsFile);
  }

  /**
   * This method returns the duration
   * 
   * @return a long representing the duration
   */
  public long getResetDuration(){
    return this.resetDuration;
  }

  /**
   * This method returns the maximum system weight
   * 
   * @return a long representing the maximum system weight.
   */
  public long getMaxSystemWeight(){
    return this.maxSystemWeight;
  }

  /**
   * Returns the default weightSet
   * 
   * @return {WeightSet}
   */
  public WeightSet getDefaultSet(){
    return this.defaultSet;
  }

  /**
   * Returns the limit
   * 
   * @return The value of the limit
   */
  public long getLimit(){
    return this.defaultSet.getLimit();
  }

  /**
   * This method returns the Weight object associated with a url and a REST method
   * 
   * @param url
   * @param method
   * @return {Weight}
   */
  public Weight getWeight(String url, Rest method){
    return this.defaultSet.getWeight(url, method);
  }

  public int getWeightValue(String url, Rest method){
    return this.defaultSet.getWeight(url, method).getWeight();
  }

  public int incrementCounter(String url, Rest method, int amount){
    return this.defaultSet.incrementCounter(url, method, amount);
  }

  public int getCounter(String url, Rest method){
    return this.defaultSet.getCounter(url, method);
  }

  /**
   * This method loads the weights from a JSON file
   */
  public void loadWeightsFromFile(){
    // loadWeightsFromFile("resources/weights.json");
    loadWeightsFromFile("/weights.json");
  }

  /**
   * This method is responsible for parsing the application input file specifying the weight of each
   * call to the BSS API .
   * <p>
   * 
   * @param weightsFilePath weights json file path<br>
   * @return a <code>Map</code> keeping track of the weight associated with each call to the BSS API
   */
  private void loadWeightsFromFile(String weightsFilePath){
    logger.info("Loading weights from file");
    String weightsJson = null;
    JsonDataHandler handler = null;
    try{
      weightsJson = Util.readWeightsJson(weightsFilePath, BSSProvisioning.isWeightsFileAsInput());
      // weightsJson = Util.readWeightsJson(weightsFilePath,
      // BSSProvisioning.isWeightsFileAsInput());
      handler = new JsonDataHandler(weightsJson);
    }catch(IOException ioe){
      System.out.println(ioe.getMessage());
    }catch(JsonException e){
      System.out.println(e.getMessage());
    }

    if(weightsJson != null && handler != null){
      long resetDuration = handler.getAsLong("settings/resetDuration");
      long maxSystemWeight = handler.getAsLong("settings/maxSystemWeight");
      this.maxSystemWeight = maxSystemWeight;
      this.resetDuration = resetDuration;

      JsonJavaObject defaultObj = (JsonJavaObject) handler.getAsObject("default");

      defaultSet = new WeightSet();
      long threshold = defaultObj.getAsLong("limit");
      defaultSet.setLimit(threshold);

      for(BSSEndpoints endpoint:BSSEndpoints.values()){
        String url = endpoint.getEndpointString();
        ArrayList<Weight> weightsList = new ArrayList<Weight>();
        if(endpoint.hasSameWeight()){
          JsonJavaArray jsonArray = defaultObj.getAsArray(url);
          JsonJavaObject weightArr = (JsonJavaObject) jsonArray.get(0);
          String callWeight = weightArr.getString("weight");
          int weightValue = Integer.parseInt(callWeight.replace(".0", ""));
          weightsList.add(new Weight(url, Rest.ALL, weightValue));
          /*
           * weightsList.add(new Weight(url, "GET", weightValue)); weightsList.add(new Weight(url,
           * "POST", weightValue)); weightsList.add(new Weight(url, "DELETE", weightValue));
           */
        }else{
          JsonJavaArray resourceCustomer = defaultObj.getAsArray(endpoint.getEndpointString());
          for(int i = 0; i < resourceCustomer.length(); i++){
            JsonJavaObject entry = (JsonJavaObject) resourceCustomer.get(i);
            String callWeight = entry.getString("weight");
            String method = entry.getString("method");
            int weightValue = Integer.parseInt(callWeight.replace(".0", ""));
            weightsList.add(new Weight(url, Rest.valueOf(method), weightValue));
          }
        }
        defaultSet.putMethodWeights(weightsList);
      }
      logger.finest("latest weights settings :");
    }
  }
}
