package co.edu.uptc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class VolunteerTest {

    @Test
    void testVolunteerConstructor() {
        Volunteer volunteer = new Volunteer("John Doe", 25, "john@example.com");
        assertEquals("John Doe", volunteer.getName());
        assertEquals(25, volunteer.getAge());
        assertEquals("john@example.com", volunteer.getEmail());
    }

    @Test
    void testSetName() {
        Volunteer volunteer = new Volunteer();
        volunteer.setName("Alice");
        assertEquals("Alice", volunteer.getName());
    }

    @Test
    void testSetAgeValid() {
        Volunteer volunteer = new Volunteer();
        volunteer.setAge(20);
        assertEquals(20, volunteer.getAge());
    }

    @Test
    void testSetAgeInvalid() {
        Volunteer volunteer = new Volunteer();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            volunteer.setAge(17);
        });
        assertEquals("The volunteer must be at least 18 years old.", exception.getMessage());
    }

    @Test
    void testSetEmail() {
        Volunteer volunteer = new Volunteer();
        volunteer.setEmail("alice@example.com");
        assertEquals("alice@example.com", volunteer.getEmail());
    }

    @Test
    void testToString() {
        Volunteer volunteer = new Volunteer("Jane Doe", 30, "jane@example.com");
        String expected = "Volunteer{name='Jane Doe', age=30, email='jane@example.com'}";
        assertEquals(expected, volunteer.toString());
    }
}

