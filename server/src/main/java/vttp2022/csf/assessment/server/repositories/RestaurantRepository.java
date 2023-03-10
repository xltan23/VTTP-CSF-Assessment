package vttp2022.csf.assessment.server.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;

import static vttp2022.csf.assessment.server.Utils.*;

@Repository
public class RestaurantRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	// db.restaurants.distinct('cuisine') 
	public List<String> getCuisines() {
		// Implmementation in here
		System.out.println("In Restauarant Repository");
		List<String> cuisines = mongoTemplate.findDistinct(new Query(), "cuisine", "restaurants", String.class);
		List<String> cuisinesUS = new LinkedList<>();
		// Replace cuisines with / in name with _
		for (String c:cuisines) {
			System.out.println(c);
			String cuisineUS = c.replace("/", "_");
			cuisinesUS.add(cuisineUS);
		}
		return cuisinesUS;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	// db.restaurants.find({cuisine:"restaurant.cuisine"},{_id:0,name:1}).sort({name:-1})
	public List<String> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		List<String> restaurants = new LinkedList<>();
		List<String> restaurantsUS = new LinkedList<>();
		Criteria criteria = Criteria.where("cuisine").is(cuisine);
		Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC,"name"));
		query.fields().exclude("_id").include("name");
		List<Document> restaurantsDoc = mongoTemplate.find(query, Document.class, "restaurants");
		for (Document restaurantDoc:restaurantsDoc) {
			String restaurant = docToString(restaurantDoc);
			restaurants.add(restaurant);
		}
		// Replace cuisines with / in name with _
		for (String r:restaurants) {
			System.out.println(r);
			String restaurantUS = r.replace("/", "_");
			restaurantsUS.add(restaurantUS);
		}
		return restaurants;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	// db.restaurants.find({name:"restaurant.name"},{_id:0,restaurant_id:1,name:1,cuisine:1,address:1,borough:1})
	public Optional<Restaurant> getRestaurant(String restaurant) {
		// Implmementation in here
		List<Restaurant> restaurants = new LinkedList<>();
		Criteria criteria = Criteria.where("name").is(restaurant);
		Query query = Query.query(criteria);
		query.fields().exclude("_id").include("restaurant_id","name","cuisine","address","borough");
		List<Document> restaurantsDoc = mongoTemplate.find(query, Document.class, "restaurants");
		for (Document restaurantDoc:restaurantsDoc) {
			Restaurant r = docToObject(restaurantDoc);
			restaurants.add(r);
		}
		if (null == restaurants) {
			return Optional.empty();
		}
		return Optional.of(restaurants.get(0));
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	// db.comments.insertOne({name:"comment.name",rating:comment.rating,text:"comment.text"}) 
	public void addComment(Comment comment) {
		// Implmementation in here
		Document newDoc = mongoTemplate.insert(commentToDoc(comment),"comments");
		ObjectId id = newDoc.getObjectId("_id");
	}
	
	// You may add other methods to this class

}
