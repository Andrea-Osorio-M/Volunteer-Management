package co.edu.uptc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.uptc.model.Activity;
import co.edu.uptc.model.Volunteer;
import co.edu.uptc.persistence.JSONPersistence;

public class VolunteerService {
    private List<Volunteer> volunteers;
    private List<Activity> activities;

    public VolunteerService() {
        try {
            this.volunteers = JSONPersistence.loadVolunteers();
            this.activities = JSONPersistence.loadActivities();
    
            System.out.println("Loaded activities: " + activities.size());
            for (Activity activity : activities) {
                System.out.println("Activity: " + activity.getName() + ", Type: " + activity.getType());
            }
    
        } catch (Exception e) {
            throw new RuntimeException("Error loading data: " + e.getMessage(), e);
        }
    }

    public boolean registerVolunteer(Volunteer volunteer) {
        if (volunteer.getAge() < 18) {
            throw new IllegalArgumentException("The volunteer must be at least 18 years old to register.");
        }
        if (!volunteers.contains(volunteer)) {
            volunteers.add(volunteer);
            JSONPersistence.saveVolunteers(volunteers);
            return true;
        }
        return false;
    }

    public List<Volunteer> getVolunteers() {
        return new ArrayList<>(volunteers);
    }

    public List<Activity> getActivities() {
        return new ArrayList<>(activities);
    }    

    public Volunteer findVolunteerByEmail(String email) {
        return volunteers.stream()
                .filter(v -> v.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Volunteer getVolunteerByName(String name) {
        return volunteers.stream()
                .filter(v -> v.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean cancelVolunteerFromActivity(Volunteer volunteer, Activity activity) {
        if (!activity.getRegisteredVolunteers().contains(volunteer)) {
            throw new IllegalStateException("Volunteer is not registered in this activity.");
        }
        boolean success = activity.cancelRegistration(volunteer);
        if (success) JSONPersistence.saveActivities(activities);
        return success;
    }


    public void generateParticipationReport() {
        JSONPersistence.generateReport(volunteers, activities);
    }

    public boolean removeVolunteerByEmail(String email) {
        Volunteer volunteer = findVolunteerByEmail(email);
        if (volunteer != null) {
            volunteers.remove(volunteer);
            JSONPersistence.saveVolunteers(volunteers);
            return true;
        }
        return false;
    }
    public boolean registerVolunteerToActivity(Volunteer volunteer, Activity activity) {
        if (activity.getRegisteredVolunteers().contains(volunteer)) {
            throw new IllegalStateException("Volunteer is already registered for this activity.");
        }
        if (activity.getRegisteredVolunteers().size() >= activity.getMaxParticipants()) {
            throw new IllegalStateException("Activity is already full.");
        }
    
        // Cargar actividades actuales antes de registrar
        activities = JSONPersistence.loadActivities(); // Asegúrate de cargar las actividades previamente guardadas
    
        boolean success = activity.registerVolunteer(volunteer);
        if (success) {
            // Verifica si la actividad ya está en la lista
            if (!activities.contains(activity)) {
                activities.add(activity); // Si no está, añadirla a la lista
            }
            JSONPersistence.saveActivities(activities); // Guardar la lista de actividades
        }
        return success;
    }
}