package vttp2022.csf.assessment.server;

import java.io.StringReader;
import java.util.ArrayList;

import org.bson.Document;
import org.springframework.util.MultiValueMap;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

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

    public static Restaurant docToObject(Document document) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(document.getString("restaurant_id"));
        restaurant.setName(document.getString("name"));
        restaurant.setCuisine(document.getString("cuisine"));
        String addressStr = document.getString("address");
        JsonObject addressJson = stringToJSON(addressStr);
        String building = addressJson.getString("building");
        String street = addressJson.getString("street");
        String zipcode = addressJson.getString("zipcode");
        String borough = document.getString("borough");
        String newAddress = "%s, %s, %s, %s".formatted(building, street, zipcode, borough);
        restaurant.setAddress(newAddress);
        String coordinatesStr = addressJson.getString("coord");
        String removeBrac = coordinatesStr.replaceAll("[", "").replaceAll("]", "").trim();
        String[] coordinates = removeBrac.split(",");
        Float latitude = Float.parseFloat(coordinates[1]);
        Float longitude = Float.parseFloat(coordinates[0]);
        LatLng ll = new LatLng();
        ll.setLatitude(latitude);
        ll.setLongitude(longitude);
        restaurant.setCoordinates(ll);
        return restaurant;
    }

    public static JsonObject stringToJSON(String jsonString) {
        StringReader sr = new StringReader(jsonString);
        JsonReader jr = Json.createReader(sr);
        return jr.readObject();
    }

    public static Comment formToComment(MultiValueMap<String,String> form) {
        Comment comment = new Comment();
        comment.setName(form.getFirst("name"));
        comment.setRating(Integer.parseInt(form.getFirst("rating")));
        comment.setRestaurantId(form.getFirst("restaurant_id"));
        comment.setText(form.getFirst("text"));
        return comment;
    }

    public static Document commentToDoc(Comment comment) {
        Document document = new Document();
        document.put("name", comment.getName());
        document.put("rating", comment.getRating());
        document.put("text", comment.getText());
        document.put("restaurant_id", comment.getRestaurantId());
        return document;
    }
}
