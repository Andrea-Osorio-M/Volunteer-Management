package co.edu.uptc.model;

import java.util.Date;

/**
 * Represents an online virtual activity.
 */
public class VirtualActivity extends Activity {
    private String platform;

    public VirtualActivity(String name, String description, Date date, int maxParticipants, String platform, String type) {
        super(name, description, date, maxParticipants);
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String getType() {
        return "Virtual";
    }
}