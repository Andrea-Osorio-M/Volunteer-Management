package co.edu.uptc.view;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import co.edu.uptc.controller.InputUtils;
import co.edu.uptc.controller.InputValidator;
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
    static InputValidator validator = InputUtils::validateChoice;
        public static void main(String[] args) {
            while (true) {
                System.out.println("\n===== Volunteer Management System =====");
                System.out.println("1. Manage Volunteers");
                System.out.println("2. Manage Activities");
                System.out.println("3. Save & Exit");
                System.out.print("Choose an option: ");
                int choice;
                while (true) {
                    choice = validator.validateChoice(scanner, 1, 3);
                    if (choice == InputUtils.Errorisnotanumber) {
                        System.out.println("Error: Debe ingresar un número.");
                    } else if (choice == InputUtils.Errorbecauseisoutoftherange) {
                        System.out.println("Error: El número debe estar entre 1 y 3.");
                    } else {
                        break; // Entrada válida
                    }
                    System.out.print("Intente nuevamente: ");
                }
                switch (choice) {
                    case 1 -> manageVolunteers(validator);
                    case 2 -> manageActivities(validator);
                    case 3 -> { saveData();  return; }
                }
            }
        }
        private static void manageVolunteers(InputValidator validator) {
            while (true) {
                System.out.println("\n===== Volunteer Management =====");
                System.out.println("1. Register Volunteer");
                System.out.println("2. List Volunteers");
                System.out.println("3. Delete Volunteer");
                System.out.println("4. Back to Main Menu");
                System.out.print("Choose an option: ");
                int choice;
                while (true) {
                    choice = validator.validateChoice(scanner, 1, 4);
                    if (choice == InputUtils.Errorisnotanumber) {
                        System.out.println("Error: Debe ingresar un número.");
                    } else if (choice == InputUtils.Errorbecauseisoutoftherange) {
                        System.out.println("Error: El número debe estar entre 1 y 4.");
                    } else {
                        break; // Entrada válida
                    }
                    System.out.print("Intente nuevamente: ");
                }
                switch (choice) {
                    case 1 -> registerVolunteer();
                    case 2 -> listVolunteers();
                    case 3 -> deleteVolunteer();
                    case 4 -> { return; }
                }
            }
        }

        private static void manageActivities(InputValidator validator) {
            while (true) {
                System.out.println("\n===== Activity Management =====");
                System.out.println("1. Create Activity");
                System.out.println("2. List Volunteers By Activities");
                System.out.println("3. Enroll Volunteer in Activity");
                System.out.println("4. Cancel Volunteer Enrollment");
                System.out.println("5. Generate Report");
                System.out.println("6. Back to Main Menu");
                System.out.print("Choose an option: ");
                int choice;
                while (true) {
                    choice = validator.validateChoice(scanner, 1, 6);
                    if (choice == InputUtils.Errorisnotanumber) {
                        System.out.println("Error: Debe ingresar un número.");
                    } else if (choice == InputUtils.Errorbecauseisoutoftherange) {
                        System.out.println("Error: El número debe estar entre 1 y 6.");
                    } else {
                        break; // Entrada válida
                    }
                    System.out.print("Intente nuevamente: ");
                }
                switch (choice) {
                    case 1 ->createActivity();
                    case 2 ->listVolunteersByActivity();
                    case 3 ->enrollVolunteer();                        
                    case 4 ->cancelEnrollment();
                    case 5 ->volunteerService.generateParticipationReport();
                    case 6->{return;}
                }
            }
        }

    private static void saveData() {
            JSONPersistence.saveVolunteers(volunteerService.getVolunteers());
            JSONPersistence.saveActivities(activities);
    }

    private static void registerVolunteer() {
    Volunteer volunteer = null;
    while (volunteer == null) {
        try {
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

        volunteer = new Volunteer(name, age, email);
        volunteerService.registerVolunteer(volunteer);
            
            System.out.println("Volunteer registered successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + " Please try again.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            scanner.nextLine(); // Limpiar buffer en caso de error
        }
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
            System.out.println("Invalid date format, please try again.");
            return;
        }

        System.out.print("Enter max capacity: ");
        int maxCapacity = scanner.nextInt();
    
        scanner.nextLine(); // Consume newline
        
        System.out.println("Select activity type: 1. Presential  2. Virtual");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        Activity activity;
        try {
            if (type == 1) {
                System.out.print("Enter location: ");
                String location = scanner.nextLine().trim();
                if (location.isEmpty()) {
                    throw new IllegalArgumentException("Location cannot be empty.");
                }
                activity = new InPersonActivity(activityName, description, date, maxCapacity, location, "In-Person");
            } else {
                System.out.print("Enter platform: ");
                String platform = scanner.nextLine().trim();
                if (platform.isEmpty()) {
                    throw new IllegalArgumentException("Platform cannot be empty.");
                }
                activity = new VirtualActivity(activityName, description, date, maxCapacity, platform, "Virtual");
            }
            activities.add(activity);
            System.out.println("Activity created successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
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
    
    
        }    System.out.print("Select activity number: ");
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
        System.out.println("Total activities loaded: " + activities.size()); // <-- Agregar esto
    
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
}