package co.edu.uptc.model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityTest {
    private Activity activity;
    private Volunteer volunteer;

    @BeforeEach
    void setUp() {
        activity = new Activity("Community Cleanup", "Cleaning public spaces", new Date(), 3) {
            @Override
            public String getType() {
                return "Community Service";
            }
        };
        volunteer = new Volunteer("John Doe",25,"john@example.com");
    }

    @Test
    void testRegisterVolunteer_Success() {
        assertTrue(activity.registerVolunteer(volunteer));
        assertEquals(1, activity.getRegisteredVolunteers().size());
    }

    @Test
    void testRegisterVolunteer_Fail_MaxReached() {
        activity.registerVolunteer(new Volunteer("Alice", 22, "alice@example.com"));
        activity.registerVolunteer(new Volunteer("Bob",30,"bob@example.com"));
        activity.registerVolunteer(new Volunteer("Charlie",27,"charlie@example.com" ));
        assertFalse(activity.registerVolunteer(volunteer));
    }

    @Test
    void testCancelRegistration_Success() {
        activity.registerVolunteer(volunteer);
        assertTrue(activity.cancelRegistration(volunteer));
        assertTrue(activity.getRegisteredVolunteers().isEmpty());
    }

    @Test
    void testCancelRegistration_Fail_NotRegistered() {
        assertFalse(activity.cancelRegistration(volunteer));
    }

    @Test
    void testSetMaxParticipants_Valid() {
        activity.setMaxParticipants(5);
        assertEquals(5, activity.getMaxParticipants());
    }

    @Test
    void testSetMaxParticipants_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> activity.setMaxParticipants(0));
    }
}
