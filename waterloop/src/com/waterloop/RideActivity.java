package com.waterloop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.waterloop.network.NetworkUtils;

public class RideActivity extends Activity {

	private String origin;
	private String destination;
	private String driverName;
	private int seatsLeft;
	private Ride ride;

	private static final String GRAPH_ID_URL = "http://graph.facebook.com/";
	private static final String JOIN_RIDE_URL = "http://testapi.spearmunkie.com/addPassenger";
	//		passenger_id: string
	//	ride_id: string

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent openRideIntent = getIntent();
		ride = (Ride) openRideIntent.getParcelableExtra(Ride.RIDE_PARCEL_KEY);

		setContentView(R.layout.ride_activity);


		TextView title = (TextView) findViewById(R.id.ride_title);
		title.setText(ride.getOrigin() + "-" + ride.getDest());

		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.driver_pic);
		profilePictureView.setCropped(true);
		profilePictureView.setProfileId(ride.getDriver());

		TextView rideDriver = (TextView) findViewById(R.id.ride_driver);
		rideDriver.setText(getDriverName());

		TextView rideDesc = (TextView) findViewById(R.id.seats_desc);
		rideDesc.setText(ride.getSeatsLeft() + " / " + ride.getNumSeats() + "available");
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		String d = formatter.format(new Date(ride.getDate()));
		TextView rideDate = (TextView) findViewById(R.id.ride_date);
		rideDate.setText(d);
		
		TextView ridePrice = (TextView) findViewById(R.id.ride_price);
		ridePrice.setText("$" + ride.getPrice());

		Button joinButton = (Button) findViewById(R.id.join_ride_button);
		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new JoinRideTask().execute();
			}
		});

	}


	private String getDriverName() {
		new getNameTask().execute();
		return driverName;
	}

	public class getNameTask extends AsyncTask<String, Void, Void>{

		private ProgressDialog loadSpinner;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadSpinner = ProgressDialog.show(RideActivity.this, "Loading..", "");
		}

		@Override
		protected Void doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(GRAPH_ID_URL + ride.getDriver());
			InputStream is=null;
			String json=null;

			try {
				HttpResponse response = client.execute(httpGet);
				System.out.println(response.getStatusLine());
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					json = NetworkUtils.jsonParser(response);
					JSONObject driverJson = new JSONObject(json);
					driverName = driverJson.getString("name");
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}



			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			loadSpinner.dismiss();
		}
	}

	public class JoinRideTask extends AsyncTask<Void, Void, Boolean> {

		private HttpClient client = new DefaultHttpClient();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try{
				// TODO Auto-generated method stub
				HttpPost post = new HttpPost(JOIN_RIDE_URL);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("passenger_id", UserProfileSettings.getUserProfileSettings().getFbId()));
				param.add(new BasicNameValuePair("ride_id", ride.getRideId()));
				post.setEntity(new UrlEncodedFormEntity(param));

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
				Toast t = Toast.makeText(getApplicationContext(), "Ride successfully joined", Toast.LENGTH_LONG);
				t.show();
				Intent i = new Intent(RideActivity.this, MainActivity.class);
				startActivity(i);

			} else {
				Toast t = Toast.makeText(getApplicationContext(), "Error joining ride", Toast.LENGTH_LONG);
				t.show();
			}
		}	
	}
}
