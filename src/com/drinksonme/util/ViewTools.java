package com.drinksonme.util;


import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;


public class ViewTools {
    
	private static ProgressDialog sProgressDialog;
	
	public static Dialog showProgressDialog(Context ctxt, String title, String message) {
        ProgressDialog dialog = new ProgressDialog(ctxt);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                //TODO: Change this.
            	//ctxt.finish();
            }
        });
        sProgressDialog = dialog;
        sProgressDialog.show();
        return sProgressDialog;
    }
	
	

	public static AlertDialog createDialog(Context ctxt, String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
    	builder.setMessage(message)
    		     .setNeutralButton("OK", new DialogInterface.OnClickListener() {
    		     		public void onClick(DialogInterface dialog, int id) {
    		        	dialog.cancel();
    		        }
    		      });
    	return builder.create();
	}
	
    public static void dismissProgressDialog() {
    	try {
    		if (sProgressDialog != null)
    			sProgressDialog.dismiss();
    	} catch (IllegalArgumentException e) {
    		// We don't mind. android cleared it for us.
    	}
    }    
    
    public static String dateString(Date mCreatedAt) {
	    Date today = new Date();

	    String timeString = "";
	    
	    if (mCreatedAt != null) { 
	    	// Get msec from each, and subtract.
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
	    }
	    
	    return timeString;
	}

}
