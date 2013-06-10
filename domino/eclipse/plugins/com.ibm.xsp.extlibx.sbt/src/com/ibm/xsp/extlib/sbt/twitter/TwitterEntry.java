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
package com.ibm.xsp.extlib.sbt.twitter;

import java.util.Date;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.model.AbstractViewRowData;


/**
 * @author doconnor
 * @author tony.mcguckin@ie.ibm.com
 *
 */
public class TwitterEntry extends AbstractViewRowData {
    private String author;
    private String authorLink;
    private String tweetLink;
    private String title;
    private String content;
    private String publishedDate;
    private String updatedDate;
    private String image;
    
    /**
     * 
     */
    private static final long serialVersionUID = -8052413816236884987L;

    public TwitterEntry(){
        
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.model.AbstractViewRowData#getColumnValue(java.lang.String)
     */
    @Override
    public Object getColumnValue(String name) {
        if(StringUtil.equals("author", name)){
            return getAuthor();
        }
        if(StringUtil.equals("authorLink", name)){
            return getAuthorLink();
        }
        if(StringUtil.equals("tweetLink", name)){
            return getTweetLink();
        }
        if(StringUtil.equals("title", name)){
            return getTitle();
        }
        if(StringUtil.equals("content", name)){
            return getContent();
        }
        if(StringUtil.equals("publishedDate", name)){
            return getPublishedDate();
        }
        if(StringUtil.equals("updatedDate", name)){
            return getUpdatedDate();
        }
        if(StringUtil.equals("image", name)){
            return getImage();
        }
        return null;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the authorLink
     */
    public String getAuthorLink() {
        return authorLink;
    }

    /**
     * @param set the authorLink
     */
    public void setAuthorLink(String authorLink) {
        this.authorLink = authorLink;
    }

    /**
     * @return the tweetLink
     */
    public String getTweetLink() {
        return tweetLink;
    }

    /**
     * @param set the tweetLink
     */
    public void setTweetLink(String tweetLink) {
        this.tweetLink = tweetLink;
    }

    /**
     * @return the content
     */
    @Deprecated
    public String getTweetContent() {
        return getContent();
    }

    /**
     * @param set the content
     */
    @Deprecated
    public void setTweetContent(String content) {
        setContent(content);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param set the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param set the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the publishedDate
     */
    @Deprecated
    public Date getTweetDate() {
        return null;
    }

    /**
     * @param set the publishedDate
     */
    @Deprecated
    public void setTweetDate(Date tweetDate) {
        // do nothing
    }

    /**
     * @return the publishedDate
     */
    public String getPublishedDate() {
        return publishedDate;
    }

    /**
     * @param set the publishedDate
     */
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    /**
     * @return the updatedDate
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param set the updatedDate
     */
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param set the image
     */
    public void setImage(String image) {
        this.image = image;
    }
}
