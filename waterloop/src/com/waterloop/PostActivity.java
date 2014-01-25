package com.waterloop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PostActivity extends Activity {
	
	private Ride ride;

	private static final String POST_RIDE_URL = "http://waterloop.sidprak.com/rides/";
	
	private long date;
	private GraphUser driver;

	private MenuItem settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity);
		
		final DatePicker dp = (DatePicker) findViewById(R.id.date_picker);
		
		final Spinner origin = (Spinner) findViewById(R.id.origin_picker);
		final Spinner destination = (Spinner) findViewById(R.id.destination_picker);
		final EditText price = (EditText) findViewById(R.id.price_field);
		final EditText numSeats = (EditText) findViewById(R.id.seats_field);
		
		Button postButton = (Button) findViewById(R.id.post_btn);
		
		postButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String from = origin.getSelectedItem().toString();
				String to = destination.getSelectedItem().toString();
				
				date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth()).getTime()/1000; //divide to get UNIX epoch
				System.out.println(UserProfileSettings.getUserProfileSettings().getName());
				System.out.println(from);
				System.out.println(to);
				System.out.println(date);
				System.out.println(price);
				System.out.println(numSeats);
				postRide(UserProfileSettings.getUserProfileSettings().getFbId(), from, to, date, price.getText().toString(), numSeats.getText().toString());
			}
		});
		
	}

	protected void postRide(String fbid, String from, String to, long dateInMillis,
			String price, String numSeats) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = new JSONObject();
		try {
			json.put("driverId", fbid);
			json.put("origin", from);
			json.put("destination", to);
			json.put("datetime", dateInMillis);
			json.put("numSeats", numSeats);
			json.put("price", price);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		params.add(new BasicNameValuePair("ride[driver]", name));
//		params.add(new BasicNameValuePair("ride[origin]", from));
//		params.add(new BasicNameValuePair("ride[destination]", to));
//		params.add(new BasicNameValuePair("ride[date]",  String.valueOf(dateInMillis)));
//		params.add(new BasicNameValuePair("ride[price]", price));
//		params.add(new BasicNameValuePair("ride[num_passengers]", numSeats));

//		params.add(new BasicNameValuePair("authenticity_token", "MD2BswheH6dEGHB20NA3ffj9+50Vjb2aDPcTfNUGbRs="));
//		params.add(new BasicNameValuePair("commit", "Create user"));
		
		new PostRideTask().execute(json.toString());
		
	}

	public class PostRideTask extends AsyncTask<String, Void, Boolean> {

		private HttpClient client = new DefaultHttpClient();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			try{
			// TODO Auto-generated method stub
			HttpPost post = new HttpPost(POST_RIDE_URL);
			post.setEntity(new StringEntity(params[0]));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				return false;
			}
			
			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("ERROR: ");
				return false;
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast t = Toast.makeText(getApplicationContext(), "Ride successfully posted", Toast.LENGTH_LONG);
				t.show();
			} else {
				Toast t = Toast.makeText(getApplicationContext(), "Error posting ride", Toast.LENGTH_LONG);
				t.show();
			}
		}	
	}
	
}
