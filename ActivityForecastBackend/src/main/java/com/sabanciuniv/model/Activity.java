package com.sabanciuniv.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Activity {
	
	@Id
	private String id;
	
	private String icon;
	private String type;
	private String name;
	private String photo;
	private String details;
	

	public Activity() {
		super();
	}

	public Activity(String icon, String type, String name, String photo, String details) {
		super();
		this.icon = icon;
		this.type = type;
		this.name = name;
		this.photo = photo;
		this.details = details;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
	
	
	
	
}
