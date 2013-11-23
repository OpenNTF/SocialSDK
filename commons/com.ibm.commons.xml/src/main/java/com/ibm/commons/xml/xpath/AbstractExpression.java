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

/*
 * Created on May 29, 2005
 * 
 */
package com.ibm.commons.xml.xpath;

import java.util.List;

import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.XResult;


/**
 * @author Mark Wallace
 * @author Philippe Riand
 */
abstract public class AbstractExpression implements XPathExpression {
    
    public static final ProfilerType profilerEval = new ProfilerType("XPath evaluation"); //$NON-NLS-1$
    public static final ProfilerType profilerSetValue = new ProfilerType("XPath set value"); //$NON-NLS-1$
    public static final ProfilerType profilerCreateNodes = new ProfilerType("XPath createNodes"); //$NON-NLS-1$
	
    protected String _expression;
    
    /**
     * Construct an XPathExpression object for the specified expression
     * 
     * @param expression
     */
    protected AbstractExpression(String expression) {
        _expression = expression;
    }

    /* (non-Javadoc)
     * @see com.ibm.xfaces.xpath.XPathExpression#getExpression()
     */
    public String getExpression() {
        return _expression;
    }
    
	/* (non-Javadoc)
	 * @see com.ibm.commons.xml.XPathExpression#getType(java.lang.Object)
	 */
	public Class getType(Object node) throws XPathException {
		XResult result = eval(node, null);
		if (result != null) {
			switch (result.getValueType()) {
			case XResult.TYPE_EMPTY:
				return Void.class;
			case XResult.TYPE_STRING:
				return String.class;
			case XResult.TYPE_BOOLEAN:
				return Boolean.class;
			case XResult.TYPE_NUMBER:
				return Number.class;
			case XResult.TYPE_SINGLENODE:
				return Object.class;
			case XResult.TYPE_MULTIPLENODES:
				return List.class;
			}
		}
		return Void.class;
	}
    
    /**
     * Resolve an namespace context for a particular node.
     * @param node
     * @param ns
     * @return
     */
	public abstract NamespaceContext resolveNamespaceContext(Object node, NamespaceContext ns);

	
	// Profiled methods
    public final XResult eval(Object node, NamespaceContext namespaceContext) throws XPathException {
        if( Profiler.isEnabled() ) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerEval,getExpression());
            long ts = Profiler.getCurrentTime();
            try {
                return doEval(node,namespaceContext);
            } finally {
                Profiler.endProfileBlock(agg,ts);
            }
        } else {
            return doEval(node,namespaceContext);
        }
    }
    public final Object createNodes(Object node, NamespaceContext namespaceContext) throws XPathException {
        if( Profiler.isEnabled() ) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerEval,getExpression());
            long ts = Profiler.getCurrentTime();
            try {
                return doCreateNodes(node,namespaceContext);
            } finally {
                Profiler.endProfileBlock(agg,ts);
            }
        } else {
            return doCreateNodes(node,namespaceContext);
        }
    }
    public final void setValue(Object node, Object value, NamespaceContext namespaceContext, boolean autoCreate) throws XPathException {
        if( Profiler.isEnabled() ) {
            ProfilerAggregator agg = Profiler.startProfileBlock(profilerEval,getExpression());
            long ts = Profiler.getCurrentTime();
            try {
                doSetValue(node,value,namespaceContext,autoCreate);
            } finally {
                Profiler.endProfileBlock(agg,ts);
            }
        } else {
            doSetValue(node,value,namespaceContext,autoCreate);
        }
    }

    protected abstract XResult doEval(Object node, NamespaceContext namespaceContext) throws XPathException;
    protected abstract Object doCreateNodes(Object node, NamespaceContext namespaceContext) throws XPathException;
    protected abstract void doSetValue(Object node, Object value, NamespaceContext namespaceContext, boolean autoCreate) throws XPathException;
}
