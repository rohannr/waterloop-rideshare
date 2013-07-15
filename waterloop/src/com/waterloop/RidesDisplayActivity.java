package com.waterloop;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RidesDisplayActivity extends ListActivity {

	static final String[] RIDES = new String[] { "Toronto-Waterloo", "Waterloo-Mississauga", "Waterloo-Markham",
		"Waterloo-Hamilton", "Waterloo-East York"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.ride_list_view, RIDES));
		
		ListView rideListView = getListView();
		rideListView.setTextFilterEnabled(true);
		
	}
}
