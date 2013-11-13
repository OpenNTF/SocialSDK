/**
 * 
 */
package com.ibm.sbt.test.js.connections.communities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseAuthServiceTest;

/**
 * @author mwallace
 *
 */
public class GetCommunityForumTopics extends BaseAuthServiceTest {

    @Test
    public void testNoError() {
        boolean result = checkNoError("Social_Communities_Get_Community_Forum_Topics");
        assertTrue(getNoErrorMsg(), result);
    }

}
