package co.edu.uptc.controller;

import java.util.List;

import co.edu.uptc.model.Activity;
import co.edu.uptc.model.Volunteer;
import co.edu.uptc.persistence.JSONPersistence;

/**
 * Service class for managing volunteers.
 */
public class VolunteerService {
    private List<Volunteer> volunteers;

    /**
     * Constructor to initialize the volunteer list from JSON.
     */
    public VolunteerService() {
        this.volunteers = JSONPersistence.loadVolunteers();
    }

    /**
     * Registers a new volunteer if they meet the age requirement.
     * @param volunteer The volunteer to register.
     * @return True if registration is successful, false otherwise.
     */
    public boolean registerVolunteer(Volunteer volunteer) {
        if (volunteer.getAge() >= 18) {
            volunteers.add(volunteer);
            JSONPersistence.saveVolunteers(volunteers);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of registered volunteers.
     * @return List of volunteers.
     */
    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    /**
     * Finds a volunteer by email.
     * @param email The email to search for.
     * @return The found volunteer or null if not found.
     */
    public Volunteer findVolunteerByEmail(String email) {
        for (Volunteer v : volunteers) {
            if (v.getEmail().equalsIgnoreCase(email)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Finds a volunteer by name.
     * @param name The name to search for.
     * @return The found volunteer or null if not found.
     */
    public Volunteer getVolunteerByName(String name) {
        for (Volunteer v : volunteers) {
            if (v.getName().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Enrolls a volunteer in an activity if there is available space.
     * @param volunteer The volunteer to enroll.
     * @param activity The activity to enroll in.
     * @return True if enrollment is successful, false otherwise.
     */
    public boolean registerVolunteerToActivity(Volunteer volunteer, Activity activity) {
        if (activity.getRegisteredVolunteers().contains(volunteer)) {
            System.out.println("Volunteer is already registered for this activity.");
            return false;
        }
        return activity.registerVolunteer(volunteer);
    }

    /**
     * Removes a volunteer from an activity.
     * @param volunteer The volunteer to remove.
     * @param activity The activity to remove from.
     * @return True if removal is successful, false otherwise.
     */
    public boolean cancelVolunteerFromActivity(Volunteer volunteer, Activity activity) {
        if (!activity.getRegisteredVolunteers().contains(volunteer)) {
            System.out.println("Volunteer is not registered in this activity.");
            return false;
        }
        return activity.cancelRegistration(volunteer);
    }
}
