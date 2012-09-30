package com.drinksonme.util;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

//Class that houses all venmo API calls 
public class VenmoApi {
	
	String ROOT = "https://api.foursquare.com/v2/";
	String VERSION = "&v=20110908";
	
	private static final String myAppId = "1171";
	
	String CLIENT_ID = "DQ0F3QJ3AUDKN5EG11VTXKLC02XG1OP1ZJ0OI5LFF5SFIDJ5"; //need to get this from foursquare (https://foursquare.com/oauth/register)
	static String SECRET = "yy3qJsMsmGDeM9j4JreTn8AcYgtNJbjP";  //need to get this from Venmo (https://venmo.com/account/app/new)
	
	
	public static Hashtable<String,String> venmoHashtable = new Hashtable<String,String>();
	
	Context mContext;
	static ApiUtils mApi;
	ArrayList<User> mFriends;
	ArrayList<String> mFriendsIDs;
	ArrayList<String> mFriendsPhotos;


	// Initiate context and ApiUtils
	public VenmoApi(Context ctxt) {
		mContext = ctxt;
		mApi = new ApiUtils();
		mFriendsPhotos = new ArrayList<String>();
		mFriendsIDs = new ArrayList<String>();
		mFriends = new ArrayList<User>();
	}
	
	public static void getAllVenmoUsernames(ArrayList<String> fsIDs_array)
	{
		Log.v("Drinks VenmoAPI", "reached getAllVenmoUsernames**********");
		String fsIDs = "";
		
		if(fsIDs_array.size() > 0) {
			fsIDs += fsIDs_array.get(0);
		}
		
		for(int i = 1; i < fsIDs_array.size(); i++) {
			fsIDs += "," + fsIDs_array.get(i);
		}
		String url = "https://venmo.com/api/v2/user_find?client_id=" + myAppId + "&client_secret=" + SECRET + "&foursquare_ids=" + fsIDs;
		
		Log.v("Drinks VenmoAPI", "URL sent: " + url + "*************");
		
		mApi = new ApiUtils();
		JSONObject rawJSON = mApi.doHTTPRequest(url);
		
		JSONArray users = null;
		// Parse the JSON
		try {
			users = (JSONArray)rawJSON.get("data");
			//Log.v("Drinks VenmoAPI", "users: " + users.toString(2));
			
		} catch (JSONException e) {Log.v("Drinks ERROR", e.toString());}
		
		
		for(int i=0;i < users.length();i++) {
			try {
				JSONObject userJSON = users.getJSONObject(i);
				
				Log.v("Drinks VenmoApi", "userJSON: " + userJSON.toString());
				
				String venmoUsername = (String)userJSON.get("username");
				Log.v("Drinks VenmoApi", "venmoUsername: " + venmoUsername);
				String foursquare_id = (String)userJSON.get("foursquare_id");
				Log.v("Drinks VenmoApi", "foursquare_id: " + foursquare_id);
				
				
				venmoHashtable.put(foursquare_id, venmoUsername);
				Log.v("Drinks VenmoApi", "successfully added********************");
			}
			catch(Exception e) {
				Log.v("Drinks VenmoApi", "Exception caught in VenmoApi: " + e.toString());
			}
		}
		
		
	}
	
	public static String GetVenmoId(String fsID)
	{
		String url = "https://venmo.com/api/v2/user_find?client_id=" + myAppId + "&client_secret=" + SECRET + "&foursquare_ids=" + fsID;
		mApi = new ApiUtils();
		JSONObject rawJSON = mApi.doHTTPRequest(url);
		
		String venmoId = null;
		// Parse the JSON
		try {
			JSONArray users = (JSONArray)rawJSON.get("data");
			//Log.v("Drinks VenmoAPI", "users: " + users.toString(2));
			
			for(int i=0;i < users.length();i++) {   
				JSONObject userJSON = users.getJSONObject(i);
				venmoId = (String)userJSON.get("username");
			}
			
		} catch (JSONException e) {Log.v("Drinks ERROR", e.toString());}
		
		//Log.v("Drinks VenmoAPI", "VenmoID for fsID" + fsID + "is: " + venmoId);
		
		return venmoId;
	}
	
	
	// Determine if array of fsIds are on venmo
	public void checkIfOnVenmo(Context ctxt, ArrayList<String> fsIds) throws JSONException{
		
		Boolean onVenmo; 
		
		for (String id : fsIds) {
			
			onVenmo = (Integer.parseInt(id) % 2 == 0); // Pseudo check to see if on Venmo
				
				//Find the user who matches the user ID and make set their onVenmo attribute.
				
		}
	}
	
}
