package com.waterloop;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphUser;

                          
public class Ride implements Parcelable{

	private String origin;
	private String dest;
	private long date;
	private String driver;
	private String[] passengers;
	private int numSeats;
	private int seatsLeft;
	private int price;
	private String rideId;
	
	public static final String RIDE_PARCEL_KEY = "ride";

	public Ride(String origin, String dest, long date, String driver,
			String[] passengers, int price, int numSeats, String rideId) {
		super();
		this.origin = origin;
		this.dest = dest;
		this.date = date;
		this.driver = driver;
		this.passengers = passengers;
		this.numSeats = numSeats;
//		this.seatsLeft = numSeats - passengers.length;
		this.seatsLeft = numSeats;
		this.price=price;
		this.rideId = rideId;
	}
	
	public Ride(Parcel in){
		this.origin = in.readString();
		this.dest = in.readString();
		this.date = in.readLong();
		this.driver = in.readString();
//		in.readStringArray(passengers);
		this.numSeats = in.readInt();
		this.seatsLeft = in.readInt();
		this.price = in.readInt();
		this.rideId = in.readString();
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

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Ride> CREATOR
	= new Parcelable.Creator<Ride>() {
		public Ride createFromParcel(Parcel in) {
			return new Ride(in);
		}

		public Ride[] newArray(int size) {
			return new Ride[size];
		}
	};
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(origin);
		dest.writeString(this.dest);
		dest.writeLong(date);
		dest.writeString(driver);
//		dest.writeStringArray(passengers);
		dest.writeInt(numSeats);
		dest.writeInt(seatsLeft);
		dest.writeInt(price);
		dest.writeString(rideId);

	}

	public String getRideId() {
		return rideId;
	}

	public void setRideId(String rideId) {
		this.rideId = rideId;
	}

	
}
