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

public class ActivityAdapter implements JsonSerializer<Activity>, JsonDeserializer<Activity> {
    @Override
    public JsonElement serialize(Activity activity, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(activity).getAsJsonObject();
        String activityType = activity.getClass().getSimpleName();
        jsonObject.addProperty("type", activityType);  // Agregar identificador de tipo
        return jsonObject;
    }

    @Override
public Activity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();

    if (!jsonObject.has("type") || jsonObject.get("type").isJsonNull()) {
        throw new JsonParseException("Missing or null 'type' field in JSON: " + jsonObject);
    }

    String activityType = jsonObject.get("type").getAsString();

    switch (activityType) {
        case "Virtual":
            return context.deserialize(json, VirtualActivity.class);
        case "In-Person":
            return context.deserialize(json, InPersonActivity.class);
        default:
            throw new JsonParseException("Unknown activity type: " + activityType);
    }
}

}