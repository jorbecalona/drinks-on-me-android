package com.drinksonme.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.drinksonme.FriendsAdapter;
import com.drinksonme.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


// Class that houses all foursquare related calls 
public class FoursquareManager {
	
	Context mContext;
	FoursquareApi mFsApi;
	
	public static ArrayList<String> sFriendsIDs;
	public static ArrayList<User> sFriends;
	public User[] friends;
	
	public static ArrayList<String> sPeopleAtVenueIDs;
	public static ArrayList<User> sPeopleAtVenue;
	public User[] peopleAtVenue;
	
	public int mWhichOne = 1; //variable indicating which tab (friends or @locaiton) is selected. 1 is friends, 2 is @location
	
	public String sVenueName;
	
	private FriendsAdapter adapter;
	private ListActivity myListActivity;
	

	// Initiate context and ApiUtils
	public FoursquareManager(Context ctxt) {
		mContext = ctxt;
		mFsApi = new FoursquareApi(mContext);
		sFriendsIDs = new ArrayList<String>();
		sFriends = new ArrayList<User>();
		sPeopleAtVenue = new ArrayList<User>();
		
	}
	
	public void SetListActivity(ListActivity test)
	{
		this.myListActivity = test;
	}
	
	
	// Get all foursquare info and set it, return boolean indicating success
	// Get all the data we need in order to populate the lists on the homescreen
	// May return users as they come in??
	public boolean RetrieveFoursquareFriends(Context ctxt, User user, FriendsAdapter adapter) {
		
		this.adapter = adapter;
		// Get array of friends ids
		try { 
			sFriends = new ArrayList<User>();
			sFriendsIDs = mFsApi.getFriendsIDs(mContext);
			
		} catch (JSONException e) {Log.v("ERROR", e.toString());}
		
		// If array is not empty, check for venmo connection and get more fs_user data
		if (sFriendsIDs.size() != 0 || sFriendsIDs!=null){
			
			//new CheckIfOnVenmoTask().execute(sFriendsIDs);
			
			for (String id: sFriendsIDs) {
				new GetFsUserInfoTask_Friends().execute(id); //new, asynchronous way of doing this 
			}
			new GetVenmoUsernamesTask_Friends().execute(); //Get the list of Venmo usernames 
		}
		return true;
    }
	
	public ArrayList<User> GetFriends()
	{
		return sFriends;
	}
	
	public void GetVenueName()
	{
		User self = mFsApi.getFsUserInfo(mContext,  "self");
		String venue_id = self.mLastCheckin.mVenueId;
		sVenueName = mFsApi.getVenueName(venue_id);
		
	}
	
	/*
	 * Return people at venue
	 */
	public boolean RetrievePeopleAtVenue(Context ctxt, User user, FriendsAdapter adapter)
	{
		this.adapter = adapter;
		User self = mFsApi.getFsUserInfo(mContext,  "self");
		String venue_id = self.mLastCheckin.mVenueId;
		
		peopleAtVenue = new User[sPeopleAtVenue.size()];
		
		Log.v("Drinks FoursquareManager", "venue_id: " + venue_id);
		// Get array of friends ids
		try { 
			sPeopleAtVenue = new ArrayList<User>();
			sPeopleAtVenueIDs = mFsApi.getHereNow(venue_id); 
			
		} catch (JSONException e2) {Log.v("ERROR", e2.toString());}
		
		// If array is not empty, check for venmo connection and get more fs_user data
		
		Log.v("Drinks FoursquareManager", "sPeopleAtVenueIDs: " + sPeopleAtVenueIDs);
		Log.v("Drinks FoursquareManager", "size of sPeopleAtVenueIDs: " + sPeopleAtVenueIDs.size()	);
		
		if (sPeopleAtVenueIDs!=null){
			for (String id: sPeopleAtVenueIDs) {
				new GetFsUserInfoTask_Venue().execute(id); //new, asynchronous way of doing this 
			}
			new GetVenmoUsernamesTask_PeopleAtVenue().execute(); //Get the list of Venmo usernames 
		}
		return true;
	}
	
