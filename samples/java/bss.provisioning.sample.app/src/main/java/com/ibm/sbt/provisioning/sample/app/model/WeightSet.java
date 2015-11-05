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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the collection of weights for each BSS call
 * 
 * @author Carlos Manias
 *
 */
public class WeightSet{
  private long limit;

  private Map<String, HashMap<Rest, Weight>> weights;

  public WeightSet(){
    weights = new HashMap<String, HashMap<Rest, Weight>>();
  }

  /**
   * This method populates the weights hashmap with an ArrayList of Weight objects
   * 
   * @param weightsList
   */
  public void putMethodWeights(ArrayList<Weight> weightsList){
    HashMap<Rest, Weight> methodWeights = new HashMap<Rest, Weight>();
    String url = null;
    for(Weight weight:weightsList){
      if(url == null){
        url = weight.getUrl();
      }
      methodWeights.put(weight.getMethod(), weight);
    }
    weights.put(url, methodWeights);
  }

  /**
   * This method returns the Weight object associated with a url and a REST method
   * 
   * @param url
   * @param method
   * @return {Weight}
   */
  public Weight getWeight(String url, Rest method){
    return weights.get(url).get(method);
  }

  public int incrementCounter(String url, Rest method, int amount){
    return weights.get(url).get(method).incrementCounter(amount);
  }

  public int getCounter(String url, Rest method){
    return weights.get(url).get(method).getCounter();
  }

  /**
   * This method sets the value of the limit
   * 
   * @param limit
   */
  public void setLimit(long limit){
    this.limit = limit;
  }

  /**
   * Returns the limit
   * 
   * @return {long}
   */
  public long getLimit(){
    return limit;
  }

  /**
   * Returns all the urls
   * 
   * @return {Set<String>}
   */
  public Set<String> getUrls(){
    return weights.keySet();
  }

  /**
   * Returns everything for a URL
   * 
   * @param url
   * @return {HashMap<Rest, Weight>}
   */
  public HashMap<Rest, Weight> getAllFromUrl(String url){
    return weights.get(url);
  }

}
