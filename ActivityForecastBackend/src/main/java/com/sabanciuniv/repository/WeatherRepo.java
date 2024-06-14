package com.sabanciuniv.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sabanciuniv.model.Weather;

public interface WeatherRepo extends MongoRepository<Weather, String>{

}