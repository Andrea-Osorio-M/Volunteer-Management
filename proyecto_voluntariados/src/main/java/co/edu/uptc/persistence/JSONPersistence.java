package co.edu.uptc.persistence;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.model.Activity;
import co.edu.uptc.model.Volunteer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles JSON persistence using Gson.
 */
public class JSONPersistence {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String VOLUNTEERS_FILE = "volunteers.json";
    private static final String ACTIVITIES_FILE = "activities.json";

    /**
     * Saves a list of volunteers to a JSON file.
     * @param volunteers List of volunteers.
     */
    public static void saveVolunteers(List<Volunteer> volunteers) {
        try (FileWriter writer = new FileWriter(VOLUNTEERS_FILE)) {
            GSON.toJson(volunteers, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of volunteers from a JSON file.
     * @return List of volunteers.
     */
    public static List<Volunteer> loadVolunteers() {
        try (FileReader reader = new FileReader(VOLUNTEERS_FILE)) {
            Type listType = new TypeToken<ArrayList<Volunteer>>() {}.getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Saves a list of activities to a JSON file.
     * @param activities List of activities.
     */
    public static void saveActivities(List<Activity> activities) {
        try (FileWriter writer = new FileWriter(ACTIVITIES_FILE)) {
            GSON.toJson(activities, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a list of activities from a JSON file.
     * @return List of activities.
     */
    public static List<Activity> loadActivities() {
        try (FileReader reader = new FileReader(ACTIVITIES_FILE)) {
            Type listType = new TypeToken<ArrayList<Activity>>() {}.getType();
            return GSON.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}

