package com.waterloop;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

public class PostActivity extends Activity {
	
	private Ride ride;

	
	private Date date;


	private MenuItem settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity);
		
		final DatePicker dp = (DatePicker) findViewById(R.id.date_picker);
		
		final Spinner origin = (Spinner) findViewById(R.id.origin_picker);
		final Spinner destination = (Spinner) findViewById(R.id.destination_picker);
		
		Button postButton = (Button) findViewById(R.id.post_btn);
		postButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String from = origin.getSelectedItem().toString();
				String to = destination.getSelectedItem().toString();
				date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
				ride = new Ride(from, to, date, null, null);
				System.out.println("Ride: " + ride.getDate().toString());
			}
		});
		
	}
	
}
