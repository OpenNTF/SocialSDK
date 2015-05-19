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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class models the weight of a BSS call for a given URL and a given REST method (GET, POST,
 * DELETE)
 * 
 * @author Carlos Manias
 *
 */
public class Weight{
  private String url;
  private Rest method;
  private int weight;
  private AtomicInteger counter;

  public Weight(String url, Rest method, int weight){
    this.url = url;
    this.method = method;
    this.weight = weight;
    this.counter = new AtomicInteger();
  }

  /**
   * This method returns the URL for the BSS call
   * 
   * @return a String representing the URL
   */
  public String getUrl(){
    return this.url;
  }

  /**
   * This method returns the REST method (GET, POST, DELETE)
   * 
   * @return a String representing the REST method
   */
  public Rest getMethod(){
    return this.method;
  }

  /**
   * This method returns the weight of the BSS call
   * 
   * @return an integer representing the weight
   */
  public int getWeight(){
    return this.weight;
  }

  /**
   * Returns the current amount of throttling used for this call
   * 
   * @return
   */
  public int getCounter(){
    return this.counter.addAndGet(0);
  }

  /**
   * Increments the current level of throttling used for this call and returns it
   * 
   * @param amount
   * @return
   */
  public int incrementCounter(int amount){
    return this.counter.addAndGet(amount);
  }
}
