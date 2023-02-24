package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class MapCache {

	public static final String URL = "http://map.chuklee.com/map";
	private Logger logger = Logger.getLogger(MapCache.class.getName());

	@Value("${spaces.endpoint.bucket}")
	private String spacesBucket;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

	@Autowired
	private AmazonS3 s3;

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type
	// DO NOT CHNAGE THE METHOD'S NAME
	public void getMap(Restaurant restaurant) throws IOException {
		// Implmementation in here
		LatLng ll = restaurant.getCoordinates();
		String url = UriComponentsBuilder.fromUriString(URL)
				.queryParam("lat", ll.getLatitude())
				.queryParam("lng", ll.getLongitude())
				.toUriString();
		System.out.println(url);
		byte[] payload = null;
		try {
			// Create GET request
			RequestEntity<Void> req = RequestEntity.get(url).build();

			// Make the call to map.chuklee.com
			RestTemplate template = new RestTemplate();
			ResponseEntity<byte[]> resp;

			// Throws an exception if status code in between 200-399
			resp = template.exchange(req, byte[].class);

			// Get payload and perform an action with it
			payload = resp.getBody();
		} catch (Exception ex) {
			System.err.printf("Error: %s\n", ex.getMessage());
		}
		// Upload to S3 and update restaurant MapURL attribute
		upload(restaurant, payload);
	}

	// You may add other methods to this class
	// Upload file to S3 and set update post attributes
	public boolean upload(Restaurant restaurant, byte[] imgContent) throws IOException {
		InputStream is = new ByteArrayInputStream(imgContent);
		String contentType = URLConnection.guessContentTypeFromStream(is);
	    // Define Object Metadata
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);
		metadata.setContentLength(imgContent.length);
        try {
            PutObjectRequest putReq = new PutObjectRequest(spacesBucket, restaurant.getRestaurantId(), is, metadata);
            putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Put S3", ex);
			return false;
        }
        // https://xlbucket.sgp1.digitaloceanspaces.com/{restaurant_id} Image will be saved as this link
        String imageUrl = "https://%s.%s/%s".formatted(spacesBucket, spacesEndpointUrl, restaurant.getRestaurantId());
        restaurant.setMapURL(imageUrl);
        return true;
	}
}
