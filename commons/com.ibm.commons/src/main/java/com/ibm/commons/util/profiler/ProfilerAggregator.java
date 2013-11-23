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

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.TDiag;


/**
 * Profiler aggregator.
 * @ibm-api
 */
public final class ProfilerAggregator {

    private static int idCounter = 0;

    private int id;
    private ProfilerAggregator parent;
    private List<ProfilerAggregator> children;
    private ProfilerType type;
    private String param;
    private int detailLevel;
    private int count;
    private long minTime = 0;
    private long maxTime = 0;
    private long totalTime;
    private long childrenTime;

    public ProfilerAggregator(ProfilerAggregator parent, ProfilerType type, String param, int detailLevel) {
        synchronized(getClass()) {
            this.id = idCounter++;
        }
        this.parent = parent;
        this.children = new ArrayList<ProfilerAggregator>();
        this.type = type;
        this.param = param;
        this.detailLevel = detailLevel;
    }

    public int getDetailLevel() {
        return detailLevel;
    }

    // Maybe we should enhance that method by providing an object that compares better
    ProfilerAggregator get(ProfilerType type, String param) {
        int childCount = children.size();
        for( int i=0; i<childCount; i++ ) {
            ProfilerAggregator pa = children.get(i);
            if( pa.type==type ) {
                if( StringUtil.equals(param, pa.getParam()) ) {
                    return pa;
                }
            }
        }
        return null;
    }

    void add(ProfilerAggregator child) {
        children.add(child);
    }

    ProfilerAggregator getParent() {
        return parent;
    }

    // Only called for a main object
    // Time related attributes are not used here
    void reinit() {
        this.children.clear();
    }

    synchronized void addInfo( long time ) {
        //System.out.println("Aggregator: "+time+", "+getType().toString()+", "+param); // $NLS-ProfilerAggregator.Aggregator-1$
        if( count>0 ) {
            if( time<minTime ) minTime = time;
            if( time>maxTime ) maxTime = time;
        } else {
            minTime = time;
            maxTime = time;
        }
        count++;
        totalTime += time;
        parent.childrenTime += time;
        //System.out.println("Aggregator: Count:"+count+", "+getType().toString()+", "+param);
    }

    // This is used by the profiler application to temporarily aggregate the
    // similar aggregator into a single one. For example, we are aggregating all
    // the calls to the same JS expression so we have the whole total for this
    // expression.
    public void merge( ProfilerAggregator aggregator ) {
        if(aggregator!=null && aggregator.count>0) {
            if( this.count>0 ) {
                if( aggregator.minTime<minTime ) {
                    this.minTime = aggregator.minTime;
                }
                if( aggregator.maxTime>maxTime ) {
                    this.maxTime = aggregator.maxTime;
                }
            } else {
                this.minTime = aggregator.minTime;
                this.maxTime = aggregator.maxTime;
            }
            this.count += aggregator.count;
//            if(StringUtil.equals(param, "/xpddb.nsf/allDocuments.xsp")) {
//                System.out.println("Merging count for alldocuments is now: "+count);
//            }
            this.totalTime += aggregator.totalTime;
            this.childrenTime += aggregator.childrenTime;
        }
    }

    public boolean isRootAggregator() {
        return parent==null;
    }

    public int getId() {
        return id;
    }

    public ProfilerType getType() {
        return type;
    }

    public String getParam() {
        return param;
    }

    public int getChildCount() {
        return children.size();
    }

    public ProfilerAggregator getChild( int index ) {
        return children.get(index);
    }

    public int getCount() {
        return count;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getChildrenTime() {
        return childrenTime;
    }

    public long getAvgTime() {
        return count>0 ? totalTime/count : 0;
    }

    // ======================================================================
    // Debug dump
    // ======================================================================

    public void dump() {
        synchronized(TDiag.getSyncObject()) {
            dump(this,0);
        }
    }

    private static void dump(ProfilerAggregator a, int level) {
        for( int i=0; i<level; i++ ) {
            Platform.getInstance().getOutputStream().print( "    " );
        }
        if( a.getParam()!=null ) {
            String str = a.getParam();
            if( str.length()>128 ) {
                str = str.substring( 0, 128 ) + "...";
            }
            Platform.getInstance().getOutputStream().print(a.getType()+"["+str+"], "); // To Javastring???
        } else {
            Platform.getInstance().getOutputStream().print(a.getType()+", ");
        }
        if( a.getCount()>0 ) {
            Platform.getInstance().getOutputStream().println("Count="+a.getCount()+", total="+a.getTotalTime()+", average="+a.getAvgTime()+", minimum="+a.getMinTime()+", maximum="+a.getMaxTime()); // $NLS-ProfilerAggregator.count-1$ $NLS-ProfilerAggregator.total-2$ $NLS-ProfilerAggregator.avg-3$ $NLS-ProfilerAggregator.min-4$ $NLS-ProfilerAggregator.max-5$
        } else {
            Platform.getInstance().getOutputStream().println("Count="+a.getCount()); // $NLS-ProfilerAggregator.count.1-1$
        }
        for( int i=0; i<a.getChildCount(); i++ ) {
            dump( a.getChild(i), level+1 );
        }
    }
}