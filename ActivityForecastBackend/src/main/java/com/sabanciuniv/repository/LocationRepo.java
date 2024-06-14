package com.sabanciuniv.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sabanciuniv.model.Location;

public interface LocationRepo extends MongoRepository<Location, String>{
	
	List<Location> findByName(String name);
	List<Location> findAll();
	
	
}