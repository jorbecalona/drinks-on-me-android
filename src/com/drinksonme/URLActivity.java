package com.drinksonme;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drinksonme.util.VenmoResponse;
import com.drinksonme.util.VenmoSDK;

//import com.venmo.demo.VenmoSDK.VenmoResponse; //Make sure this is included

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Handles venmo1001:// URIs
 */
public class URLActivity extends Activity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Log.d("Venmo PropsURLActivity", "reached PropsURLActivity!!!");
		
		if (getIntent().getData() == null) {
			Log.d("Venmo PropsURLActivity", "getIntent().getData() is NULL");
			finish();
		}
		Log.d("Venmo PropsURLActivity", "getIntent().getData(): " + getIntent().getData());
		
		Uri data = getIntent().getData();
		String signed_request = data.getQueryParameter("signed_request");
		
		if (signed_request == null) {
			Log.d("Venmo PropsURLActivity", "signed request is null");
			finish();
		}
		
		Log.d("Venmo PropsURLActivity", "signed_request: " + signed_request);
		
		//VenmoSDK myVenmoSDK = new VenmoSDK(myAppId, myAppLocalId, myAppName); //add in appSecret, appName, appLocalId
		String app_secret = "yy3qJsMsmGDeM9j4JreTn8AcYgtNJbjP"; //secret key of app registered with Venmo
		VenmoResponse response = VenmoSDK.validateVenmoPaymentResponse(signed_request, app_secret);
		
		setContentView(R.layout.main);
		
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("note", response.getNote()); //each of these will be null if the transaction did not go through successfully.    
		i.putExtra("amount", response.getAmount());
		i.putExtra("payment_id", response.getPaymentId());
		i.putExtra("success", response.getSuccess());
		
		startActivity(i);
		finish();
	}
}
