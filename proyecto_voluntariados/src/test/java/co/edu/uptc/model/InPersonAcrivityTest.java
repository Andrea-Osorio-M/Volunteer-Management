package co.edu.uptc.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class InPersonActivityTest {

    @Test
    void testConstructorAndGetters() {
        Date date = new Date();
        InPersonActivity activity = new InPersonActivity("Tree Planting", "Plant trees in the park", date, 5, "Central Park");
        
        assertEquals("Tree Planting", activity.getName());
        assertEquals("Plant trees in the park", activity.getDescription());
        assertEquals(date, activity.getDate());
        assertEquals(5, activity.getMaxParticipants());
        assertEquals("Central Park", activity.getLocation());
        assertEquals("In-Person", activity.getType());
    }

    @Test
    void testSetLocation() {
        InPersonActivity activity = new InPersonActivity("Beach Cleanup", "Clean up the beach", new Date(), 10, "Main Beach");
        activity.setLocation("New Beach Location");
        assertEquals("New Beach Location", activity.getLocation());
    }
}
