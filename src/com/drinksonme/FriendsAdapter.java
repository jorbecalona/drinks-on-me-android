package com.drinksonme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;



import com.drinksonme.util.User;
import com.drinksonme.util.ImageLoader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * List adapter for transactions
 *
 */
public class FriendsAdapter extends ArrayAdapter<User>
{
	private Context context;
	private int textViewResourceId;
	private ListActivity test;
	private User[] users;
	public FriendsAdapter(Context context, int textViewResourceId, int textid, User[] users, ListActivity test) {
		super(context, textViewResourceId, textid, users);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.test = test;
		this.users = users;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		LayoutInflater inflater=(test).getLayoutInflater();
		View row=inflater.inflate(textViewResourceId, parent, false);
		
		TextView label=(TextView)row.findViewById(R.id.weekofday);
		label.setText(users[position].toString());

		TextView location=(TextView)row.findViewById(R.id.location);
		User u = (User)users[position];
		location.setText(u.mLastCheckin.mName);
		//location.setText(u.mVenmoId);
		
		String imageURL = u.mPhoto;
		
		ImageView imgView = new ImageView(context);
		imgView = (ImageView)row.findViewById(R.id.icon);
		imgView.getLayoutParams().height = 80;
		imgView.getLayoutParams().width = 80;
		if(imageURL == null || imageURL == "")
		{
			imgView.setImageResource(R.drawable.blank_boy);
		}
		else
		{
			Log.v("Drinks FriendsAdapater", "image url for " + u.mFullName + ": " + u.mPhoto);
			Drawable image = ImageOperations(context, imageURL, "image.jpg");
			imgView.setImageDrawable(image);	
		}
		
		
		return row;
		
	}
	
	private Drawable ImageOperations(Context ctx, String url, String saveFilename) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	
}