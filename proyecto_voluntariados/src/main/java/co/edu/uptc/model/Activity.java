package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an activity that volunteers can participate in.
 * Abstract class to be extended by specific activity types.
 */
public abstract class Activity {
    protected String name;
    protected String description;
    protected Date date;
    protected int maxParticipants;
    protected List<Volunteer> registeredVolunteers;
    protected String type;

    /**
     * Constructor for Activity.
     * @param name The name of the activity.
     * @param description A brief description of the activity.
     * @param date The scheduled date of the activity.
     * @param maxParticipants The maximum number of participants allowed.
     * @param type The type of activity (e.g., "Cleaning", "Teaching", etc.). 
     */
    public Activity(String name, String description, Date date, int maxParticipants) {
        this.name = name;
        this.description = description;
        this.date = date;
        // Type will be initialized in the subclass constructor
        if (maxParticipants <= 0) {
            throw new IllegalArgumentException("The maximum number of participants must be greater than zero.");
        }
        this.maxParticipants = maxParticipants;
        this.registeredVolunteers = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setMaxParticipants(int maxParticipants) {
        if (maxParticipants <= 0) {
            throw new IllegalArgumentException("The maximum number of participants must be greater than zero.");
        }
        this.maxParticipants = maxParticipants;
    }

    public boolean registerVolunteer(Volunteer volunteer) {
        if (registeredVolunteers.size() >= maxParticipants) {
            return false;
        }
        return registeredVolunteers.add(volunteer);
    }

    public boolean cancelRegistration(Volunteer volunteer) {
        return registeredVolunteers.remove(volunteer);
    }

    public List<Volunteer> getRegisteredVolunteers() {
        return registeredVolunteers;
        }

        @Override
        public String toString() {
        return "Activity{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", date=" + date +
            ", maxParticipants=" + maxParticipants +
            ", type='" + getType() + '\'' +
            ", registeredVolunteers=" + registeredVolunteers.size() +
            '}';
        }
}