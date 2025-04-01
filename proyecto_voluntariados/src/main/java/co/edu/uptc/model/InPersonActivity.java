package co.edu.uptc.model;

import java.util.Date;

/**
 * Represents a physical in-person activity.
 */
public class InPersonActivity extends Activity {
    private String location;

    public InPersonActivity(String name, String description, Date date, int maxParticipants, String location, String type) {
        super(name, description, date, maxParticipants);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getType() {
        return "In-Person";
    }
}