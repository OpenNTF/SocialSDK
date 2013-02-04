/*
 * © Copyright IBM Corp. 2012-2013
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

package com.ibm.commons.util.profiler;

import com.ibm.commons.util.TDiag;
import com.ibm.commons.util.ThreadMap;


/**
 * This class is used to enable/disable the profiler.
 * <code>
 * if( Profiler.enabled ) {
 *     startProfiling(type,param);
 *     try {
 *         // Do stuff...
 *     } finally {
 *         endProfiling();
 *     }
 * } else {
 *     // Do stuff...
 * }
 * @ibm-api
 */
public class Profiler {

    private static ProfilerAggregator mainAggregator = new ProfilerAggregator(null,new ProfilerType("Root"), null, 0); // $NON-NLS-1$
    private static ThreadMap threadMap = null;

    private static boolean enabled = false;

    private static HighResolutionTimer _timer = null;



    // ============================================================================
    // Profiler public API
    // ============================================================================

    /**
     * Globally enable the profiler.
     * @ibm-api
     */
    public static void enableProfiler( HighResolutionTimer timer ) {
        synchronized(Profiler.class) {
            // If the profiler is already enabled, do nothing
            if(enabled) {
                return;
            }

            _timer = timer;

            switch( _timer.getTimerMode() ) {
                case HighResolutionTimer.CPU_TIME_COUNTER:
                        TDiag.console( "Profiler is measuring CPU time" ); // $NON-NLS-1$
                    break;
                case HighResolutionTimer.WALL_TIME_COUNTER:
                		TDiag.console( "Profiler is measuring wall time" ); // $NON-NLS-1$
                    break;
            }

            // If the profiler is not yet enabled
            enabled = true;
            threadMap = new ThreadMap();
        }
    }

    /**
     * Globally disable the profiler.
     * @ibm-api
     */
    public static void disableProfiler() {
        synchronized(Profiler.class) {
        	if(enabled) {
	    		TDiag.console( "Profiler is disabled" ); // $NON-NLS-1$
	            enabled = false;
	            threadMap = null;
	            _timer = null;
        	}
        }
    }

    /**
     * Profiler start.
     * This method initialize and starts the profiler for the current thread.
     * @ibm-api
     */
    public static boolean startProfiler() {
        if(enabled) {
        	// Only start the profiler if not already started for the current thread.
        	Thread t = Thread.currentThread();
        	if(threadMap.get(t)==null) {
                // Thread Map is already synchronized
                threadMap.put(t,mainAggregator);
                return true;
        	}
        }
        return false;
    }

    /**
     * Profiler end.
     * This method ends the profiler for the current thread.
     * @ibm-api
     */
    public static void endProfiler(boolean started) {
    	if(started) {
	    	// This call avoids a synchronization and a call to get the current thread
	    	if(threadMap.hasItems()) {
	    		// Thread Map is already synchronized
	    		threadMap.remove(Thread.currentThread());
	    	}
    	}
    }
    public static void endProfiler() {
    	endProfiler(true);
    }
    
    /**
     * Check if the profiler is started
     */
    public static boolean isStarted() {
    	return enabled && threadMap.get(Thread.currentThread())!=null;
    }

    /**
     * Check if the profiler is enabled for the current thread
     * @ibm-api
     */
    public static boolean isEnabled() {
        return enabled;
    }
    
    /** 
     * Get the current timer.
     * @ibm-api
     */
    public static HighResolutionTimer getCurrentTimer() {
        return _timer;
    }
    

    /**
     * Method that should be called when a block should be profiled.
     * @ibm-api
     */
    public static ProfilerAggregator startProfileBlock(ProfilerType type, String param) {
    	return startProfileBlock(type, param, 0 );
    }
    
    /**
     * Method that should be called when a block should be profiled.
     * @ibm-api
     */
    public static ProfilerAggregator startProfileBlock(ProfilerType type, String param, int detailLevel) {
        ProfilerAggregator parent = (ProfilerAggregator)threadMap.get(Thread.currentThread());
        if( parent!=null ) {
            // We should look if it already contains an aggregator for that child
            ProfilerAggregator child = parent.get( type, param );
            if( child==null ) {
                synchronized(parent) {
                    child = parent.get( type, param );
                    if( child==null ) {
                        child = new ProfilerAggregator( parent, type, param, detailLevel );
                        parent.add( child );
                    }
                }
            }
            // That aggregator is the new current one
            threadMap.put(Thread.currentThread(),child);
            return child;
        }
        return null;
    }
    
    /**
     * Method that should be called when a block should be profiled.
     * @ibm-api
     */
    public static ProfilerAggregator startProfileBlock(String type, String param) {
        ProfilerType t = ProfilerType.get(type);
        return startProfileBlock(t, param, 0);
    }

    /**
     * Method that should be called at the end of a profiled block.
     * @ibm-api
     */
    public static void endProfileBlock(ProfilerAggregator aggregator, long startTime) {
    	// PHIL: the profiler may have been disabled between the start and the end
        if(aggregator!=null && enabled) {
            aggregator.addInfo(getCurrentTime()-startTime);
            // Maybe the thread map disapeared...
            if( enabled ) {
                // The parent aggregator is the new current one
                threadMap.put(Thread.currentThread(),aggregator.getParent());
            }
        }
    }

    /**
     * Method that should be called to reset the profiler.
     * @ibm-api
     */
    public static void resetProfiler() {
        mainAggregator.reinit();
    }

    /**
     * Method that returns the main aggregator (root).
     * @ibm-api
     */
    public static ProfilerAggregator getMainAggregator() {
        return mainAggregator;
    }

    /**
     * Method that dumps the main aggregator (root).
     * @ibm-api
     */
    public static void dump() {
        mainAggregator.dump();
    }

    /**
     * Method used to execute and profile a Runnable.
     * @ibm-api
     */
    public static void profileRunnable(ProfilerType type, String id, Runnable runnable) {
        if (isEnabled()) {
            ProfilerAggregator agg = Profiler.startProfileBlock(type, id);
            long ts = Profiler.getCurrentTime();
            try {
                runnable.run(); 
            } 
            finally {
                Profiler.endProfileBlock(agg, ts);
            }
        } 
        else {
            runnable.run(); 
        }
    }

    // ========================================================================
    // TIMER ACCESS
    // ========================================================================

    /**
     * Get the current time in micros.
     * @ibm-api
     */
    public static long getCurrentTime() {
        return _timer.getTime();
    }
}
