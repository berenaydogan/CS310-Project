package com.sabanciuniv.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sabanciuniv.model.Activity;
import com.sabanciuniv.model.Review;

public interface ReviewRepo extends MongoRepository<Review, String>{
	
	List<Review> findByActivity(Activity activity);

}