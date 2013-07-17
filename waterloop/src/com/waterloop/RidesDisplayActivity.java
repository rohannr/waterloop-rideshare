package com.waterloop;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;

public class RidesDisplayActivity extends ListActivity {

	private Ride[] rides; 
	private ProgressDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		rides = new Ride[1];
		setListAdapter(new RideArrayAdapter(this, rides));
		
		ListView rideListView = getListView();
		rideListView.setTextFilterEnabled(true);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Ride selectedRide = rides[position];
		Intent openRide = new Intent(RidesDisplayActivity.this, RideActivity.class);
//		openRide.putExtra("Ride", selectedRide);
		startActivity(openRide);
	}
	
	
}
