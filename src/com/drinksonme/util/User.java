package com.drinksonme.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

// Holds all foursquare and venmo data for a user
public class User {
	public String mFsId;
	public String mEmail;
	public String mPhone;
	public String mTwitter;
	public String mFacebook;
	public String mFirstName;
	public String mLastName;
	public String mFullName;
	public String mPhoto;
	public LastCheckin mLastCheckin;
	public Boolean mOnVenmo;
	public String mVenmoId;

	
	// Class that holds all the data related to the users last checkin
	public class LastCheckin {
		public Integer mCreatedAt; //the last time they checked in, in msec from 1970 format 
		public String mPrettyDate; // THIS we need to create 
		public Double mLng;
		public Double mLat;
		public String mCity;
		public String mCrossStreet;
		public String mVenueId;
		public String mName;
		public String mState;
		public String mAddress;
	}
	
	public String getImageUrl()
	{
		return mPhoto;
	}
	
	public String toString()
	{
		return mFullName;
	}
	
	public static String dateString(String mRawCreatedAt) {
	    
		//Log.v("Drinks User datestring", "mRawCreatedAt: " + mRawCreatedAt + "******************");
		
		Date today = new Date();

	    String timeString = "";
	    Date mCreatedAt = null;
	    if (mCreatedAt == null) {
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss ZZZ"); // Sun, Aug 08 2010 01:53:15 GMT
			try {
				mCreatedAt = formatter.parse(mRawCreatedAt);
				//Log.v("Drinks User datestring", "try block. mCreatedAt: " + mCreatedAt);
			} catch (Exception e) {
				e.toString();
				//Log.v("Drinks User datestring", "Catch block. e: " + e.toString());
			}
	    }
	    
	    if (mCreatedAt != null) { 
	    	// Get msec from each, and subtract.
	    	//Log.v("Drinks User datestring", "mCreatedAt is not null");
	    	long diff = today.getTime() - mCreatedAt.getTime();
	    	Long daysOld = (diff / (1000 * 60 * 60 * 24));
	    	if (daysOld > 0) {
	    		timeString += daysOld.toString() + " days";
	    	} else {
	    		Long hoursOld = (diff / (1000 * 60 * 60));
	    		if (hoursOld > 0) {
	    			timeString += hoursOld + " hours";
	    		} else {
	    			Long minutesOld = (diff / (1000 * 60));
	    			timeString += minutesOld + " minutes";
	    		}
	    	}
	    	
	    	// If it's a zero don't make the unit plural...
	    	if (timeString.substring(0, 1).equals("1") && timeString.substring(1,2).equals(" ")) {
	    		timeString = timeString.substring(0, timeString.length() - 1);
	    	}
		
	    	timeString += " ago";
	    }
		
		return timeString;
	}
	
	// Parses all the user info from json into a user object
	public static User userFromJson(JSONObject json) {
		User mUser = new User();
		mUser.mLastCheckin = mUser.new LastCheckin();
		try {
			
			// Set general info
			mUser.mFsId = (String)json.get("id");
			
			
			mUser.mFirstName = (String)json.get("firstName");
			//Log.v("Drinks User", "User first name: " + mUser.mFirstName);
			
			try
			{
				if(json.get("lastName") != null) {
					mUser.mLastName = (String)json.get("lastName");
				} else {
					mUser.mLastName = "";
				}
			}
			catch(Exception e)
			{
				mUser.mLastName = "";
			}
			
			mUser.mFullName = mUser.mFirstName + " " + mUser.mLastName;
			//Log.v("Drinks User", "User full name: " + mUser.mFullName);
			
			//Log.v("Drinks User", "User last name: " + mUser.mLastName);
			
			try
			{
				//mUser.mVenmoId = VenmoApi.GetVenmoId(mUser.mFsId);
				mUser.mVenmoId = VenmoApi.venmoHashtable.get(mUser.mFsId);
				//Log.v("Drinks User", "mUser.mVenmoId has been set for " + mUser.mFullName + "!: " + mUser.mVenmoId);
			}
			catch(Exception e)
			{
				Log.v("Drinks User", "Exception caught when trying to get Venmo ID:" + e.toString());
			}
			
			// Set contact info
			JSONObject contact = (JSONObject)json.get("contact");
				try {
					if (contact.has("email")) 
						mUser.mEmail = (String)contact.get("email");
					if (contact.has("facebook")) 
						mUser.mFacebook = (String)contact.get("facebook");
					if (contact.has("phone")) 
						mUser.mPhone = (String)contact.get("phone");
					if (contact.has("twitter")) 
						mUser.mTwitter = (String)contact.get("twitter");
				} catch (JSONException e) { Log.v("Drinks ERROR", e.toString());}
				
				
			// Set last checkin data
			JSONObject checkins = (JSONObject)json.get("checkins");
		
			JSONArray items = (JSONArray)checkins.get("items");
			JSONObject item = (JSONObject)items.get(0);
			mUser.mLastCheckin.mCreatedAt = (Integer)item.get("createdAt");
			
			JSONObject venue = (JSONObject)item.get("venue");
			
			mUser.mLastCheckin.mVenueId = (String)venue.get("id");
			mUser.mLastCheckin.mName = (String)venue.get("name");
			JSONObject location = (JSONObject)venue.get("location");
			mUser.mLastCheckin.mAddress = (String)location.get("address");
			mUser.mLastCheckin.mCrossStreet = (String)location.get("crossStreet");
			mUser.mLastCheckin.mLat = (Double)location.get("lat");
			mUser.mLastCheckin.mLng = (Double)location.get("lng");
			mUser.mLastCheckin.mCity = (String)location.get("city");
			
			
			
			String date_string = mUser.mLastCheckin.mCreatedAt.toString();
			mUser.mLastCheckin.mPrettyDate = dateString(date_string);
			
			mUser.mPhoto = (String)json.get("photo");
				
				

			} catch (JSONException e) { Log.v("DRinks ERROR", e.toString());}

		//Log.v("drinks", "END" + mUser.mFirstName);
		return mUser;
	}
	

}
