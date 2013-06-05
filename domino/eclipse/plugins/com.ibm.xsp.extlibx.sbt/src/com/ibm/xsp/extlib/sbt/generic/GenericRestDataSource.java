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

package com.ibm.xsp.extlib.sbt.generic;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.extlib.sbt.model.RestDataSource;

/**
 * Generic Rest Data Source.
 * @author Philippe Riand
 */
public abstract class GenericRestDataSource extends RestDataSource {

    // Keywords
    private String splitPath;
    private String totalCountPath;
    private String paramFirst;
    private String paramFirstType;
    private String paramCount;

    public GenericRestDataSource() {
    }

    public String getSplitPath() {
        if (null != splitPath) {
            return splitPath;
        }
        ValueBinding valueBinding = getValueBinding("splitPath");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setSplitPath(String splitPath) {
        this.splitPath = splitPath;
    }

    public String getTotalCountPath() {
        if (null != totalCountPath) {
            return totalCountPath;
        }
        ValueBinding valueBinding = getValueBinding("totalCountPath");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setTotalCountPath(String totalCountPath) {
        this.totalCountPath = totalCountPath;
    }

    public String getParamFirst() {
        if (null != paramFirst) {
            return paramFirst;
        }
        ValueBinding valueBinding = getValueBinding("paramFirst");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setParamFirst(String paramFirst) {
        this.paramFirst = paramFirst;
    }

    public String getParamFirstType() {
        if (null != paramFirstType) {
            return paramFirstType;
        }
        ValueBinding valueBinding = getValueBinding("paramFirstType");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setParamFirstType(String paramFirstType) {
        this.paramFirstType = paramFirstType;
    }

    public String getParamCount() {
        if (null != paramCount) {
            return paramCount;
        }
        ValueBinding valueBinding = getValueBinding("paramCount");
        if (valueBinding != null) {
            String value = (String)valueBinding.getValue(getFacesContext());
            return value;
        }
        return null;
    }
    public void setParamCount(String paramCount) {
        this.paramCount = paramCount;
    }
    
    @Override
    public Object saveState(FacesContext context) {
        if (isTransient()) {
            return null;
        }
        Object[] state = new Object[6];
        state[0] = super.saveState(context);
        state[1] = splitPath;
        state[2] = totalCountPath;
        state[3] = paramFirst;
        state[4] = paramFirstType;
        state[5] = paramCount;
        return state;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[])state;
        super.restoreState(context, values[0]);
        splitPath = (String)values[1];
        totalCountPath = (String)values[2];
        paramFirst = (String)values[3];
        paramFirstType = (String)values[4];
        paramCount = (String)values[5];
    }
}
