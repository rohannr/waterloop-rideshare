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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
				date = new Date(dp.getYear(), dp.getMonth(), dp.getDayOfMonth()).getTime();
				String[] params = new String[]{ from, to, String.valueOf(date)};
				//make call to server
				new searchTask(context).execute(params);

			}
		});
	}


	public class searchTask extends AsyncTask<String, Void, Boolean> {
		private static final String SEARCH_RIDE_URL = "http://testapi.spearmunkie.com/search";
		private ProgressDialog loadSpinner;
		private Context context;
		private HttpClient client;



		public searchTask(Context context) {
			this.context = context;
			//			this.loadSpinner = new ProgressDialog(context);
			client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
		}

		@Override
		protected void onPreExecute() {
			loadSpinner = ProgressDialog.show(SearchActivity.this, "Searching..", "Getting rides..");
		}

		@Override
		protected Boolean doInBackground(String... params) {

			InputStream inputStream = null;

			try {
				HttpPost post = new HttpPost(SEARCH_RIDE_URL);
				post.setHeader("Content-type", "application/json");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("origin", params[0]));
				nameValuePairs.add(new BasicNameValuePair("destination", params[1]));
				nameValuePairs.add(new BasicNameValuePair("date", params[2]));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);

				JSONArray jsonSearchResults = new JSONArray(readResponse(response));
				Log.i(SearchActivity.class.getName(),
						"Number of entries " + jsonSearchResults.length());
				for (int i = 0; i < jsonSearchResults.length(); i++) {
					JSONObject jsonObject = jsonSearchResults.getJSONObject(i);
					Log.i(SearchActivity.class.getName(), jsonObject.getString("id"));
				}																																																																																																				
				System.out.println(response.getStatusLine());
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					return false;
				} else {
					System.out.println(EntityUtils.toString(response.getEntity()));
				}

			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("ERROR: ");
				return false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			loadSpinner.dismiss();
			if(result){
				Toast t = Toast.makeText(getApplicationContext(), "Search successful", Toast.LENGTH_LONG);
				t.show();
			} else {
				Toast t = Toast.makeText(getApplicationContext(), "Error searching ride", Toast.LENGTH_LONG);
				t.show();
			}
			Intent intent = new Intent(SearchActivity.this, RidesDisplayActivity.class);
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
