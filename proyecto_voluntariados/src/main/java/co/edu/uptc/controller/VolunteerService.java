package co.edu.uptc.controller;

import java.io.FileWriter;
import java.io.IOException;
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
        } catch (Exception e) {
            throw new RuntimeException("Error loading data: " + e.getMessage(), e);
        }
    }

    public boolean registerVolunteer(Volunteer volunteer) {
        if (volunteer.getAge() < 18) {
            throw new IllegalArgumentException("The volunteer must be at least 18 years old to register.");
        }
        volunteers.add(volunteer);
        JSONPersistence.saveVolunteers(volunteers);
        return true;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public List<Activity> getActivities() {
        return activities;
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

    public boolean registerVolunteerToActivity(Volunteer volunteer, Activity activity) {
        if (activity.getRegisteredVolunteers().contains(volunteer)) {
            throw new IllegalStateException("Volunteer is already registered for this activity.");
        }
        if (activity.getRegisteredVolunteers().size() >= activity.getMaxParticipants()) {
            throw new IllegalStateException("Activity is already full.");
        }
        boolean success = activity.registerVolunteer(volunteer);
        if (success) JSONPersistence.saveActivities(activities);
        return success;
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
        Map<Volunteer, Long> participationCounts = activities.stream()
            .flatMap(activity -> activity.getRegisteredVolunteers().stream())
            .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        List<Map.Entry<Volunteer, Long>> sortedVolunteers = new ArrayList<>(participationCounts.entrySet());
        sortedVolunteers.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        try (FileWriter writer = new FileWriter("volunteer_report.txt")) {
            for (Map.Entry<Volunteer, Long> entry : sortedVolunteers) {
                writer.write(entry.getKey().getName() + " - " + entry.getValue() + " activities\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}