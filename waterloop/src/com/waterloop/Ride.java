package com.waterloop;

import java.util.Date;

public class Ride {

	private String origin;
	private String dest;
	private Date date;
	private String driver;
	private String[] passengers;
	
	public Ride(String origin, String dest, Date date, String driver,
			String[] passengers) {
		super();
		this.origin = origin;
		this.dest = dest;
		this.date = date;
		this.driver = driver;
		this.passengers = passengers;
	}
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String[] getPassengers() {
		return passengers;
	}
	public void setPassengers(String[] passengers) {
		this.passengers = passengers;
	}
	
}
