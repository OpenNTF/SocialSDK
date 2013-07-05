/*
 * © Copyright IBM Corp. 2011
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
package com.ibm.xsp.extlib.sbt.files;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.tree.impl.BasicLeafTreeNode;
import com.ibm.xsp.extlib.tree.impl.TreeUtil;
import com.ibm.xsp.util.FacesUtil;

/**
 * @author doconnor
 *
 */
public class FilesBreadCrumbNode implements ITreeNode {

    /**
     * 
     */
    private static final long serialVersionUID = -2216826908869792311L;

    private NodeContext context;

    /**
     * 
     */

    public String getHref() {
        return null;
    }

    public String getImage() {
        return null;
    }

    public String getImageAlt() {
        return null;
    }

    public String getImageHeight() {
        return null;
    }

    public String getImageWidth() {
        return null;
    }

    public String getLabel() {
        if(getNodeContext() != null){
            
            NodeContext ctx = getNodeContext();
            if(ctx.isFirstNode()){
                return "Root";
            }
            FileServiceData ds = getDataSource();
            if(ds != null){
                return ds.getCurrentDirectory();
            }
            
        }
        return "Test";
    }
    
    private FileServiceData getDataSource(){
        Object ds = FacesUtil.resolveVariable(FacesContext.getCurrentInstance(), "fileServiceData1.DATASOURCE");
        if(ds instanceof FileServiceData){
            FileServiceData data = (FileServiceData)ds;
            return data;
        }
        return null;
    }

    public NodeContext getNodeContext() {
        return context;
    }

    public String getOnClick() {
        return null;
    }

    public String getRole() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public String getStyle() {
        return "font-weight:bold;color:#ffffff;";
    }

    public String getStyleClass() {
        return null;
    }

    public String getSubmitValue() {
        return "";
    }

    public int getType() {
        return ITreeNode.NODE_NODELIST;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isExpanded() {
        FileServiceData ds = getDataSource();
        if(ds != null){
            return !ds.isRootDirectory();
        }
        return false;
    }

    public boolean isRendered() {
        return true;
    }

    public boolean isSelected() {
        FileServiceData ds = getDataSource();
        if(ds != null){
            return !ds.isRootDirectory();
        }
        return false;
    }

    public NodeIterator iterateChildren(int arg0, int arg1) {
        FileServiceData data = getDataSource();
        if(data != null){
            List<ITreeNode> children = new ArrayList<ITreeNode>();
            if(!data.isRootDirectory()){
                BasicLeafTreeNode node = new BasicLeafTreeNode();
                node.setLabel("Root");
                node.setSubmitValue("/");
                node.setSelected(true);
                node.setExpanded(true);
                node.setNodeContext(getNodeContext());
                children.add(node);
                
                String name = data.getCurrentDirectory();
                if(StringUtil.isNotEmpty(name)){
                    String[] parts = name.split("/");
                    if(parts != null){
                        StringBuffer path = new StringBuffer();
                        for(String part : parts){
                            if(StringUtil.isEmpty(part)){
                                continue;
                            }
                            node = new BasicLeafTreeNode();
                            try {
                                //Present a readable label in the bread crumb label
                                node.setLabel(URLDecoder.decode(part, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            path.append("/");
                            path.append(part);
                            node.setSubmitValue(path.toString());
                            node.setSelected(true);
                            node.setExpanded(true);
                            node.setNodeContext(getNodeContext());
                            children.add(node);
                        }
                    }
                }
            }else{
                BasicLeafTreeNode node = new BasicLeafTreeNode();
                node.setLabel("Root");
                children.add(node);
            }
            return TreeUtil.getIterator(children, 0, children.size());
        }
        return null;
    }

    public void setNodeContext(NodeContext context) {
        this.context = context;
        
    }

}
