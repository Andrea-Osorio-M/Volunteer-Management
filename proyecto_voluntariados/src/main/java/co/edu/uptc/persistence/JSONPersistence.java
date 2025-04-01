package co.edu.uptc.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.model.Activity;
import co.edu.uptc.model.Volunteer;

public class JSONPersistence {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Activity.class, new ActivityAdapter())
            .setPrettyPrinting()
            .create();
    private static final String VOLUNTEERS_FILE = "volunteers.json";
    private static final String ACTIVITIES_FILE = "activities.json";
    private static final String REPORT_FILE = "report.txt";

    public static void saveVolunteers(List<Volunteer> volunteers) {
        try (FileWriter writer = new FileWriter(VOLUNTEERS_FILE)) {
            GSON.toJson(volunteers, writer);
        } catch (IOException e) {
            System.err.println("Error saving volunteers: " + e.getMessage());
        }
    }

    public static List<Volunteer> loadVolunteers() {
        try (FileReader reader = new FileReader(VOLUNTEERS_FILE)) {
            Type listType = new TypeToken<ArrayList<Volunteer>>() {}.getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Error loading volunteers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveActivities(List<Activity> activities) {
        try (FileWriter writer = new FileWriter(ACTIVITIES_FILE)) {
            GSON.toJson(activities, writer);
        } catch (IOException e) {
            System.err.println("Error saving activities: " + e.getMessage());
        }
    }

    public static List<Activity> loadActivities() {
        try (FileReader reader = new FileReader(ACTIVITIES_FILE)) {
            Type listType = new TypeToken<ArrayList<Activity>>() {}.getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Error loading activities: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void generateReport(List<Volunteer> volunteers, List<Activity> activities) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            writer.println("===== VOLUNTEER PARTICIPATION REPORT =====\n");

            for (Volunteer volunteer : volunteers) {
                writer.println("Volunteer: " + volunteer.getName());
                writer.println("Activities Registered:");
                for (Activity activity : activities) {
                    if (activity.getRegisteredVolunteers().contains(volunteer)) {
                        writer.println("  - " + activity.getName() + " on " + activity.getDate());
                    }
                }
                writer.println();
            }
            
            System.out.println("Report generated successfully: " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}