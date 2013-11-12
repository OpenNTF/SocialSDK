/*
 * © Copyright IBM Corp. 2010
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

package nsf.playground.jobs;

import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;

public class TestAction extends AsyncAction {

	public static final String ACTION_ID	= "TestAction";
	
	public static boolean cancel() {
		return cancel(ACTION_ID);
	}
	
	public static synchronized boolean start(String databaseName, String sourceName) {
		if(!actionExists(ACTION_ID)) {
			registerAction(ACTION_ID, new TestAction());
		}
		return runAction(ACTION_ID);
	}	
	
    public String getActionLabel() {
    	return "Test Action";
    }
    public void run(Session session, Object parameters) throws NotesException {
        System.out.println("Start TASK");
        System.out.println("Created session for task: "+session.getUserName()+", Effective User:"+session.getEffectiveUserName());
        int numberOfTasks = 15;
main:   for(int ts=0; ts<numberOfTasks; ts++) {
            if(isCancelled()) {
                break main;
            }
            int baseCompletion = (ts*100)/numberOfTasks; 
            synchronized(this) {
                updateTask(StringUtil.format("Task #{0}: ", ts));
                updateCompletion(0, baseCompletion);
            }
        
	        int numberOfSubTasks = (int)(Math.random()*20);
	        for(int p=0; p<numberOfSubTasks; p++) {
	            if(isCancelled()) {
	                break main;
	            }
	            int taskCompletion = muldiv(p,100,numberOfSubTasks);
	            int completion = baseCompletion + (taskCompletion/numberOfTasks); 
	            synchronized(this) {
	                updateCompletion(taskCompletion, completion);
	            }
	            System.out.println(getTaskLabel()+", "+taskCompletion+" ["+completion+"]");
	            try {
	                Thread.sleep((long)(Math.random()*300));
	            } catch(InterruptedException ex) {}
	        }
		}	
    }
}
