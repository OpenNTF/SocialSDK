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
package com.ibm.xsp.extlib.sbt.files;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.FacesExceptionEx;
import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.extlib.model.AbstractViewRowData;
import com.ibm.xsp.http.IUploadedFile;
import com.ibm.xsp.util.URLEncoding;

/**
 * @author Justin Murray
 * @author doconnor
 */
public class FileEntry extends AbstractViewRowData {

    private static final long serialVersionUID = 1L;

    private String            _authorEmail;
    private String            _authorName;
    private int               _bytes;
    private String            _description;
    private IUploadedFile     _file;
    private String            _fileId;
    private String            _href;
    private String            _icon;
    private String            _iconHref;
    private boolean           _isDirectory;
    private String            _mimeType;
    private String            _path;
    private String            _proxyURL;
    private Date              _published;
    private String            _repository;
    private String            _size;
    private String            _tag;
    private String            _title;
    private String            _uniqueId;
    private Date              _updated;
    private String            _userId;
    private String            _version;
    private String            _visibility;

    public FileEntry() {

    }

    public FileEntry(String title) {
        _title = title;
    }

    public String getAuthorEmail() {
        return _authorEmail;
    }

    public String getAuthorName() {
        return _authorName;
    }

    public int getBytes() {
        return _bytes;
    }

    @Override
    public Object getColumnValue(String name) {
        if (name != null) {
            if (name.equals("title")) {
                return getTitle();
            }
            if (name.equals("href")) {
                return getHref();
            }
            if (name.equals("path")) {
                String path = getPath();
                try {
                    path = URLDecoder.decode(path, "UTF-8");
                    if(StringUtil.isNotEmpty(path)){
                        if(path.startsWith("/")){
                            path = path.substring(1);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new FacesExceptionEx(e, "Filed to decode file path for column ({0})", path);
                }
                return path;
            }
            if (name.equals("description")) {
                return getDescription();
            }
            if (name.equals("updated")) {
                return getUpdated();
            }
            if (name.equals("published")) {
                return getPublished();
            }
            if (name.equals("version")) {
                return getVersion();
            }
            if (name.equals("visibility")) {
                return getVisibility();
            }
            if (name.equals("authorName")) {
                return getAuthorName();
            }
            if (name.equals("authorEmail")) {
                return getAuthorEmail();
            }
            if (name.equals("size")) {
                return getSize();
            }
        }
        return null;
    }

    public String getDescription() {
        return _description;
    }

    public IUploadedFile getFile() {
        return _file;
    }

    public String getFileId() {
        return _fileId;
    }

    public String getHref() {
        return _href;
    }

    public String getIcon() {
        return _icon;
    }

    public String getIconHref() {
        return _iconHref;
    }

    public String getMimeType() {
        return _mimeType;
    }

    public String getPath() {
        try {
            return URLEncoding.encodeURIString(_path, null, 0, false);
        } catch (UnsupportedEncodingException e) {
            throw new FacesExceptionEx(e, "Failed to encode url, {0}", _path);
        } catch (IOException e) {
            throw new FacesExceptionEx(e, "Failed to encode url, {0}", _path);
        }
    }

    public String getProxyURL() {
        return _proxyURL;
    }

    public Date getPublished() {
        return _published;
    }

    /**
    * @return the _repository
    */
    public String getRepository() {
        return _repository;
    }

    public String getSize() {
        return _size;
    }

    public String getTag() {
        return _tag;
    }

    public String getTitle() {
        return _title;
    }

    public String getUniqueId() {
        return _uniqueId;
    }

    public Date getUpdated() {
        return _updated;
    }

    public String getUserId() {
        return _userId;
    }

    public String getVersion() {
        return _version;
    }

    public String getVisibility() {
        return _visibility;
    }

    public boolean isDirectory() {
        return _isDirectory;
    }

    @Override
    public boolean isReadOnly(String name) {
        return false;
    }

    public void save() {
        // do something!
    }

    public void setAuthorEmail(String authorEmail) {
        _authorEmail = authorEmail;
    }

    public void setAuthorName(String authorName) {
        _authorName = authorName;
    }

    public void setBytes(int bytes) {
        _bytes = bytes;
    }

    @Override
    public void setColumnValue(String name, Object value) {
        if (StringUtil.equals(name, "file")) {
            if (value instanceof UploadedFile) {
                setFile(((UploadedFile) value).getUploadedFile());
            }
        }
        if (StringUtil.equals(name, "description")) {
            setDescription((String) value);
        }
    }

    public void setDescription(String description) {
        _description = description;
    }

    public void setFile(IUploadedFile file) {
        _file = file;
    }

    public void setFileId(String fileId) {
        _fileId = fileId;
    }

    public void setHref(String href) {
        _href = href;
    }

    public void setIcon(String icon) {
        _icon = icon;
    }

    public void setIconHref(String iconHref) {
        _iconHref = iconHref;
    }

    public void setIsDirectory(boolean isDirectory) {
        _isDirectory = isDirectory;
    }

    public void setMimeType(String mimeType) {
        _mimeType = mimeType;
    }

    public void setPath(String path) {
        _path = path;
    }

    public void setProxyURL(String url) {
        _proxyURL = url;
    }

    public void setPublished(Date published) {
        _published = published;
    }

    /**
    * @param _repository the _repository to set
    */
    public void setRepository(String repository) {
        this._repository = repository;
    }

    public void setSize(String size) {
        _size = size;
    }

    public void setTag(String tag) {
        _tag = tag;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public void setUniqueId(String uniqueId) {
        _uniqueId = uniqueId;
    }

    public void setUpdated(Date updated) {
        _updated = updated;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public void setVersion(String version) {
        _version = version;
    }

    public void setVisibility(String visibility) {
        _visibility = visibility;
    }

}
