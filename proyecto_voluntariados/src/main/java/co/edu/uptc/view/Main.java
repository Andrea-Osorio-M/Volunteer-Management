package co.edu.uptc.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import co.edu.uptc.controller.VolunteerService;
import co.edu.uptc.model.Activity;
import co.edu.uptc.model.InPersonActivity;
import co.edu.uptc.model.VirtualActivity;
import co.edu.uptc.model.Volunteer;
import co.edu.uptc.persistence.JSONPersistence;

/**
 * Main class to manage volunteer registration via console.
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);
    static VolunteerService volunteerService = new VolunteerService();
    static List<Activity> activities = new ArrayList<>();
    
        public static void main(String[] args) {
            while (true) {
                System.out.println("\n===== Volunteer Management System =====");
                System.out.println("1. Manage Volunteers");
                System.out.println("2. Manage Activities");
                System.out.println("3. Save & Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        manageVolunteers();
                        break;
                    case 2:
                        manageActivities();
                        break;
                    case 3:
                        saveData();
                        System.out.println("Data saved. Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    
        private static void manageVolunteers() {
            while (true) {
                System.out.println("\n===== Volunteer Management =====");
                System.out.println("1. Register Volunteer");
                System.out.println("2. List Volunteers");
                System.out.println("3. Delete Volunteer");
                System.out.println("4. Back to Main Menu");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        registerVolunteer();
                        break;
                    case 2:
                        listVolunteers();
                        break;
                    case 3:
                        deleteVolunteer();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    
        private static void manageActivities() {
            while (true) {
                System.out.println("\n===== Activity Management =====");
                System.out.println("1. Create Activity");
                System.out.println("2. List Volunteers By Activities");
                System.out.println("3. Enroll Volunteer in Activity");
                System.out.println("4. Cancel Volunteer Enrollment");
                System.out.println("5. Back to Main Menu");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        createActivity();
                        break;
                    case 2:
                        listVolunteersByActivity();
                        break;
                    case 3:
                        enrollVolunteer();
                        break;
                    case 4:
                        cancelEnrollment();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    
        private static void saveData() {
            JSONPersistence.saveVolunteers(volunteerService.getVolunteers());
        JSONPersistence.saveActivities(activities);
    }

    private static void registerVolunteer() {
        System.out.print("Enter volunteer full name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        Volunteer volunteer = new Volunteer(name, age, email);
        if (volunteerService.registerVolunteer(volunteer)) {
            System.out.println("Volunteer registered successfully.");
        } else {
            System.out.println("Registration failed. Volunteer must be at least 18 years old.");
        }
    }

    private static void createActivity() {
        System.out.print("Enter activity name: ");
        String activityName = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.print("Enter max capacity: ");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Select activity type: 1. Presential  2. Virtual");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Activity activity;
        if (type == 1) {
            System.out.print("Enter location: ");
            String location = scanner.nextLine();
            activity = new InPersonActivity(activityName, description, date, maxCapacity, location);
        } else {
            System.out.print("Enter platform: ");
            String platform = scanner.nextLine();
            activity = new VirtualActivity(activityName, description, date, maxCapacity, platform);
        }

        activities.add(activity);
        System.out.println("Activity created successfully.");
    }

    private static void enrollVolunteer() {
        System.out.print("Enter volunteer name: ");
        String name = scanner.nextLine();
        
        Volunteer volunteer = volunteerService.getVolunteerByName(name);
        if (volunteer == null) {
            System.out.println("Volunteer not found.");
            return;
        }

        System.out.println("Available activities:");
        for (int i = 0; i < activities.size(); i++) {
            System.out.println((i + 1) + ". " + activities.get(i).getName());
        }

        System.out.print("Select activity number: ");
        int activityIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (activityIndex < 0 || activityIndex >= activities.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Activity activity = activities.get(activityIndex);
        if (volunteerService.registerVolunteerToActivity(volunteer, activity)) {
            System.out.println("Volunteer enrolled successfully.");
        } else {
            System.out.println("Enrollment failed. Activity may be full or volunteer is already enrolled.");
        }
    }

    private static void cancelEnrollment() {
        System.out.print("Enter volunteer name: ");
        String name = scanner.nextLine();
        
        Volunteer volunteer = volunteerService.getVolunteerByName(name);
        if (volunteer == null) {
            System.out.println("Volunteer not found.");
            return;
        }

        System.out.println("Enrolled activities:");
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getRegisteredVolunteers().contains(volunteer)) {
                System.out.println((i + 1) + ". " + activities.get(i).getName());
            }
        }

        System.out.print("Select activity number to cancel enrollment: ");
        int activityIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (activityIndex < 0 || activityIndex >= activities.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Activity activity = activities.get(activityIndex);
        if (volunteerService.cancelVolunteerFromActivity(volunteer, activity)) {
            System.out.println("Enrollment cancelled successfully.");
        } else {
            System.out.println("Cancellation failed.");
        }
    }

    private static void listVolunteersByActivity() {
        if (activities.isEmpty()) {
            System.out.println("No activities available.");
            return;
        }

        System.out.println("Activities:");
        for (int i = 0; i < activities.size(); i++) {
            System.out.println((i + 1) + ". " + activities.get(i).getName());
        }

        System.out.print("Select activity number to view volunteers: ");
        int activityIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (activityIndex < 0 || activityIndex >= activities.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Activity activity = activities.get(activityIndex);
        List<Volunteer> enrolledVolunteers = activity.getRegisteredVolunteers();

        if (enrolledVolunteers.isEmpty()) {
            System.out.println("No volunteers enrolled in this activity.");
        } else {
            System.out.println("Volunteers enrolled:");
            for (Volunteer v : enrolledVolunteers) {
                System.out.println("- " + v.getName());
            }
        }
    }

    private static void listVolunteers() {
        List<Volunteer> volunteers = volunteerService.getVolunteers();
        
        if (volunteers.isEmpty()) {
            System.out.println("No volunteers registered.");
            return;
        }
    
        System.out.println("\n===== Registered Volunteers =====");
        for (Volunteer v : volunteers) {
            System.out.println("Name: " + v.getName() + " | Age: " + v.getAge() + " | Email: " + v.getEmail());
        }
    }    
    
    private static void deleteVolunteer() {
        System.out.print("Enter the Volunteer Email to delete: ");
        String email = scanner.nextLine();
    
        boolean removed = volunteerService.removeVolunteerByEmail(email);
        
        if (removed) {
            System.out.println("Volunteer removed successfully.");
            saveData(); // Guardar cambios en el JSON
        } else {
            System.out.println("Volunteer not found.");
        }
    }
}
