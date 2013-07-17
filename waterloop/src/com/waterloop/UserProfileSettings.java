package com.waterloop;

public class UserProfileSettings {

	private String name;
	private String firstName;
	private String lastName;
	private String fbId;
	
	private static UserProfileSettings settings;
	
	private UserProfileSettings(){
		;
	}
	
	public static UserProfileSettings getUserProfileSettings(){
		
		if(settings == null){
			settings = new UserProfileSettings();
		}
		
		return settings;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	
	
	
	
}
