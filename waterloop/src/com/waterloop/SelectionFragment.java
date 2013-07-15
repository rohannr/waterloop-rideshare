package com.waterloop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class SelectionFragment extends Fragment {

	private static final String TAG = "SelectionFragment";
	private static final String REGISTER_USER_URL = "http://testapi.spearmunkie.com/users";
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	private String[] userParams = new String[2];

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
			new registerTask().execute(userParams);
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
		
		return view;
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
						// Set the Textview's text to the user's name.
						userNameView.setText(user.getName());
						userParams[0] = user.getName();
						userParams[1] = user.getId();
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
				nameValuePairs.add(new BasicNameValuePair("user[name]", user));
				nameValuePairs.add(new BasicNameValuePair("user[fb_id]", user_id));
				nameValuePairs.add(new BasicNameValuePair("authenticity_token", "Y7Y0RpSphkkpeGKYj/lqE+soHITuAHcHvnqcWTURmms="));
				nameValuePairs.add(new BasicNameValuePair("commit", "Create user"));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = client.execute(post);

			} catch (IOException e) {
		        // TODO Auto-generated catch block
		    }

			return null;
		}
		
	}
}