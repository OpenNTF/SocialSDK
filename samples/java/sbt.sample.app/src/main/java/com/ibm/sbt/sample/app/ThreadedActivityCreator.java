package com.ibm.sbt.sample.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.Activity;
import com.ibm.sbt.services.client.connections.activities.ActivityNode;
import com.ibm.sbt.services.client.connections.activities.ActivityService;
import com.ibm.sbt.services.client.connections.activities.Field;
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
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author Lorenzo Boccaccia
 * @since 13 May 2014
 */
public class ThreadedActivityCreator {

    public static void main(String[] args) throws Exception {
        HashMap<String, String> p = new HashMap<String, String>();
        p.put("user", "user@example.com");
        p.put("password", "password");
        p.put("url", "https://apps.na.collabserv.com/");

        for (int i = 0; i < args.length - 1; i += 2) {
            p.put(args[i], args[i + 1]);
        }

        System.out.println("running with " + p);
        System.out.println("pass parameters with  -Duser=   -Dpassword=   -Durl=  ");

        BasicEndpoint ep = new BasicEndpoint();
        ep.setUrl(p.get("url"));
        ep.setUser(p.get("user"));
        ep.setPassword(p.get("password"));
        ep.setForceTrustSSLCertificate(true);

        final ActivityService svc = new ActivityService(ep);
        Activity c = new Activity();
        c = svc.createActivity(c);
        final String uuid = c.getActivityUuid();
        
        
        //fails even when mutexed
        final Object mutex = new Object();

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                Activity a;
                try {
                    a = svc.getActivity(uuid);
                    
                    for (int i = 0; i < 10; i++) {
                        a.setCompleted(a.isCompleted());
                        a.setContent("content " + System.currentTimeMillis());
                        synchronized (mutex) {
                            svc.updateActivity(a);
                        }
                    }
                } catch (ClientServicesException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    for (int i = 0; i < 10; i++) {
                        ActivityNode activityNode = new ActivityNode(svc);
                        activityNode.setActivityUuid(uuid);
                        //activityNode.setEntryType(type);
                        activityNode.setTitle("Node from Test " + System.currentTimeMillis());
                        activityNode.setContent("Node Content " + System.currentTimeMillis());
                        List<String> tagList = new ArrayList<String>();
                        tagList.add("tag1");
                        tagList.add("tag2");
                        activityNode.setTags(tagList);
                        synchronized (mutex) {
                            activityNode = svc.createActivityNode(activityNode);
                        }
                    }
                } catch (ClientServicesException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        
        t1.join();
        t2.join();

        //c.delete();
    }

}
