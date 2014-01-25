package com.waterloop;

import java.io.IOException;
import java.util.ArrayList;
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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.waterloop.network.NetworkUtils;

public class SelectionFragment extends ListFragment {

	private static final String TAG = "SelectionFragment";
	private static final String REGISTER_USER_URL = "http://waterloop.sidprak.com/users/";
	private static final int REAUTH_ACTIVITY_CODE = 100;

	private String[] userParams = new String[2];
	private Ride[] currentRides;

	private ProfilePictureView profilePictureView;
	private TextView userNameView;

	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.selection, 
				container, false);

		// Find the user's profile picture custom view
		profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
		profilePictureView.setCropped(true);

		// Find the user's name view
		userNameView = (TextView) view.findViewById(R.id.selection_user_name);

		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			// Get the user's data
			makeMeRequest(session);
		}


		Button postButton = (Button) view.findViewById(R.id.post_ride_button);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity().getApplicationContext(), PostActivity.class);
				getActivity().startActivity(intent);
			}
		});

		Button searchButton = (Button) view.findViewById(R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
				getActivity().startActivity(intent);
			}
		});

		//		new registerTask().execute(userParams);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ListView rideListView = getListView();
		//		new ShowRideTask().execute();

		//		System.out.println("Rides: " + currentRides.length);

		if(currentRides != null){
			setListAdapter(new RideArrayAdapter(getActivity(), currentRides));
			rideListView.setTextFilterEnabled(true);
		} else {
			TextView noRidesMessage = (TextView) getActivity().findViewById(R.id.no_ride_text_view);
			//			rideListView.setVisibility(View.GONE);
			//			noRidesMessage.setVisibility(View.VISIBLE);
		}

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Ride selectedRide = currentRides[position];
		Intent openRide = new Intent(getActivity(), RideActivity.class);
		openRide.putExtra(Ride.RIDE_PARCEL_KEY, selectedRide);
		startActivity(openRide);
	}


	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a 
		// new callback to handle the response.
		Request request = Request.newMeRequest(session, 
				new Request.GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
				// If the response is successful
				if (session == Session.getActiveSession()) {
					if (user != null) {
						// Set the id for the ProfilePictureView
						// view that in turn displays the profile picture.
						profilePictureView.setProfileId(user.getId());
						System.out.println(user.getId());
						// Set the Textview's text to the user's name.
						userNameView.setText(user.getName());
						userParams[0] = user.getName();
						userParams[1] = user.getId();
						UserProfileSettings.getUserProfileSettings().setName(user.getName());
						UserProfileSettings.getUserProfileSettings().setFbId((user.getId()));
						new registerTask().execute(userParams);

					}
				}
				if (response.getError() != null) {
					Log.e(getTag(), response.getError().getErrorMessage());
				}
			}
		});
		request.executeAsync();
	} 

	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			// Get the user's data.
			makeMeRequest(session);
		}
	}

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state, final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			uiHelper.onActivityResult(requestCode, resultCode, data);
		}
	}

	public class registerTask extends AsyncTask<String, Void, Void> {

		private HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String user = params[0];
			String user_id = params[1];

			try {
				HttpPost post = new HttpPost(REGISTER_USER_URL);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				JSONObject json = new JSONObject();
				try {
					json.put("name", user);
					json.put("fbId", user_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				post.setEntity(new StringEntity(json.toString()));
				
//				nameValuePairs.add(new BasicNameValuePair("name", user));
//				nameValuePairs.add(new BasicNameValuePair("fbId", user_id));
				//				nameValuePairs.add(new BasicNameValuePair("authenticity_token", "MD2BswheH6dEGHB20NA3ffj9+50Vjb2aDPcTfNUGbRs="));
//				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				Log.i("registerTask","User:" + user);
				Log.i("registerTask","UserID: " + user_id);
				// Execute HTTP Post Request
				response = client.execute(post);


			} catch (IOException e) {
				// TODO Auto-generated catch block
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
				Toast t = Toast.makeText(getActivity(), "Registration unsuccessful", Toast.LENGTH_LONG);
				t.show();
			}
			System.out.println(response.getStatusLine());
			new ShowRideTask().execute();

		}

	}


	public class ShowRideTask extends AsyncTask<Void, Void, Boolean> {

		private HttpClient client = new DefaultHttpClient();
		private static final String SHOW_RIDES_URL = "http://waterloop.sidprak.com/userRides/";

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
				HttpPost post = new HttpPost(SHOW_RIDES_URL);
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				System.out.println(UserProfileSettings.getUserProfileSettings().getFbId());
				param.add(new BasicNameValuePair("user_id", UserProfileSettings.getUserProfileSettings().getFbId()));
				post.setEntity(new UrlEncodedFormEntity(param));

				// Execute HTTP Post Request
				HttpResponse response = client.execute(post);
				System.out.println("SHOW RIDES RESPONSE: " + response.getStatusLine());
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
					return false;
				} else {
					currentRides = NetworkUtils.makeRideArray(response);
				}

			} catch (IOException e) {
				// TODO: handle exceptionconversation
				System.out.println("ERROR: ");
				return false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("ERROR: JSON");
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			TextView noRides = (TextView) getActivity().findViewById(R.id.no_ride_text_view);

			if(currentRides == null){
				noRides.setVisibility(View.VISIBLE);
				return;
			}
			
			if(true){
				setListAdapter(new RideArrayAdapter(getActivity(), currentRides));
			} 
		}
	}
}