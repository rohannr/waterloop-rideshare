package com.waterloop;

import java.util.Date;

import com.facebook.model.GraphUser;

public class Ride {

	private String origin;
	private String dest;
	private long date;
	private GraphUser driver;
	private String[] passengers;
	private int numSeats;
	private int seatsLeft;
	private int price;
	
	public Ride(String origin, String dest, long date, GraphUser driver,
			String[] passengers, int price, int numSeats) {
		super();
		this.origin = origin;
		this.dest = dest;
		this.date = date;
		this.driver = driver;
		this.passengers = passengers;
		this.numSeats = numSeats;
		this.seatsLeft = numSeats - passengers.length;
	}
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public int getSeatsLeft() {
		return seatsLeft;
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
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public GraphUser getDriver() {
		return driver;
	}
	public void setDriver(GraphUser driver) {
		this.driver = driver;
	}
	public String[] getPassengers() {
		return passengers;
	}
	public void setPassengers(String[] passengers) {
		this.passengers = passengers;
	}
	
}
