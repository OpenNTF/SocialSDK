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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.xsp.module.nsf.ThreadSessionExecutor;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.std.ObjectObject;
import com.ibm.jscript.types.FBSBoolean;
import com.ibm.jscript.types.FBSNull;
import com.ibm.jscript.types.FBSNumber;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 * Base class for application related asynchronous actions.
 */
public abstract class AsyncAction {
	
	private static final String ASYNC_KEY	= "_asyncAction"; 
	
	@SuppressWarnings("unchecked")
	public static synchronized Map<String,AsyncAction> getActions() {
		Map<String,AsyncAction> map = (Map<String,AsyncAction>)ExtLibUtil.getApplicationScope().get(ASYNC_KEY);
		if(map==null) {
			map = new HashMap<String,AsyncAction>();
			ExtLibUtil.getApplicationScope().put(ASYNC_KEY,map);
		}
		return map;
	}

	public static synchronized void registerAction(String actionId, AsyncAction action) {
		getActions().put(actionId,action);
	}

	public static synchronized boolean actionExists(String actionId) {
		AsyncAction action = getActions().get(actionId);
		return action!=null;
	}

	public static synchronized AsyncAction getAction(String actionId) {
		AsyncAction action = getActions().get(actionId);
		return action;
	}

	public static synchronized boolean isActionRunning(String actionId) {
		AsyncAction action = getActions().get(actionId);
		return action!=null && action.isRunning();
	}

	public static synchronized boolean isActionCancelled(String actionId) {
		AsyncAction action = getActions().get(actionId);
		return action!=null && action.isCancelled();
	}

	public static synchronized boolean cancel(String actionId) {
		AsyncAction action = getActions().get(actionId);
		if(action!=null) {
			action.cancelAction();
			return true;
		}
		return false;
	}
	
	public static boolean runAction(String actionId) {
		return runAction(actionId, null);
	}
	public static synchronized boolean runAction(String actionId, Object parameters) {
		AsyncAction action = getActions().get(actionId);
		if(action!=null) {
			return runAction(action,parameters);
		}
		return false;
	}
	
	public static synchronized boolean runAction(final AsyncAction action, final Object parameters) {
		// Do not start if it is already running
		if(action!=null && action.isRunning()) {
			return false;
		}
		
		ThreadSessionExecutor<IStatus> executor = new ThreadSessionExecutor<IStatus>() {
            @Override
            protected IStatus run(Session session) throws NotesException {
                //testSecurity("c:\\threadexec2.txt");
            	action.running = true;
            	action.cancelled = false;
            	action.runCount++;
            	try {
                	action.run(session,parameters);
                    return action.isCancelled() ? Status.CANCEL_STATUS : Status.OK_STATUS;
            	} finally {
                	action.running = false;
//                	/runningJobs.remove(jobId);
            	}
            }
        };
		final InternalEclipseJob newJob = new InternalEclipseJob(action,executor);
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
            	newJob.schedule();
        		return null;
            }
        });
		return true;
	}
	
	public static synchronized FBSValue getJobProgressJavaScript(String jobId) {
		try {
			AsyncAction action = getActions().get(jobId);
			if(action!=null) {
				synchronized(action) {
					ObjectObject o = new ObjectObject();
					o.put("exists", FBSBoolean.TRUE);
					o.put("running", FBSUtility.wrap(action.isRunning()));
					o.put("runCount", FBSUtility.wrap(action.getRunCount()));
					o.put("statusTs", FBSUtility.wrap(action.getStatusTs()));
					o.put("cancelled", FBSUtility.wrap(action.isCancelled()));
					o.put("label", FBSUtility.wrap(action.getActionLabel()));
					o.put("taskLabel", FBSUtility.wrap(action.getTaskLabel()));
					o.put("taskCompletion", FBSUtility.wrap(action.getTaskCompletion()));
					o.put("fullCompletion", FBSUtility.wrap(action.getTotalCompletion()));
					return o;
				}
			} else {
				ObjectObject o = new ObjectObject();
				o.put("exists", FBSBoolean.FALSE);
				o.put("running", FBSBoolean.FALSE);
				o.put("runCount", FBSNumber.Zero);
				o.put("cancelled", FBSBoolean.FALSE);
				o.put("statusTs", FBSNumber.Zero);
				o.put("label", FBSString.emptyString);
				o.put("taskLabel", FBSString.emptyString);
				o.put("taskCompletion", FBSNumber.Zero);
				o.put("fullCompletion", FBSNumber.Zero);
				return o;
			}
		} catch(InterpretException ex) {
			Platform.getInstance().log(ex);
		}
		return FBSNull.nullValue;
	}
	
	private static final class InternalEclipseJob extends Job {
	
		private AsyncAction action;
        private ThreadSessionExecutor<IStatus> executor;
		
		public InternalEclipseJob(final AsyncAction action, ThreadSessionExecutor<IStatus> executor) {
			super(action.getActionLabel());
			this.action = action;
			this.executor = executor;
		}
		protected final IStatus run(IProgressMonitor monitor) {
            try {
            	return executor.run();
            } catch(Exception ex) {
                return Status.CANCEL_STATUS;
            }
		}
	};
	
	private boolean running;
	private boolean cancelled;
    private String task;
	private int taskCompletion;
	private int completion;
	private int runCount;
	private long statusTs;
	
	public AsyncAction() {
	}
	public boolean isRunning() {
		return running;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public long getStatusTs() {
		return statusTs;
	}
	public void cancelAction() {
		this.cancelled = true;
		this.completion = 100;
		this.taskCompletion = 0;	
		this.task = "Action cancelled by user";
		this.statusTs = System.currentTimeMillis();
	}
	public String getTaskLabel() {
		return task;
	}
	public int getTaskCompletion() {
		return taskCompletion;
	}
	public int getTotalCompletion() {
		return completion;
	}
	public int getRunCount() {
		return runCount;
	}
	public synchronized void updateTask(String msg, Object...params) {
		if(!cancelled) {
			this.task = StringUtil.format(msg,params);
			this.taskCompletion = 0;	
			this.statusTs = System.currentTimeMillis();
		}
	}
	public synchronized void updateCompletion(int task, int completion) {
		if(!cancelled) {
			this.taskCompletion = task;
			this.completion = completion;
			this.statusTs = System.currentTimeMillis();
		}
	}
	public synchronized void updateException(Throwable t) {
		this.task = t.getMessage();
		this.completion = 100;
		this.taskCompletion = 0;	
		this.cancelled = true;
		this.statusTs = System.currentTimeMillis();
	}
	public synchronized void updateException(Throwable t, String msg, Object...params) {
		this.task = StringUtil.format(msg,params)+"\n"+t.getMessage();
		this.completion = 100;
		this.taskCompletion = 0;	
		this.cancelled = true;
		this.statusTs = System.currentTimeMillis();
	}

    // Helpers...
    public static int muldiv(int a, int b, int div) {
		return (int)((long)a*(long)b/(long)div);
	}

    
    //
    // Required methods
    //
    public abstract String getActionLabel();
    public abstract void run(Session session, Object parameters) throws NotesException;
	
    
	// Quick security manager test
	protected void testSecurity(String fileName) {
        File f = new File(fileName);
        try {
            //SecurityManager sm = System.getSecurityManager();
            FileWriter fw = new FileWriter(f);
            fw.write("TEST WRITING A FILE, "+(new Date()).toString());
            fw.close();
        } catch(IOException io) {io.printStackTrace();}
	}
}
