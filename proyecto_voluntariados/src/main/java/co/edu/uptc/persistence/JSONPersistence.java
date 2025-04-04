package co.edu.uptc.persistence;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import co.edu.uptc.model.Activity;
import co.edu.uptc.model.Volunteer;

public class JSONPersistence {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Activity.class, new ActivityAdapter()) // Usar el adaptador para Activity
            .setPrettyPrinting()
            .create();
    private static final String VOLUNTEERS_FILE = "volunteers.json";
    private static final String ACTIVITIES_FILE = "activities.json";
    private static final String REPORT_FILE = "report.txt";

    public static void saveVolunteers(List<Volunteer> volunteers) {
        try (FileWriter writer = new FileWriter(VOLUNTEERS_FILE)) {
            GSON.toJson(volunteers, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error saving volunteers: " + e.getMessage(), e);
        }
    }

    public static List<Volunteer> loadVolunteers() {
        try (FileReader reader = new FileReader(VOLUNTEERS_FILE)) {
            return GSON.fromJson(reader, new TypeToken<List<Volunteer>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    public static void saveActivities(List<Activity> newActivities) {
        List<Activity> existingActivities = loadActivities(); // Cargar actividades previas
    
        // Filtrar actividades nuevas que ya existan para evitar duplicados
        for (Activity newActivity : newActivities) {
            boolean exists = existingActivities.stream()
                    .anyMatch(a -> a.getName().equals(newActivity.getName()) &&
                                   a.getDate().equals(newActivity.getDate()) &&
                                   a.getType().equals(newActivity.getType()));
            if (!exists) {
                existingActivities.add(newActivity);
            }
        }
    
        try (FileWriter writer = new FileWriter(ACTIVITIES_FILE, false)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray jsonArray = new JsonArray();
    
            for (Activity activity : existingActivities) {
                JsonObject jsonObject = gson.toJsonTree(activity).getAsJsonObject();
                jsonObject.addProperty("type", activity.getType());
                jsonArray.add(jsonObject);
            }
    
            writer.write(gson.toJson(jsonArray));
        } catch (IOException e) {
            throw new RuntimeException("Error saving activities: " + e.getMessage(), e);
        }
    }
    
    
    public static List<Activity> loadActivities() {
        try (FileReader reader = new FileReader(ACTIVITIES_FILE)) {
            Type listType = new TypeToken<ArrayList<Activity>>() {}.getType();
            List<Activity> activities = GSON.fromJson(reader, listType);
    
            // Debugging: Verificar si realmente se están cargando todas
            System.out.println("Activities loaded: " + activities.size());
            for (Activity a : activities) {
                System.out.println("- " + a.getName() + " | " + a.getDate());
            }
    
            return activities;
        } catch (IOException e) {
            System.err.println("Error loading activities: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public static void generateReport(List<Volunteer> volunteers, List<Activity> activities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORT_FILE))) {
            writer.write("\n===== VOLUNTEER PARTICIPATION REPORT =====\n\n");

            // Contar cuántas actividades tiene cada voluntario
            Map<String, Long> volunteerParticipation = activities.stream()
                .flatMap(activity -> activity.getRegisteredVolunteers().stream())
                .collect(Collectors.groupingBy(Volunteer::getName, Collectors.counting()));

            // Ordenar los voluntarios de mayor a menor participación
            List<Volunteer> sortedVolunteers = volunteers.stream()
                .sorted((v1, v2) -> Long.compare(
                    volunteerParticipation.getOrDefault(v2.getName(), 0L),
                    volunteerParticipation.getOrDefault(v1.getName(), 0L)))
                .collect(Collectors.toList());

            writer.write("Registered Volunteers (Most Active First):\n");
            for (Volunteer volunteer : sortedVolunteers) {
                long participationCount = volunteerParticipation.getOrDefault(volunteer.getName(), 0L);
                writer.write("- " + volunteer.getName() + " (Age: " + volunteer.getAge() + ", Email: " + volunteer.getEmail() +
                             ", Activities: " + participationCount + ")\n");
            }
            writer.write("\n");

            writer.write("Activities:\n");
            for (Activity activity : activities) {
                writer.write("Activity: " + activity.getName() + " | " + activity.getDescription() + " | " +
                             activity.getDate() + " | Max Participants: " + activity.getMaxParticipants() +
                             " | Type: " + activity.getType() + "\n");

                writer.write("  Registered Volunteers:\n");
                for (Volunteer v : activity.getRegisteredVolunteers()) {
                    writer.write("    - " + v.getName() + " (Email: " + v.getEmail() + ")\n");
                }
                writer.write("\n");
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }
}