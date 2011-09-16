package com.drinksonme;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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

public class PickVenueActivity extends ListActivity {

	Context mContext;
	ApplicationState mAppState;
	User mUser = new User();
	Button button;
	FriendsAdapter adapter;
	
   
	// Launched when the app is first opened
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.v("drinks", "in Main Activity");
        
        mAppState = (ApplicationState)getApplicationContext();
        mContext = this;
        
        setContentView(R.layout.pickvenue);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.v("Drinks MainActivity", "longitude: " + longitude);
        Log.v("Drinks MainActivity", "latitude: " + latitude);
       
    }
    
    
    
}