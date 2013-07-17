package com.waterloop;

import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RideArrayAdapter extends ArrayAdapter<Ride> {

	private final Context context;
	private final Ride[] rides;
	
	public RideArrayAdapter(Context context, Ride[] rides) {
		super(context, R.layout.ride_list_item, rides);
		this.context = context;
		this.rides = rides;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.ride_list_item, parent, false);
			TextView rideTitle = (TextView) rowView.findViewById(R.id.ride_title);
			rideTitle.setText(rides[position].getOrigin() + " - " + rides[position].getDest());	
			
			
			TextView seatsLeft = (TextView) rowView.findViewById(R.id.seats_left);
			seatsLeft.setText(rides[position].getSeatsLeft() + " / seats left");

//			ProfilePictureView driver_photo = (ProfilePictureView) rowView.findViewById(R.id.driver_photo);
//			driver_photo.setCropped(true);
//
//			driver_photo.setProfileId(rides[position].getDriver().getId());
			
			
			return rowView;
	}
	
	

}
