package com.waterloop.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.waterloop.Ride;

public class NetworkUtils {

	public static String jsonParser(HttpResponse response) throws IOException{

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			StringBuilder builder = new StringBuilder();
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		}
		return null;
	}
	
	/**
	 * @param jsonSearchResults
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static Ride[] makeRideArray(HttpResponse response) throws JSONException, IOException {
		JSONArray jsonSearchResults = new JSONArray(readResponse(response));
//		System.out.println(readResponse(response));
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
//				JSONArray passengers = (JSONArray) jsonObject.get("passengers");
				
				
				System.out.println(name);
				System.out.println(origin);
				System.out.println(destination);
				System.out.println(price);
				System.out.println(numSeats);


				
				//possibly unnecessary
//				if(passengers.length == 0){
//					passengers = null;
//				}
				Ride ride = new Ride(origin, destination, date, name, null, price, numSeats, rideID);
				result[i] = ride;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/*
	 * Parses HTTP response to make a JSONArray
	 */
	public static String readResponse(HttpResponse response) throws IOException{
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
