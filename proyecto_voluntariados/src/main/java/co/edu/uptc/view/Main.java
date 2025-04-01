package co.edu.uptc.view;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private static final Scanner scanner = new Scanner(System.in);
    private static final VolunteerService volunteerService = new VolunteerService();
    private static final List<Activity> activities = volunteerService.getActivities();


    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== Volunteer Management System =====");
            System.out.println("1. Register Volunteer");
            System.out.println("2. Create Activity");
            System.out.println("3. Enroll Volunteer in Activity");
            System.out.println("4. Cancel Volunteer Enrollment");
            System.out.println("5. List Volunteers by Activity");
            System.out.println("6. Generate Report");
            System.out.println("7. Save & Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> registerVolunteer();
                case 2 -> createActivity();
                case 3 -> enrollVolunteer();
                case 4 -> cancelEnrollment();
                case 5 -> listVolunteersByActivity();
                case 6 -> {
                    generateReport();
                    System.out.println("Report generated successfully.");
                }
                case 7 -> {
                    saveData();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerVolunteer() {
        System.out.print("Enter volunteer full name: ");
        String name = scanner.nextLine();

        if (!name.matches("[a-zA-Z\\s]+")) {
            System.out.println("Invalid name. Only letters and spaces are allowed.");
            return;
        }
        
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
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            return;
        }

        System.out.print("Enter max capacity: ");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Select activity type: 1. Presential  2. Virtual");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Activity activity;
        switch (type) {
            case 1 -> {
                System.out.print("Enter location: ");
                String location = scanner.nextLine();
                activity = new InPersonActivity(activityName, description, date, maxCapacity, location, "In-Person");
            }
            case 2 -> {
                System.out.print("Enter platform: ");
                String platform = scanner.nextLine();
                activity = new VirtualActivity(activityName, description, date, maxCapacity, platform, "Virtual");
            }
            default -> {
                System.out.println("Invalid activity type. Please try again.");
                return;
            }
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

    private static void generateReport() {
        try (FileWriter writer = new FileWriter("report.txt")) {
            writer.write("===== Volunteer Management Report =====\n\n");
            
            writer.write("Registered Volunteers:\n");
            for (Volunteer v : volunteerService.getVolunteers()) {
                writer.write("- " + v.getName() + " (Age: " + v.getAge() + ", Email: " + v.getEmail() + ")\n");
            }

            writer.write("\nActivities:\n");
            for (Activity a : activities) {
                writer.write(a.toString() + "\n");
                for (Volunteer v : a.getRegisteredVolunteers()) {
                    writer.write("  * " + v.getName() + "\n");
                }
            }
            
            System.out.println("Report saved as report.txt");
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    private static void saveData() {
        JSONPersistence.saveVolunteers(volunteerService.getVolunteers());
        JSONPersistence.saveActivities(activities);
    }
}