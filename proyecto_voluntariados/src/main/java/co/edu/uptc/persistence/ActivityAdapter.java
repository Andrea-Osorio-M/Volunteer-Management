package co.edu.uptc.persistence;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import co.edu.uptc.model.Activity;
import co.edu.uptc.model.InPersonActivity;
import co.edu.uptc.model.VirtualActivity;

/**
 * Gson TypeAdapter to handle serialization and deserialization of Activity and its subclasses.
 */
public class ActivityAdapter implements JsonSerializer<Activity>, JsonDeserializer<Activity> {

    @Override
    public JsonElement serialize(Activity activity, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(activity).getAsJsonObject();
        jsonObject.addProperty("type", activity.getClass().getSimpleName()); // Add type identifier
        return jsonObject;
    }

    @Override
    public Activity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) 
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String activityType = jsonObject.get("type").getAsString();

        // Determine the correct subclass to instantiate
        switch (activityType) {
            case "In-Person" -> {
                return context.deserialize(jsonObject, InPersonActivity.class);
            }
            case "Virtual" -> {
                return context.deserialize(jsonObject, VirtualActivity.class);
            }
            default -> throw new JsonParseException("Unknown activity type: " + activityType);
        }
    }
}