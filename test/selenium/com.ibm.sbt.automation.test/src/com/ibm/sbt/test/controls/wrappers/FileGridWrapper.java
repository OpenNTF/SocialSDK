package com.ibm.sbt.test.controls.wrappers;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.ibm.sbt.automation.core.test.BaseWrapperTest;
/*
 * � Copyright IBM Corp. 2012
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

/**
 * @author Francis 
 * @date 16 Jul 2013
 */
public class FileGridWrapper extends BaseWrapperTest {

    @Test
    public void testFileGridWrapper() {
        assertTrue("Expected the test to generate a grid in an iframe", checkFileGridWrapper("Social_Files_Controls_File_Grid_Wrapper"));
    }

}
