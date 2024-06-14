package com.sabanciuniv.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sabanciuniv.model.Activity;

public interface ActivityRepo extends MongoRepository<Activity, String>{
	
	List<Activity> findByTypeAndIcon(String type, String icon);
	List<Activity> findByTypeAndIconAndName(String type, String icon, String name);

}
