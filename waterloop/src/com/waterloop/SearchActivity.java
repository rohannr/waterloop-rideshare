package com.waterloop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.waterloop.network.NetworkUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity {

	private long date;
	private Ride ride;
	public Context context;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		this.context = this;

		final DatePicker dp = (DatePicker) findViewById(R.id.date_picker);

		final Spinner origin = (Spinner) findViewById(R.id.origin_picker);
		final Spinner destination = (Spinner) findViewById(R.id.destination_picker);

		Button searchButton = (Button) findViewById(R.id.search_btn);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String from = origin.getSelectedItem().toString();
				String to = destination.getSelectedItem().toString();
				date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth()).getTime()/1000;
				String[] params = new String[]{ from, to, String.valueOf(date)};
				//make call to server
				new searchTask(context).execute(params);

			}
		});
	}


	public class searchTask extends AsyncTask<String, Void, Ride[]> {
		private static final String SEARCH_RIDE_URL = "http://waterloop.sidprak.com/search/";
		private ProgressDialog loadSpinner;
		private Ride[] rides;
		private Context context;
		private HttpClient client;



		public searchTask(Context context) {
			this.context = context;
			this.rides = null;
			//			this.loadSpinner = new ProgressDialog(context);
			this.client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		}

		@Override
		protected void onPreExecute() {
			loadSpinner = ProgressDialog.show(SearchActivity.this, "Searching..", "Getting rides..");
		}

		@Override
		protected Ride[] doInBackground(String... params) {


			try {
				HttpPost post = new HttpPost(SEARCH_RIDE_URL);
				JSONObject json = new JSONObject();
				json.put("origin", params[0]);
				json.put("destination", params[1]);
				json.put("datetime", params[2]);
				System.out.println("Search EPOCH: " + date);
				post.setEntity(new StringEntity(json.toString()));
//				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);

				System.out.println(response.getStatusLine());
																																																																																									
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					return null;
				} else {
					JSONArray jsonSearchResults = new JSONArray(readResponse(response));
					this.rides = new Ride[jsonSearchResults.length()];
					this.rides = makeRideArray(jsonSearchResults);
//					System.out.println(EntityUtils.toString(response.getEntity()));
				}

			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("ERROR: ");
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return rides;
		}

		private Ride[] makeRideArray(JSONArray jsonSearchResults) {
			int len = jsonSearchResults.length();
			Ride[] result = new Ride[len];
			
			for (int i = 0; i < jsonSearchResults.length(); i++) {
				try {
					JSONObject jsonObject = jsonSearchResults.getJSONObject(i);
					String name = jsonObject.getString("driver");
					String origin = jsonObject.getString("origin");
					String destination = jsonObject.getString("dest");
					String rideID = jsonObject.getString("id");
					int price = Integer.parseInt(jsonObject.getString("price"));
					int numSeats = Integer.parseInt(jsonObject.getString("seats"));
					long date = jsonObject.getLong("date");
//					JSONArray passengers = (JSONArray) jsonObject.get("passengers");
					
					
//					System.out.println(name);
//					System.out.println(origin);
//					System.out.println(destination);
//					System.out.println(price);
//					System.out.println(numSeats);


					
					//possibly unnecessary
//					if(passengers.length == 0){
//						passengers = null;
//					}
					Ride ride = new Ride(origin, destination, date, name, null, price, numSeats, rideID);
					result[i] = ride;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Ride[] result) {
			super.onPostExecute(result);
			if(result!= null){
				Toast t = Toast.makeText(getApplicationContext(), "Search successful", Toast.LENGTH_SHORT);
				t.show();
			} else {
				Toast t = Toast.makeText(getApplicationContext(), "No rides found", Toast.LENGTH_SHORT);
				t.show();
			}
			loadSpinner.dismiss();
			Intent intent = new Intent(SearchActivity.this, RidesDisplayActivity.class);
			intent.putExtra("rides", this.rides);
			startActivity(intent);
		}

		private String readResponse(HttpResponse response) throws IOException{
			HttpEntity entity = response.getEntity();
			StringBuilder builder = new StringBuilder();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		}
	}
}
