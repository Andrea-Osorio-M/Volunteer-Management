package co.edu.uptc.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;


class VirtualActivityTest {
    private VirtualActivity virtualActivity;

    @BeforeEach
    void setUp() {
        virtualActivity = new VirtualActivity("Online Workshop", "A coding workshop", new Date(), 50, "Zoom");
    }

    @Test
    void testGetPlatform() {
        assertEquals("Zoom", virtualActivity.getPlatform(), "Platform should be Zoom");
    }

    @Test
    void testSetPlatform() {
        virtualActivity.setPlatform("Google Meet");
        assertEquals("Google Meet", virtualActivity.getPlatform(), "Platform should be updated to Google Meet");
    }

    @Test
    void testGetType() {
        assertEquals("Virtual", virtualActivity.getType(), "Type should be Virtual");
    }
}
