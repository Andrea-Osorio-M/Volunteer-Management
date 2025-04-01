package co.edu.uptc.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uptc.controller.VolunteerService;
import co.edu.uptc.model.Activity;
import co.edu.uptc.model.InPersonActivity;
import co.edu.uptc.model.VirtualActivity;
import co.edu.uptc.model.Volunteer;
import co.edu.uptc.persistence.JSONPersistence;

class MainTest {
    private VolunteerService volunteerService;
    private List<Activity> activities;

    @BeforeEach
    void setUp() {
        volunteerService = new VolunteerService();
        activities = new ArrayList<>();
    }

    @Test
    void testRegisterVolunteer() {
        Volunteer volunteer = new Volunteer("John Doe", 25, "john@example.com");
        assertTrue(volunteerService.registerVolunteer(volunteer));
    }

    @Test
    void testCreateActivity() throws Exception {
        String dateStr = "2025-04-01";
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        Activity activity = new InPersonActivity("Clean-Up", "Community clean-up event", date, 10, "City Park");
        activities.add(activity);
        assertEquals(1, activities.size());
    }

    @Test
    void testEnrollVolunteer() {
        Volunteer volunteer = new Volunteer("Alice", 30, "alice@example.com");
        volunteerService.registerVolunteer(volunteer);
        Activity activity = new VirtualActivity("Webinar", "Online learning", new Date(), 5, "Zoom");
        activities.add(activity);

        assertTrue(volunteerService.registerVolunteerToActivity(volunteer, activity));
    }

    @Test
    void testCancelEnrollment() {
        Volunteer volunteer = new Volunteer("Alice", 30, "alice@example.com");
        volunteerService.registerVolunteer(volunteer);
        Activity activity = new VirtualActivity("Webinar", "Online learning", new Date(), 5, "Zoom");
        activities.add(activity);
        volunteerService.registerVolunteerToActivity(volunteer, activity);
        
        assertTrue(volunteerService.cancelVolunteerFromActivity(volunteer, activity));
    }

    @Test
    void testSaveData() {
        Volunteer volunteer = new Volunteer("Bob", 40, "bob@example.com");
        volunteerService.registerVolunteer(volunteer);
        JSONPersistence.saveVolunteers(volunteerService.getVolunteers());
        List<Volunteer> loadedVolunteers = JSONPersistence.loadVolunteers();
        assertFalse(loadedVolunteers.isEmpty());
    }
}

