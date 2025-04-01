package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents an activity that volunteers can participate in.
 */
public abstract class Activity {
    protected String name;
    protected String description;
    protected Date date;
    protected int maxParticipants;
    protected List<Volunteer> registeredVolunteers;

    /**
     * Constructor for Activity.
     * @param name The name of the activity.
     * @param description A brief description of the activity.
     * @param date The scheduled date of the activity.
     * @param maxParticipants The maximum number of participants allowed.
     */
    public Activity(String name, String description, Date date, int maxParticipants) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.setMaxParticipants(maxParticipants);
        this.registeredVolunteers = new ArrayList<>();
    }

    /**
     * Gets the name of the activity.
     * @return The activity name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the activity.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the activity.
     * @return The activity description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the activity.
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the scheduled date of the activity.
     * @return The date of the activity.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the scheduled date of the activity.
     * @param date The date to set.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the maximum number of participants.
     * @return The max number of participants.
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Sets the maximum number of participants.
     * @param maxParticipants The max number to set (must be greater than 0).
     */
    public void setMaxParticipants(int maxParticipants) {
        if (maxParticipants > 0) {
            this.maxParticipants = maxParticipants;
        } else {
            throw new IllegalArgumentException("The maximum number of participants must be greater than zero.");
        }
    }

    /**
     * Registers a volunteer for the activity.
     * @param volunteer The volunteer to register.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerVolunteer(Volunteer volunteer) {
        if (registeredVolunteers.size() < maxParticipants) {
            return registeredVolunteers.add(volunteer);
        }
        return false;
    }

    /**
     * Cancels a volunteer's registration.
     * @param volunteer The volunteer to remove.
     * @return true if removal is successful, false otherwise.
     */
    public boolean cancelRegistration(Volunteer volunteer) {
        return registeredVolunteers.remove(volunteer);
    }

    /**
     * Gets the list of registered volunteers.
     * @return List of registered volunteers.
     */
    public List<Volunteer> getRegisteredVolunteers() {
        return registeredVolunteers;
    }

    /**
     * Returns a string representation of the activity.
     * @return A formatted string with activity details.
     */
    @Override
    public String toString() {
        return "Activity{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", maxParticipants=" + maxParticipants +
                ", registeredVolunteers=" + registeredVolunteers.size() +
                '}';
    }

    /**
     * Abstract method to define the type of activity.
     * @return The type of activity.
     */

     //Está aquí porque la idea es que cada tipo específico de actividad (por ejemplo, Presencial y Virtual) implemente su propia versión de este método para indicar su tipo.
    public abstract String getType();
}