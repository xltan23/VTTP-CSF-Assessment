package vttp2022.csf.assessment.server;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Utils {
    
    public static JsonObject stringToJSONC(String string) {
        return Json.createObjectBuilder()
                    .add("cuisine", string)
                    .build();
    }

    public static JsonObject stringToJSONR(String string) {
        return Json.createObjectBuilder()
                    .add("restaurant", string)
                    .build();
    }

    public static String docToString(Document document) {
        return document.getString("name");
    }
}
