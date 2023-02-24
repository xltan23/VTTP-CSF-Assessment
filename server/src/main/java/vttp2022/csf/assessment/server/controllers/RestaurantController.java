package vttp2022.csf.assessment.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp2022.csf.assessment.server.services.RestaurantService;

import static vttp2022.csf.assessment.server.Utils.*;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantSvc;
    
    // GET/api/cuisines (Angular to make the Http Call to retrieve)
    @GetMapping(path = "/cuisines")
    public ResponseEntity<String> getCuisines() {
        List<String> cuisineList = restaurantSvc.getCuisines();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String cuisine:cuisineList) {
            jab.add(stringToJSONC(cuisine));
        }
        return ResponseEntity.ok(jab.build().toString());
    }

    // GET/api/{cuisine}/restaurants
    @GetMapping(path = "/{cuisine}/restaurants")
    public ResponseEntity<String> getRestaurants(@PathVariable String cuisine) {
        List<String> restaurantList = restaurantSvc.getRestaurantsByCuisine(cuisine);
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String restaurant:restaurantList) {
            jab.add(stringToJSONR(restaurant));
        }
        return ResponseEntity.ok(jab.build().toString());
    }
}
