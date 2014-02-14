package com.ibm.sbt.services.client.connections.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the java connections Activities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Vineet Kanwal
 */
public class ActivityServiceGetTest extends BaseActivityServiceTest {

	@Test
	public void testGetMyActivities() throws ActivityServiceException {
		ActivityList activities = activityService.getMyActivities();
		for (Activity activity : activities) {
			assertEquals("Frank Adams", activity.getAuthor().getName());
		}
	}

	@Test
	public void testGetCompletedActivities() throws ActivityServiceException {
		ActivityList activities = activityService.getCompletedActivities();
		for (Activity activity : activities) {
			assertTrue(activity.getCategoryFlagCompleted()
					.contains("Completed"));
		}
	}

	@Test
	public void testGetAllActivities() throws ActivityServiceException {
		ActivityList activities = activityService.getAllActivities();
		for (Activity activity : activities) {
			assertNotNull(activity.getTitle());
		}
	}

	@Test
	public void testGetAllTodos() throws ActivityServiceException {
		ActivityList activities = activityService.getAllTodos();
		for (Activity activity : activities) {
			assertEquals("todo", activity.getEntryType());
		}
	}

	@Test
	public void testGetAllTags() throws ActivityServiceException {
		TagList tags = activityService.getAllTags();
		for (Tag tag : tags) {
			assertNotNull(tag.getTerm());
		}
	}

	@Test
	public void testGetActivitiesInTrash() throws ActivityServiceException {
		ActivityList activities = activityService.getActivitiesInTrash();
		for (Activity activity : activities) {
			assertNotNull(activity.getTitle());
		}
	}
}
