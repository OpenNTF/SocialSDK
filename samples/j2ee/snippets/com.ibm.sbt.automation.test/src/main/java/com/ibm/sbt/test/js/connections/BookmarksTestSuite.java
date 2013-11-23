/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.connections.bookmarks.api.CreateBookmark;
import com.ibm.sbt.test.js.connections.bookmarks.api.DeleteBookmark;
import com.ibm.sbt.test.js.connections.bookmarks.api.GetAllBookmarks;
import com.ibm.sbt.test.js.connections.bookmarks.api.GetBookmark;
import com.ibm.sbt.test.js.connections.bookmarks.api.GetBookmarksTags;
import com.ibm.sbt.test.js.connections.bookmarks.api.UpdateBookmark;


/**
 * @author rajmeetbal
 * 
 * @date 30 Oct 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CreateBookmark.class,
	GetBookmark.class,
	GetAllBookmarks.class,
	GetBookmarksTags.class,
	UpdateBookmark.class,
	DeleteBookmark.class,
})
public class BookmarksTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
