package com.drinksonme;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.drinksonme.util.DrinksSettings;
import com.drinksonme.util.FoursquareManager;
import com.drinksonme.util.User;
import com.drinksonme.util.ViewTools;
import com.drinksonme.util.SegmentedRadioGroup;

import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends ListActivity implements OnCheckedChangeListener {

	Context mContext;
	ApplicationState mAppState;
	User mUser = new User();
	Button button;
	FriendsAdapter adapter;
   
	// Launched when the app is first opened
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Drinkstest MainActivity", "Reached Main Activity");
        
        Log.v("drinks", "in Main Activity");
        
        mAppState = (ApplicationState)getApplicationContext();
        mContext = this;
        
        mAppState.getFoursquareManager().SetListActivity(this);
        
        Log.v("drinks", DrinksSettings.getFoursquareToken(mContext));
        
       // If they are not already connected to foursquare, connect them
       if(DrinksSettings.getFoursquareToken(mContext).equals("")
    		   || DrinksSettings.getFoursquareToken(mContext)==null){
    	   foursquareOauth();
       }else{
    	   
    	   //button = new Button(mContext);
	       //button.setText("Hello, Android");
	       setContentView(R.layout.main);
    	   
    	   // Start foursquare API calls to get friends list and last check-in
           new GetFoursquareFriendsTask().execute();
           new GetPeopleAtVenueTask().execute(); 
           
           User[] loading = new User[1];
           User user_loading_placeholder = new User();
           user_loading_placeholder.setName("loading...");
           loading[0] = user_loading_placeholder;
           
           //adapter = new FriendsAdapter(mContext, R.layout.list_item, R.id.weekofday, loading, this);
		   //this.setListAdapter(adapter);
           
           /*Intent i = new Intent(this, UserListActivity.class);
           startActivity(i);*/
           SegmentedRadioGroup segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
           segmentText.setOnCheckedChangeListener(this);
        	
       }       
       
    	 // Retrieve this user's data
    	 // Launch the task to load the contacts screen 
       	
       
       
    }
	
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		Log.v("Drinks MainActivity", "onCheckedChanged listener called!!! checkedId:" + checkedId);
		switch(checkedId)
		{
		case R.id.button_one:
			Log.v("Drinks MainActivity", "Friends selected!");
			try
			{
				adapter = new FriendsAdapter(mContext, R.layout.list_item, R.id.weekofday, mAppState.getFoursquareManager().friends, this);
				mAppState.getFoursquareManager().mWhichOne = 1;
			}
			catch(Exception e)
			{
				Log.e("Drinks MainActivity", "Error: " + e.toString());
			}
			this.setListAdapter(adapter);
			break;
		case R.id.button_two:
			Log.v("Drinks MainActivity", "Location selected!");
			try
			{
				adapter = new FriendsAdapter(mContext, R.layout.list_item, R.id.weekofday, mAppState.getFoursquareManager().peopleAtVenue, this);
				mAppState.getFoursquareManager().mWhichOne = 2;
			}
			catch(Exception e)
			{
				Log.e("Drinks MainActivity", "Error: " + e.toString());
			}
			this.setListAdapter(adapter);
			
			break;
		}
	}
	
	
    // Listen and process results from the foursquare oauth
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
    	/** Use this to process ANY activity results that come through; Call startActivityForResult(intent, requestCode)
    	 * For example, see TransactionActivity
    	switch(requestCode)
    		case 1:	*/
    			
    	// Check to see if connection was successful
		if (resultCode == RESULT_OK 
				&& data.getExtras().get("user_allowed_fs_connection") != null
				&& data.getExtras().get("user_allowed_fs_connection").equals("1")){
				
			// Store the token
			String token = (String) data.getExtras().get("user_token");
			DrinksSettings.setFoursquareToken(mContext, token);
			new GetFoursquareFriendsTask().execute();
			new GetPeopleAtVenueTask().execute(); 
			Log.v("Drinks MainActivity", "About to start HelloList from onActivityResult");
			/*Intent i = new Intent(this, UserListActivity.class);
	        startActivity(i);*/
			
			try
			{
				SegmentedRadioGroup segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
			    segmentText.setOnCheckedChangeListener(this);
			}
			catch(Exception e)
			{
				Log.v("Drinks MainActivity", "Error: exception caught when trying to add segment radio: " + e.toString());
			}
			
		} 
		
		// If not successful, pop up warning
		else {
			AlertDialog d = ViewTools.createDialog(mContext, "Sorry, we were unable to connect you to foursquare");
			d.show();
			// Create a listener to see when they have dismissed or clicked OK and then re prompt them to connect
			// foursquareOauth();
		}
    }
    
    
    
    // Async task to get the user's foursquare info
	// Verify password if user has security settings enabled.
	private class GetFoursquareFriendsTask extends AsyncTask<Void, Void, Void> {
		
		Boolean mResult;

		@Override
		protected Void doInBackground(Void... voids) {
			Log.v("Drinkstest MainActivity", "Reached GetFoursquareFriends task, doInBackground");
			// Make the FoursquareAPI call to retrieve foursquare info
			
			// Unsure of what the return type should be, could be User and then you would populate the users as they come in
			// One way to get data would be to access the public array that's part of the FoursquareManager
			// mAppState.getFoursquareManager().sFriends;
			
			mResult = mAppState.getFoursquareManager().RetrieveFoursquareFriends(mContext, mUser, adapter); // May not need to pass in user
			
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			Log.v("Drinkstest MainActivity", "Reached onPostExecute of GetFoursquareFriendsTask");
			if (mResult == true) {
				//Run the task to show the contacts.  modified: now they are shown in FoursquareManager.java
			} 
			else {
				AlertDialog d = ViewTools.createDialog(mContext,
						"Unable to retreive your foursquare data :(");
				d.show();
				foursquareOauth();
			}
		} 
	}

	private class GetPeopleAtVenueTask extends AsyncTask<Void, Void, Void> 
	{
		private boolean mResult;
		protected Void doInBackground(Void...voids)
		{
			Log.v("Drinkstest MainActivity", "Reached GetPeopleAtVenueTask, doInBackground");
			mAppState.getFoursquareManager().GetVenueName();
			mResult = mAppState.getFoursquareManager().RetrievePeopleAtVenue(mContext, mUser, adapter);
			return null;
		}
		protected void onPostExecute(Void  v)
		{
			Log.v("Drinkstest MainActivity", "Reached onPostExecute of GetFoursquareFriends task");
			try
			{
				RadioButton tab = (RadioButton)findViewById(R.id.button_two);
				tab.setText("@ " + mAppState.getFoursquareManager().sVenueName);
			}
			catch(Exception e)
			{
				Log.e("Drinks FoursquareManage", "Error grabbing radiobutton: " + e.toString());
			}
		}
	}
	
	// Prompt foursquare Oauth Activity
	private void foursquareOauth() {
		Log.v("Drinks MainActivity", "Starting foursquareOauth");
		Intent intent = new Intent(MainActivity.this, FoursquareOauthActivity.class);
			startActivityForResult(intent, 1);
	}
	
	// Method to populate the lists
	// get the lists from the foursquareManager
	// 
    
    
    
}