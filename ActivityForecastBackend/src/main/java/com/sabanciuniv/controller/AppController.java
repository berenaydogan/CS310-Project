package com.sabanciuniv.controller;

import org.slf4j.LoggerFactory;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabanciuniv.model.Activity;
import com.sabanciuniv.model.Location;
import com.sabanciuniv.model.Review;
import com.sabanciuniv.model.Weather;
import com.sabanciuniv.repository.ActivityRepo;
import com.sabanciuniv.repository.LocationRepo;
import com.sabanciuniv.repository.ReviewRepo;
import com.sabanciuniv.repository.WeatherRepo;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/ActivityForecast")
public class AppController {
	
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired private RestTemplate restTemplate;
    
    @Autowired private WeatherRepo weatherRepo;
    @Autowired private LocationRepo locationRepo;
    @Autowired private ActivityRepo activityRepo;
    @Autowired private ReviewRepo reviewRepo;
   
    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;
    
	@PostConstruct
	public void init() {
		
		// Check if location database is empty
		if (locationRepo.count() == 0) {
			logger.info("Location database is empty, initializing...");
			
			// Initialize and save location instances to the database
			Location loc1 = new Location("istanbul");
			Location loc2 = new Location("ankara");
			Location loc3 = new Location("izmir");
			Location loc4 = new Location("antalya");
			Location loc5 = new Location("erzurum");
			
			
			locationRepo.save(loc1);
			locationRepo.save(loc2);
			locationRepo.save(loc3);
			locationRepo.save(loc4);
			locationRepo.save(loc5);
		}
		
		// Check if weather database is empty
		if (weatherRepo.count() == 0) {
			logger.info("Weather database is empty, initializing...");
			
			// Initialize and save weather instances to the database by the locations available in location database
			List<Location> locations = locationRepo.findAll();
	        for (Location location : locations) {
	            fetchAndSaveWeather(location.getName());
	        }
	        			
		}
		
		// Check if activity database is empty
		if (activityRepo.count() == 0){
			logger.info("Activity database is empty, initializing...");
			
			// Initialize and save activity instances to the database
			
			// Outdoor nice weather
			Activity activity1 = new Activity("01d", "outdoor", "Hiking", "https://upload.wikimedia.org/wikipedia/commons/5/5f/Hiking_trails.jpg", "Walking in natural environments would be a great source of energy in this weather. It offers physical challenge and the opportunity to explore scenic landscapes.");
			Activity activity2 = new Activity("02d", "outdoor", "Hiking", "https://upload.wikimedia.org/wikipedia/commons/5/5f/Hiking_trails.jpg", "Walking in natural environments would be a great source of energy in this weather. It offers physical challenge and the opportunity to explore scenic landscapes.");
			Activity activity3 = new Activity("01d","outdoor","Picnic", "https://cdn.stocksnap.io/img-thumbs/960w/outdoor-picnic_WSRRONVX8J.jpg", "A meal taken outdoors, often in a park or countryside, where you can enjoy the nature.");
			Activity activity4 = new Activity("02d", "outdoor", "Picnic", "https://cdn.stocksnap.io/img-thumbs/960w/outdoor-picnic_WSRRONVX8J.jpg", "A meal taken outdoors, often in a park or countryside, where you can enjoy the nature.");
			Activity activity5 = new Activity("01n","outdoor", "Outdoor Movie", "https://images.rawpixel.com/image_social_landscape/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIyLTExL2ZsNTAxMTM0MDk3MjctaW1hZ2UuanBn.jpg", "Watching a film in an open-air setting, like a park or a rooftop could be a new way to enjoy a good movie.");
			Activity activity6 = new Activity("02n","outdoor", "Outdoor Movie", "https://images.rawpixel.com/image_social_landscape/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIyLTExL2ZsNTAxMTM0MDk3MjctaW1hZ2UuanBn.jpg", "Watching a film in an open-air setting, like a park or a rooftop could be a new way to enjoy a good movie.");
			
			// Indoor nice weather
			Activity activity7 = new Activity("01d","indoor", "Yoga", "https://images.pexels.com/photos/3823073/pexels-photo-3823073.jpeg", "Practicing physical poses and breathing exercises for breath control, and meditation.");
			Activity activity8 = new Activity("02d","indoor", "Yoga", "hhttps://images.pexels.com/photos/3823073/pexels-photo-3823073.jpeg", "Practicing physical poses and breathing exercises for breath control, and meditation.");
			Activity activity9 = new Activity("01d","indoor", "Bowling", "https://staticb.yolcu360.com/blog/wp-content/uploads/2022/11/22134413/istanbul-bowling-salonu-3.jpg", "Invite your friends to a fun filled evening of bowling.");
			Activity activity10 = new Activity("02d","indoor", "Bowling", "https://staticb.yolcu360.com/blog/wp-content/uploads/2022/11/22134413/istanbul-bowling-salonu-3.jpg", "Invite your friends to a fun filled evening of bowling.");
			Activity activity11 = new Activity("01n","indoor", "Yoga", "https://images.pexels.com/photos/3823073/pexels-photo-3823073.jpeg", "Practicing physical poses and breathing exercises for breath control, and meditation.");
			Activity activity12 = new Activity("02n","indoor", "Yoga", "hhttps://images.pexels.com/photos/3823073/pexels-photo-3823073.jpeg", "Practicing physical poses and breathing exercises for breath control, and meditation.");
			Activity activity13 = new Activity("01n","indoor", "Board Games", "https://upload.wikimedia.org/wikipedia/commons/c/c0/Board_Games_for_Family_Fun.png", "Playing tabletop games that involve strategy or luck with family or in social gatherings.");
			Activity activity14 = new Activity("02n","indoor", "Board Games", "https://upload.wikimedia.org/wikipedia/commons/c/c0/Board_Games_for_Family_Fun.png", "Playing tabletop games that involve strategy or luck with family or in social gatherings.");
			
			// Outdoor cloudy weather
			Activity activity15 = new Activity("03d","outdoor", "Walk in Nature", "https://cdn12.picryl.com/photo/2016/12/31/hiking-trail-smoky-mountains-nature-landscapes-6643df-1024.jpg", "A leisurely walk taken in a natural environment, enjoying the peaceful environment and unwinding from daily stress.");
			Activity activity16 = new Activity("04d","outdoor", "Walk in Nature", "https://cdn12.picryl.com/photo/2016/12/31/hiking-trail-smoky-mountains-nature-landscapes-6643df-1024.jpg", "A leisurely walk taken in a natural environment, enjoying the peaceful environment and unwinding from daily stress.");
			Activity activity17 = new Activity("50d","outdoor", "Walk in Nature", "https://cdn12.picryl.com/photo/2016/12/31/hiking-trail-smoky-mountains-nature-landscapes-6643df-1024.jpg", "A leisurely walk taken in a natural environment, enjoying the peaceful environment and unwinding from daily stress.");
			Activity activity18 = new Activity("03d","outdoor", "Car Ride", "https://images.pexels.com/photos/14366596/pexels-photo-14366596.jpeg", "A journey taken in a car for pleasure to see new places and experience the landscape from the comfort of a vehicle.");
			Activity activity19 = new Activity("04d","outdoor", "Car Ride", "https://images.pexels.com/photos/14366596/pexels-photo-14366596.jpeg", "A journey taken in a car for pleasure to see new places and experience the landscape from the comfort of a vehicle.");
			Activity activity20 = new Activity("50d","outdoor", "Car Ride", "https://images.pexels.com/photos/14366596/pexels-photo-14366596.jpeg", "A journey taken in a car for pleasure to see new places and experience the landscape from the comfort of a vehicle.");
			Activity activity21 = new Activity("03d","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			Activity activity22 = new Activity("04d","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			Activity activity23 = new Activity("50d","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			Activity activity24 = new Activity("04n","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			Activity activity25 = new Activity("03n","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			Activity activity26 = new Activity("50n","outdoor", "Outdoor concert", "https://www.stockvault.net/data/2013/06/24/146173/preview16.jpg", "Enjoy the live music under the stars at an outdoor concert.");
			
			// Indoor cloudy weather
			Activity activity27 = new Activity("03d","indoor", "Indoor Gardening", "https://images.rawpixel.com/image_800/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvdXB3azYxODQ2MDk1LXdpa2ltZWRpYS1pbWFnZS1rb3didXF3Ny1rb3djbjRjdy5qcGc.jpg", "Growing plants inside a home or building, creating your specialized indoor gardening spaces.");
			Activity activity28 = new Activity("04d","indoor", "Indoor Gardening", "https://images.rawpixel.com/image_800/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvdXB3azYxODQ2MDk1LXdpa2ltZWRpYS1pbWFnZS1rb3didXF3Ny1rb3djbjRjdy5qcGc.jpg", "Growing plants inside a home or building, creating your specialized indoor gardening spaces.");
			Activity activity29 = new Activity("50d","indoor", "Indoor Gardening", "https://images.rawpixel.com/image_800/czNmcy1wcml2YXRlL3Jhd3BpeGVsX2ltYWdlcy93ZWJzaXRlX2NvbnRlbnQvbHIvdXB3azYxODQ2MDk1LXdpa2ltZWRpYS1pbWFnZS1rb3didXF3Ny1rb3djbjRjdy5qcGc.jpg", "Growing plants inside a home or building, creating your specialized indoor gardening spaces.");
			Activity activity30 = new Activity("03d","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			Activity activity31 = new Activity("04d","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			Activity activity32 = new Activity("50d","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			Activity activity33 = new Activity("03n","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			Activity activity34 = new Activity("04n","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			Activity activity35 = new Activity("50n","indoor", "Cooking", "https://pixnio.com/free-images/2021/04/06/2021-04-06-10-09-38-1200x1800.jpg", "Preparing food by combining, mixing, and heating ingredients could indeed be a fulfilling experience.");
			
			// Outdoor rainy weather 
			Activity activity36 = new Activity("10d","outdoor", "Outdoor Photography", "https://freerangestock.com/sample/55896/person-holding-camera-and-video-camera-in-outdoor-setting.jpg", "Taking photographs in outdoor settings, focusing on capturing natural elements, wildlife, and landscapes, feeling your connection with the nature.");
			Activity activity37 = new Activity("09d","outdoor", "Outdoor Photography", "https://freerangestock.com/sample/55896/person-holding-camera-and-video-camera-in-outdoor-setting.jpg", "Taking photographs in outdoor settings, focusing on capturing natural elements, wildlife, and landscapes, feeling your connection with the nature.");
			Activity activity38 = new Activity("11d","outdoor", "Outdoor Photography", "https://freerangestock.com/sample/55896/person-holding-camera-and-video-camera-in-outdoor-setting.jpg", "Taking photographs in outdoor settings, focusing on capturing natural elements, wildlife, and landscapes, feeling your connection with the nature.");
			Activity activity39 = new Activity("10d","outdoor", "Cafe/Restaurants", "https://freerangestock.com/sample/68027/group-of-people-sitting-at-a-coffee-shop-table.jpg", "Visit new venues to try a variety of foods and beverages.");
			Activity activity40 = new Activity("09d","outdoor", "Cafe/Restaurants", "https://freerangestock.com/sample/68027/group-of-people-sitting-at-a-coffee-shop-table.jpg", "Visit new venues to try a variety of foods and beverages.");
			Activity activity41 = new Activity("11d","outdoor", "Cafe/Restaurants", "https://freerangestock.com/sample/68027/group-of-people-sitting-at-a-coffee-shop-table.jpg", "Visit new venues to try a variety of foods and beverages.");
			
			// Indoor rainy weather 	
			Activity activity42 = new Activity("10d","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity43 = new Activity("09d","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity44 = new Activity("11d","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity45 = new Activity("10d","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "Enjoy the pleasure of reading, and engage in captivating stories.");
			Activity activity46 = new Activity("09d","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "Enjoy the pleasure of reading, and engage in captivating stories.");
			Activity activity47 = new Activity("11d","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "Enjoy the pleasure of reading, and engage in captivating stories.");
			Activity activity48 = new Activity("10d","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			Activity activity49 = new Activity("09d","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			Activity activity50 = new Activity("11d","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			Activity activity51 = new Activity("10n","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity52 = new Activity("09n","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity53 = new Activity("11n","indoor", "Movie Marathon", "https://images.pexels.com/photos/6982956/pexels-photo-6982956.jpeg", "Watching multiple films in one sitting, enjoying your favorite genres.");
			Activity activity54 = new Activity("10n","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "");
			Activity activity55 = new Activity("09n","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "");
			Activity activity56 = new Activity("11n","indoor", "Reading", "https://cdn12.picryl.com/photo/2016/12/31/book-pages-reading-596242-1024.jpg", "");
			Activity activity57 = new Activity("10n","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			Activity activity58 = new Activity("09n","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			Activity activity59 = new Activity("11n","indoor", "Painting/Drawing", "https://freerangestock.com/sample/103620/person-drawing-on-piece-of-paper.jpg", "Creating visual art using paints or pencils on surfaces like canvas or paper.");
			
			// Outdoor snowy weather
			Activity activity60 = new Activity("13d","outdoor", "Snowman building", "https://images.pexels.com/photos/10526561/pexels-photo-10526561.jpeg", "Gathering fresh snow and rolling it into large balls and stacking them to form a snowman, decorating it with items like rocks, carrots, and clothing.");
			Activity activity61 = new Activity("13d","outdoor", "Sledding", "https://images.rawpixel.com/image_800/cHJpdmF0ZS9zdGF0aWMvaW1hZ2Uvd2Vic2l0ZS8yMDIyLTA0L2xyL3B4MTAzMDUxNi1pbWFnZS1rd3Z3Zm4xdC5qcGc.jpg", "Sliding down snowy hills on a sled experiencing laughter and cheers.");
			Activity activity62 = new Activity("13n","outdoor", "Bonfire", "https://live.staticflickr.com/7162/6555198511_be083cae41_b.jpg", "Arrange a cozy bonfire in your backyard and gather around with your friends and family");
			
			// Indoor snowy weather
			Activity activity63 = new Activity("13d","indoor", "Crafts", "https://images.pexels.com/photos/14897481/pexels-photo-14897481.jpeg", "Making and decorating gingerbread cookies can be a calming and joyful experience.");
			Activity activity64 = new Activity("13d","indoor", "Making and decorating gingerbread cookies can be a calming and joyful experience.", "https://i1.pickpik.com/photos/564/926/474/cookie-gingerbread-baking-cookies-preview.jpg", "Making and decorating gingerbread cookies");
			Activity activity65 = new Activity("13d","indoor", "Christmas movies", "https://images.pexels.com/photos/14971920/pexels-photo-14971920/free-photo-of-a-couple-watching-a-movie-displayed-on-a-wall-from-a-projector-next-to-a-christmas-tree.jpeg", "Enjoying movies on the festive Christmas season, feeling themes of love, generosity, and holiday spirit.");
			Activity activity66 = new Activity("13n","indoor", "Crafts", "https://images.pexels.com/photos/14897481/pexels-photo-14897481.jpeg", "Making and decorating gingerbread cookies can be a calming and joyful experience.");
			Activity activity67 = new Activity("13n","indoor", "Making and decorating gingerbread cookies can be a calming and joyful experience.", "https://i1.pickpik.com/photos/564/926/474/cookie-gingerbread-baking-cookies-preview.jpg", "Making and decorating gingerbread cookies");
			Activity activity68 = new Activity("13n","indoor", "Christmas movies", "https://images.pexels.com/photos/14971920/pexels-photo-14971920/free-photo-of-a-couple-watching-a-movie-displayed-on-a-wall-from-a-projector-next-to-a-christmas-tree.jpeg", "Enjoying movies on the festive Christmas season, feeling themes of love, generosity, and holiday spirit."); 

			
			activityRepo.save(activity1);
			activityRepo.save(activity2);
			activityRepo.save(activity3);
			activityRepo.save(activity4);
			activityRepo.save(activity5);
			activityRepo.save(activity6);
			activityRepo.save(activity7);
			activityRepo.save(activity8);
			activityRepo.save(activity9);
			activityRepo.save(activity10);
			activityRepo.save(activity11);
			activityRepo.save(activity12);
			activityRepo.save(activity13);
			activityRepo.save(activity14);
			activityRepo.save(activity15);
			activityRepo.save(activity16);
			activityRepo.save(activity17);
			activityRepo.save(activity18);
			activityRepo.save(activity19);
			activityRepo.save(activity20);
			activityRepo.save(activity21);
			activityRepo.save(activity22);
			activityRepo.save(activity23);
			activityRepo.save(activity24);
			activityRepo.save(activity25);
			activityRepo.save(activity26);
			activityRepo.save(activity27);
			activityRepo.save(activity28);
			activityRepo.save(activity29);
			activityRepo.save(activity30);
			activityRepo.save(activity31);
			activityRepo.save(activity32);
			activityRepo.save(activity33);
			activityRepo.save(activity34);
			activityRepo.save(activity35);
			activityRepo.save(activity36);
			activityRepo.save(activity37);
			activityRepo.save(activity38);
			activityRepo.save(activity39);
			activityRepo.save(activity40);
			activityRepo.save(activity41);
			activityRepo.save(activity42);
			activityRepo.save(activity43);
			activityRepo.save(activity44);
			activityRepo.save(activity45);
			activityRepo.save(activity46);
			activityRepo.save(activity47);
			activityRepo.save(activity48);
			activityRepo.save(activity49);
			activityRepo.save(activity50);
			activityRepo.save(activity51);
			activityRepo.save(activity52);
			activityRepo.save(activity53);
			activityRepo.save(activity54);
			activityRepo.save(activity55);
			activityRepo.save(activity56);
			activityRepo.save(activity57);
			activityRepo.save(activity58);
			activityRepo.save(activity59);
			activityRepo.save(activity60);
			activityRepo.save(activity61);
			activityRepo.save(activity62);
			activityRepo.save(activity63);
			activityRepo.save(activity64);
			activityRepo.save(activity65);
			activityRepo.save(activity66);
			activityRepo.save(activity67);
			activityRepo.save(activity68);

		}
	
		
	}
	
	// Fetch weather information from Open Weather Map API by the provided location name
    public ResponseEntity<Weather> fetchAndSaveWeather(String locationName) {
    	
    	// Fetch list of locations from the database that match the provided location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Construct the URL for the weather API call
        String url = String.format("%s?q=%s&appid=%s", apiUrl, location.getName(), apiKey);
        // Make the HTTP request to the weather API and store the response
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Check if the API response is successful, if not throw an exception.
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch weather data");
        }
        
        // Extract the JSON body from the response
        String json = response.getBody();
        // Create a new weather entity
        Weather weather = new Weather();
        
        // Use an ObjectMapper object to extract weather data by parsing the JSON response
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(json);      
            
            // Extract and set the temperature
            weather.setTemperature(rootNode.path("main").path("temp").asDouble());
            // Extract and set the weather description
            weather.setDescription(rootNode.path("weather").get(0).path("description").asText());
            // Extract and set the icon code
            weather.setIcon(rootNode.path("weather").get(0).path("icon").asText());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing JSON response");
        }
        
        // Save to weather to MongoDB
        weather = weatherRepo.save(weather); 
        
        // Set the weather of the location to the fetched weather
        location.setWeather(weather);
        // Update the location on MongoDB with the weather
        locationRepo.save(location); 
        
        // Return the weather JSON
        return ResponseEntity.ok(weather);
    }
    
    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getAllLocations() {
    	// Fetch list of locations from the database 
    	List<Location> locations = locationRepo.findAll();
    	
        // Return the weather JSON
        return ResponseEntity.ok(locations);  
    }
    
    @GetMapping("/weather/{locationName}")
    public ResponseEntity<Weather> getWeatherByLocation(@PathVariable("locationName") String locationName) {
    	// Fetch list of locations from the database that match the provided location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Return the weather JSON
        return ResponseEntity.ok(location.getWeather());  
    }

    

    @GetMapping("/weather/{locationName}/activities/{type}")
    public ResponseEntity<List<Activity>> getActivitiesByLocationAndType(@PathVariable("locationName") String locationName, @PathVariable("type") String type) {
    	// Fetch list of locations from the database that match the provided location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Fetch the list of activities from MongoDB by their types and icons
    	List<Activity> activities = activityRepo.findByTypeAndIcon(type, location.getWeather().getIcon());
    	// Return the list of activities in JSON
        return ResponseEntity.ok(activities); 
    }
    
    @GetMapping("/weather/{locationName}/activities/{type}/{activityName}")
    public ResponseEntity<String> getActivityDetails(@PathVariable("locationName") String locationName, @PathVariable("type") String type, @PathVariable("activityName") String activityName) {
    	// Fetch list of locations from the database that match the provided location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Fetch the list of activities from MongoDB by their types and icons
    	List<Activity> activities = activityRepo.findByTypeAndIcon(type, location.getWeather().getIcon());
    	
    	// Initialize description as an empty string
        String details = "";

        // Iterate over the list of activities and find the matching activity by name
        for (Activity activity : activities) {
            if (activity.getName().equalsIgnoreCase(activityName)) {
                details = activity.getDetails();
                break; // Break the loop once the match is found
            }
        }
        
    	// Return the list of activities in JSON
        return ResponseEntity.ok(details); 
    }
    
    
    
    @GetMapping("/weather/{locationName}/activities/{type}/{activityName}/reviews")
    public ResponseEntity<List<Review>> getReviewsByActivity(@PathVariable("locationName") String locationName, @PathVariable("type") String type, @PathVariable("activityName") String activityName) {
    	// Fetch list of locations from MongoDB by location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Fetch the activities from MongoDB by their types, icons and names
    	List<Activity> activities = activityRepo.findByTypeAndIconAndName(type, location.getWeather().getIcon(), activityName);
    	// Get the first activity from the list (assuming there is a unique result)
    	Activity activity = activities.get(0);
    	
    	// Fetch the reviews from MongoDB by the activity they belong to
		List<Review> reviews = reviewRepo.findByActivity(activity);
		
    	// Return the list of reviews in JSON
		return ResponseEntity.ok(reviews);
    }
	
    
	@PostMapping("/weather/{locationName}/activities/{type}/{activityName}/reviews/post")
    public ResponseEntity<Review> postReview(@PathVariable("locationName") String locationName, @PathVariable("type") String type, @PathVariable("activityName") String activityName, @RequestParam("username") String username, @RequestParam("content") String content) {
		// Fetch list of locations from MongoDB by location name
    	List<Location> locations = locationRepo.findByName(locationName);
    	// Get the first location from the list (assuming there is a unique result)
        Location location = locations.get(0);
        
        // Fetch the activities from MongoDB by their types, icons and names
    	List<Activity> activities = activityRepo.findByTypeAndIconAndName(type, location.getWeather().getIcon(), activityName);
    	// Get the first activity from the list (assuming there is a unique result)
    	Activity activity = activities.get(0);
    	
    	// Create new review
    	Review review = new Review();
    	// Set the activity of the review
        review.setActivity(activity);
        // Set the username of the review
        review.setUsername(username);
        // Set the content of the review
        review.setContent(content);
        
        // Save the review to MongoDB
        reviewRepo.save(review);
        // Return the review in JSON
        return ResponseEntity.ok(review);
    }
    
    
}
