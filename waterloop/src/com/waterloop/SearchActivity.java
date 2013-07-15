package com.waterloop;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

public class SearchActivity extends Activity {

	private Date date;
	private Ride ride;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		

		final DatePicker dp = (DatePicker) findViewById(R.id.date_picker);

		final Spinner origin = (Spinner) findViewById(R.id.origin_picker);
		final Spinner destination = (Spinner) findViewById(R.id.destination_picker);
		
		Button postButton = (Button) findViewById(R.id.search_btn);
		postButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String from = origin.getSelectedItem().toString();
				String to = destination.getSelectedItem().toString();
				date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
				ride = new Ride(from, to, date, null, null);
				
				//make call to server
				new searchTask().execute();
				
			}
		});
	}
	
	
	public class searchTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent intent = new Intent(SearchActivity.this, RidesDisplayActivity.class);
			startActivity(intent);
		}
	}
}
