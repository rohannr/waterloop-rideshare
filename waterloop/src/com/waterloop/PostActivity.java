package com.waterloop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class PostActivity extends Activity {
	
	private Ride ride;

	private static final String POST_RIDE_URL = "http://testapi.spearmunkie.com/rides";
	
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
				postRide(ride);
			}
		});
		
	}

	protected void postRide(Ride r) {
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("driver[name]", ride.getDriver()));
		params.add(new BasicNameValuePair("to", ride.getOrigin()));
		params.add(new BasicNameValuePair("from", ride.getDest()));
		params.add(new BasicNameValuePair("date", ride.getDate().toString()));
		params.add(new BasicNameValuePair("authenticity_token", "MD2BswheH6dEGHB20NA3ffj9+50Vjb2aDPcTfNUGbRs="));
		params.add(new BasicNameValuePair("commit", "Create user"));
		
		new PostRideTask().execute(params);
		
	}

	public class PostRideTask extends AsyncTask<List<NameValuePair>, Void, Void> {

		private HttpClient client = new DefaultHttpClient();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(List<NameValuePair>... params) {
			
			try{
			// TODO Auto-generated method stub
			HttpPost post = new HttpPost(POST_RIDE_URL);
			
			post.setEntity(new UrlEncodedFormEntity(params[0]));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(post);
			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("ERROR: ");
				Toast t = Toast.makeText(getApplicationContext(), "Error posting ride", Toast.LENGTH_LONG);
				t.show();
			}
			
			return null;
		}
		
	}
	
}