	// Method to Check if users are on Venmo
	
	// ERROR DUE TO INCORRECT ARGUMENT TYPES
	/**private class CheckIfOnVenmoTask extends AsyncTask<ArrayList<String>, Void, Void> {
		
		@Override
		protected Void doInBackground(ArrayList<String>... friendsIDs) {
			
			Log.v("drinks", "inCheckIfOnVenmo");
			// Make the VenmoAPI call to see if user is on Venmo
			VenmoApi mVenmoApi = new VenmoApi(mContext);
			mVenmoApi.checkIfOnVenmo(mContext, friendsIDs);
			return null;
		}

	}**/
	
	
	private class GetVenmoUsernamesTask_Friends extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... voids) {
			JSONArray myArray = new JSONArray();
			try
			{
				Log.v("Drinks FoursquareManager", "sFriendsIDs: " + sFriendsIDs);
				
				VenmoApi.getAllVenmoUsernames(sFriendsIDs);
				
				Log.v("Drinks FoursquareManager", "Venmo array: " + myArray.toString() + "**********");
			}
			catch(Exception e)
			{
				Log.v("Drinks FoursquareManager", "Exception caught: " + e.toString());
			}
			
			/*for(int i=0;i < myArray.length();i++) {
				try {
					JSONObject userJSON = myArray.getJSONObject(i);
					String venmoUsername = (String)userJSON.get("username");
					String foursquare_id = (String)userJSON.get("foursquare_id");
					hashtable.put(foursquare_id, venmoUsername);
				}
				catch(Exception e) {
					Log.v("Drinks Foursquaremanager", "Exception caught in GetVenmoUsernamesTask: " + e.toString());
				}
			}*/
			
			return null;
		}
	}
	
	private class GetVenmoUsernamesTask_PeopleAtVenue extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... voids) {
			JSONArray myArray = new JSONArray();
			try
			{	
				VenmoApi.getAllVenmoUsernames(sPeopleAtVenueIDs);
			}
			catch(Exception e)
			{
				Log.v("Drinks FoursquareManager", "Exception caught: " + e.toString());
			}
			
			
			return null;
		}
	}
	
	// SyncTask to get all FS info for a given friend
	private class GetFsUserInfoTask_Friends extends AsyncTask<String, Void, Void> {
			
		@Override
		protected Void doInBackground(String... ids) {
			
			// Make the VenmoAPI call to see if user is on Venmo
			for(String id: ids) //The size of ids is always just 1.  
			{
				User mUser = mFsApi.getFsUserInfo(mContext, id);
				if(mUser.mPhone != null || mUser.mEmail != null || mUser.mVenmoId != null) {
					sFriends.add(mUser);
				}
				
				//Log.v("Drinks FoursquareManager GetFsUserInfoTask doInBackground", "User added with id: " + id + ", first name: " + mUser.mFirstName + ", full name: " + mUser.mFullName);
				publishProgress();
			}
			
			return null;
		}
		protected void onProgressUpdate(Void...progress)
		{
			//Log.v("Drinks FoursquareManager GetFsUserInfoTask onProgressUpdate", "Size of users: " + sFriends.size());
			   //button.setText("New Size: " + sFriends.size());
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			friends = new User[sFriends.size()];
			Log.v("Drinks FoursquareManager", "Size of sFriends: " + sFriends.size());
			for(int i = 0; i < sFriends.size(); i++)
			{
				if(sFriends.get(i) != null && sFriends.get(i).mFullName != null && sFriends.get(i).mFullName != "") {
					friends[i] = sFriends.get(i);
					//Log.v("Drinks FoursquareManager", "friend added: " + sFriends.get(i).mFullName);
				} 
				else
				{
					friends[i] = new User();
					//Log.v("Drinks FoursquareManager", "friend NOT added: " + sFriends.get(i).mFullName);
				}
			}
			
			adapter = new FriendsAdapter(mContext, R.layout.list_item, R.id.weekofday, friends, myListActivity);
			myListActivity.setListAdapter(adapter);

			  ListView lv = myListActivity.getListView();
			  lv.setOnItemClickListener(new OnItemClickListener() {
				    public void onItemClick(AdapterView<?> parent, View view,
				        int position, long id) {
				      // When clicked, show a toast with the TextView text
				      Log.v("Drinks MainActivity", "something was selected!");
				      
				      User user;
				      if(mWhichOne == 2) {
				    	  Log.v("Drinks Foursquaremanager", "Using peopleAtVenue********");
				    	  user = peopleAtVenue[position];
				      } else {
				    	  Log.v("Drinks Foursquaremanager", "Using friends*************");
				    	  user = friends[position];
				      }
				      
				      Log.v("Drinks FoursquareManager", "user selected was: " + user.mFullName);
				      
				      String recipient = null;
				      
				      try{
				    	  if(user.mVenmoId != null) {
				    		  Log.v("Drinks Foursquaremanager", "user.mVenmoId is not null!!!: " + user.mVenmoId + "*********");
				    		  recipient = user.mVenmoId;
				    	  }
				    	  else {
				    		  Log.v("Drinks Foursquaremanager", "venmoId was was NULL!");
				    		  throw new Exception();
				    	  }
				      }
				      catch(Exception e)
				      {
				    	  Log.v("Drinks Foursquaremanager", "Exception caught when looking up venoid for user: " + user.mFullName);
				    	  try {
						      if(user.mPhone != null && user.mPhone != "") {
						    	  recipient = user.mPhone;
						      } else {
						    	  throw new Exception();
						      }
					      } catch(Exception e2) {
					    	  try {
					    		  if(user.mEmail != null && user.mEmail != "") {
							    	  recipient = user.mEmail;
							      } else {
					    			  throw new Exception();
					    		  }
					    	  } catch(Exception e3) {
					    		  recipient = null;
					    	  }
					      }
				      }
				      
				      
				      Intent sendIntent = VenmoSDK.openVenmoPayment("1001", "abcd", "DrinksOnMe", recipient, "0.01", "for a drink on me!", "pay");
			    		
			    		try
			    		{
			    			myListActivity.startActivity(sendIntent);
			    		}
			    		catch (ActivityNotFoundException e) {
			    			// Venmo native app not install on device, so fallback to web
			    			sendIntent = VenmoSDK.openVenmoPaymentInBrowser("1001", "abcd", "DrinksOnMe", recipient, "0.01", "for testing", "pay");
			    			try
			    			{
			    				myListActivity.startActivity(sendIntent);
			    			}
			    			catch(ActivityNotFoundException e2)
			    			{
			    				Log.e("Venmo MainActivity", "Error: " + e2);
			    			}
			        	}
				    }
				  });	  
		}
	
	}
	
	
	// SyncTask to get all FS info for a given person at the venue you're at
	private class GetFsUserInfoTask_Venue extends AsyncTask<String, Void, Void> {
			
		@Override
		protected Void doInBackground(String... ids) {
			
			// Make the VenmoAPI call to see if user is on Venmo
			for(String id: ids)
			{
				User mUser = mFsApi.getFsUserInfo(mContext, id);
				if(mUser.mPhone != null || mUser.mEmail != null || mUser.mVenmoId != null) {
					sPeopleAtVenue.add(mUser);
				}
				
				//Log.v("Drinks FoursquareManager GetFsUserInfoTask doInBackground", "User added with id: " + id + ", first name: " + mUser.mFirstName + ", full name: " + mUser.mFullName);
				publishProgress();
			}
			
			return null;
		}
		protected void onProgressUpdate(Void...progress)
		{
			//Log.v("Drinks FoursquareManager GetFsUserInfoTask onProgressUpdate", "Size of users: " + sFriends.size());
			   //button.setText("New Size: " + sFriends.size());
		}
		
		protected void onPostExecute(Void result)
		{
			peopleAtVenue = new User[sPeopleAtVenue.size()];
			Log.v("Drinks FoursquareManager", "Size of sPeopleAtVenue: " + sPeopleAtVenue.size());
			for(int i = 0; i < sPeopleAtVenue.size(); i++)
			{
				if(sPeopleAtVenue.get(i) != null && sPeopleAtVenue.get(i).mFullName != null && sPeopleAtVenue.get(i).mFullName != "") {
					peopleAtVenue[i] = sPeopleAtVenue.get(i);
					Log.v("Drinks FoursquareManager", "venue person added: " + sPeopleAtVenue.get(i).mFullName);
				}
				else
				{
					peopleAtVenue[i] = new User();
					//Log.v("Drinks FoursquareManager", "friend NOT added: " + sFriends.get(i).mFullName);
				}
			}
			
			/*adapter = new FriendsAdapter(mContext, R.layout.list_item, R.id.weekofday, peopleAtVenue, myListActivity);
			myListActivity.setListAdapter(adapter);*/

			 /*ListView lv = myListActivity.getListView();
			  lv.setOnItemClickListener(new OnItemClickListener() {
				    public void onItemClick(AdapterView<?> parent, View view,
				        int position, long id) {
				      // When clicked, show a toast with the TextView text
				      Log.v("Drinks MainActivity", "something was selected!");
				      User user = null;
				      String recipient = null;
				      try{
				    	  Log.v("Drinks FoursquareManager", "Length of peopleAtVenue: " + peopleAtVenue.length);
				    	  Log.v("Drinks FoursquareManager", "position: " + position);
				    	  user = peopleAtVenue[position];
					      Log.v("Drinks FoursquareManager", "user selected was: " + user.mFullName);
					      
					      
				    	  if(user.mVenmoId != null) {
				    		  Log.v("Drinks Foursquaremanager", "user.mVenmoId is not null!!!: " + user.mVenmoId + "*********");
				    		  recipient = user.mVenmoId;
				    	  }
				    	  else {
				    		  Log.v("Drinks Foursquaremanager", "venmoId was was NULL!");
				    		  throw new Exception();
				    	  }
				      }
				      catch(Exception e)
				      {
				    	  Log.v("Drinks FoursquareManager", "Exception caught in listener: " + e.toString());
				    	  Log.v("Drinks FoursquareManager", "User: " + user);
				    	  Log.v("Drinks Foursquaremanager", "Exception caught when looking up venoid for user: " + user.mFullName);
				    	  try {
						      if(user.mPhone != null && user.mPhone != "") {
						    	  recipient = user.mPhone;
						      } else {
						    	  throw new Exception();
						      }
					      } catch(Exception e2) {
					    	  try {
					    		  if(user.mEmail != null && user.mEmail != "") {
							    	  recipient = user.mEmail;
							      } else {
					    			  throw new Exception();
					    		  }
					    	  } catch(Exception e3) {
					    		  recipient = null;
					    	  }
					    	  
					      }
				      }
				      
				      
				      Intent sendIntent = VenmoSDK.openVenmoPayment("1001", "abcd", "DrinksOnMe", recipient, "0.01", "for a drink on me!", "pay");
			    		
			    		try
			    		{
			    			myListActivity.startActivity(sendIntent);
			    		}
			    		catch (ActivityNotFoundException e) {
			    			// Venmo native app not install on device, so fallback to web
			    			sendIntent = VenmoSDK.openVenmoPaymentInBrowser("1001", "abcd", "DrinksOnMe", recipient, "0.01", "for testing", "pay");
			    			try
			    			{
			    				myListActivity.startActivity(sendIntent);
			    			}
			    			catch(ActivityNotFoundException e2)
			    			{
			    				Log.e("Venmo MainActivity", "Error: " + e2);
			    			}
			        	}
				    }
				  });*/
		}
	
	}
	
	
	
}