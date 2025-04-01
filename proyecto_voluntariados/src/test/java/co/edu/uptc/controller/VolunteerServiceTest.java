package co.edu.uptc.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.uptc.model.Volunteer;
import co.edu.uptc.model.Activity;
import co.edu.uptc.persistence.JSONPersistence;

class VolunteerServiceTest {
    private VolunteerService volunteerService;

    @BeforeEach
    void setUp() {
        JSONPersistence.saveVolunteers(List.of()); 
        volunteerService = new VolunteerService();
    }
    //Checks that an adult volunteer is successfully registered and added to the list.
    @Test
    void testRegisterVolunteer_Success() {
        Volunteer volunteer = new Volunteer("John Doe",25,"john@example.com");
        assertTrue(volunteerService.registerVolunteer(volunteer));
        assertEquals(1, volunteerService.getVolunteers().size());
    }
    //Ensures that a minor cannot register, and an exception is thrown with the correct message
    @Test
    void testRegisterVolunteer_FailUnderage() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new Volunteer("Jane Doe", 17, "jane@example.com"); 
    });

    assertEquals("The volunteer must be at least 18 years old.", exception.getMessage());
    }
    //Registers a volunteer and verifies that they can be found by email.
    @Test
    void testFindVolunteerByEmail_Success() {
        Volunteer volunteer = new Volunteer("Alice",22,"alice@example.com");
        volunteerService.registerVolunteer(volunteer);
        assertNotNull(volunteerService.findVolunteerByEmail("alice@example.com"));
    }
    //Checks that a non-existent volunteer cannot be found by email.
    @Test
    void testFindVolunteerByEmail_NotFound() {
        assertNull(volunteerService.findVolunteerByEmail("notfound@example.com"));
    }
    //Registers a volunteer and verifies that they can be found by name.
    @Test
    void testGetVolunteerByName_Success() {
        Volunteer volunteer = new Volunteer("Bob",30, "bob@example.com");
        volunteerService.registerVolunteer(volunteer);
        assertNotNull(volunteerService.getVolunteerByName("Bob"));
    }
    //Checks that a non-existent volunteer cannot be found by name.
    @Test
    void testGetVolunteerByName_NotFound() {
        assertNull(volunteerService.getVolunteerByName("Charlie"));
    }
    //Registers a volunteer to an activity and verifies that they are correctly added to the list.
    @Test
    void testRegisterVolunteerToActivity_Success() {
        Volunteer volunteer = new Volunteer("David", 28,"david@example.com");
        Activity activity = new Activity("Beach Cleanup", "Cleaning the beach", new java.util.Date(), 10) {
            @Override
            public String getType() {
                return "Environmental";
            }
        };
        assertTrue(volunteerService.registerVolunteerToActivity(volunteer, activity));
        assertEquals(1, activity.getRegisteredVolunteers().size());
    }
    //Registers a volunteer in an activity, then removes them, verifying that they are correctly removed from the list
    @Test
    void testCancelVolunteerFromActivity_Success() {
        Volunteer volunteer = new Volunteer("Emma", 26,"emma@example.com");
        Activity activity = new Activity("Tree Planting", "Planting trees in the park", new java.util.Date(), 5) {
            @Override
            public String getType() {
                return "Environmental";
            }
        };
        activity.registerVolunteer(volunteer);
        assertTrue(volunteerService.cancelVolunteerFromActivity(volunteer, activity));
        assertTrue(activity.getRegisteredVolunteers().isEmpty());
    }
}
