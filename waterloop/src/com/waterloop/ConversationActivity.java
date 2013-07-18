package com.waterloop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConversationActivity extends Activity {
	private static final String POST_MSG_URL = "http://testapi.spearmunkie.com/message";
	private static final String GET_MSG_URL = "http://testapi.spearmunkie.com/conversation";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_activity);
		
		Button sendButton = (Button) findViewById(R.id.send_btn);
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText msgView = (EditText) findViewById(R.id.msg_txt);
				String sender = "";
				String receiver = "";
				String msg = (String) msgView.getText().toString(); 
				sendMessage(sender, receiver, msg);
			}
		});
	}
	
	protected void getMessage(String sender, String receiver) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sender", sender));
		params.add(new BasicNameValuePair("receiver", receiver));

		new GetMessageTask().execute(params);
		
	}

	public class GetMessageTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {

		private HttpClient client = new DefaultHttpClient();
		
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
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			
			try{
			// TODO Auto-generated method stub
			HttpPost post = new HttpPost(GET_MSG_URL);
			post.setEntity(new UrlEncodedFormEntity(params[0]));

			// Execute HTTP Post Request
			HttpResponse response = client.execute(post);
			EditText text = (EditText) findViewById(R.id.msg_vw);
			text.setText(readResponse(response));
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
				Toast t = Toast.makeText(getApplicationContext(), "Message successfully sent", Toast.LENGTH_LONG);
				t.show();
			} else {
				Toast t = Toast.makeText(getApplicationContext(), "Error sending message", Toast.LENGTH_LONG);
				t.show();
			}
		}	
	}
	
	protected void sendMessage(String sender, String receiver, String msg) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sender", sender));
		params.add(new BasicNameValuePair("receiver", receiver));
		params.add(new BasicNameValuePair("msg", msg));

		new SendMessageTask().execute(params);
		
	}

	public class SendMessageTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {

		private HttpClient client = new DefaultHttpClient();
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			
			try{
			// TODO Auto-generated method stub
			HttpPost post = new HttpPost(POST_MSG_URL);
			post.setEntity(new UrlEncodedFormEntity(params[0]));

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
				Toast t = Toast.makeText(getApplicationContext(), "Message successfully sent", Toast.LENGTH_LONG);
				t.show();
			} else {
				Toast t = Toast.makeText(getApplicationContext(), "Error sending message", Toast.LENGTH_LONG);
				t.show();
			}
		}	
	}
}
