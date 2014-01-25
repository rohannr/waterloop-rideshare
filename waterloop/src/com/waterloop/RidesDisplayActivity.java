package com.waterloop;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;

public class RidesDisplayActivity extends ListActivity {
	private Ride[] rides=null; 
	private ProgressDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b  = getIntent().getExtras();
		Parcelable[] rideExtras =  b.getParcelableArray("rides");
		if (rideExtras == null){
			return;
		}
		System.out.println("Length of parcel: " + rideExtras.length);
			
		rides = new Ride[rideExtras.length];
		for(int i=0; i < rides.length;i++){
			rides[i] = (Ride) rideExtras[i];
		}

		setListAdapter(new RideArrayAdapter(this, rides));
		ListView rideListView = getListView();
		rideListView.setTextFilterEnabled(true);

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Ride selectedRide = rides[position];
		Intent openRide = new Intent(RidesDisplayActivity.this, RideActivity.class);
		openRide.putExtra(Ride.RIDE_PARCEL_KEY, selectedRide);
		startActivity(openRide);
	}
	
	
}
