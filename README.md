DrinksOnMe is an app built on top of Venmo that demonstrates a cool app you can build using Venmo and Foursquare.  
It allows you to buy drinks for your friends or for people who are checked in to the same place you are! 
Note that the only people at your location who will show up are those that have their Venmo accounts linked to their foursquare accounts, and your friends.  

If you are interested in building an app that uses the Venmo SDK like DrinksOnMe does, then be sure to check out the Android SDK documentation: https://github.com/venmo/venmo-android-sdk

Note that the Foursquare and Venmo secret keys have been removed from the DrinksOnMe app, so the app will not work until you enter these. 

To get these keys, first you will need to register your app with Venmo at https://venmo.com/account/app/new and with Foursquare at https://foursquare.com/oauth/register.

From Venmo you will get a client id and a secret key.  The client id needs to be entered in utils/FoursquareManager.java, on line 351, in the variable "app_id".  It also needs to be entered in the manifest file in place of 9999 (near the bottom, where you see venmo9999abcd). The secret key needs to be entered in two places: the variable SECRET at the top of Utils/VenmoAPI.java, and the variable app_secret in URLActivity.java.  

From Foursquare you will get a callback_url and a client_id.  These both need to be entered at the top of FoursquareOauthActivity.java (in CALLBACK_URL and CLIENT_ID, respectively).  The client_id also needs to be added at the top of VenmoAPI.java (the variable CLIENT_ID).

After doing this, DrinksOnMe should work!  Note that the app is kind of slow, especially if you have many foursquare friends.  A known bug is that when you first log in through foursquare, you only see your friends tab (not the location tab). But when you close and re-open the app, then the @location people appear.  
