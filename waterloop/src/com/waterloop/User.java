package com.waterloop;

public class User {

	private String id;
	private String name;
	private String loc;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public User(String id, String name, String loc) {
		super();
		this.id = id;
		this.name = name;
		this.loc = loc;
	}
}
