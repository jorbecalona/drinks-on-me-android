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
import android.widget.TextView;
import android.widget.Toast;

import com.drinksonme.util.DrinksSettings;
import com.drinksonme.util.FoursquareManager;
import com.drinksonme.util.User;
import com.drinksonme.util.ViewTools;

public class UserListActivity extends ListActivity {
   
	// Launched when the app is first opened
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  String[] countries = new String[4];
	  countries[0] = "test1";
	  countries[1] = "test2";
	  countries[2] = "test3";
	  countries[3] = "test4";
	  
	  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, countries);
	  setListAdapter(adapter);

	  //ListView lv = getListView();

	  /*lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		      // When clicked, show a toast with the TextView text
		      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
		          Toast.LENGTH_SHORT).show();
		    }
		  });*/
	  
	}
	
}