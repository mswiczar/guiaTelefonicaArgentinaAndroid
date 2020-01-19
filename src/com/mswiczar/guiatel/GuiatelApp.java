package com.mswiczar.guiatel;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Application;
import android.graphics.Bitmap;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;


public class GuiatelApp extends Application 
{

	public HashMap<String,Bitmap> urlToBitmap;
	public GoogleAnalyticsTracker tracker;
	public double the_latitude;
	public double the_longitude;
    public ArrayList<HashMap<String,String> > listItems;
	DataBaseHelper myDbHelper;

    
	public GuiatelApp ()
	{
		super();
        tracker = GoogleAnalyticsTracker.getInstance();
        urlToBitmap = new HashMap<String,Bitmap>();
        listItems = new ArrayList<HashMap<String,String>>() ;
        
        
	}
	
}
