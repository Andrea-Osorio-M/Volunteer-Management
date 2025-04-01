package co.edu.uptc.persistence;

import co.edu.uptc.model.InPersonActivity;
import co.edu.uptc.model.VirtualActivity;
import co.edu.uptc.model.Volunteer;
import co.edu.uptc.model.Activity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONPersistenceTest {
    private final String VOLUNTEERS_FILE = "volunteers.json";
    private final String ACTIVITIES_FILE = "activities.json";

    @BeforeEach
    void setUp() {
        // Clean up before each test
        new File(VOLUNTEERS_FILE).delete();
        new File(ACTIVITIES_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        new File(VOLUNTEERS_FILE).delete();
        new File(ACTIVITIES_FILE).delete();
    }

    @Test
    void testSaveAndLoadVolunteers() {
        List<Volunteer> volunteers = new ArrayList<>();
        volunteers.add(new Volunteer("John Doe", 25, "john@example.com"));
        volunteers.add(new Volunteer("Jane Smith", 30, "jane@example.com"));

        JSONPersistence.saveVolunteers(volunteers);
        List<Volunteer> loadedVolunteers = JSONPersistence.loadVolunteers();

        assertEquals(2, loadedVolunteers.size());
        assertEquals("John Doe", loadedVolunteers.get(0).getName());
        assertEquals("Jane Smith", loadedVolunteers.get(1).getName());
    }

    @Test
    void testSaveAndLoadActivities() {
        List<Activity> activities = new ArrayList<>();
        activities.add(new InPersonActivity("Tree Planting", "Plant trees in the park", new Date(), 5, "Central Park"));
        activities.add(new VirtualActivity("Online Workshop", "Learn about sustainability", new Date(), 10, "Zoom"));

        JSONPersistence.saveActivities(activities);
        List<Activity> loadedActivities = JSONPersistence.loadActivities();

        assertEquals(2, loadedActivities.size());
        assertEquals("Tree Planting", loadedActivities.get(0).getName());
        assertEquals("Online Workshop", loadedActivities.get(1).getName());
    }

    @Test
    void testLoadVolunteers_WhenFileDoesNotExist() {
        List<Volunteer> volunteers = JSONPersistence.loadVolunteers();
        assertTrue(volunteers.isEmpty());
    }

    @Test
    void testLoadActivities_WhenFileDoesNotExist() {
        List<Activity> activities = JSONPersistence.loadActivities();
        assertTrue(activities.isEmpty());
    }
}
