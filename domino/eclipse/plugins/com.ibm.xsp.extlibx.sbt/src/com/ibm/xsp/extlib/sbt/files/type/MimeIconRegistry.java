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
package com.ibm.xsp.extlib.sbt.files.type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Justin Murray
 * @author Dan O'Connor
 */
public class MimeIconRegistry {
    private static Map<String, String> icons = new HashMap<String, String>();
    private static MimeIconRegistry    instance;
    
    static {
        icons.put("application/pdf", "icon_pdf.png");
        icons.put("application/zip", "icon_zip.png");
        icons.put("application/x-gzip", "icon_zip.png");
        icons.put("application/x-rar-compressed", "icon_zip.png");

        // MS Application MIMEs
        icons.put("application/msword", "icon_document.png");
        icons.put("application/vnd.ms-excel", "icon_spreadsheet.png");
        icons.put("application/vnd.ms-powerpoint", "icon_presentation.png");

        // Open Office Application MIMEs
        icons.put("application/vnd.oasis.opendocument.presentation", "icon_presentation.png");
        icons.put("application/vnd.oasis.opendocument.spreadsheet", "icon_spreadsheet.png");
        icons.put("application/vnd.oasis.opendocument.text", "icon_document.png");

        // Audio MIMEs
        icons.put("audio/mpeg", "icon_audio.png");
        icons.put("audio/x-wav", "icon_audio.png");
        icons.put("audio/x-ms-wma", "icon_audio.png");
        icons.put("audio/basic", "icon_audio.png");
        icons.put("audio/mid", "icon_audio.png");
        icons.put("audio/x-aiff", "icon_audio.png");
        icons.put("audio/x-mpegurl", "icon_audio.png");
        icons.put("audio/x-pn-realaudio", "icon_audio.png");

        // Image MIMEs
        icons.put("image/bmp", "icon_image.png");
        icons.put("image/cis-cod", "icon_image.png");
        icons.put("image/gif", "icon_image.png");
        icons.put("image/ief", "icon_image.png");
        icons.put("image/jpeg", "icon_image.png");
        icons.put("image/pipeg", "icon_image.png");
        icons.put("image/pjpeg", "icon_image.png");
        icons.put("image/png", "icon_image.png");
        icons.put("image/svg+xml", "icon_image.png");
        icons.put("image/tiff", "icon_image.png");
        icons.put("image/x-cmu-raster", "icon_image.png");
        icons.put("image/x-cmx", "icon_image.png");
        icons.put("image/x-icon", "icon_image.png");
        icons.put("image/x-png", "icon_image.png");

        // Text MIMEs
        icons.put("text/css", "icon_text.png");
        icons.put("text/html", "icon_text.png");
        icons.put("text/plain", "icon_text.png");
        icons.put("text/richtext", "icon_text.png");
        icons.put("text/x-vcard", "icon_text.png");

        // Video MIMEs
        icons.put("video/mpeg", "icon_video.png");
        icons.put("video/quicktime", "icon_video.png");
        icons.put("video/x-msvideo", "icon_video.png");
        icons.put("video/x-ms-wmv", "icon_video.png");
        icons.put("application/octet-stream", "icon_video.png"); // WMV file extension
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.HashMap#get(java.lang.Object)
     */
    public String get(String key) {
        if (key == "undefined")
            return "icon_folder.png";
        else if (icons.containsKey(key)) {
            return icons.get(key);
        }
        else
            return "icon_text.png";
    }

    public static MimeIconRegistry getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new MimeIconRegistry();
        return instance;
    }

    private MimeIconRegistry() {
    }
}
