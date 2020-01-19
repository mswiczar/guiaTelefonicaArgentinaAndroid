package com.mswiczar.guiatel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class themapa extends MapActivity 
{    
	
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	HelloItemizedOverlay itemizedOverlay;
	
	
	private MapController mapController;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amaps);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
        GuiatelApp app = (GuiatelApp) getApplication();

        
        int lat = (int) (app.the_latitude * 1E6);
		int lng = (int) (app.the_longitude* 1E6);
		GeoPoint point = new GeoPoint(lat, lng);
		mapController = mapView.getController();
		mapController.animateTo(point); 
		mapController.setZoom(14);
        
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.pin);
		itemizedOverlay = new HelloItemizedOverlay(drawable,this);
       
		ArrayList<HashMap<String,String> > listItems;

        listItems = app.listItems ;
        
        int alat;
        int along;
        HashMap<String,String> o;
        
        /*
				<business>
					<Sno>1</Sno>
					<name>Clearview Cinemas</name>
					<address>97 Main St Ste A</address>
					<city>Chatham</city>
					<state>NJ</state>
					<country>USA</country>
					<zip>07928</zip>
					<phone>9085989191</phone>
					<website></website>
					<latitude>40.7393</latitude>
					<longitude>-74.376</longitude>
					<fulladdress>97 Main St Ste A Chatham NJ</fulladdress>
					<distance>0.71248860105595</distance>
					</business>
         */
        
        
        
        
        for (int ccc=0; ccc<listItems.size()-2; ccc++)
        {
        	
            o = listItems.get(ccc);
            
            if (o != null) 
            {
            	
	            // Log.v("BACKGROUND_PROC", o.get("name") +" latitude - "+ o.get("latitude"));
	            // Log.v("BACKGROUND_PROC", o.get("name")  +" longitude - "+ o.get("longitude"));
            	
            	double dlat = Double.valueOf(o.get("latitude"));
            	double dlong = Double.valueOf(o.get("longitude"));
            	
                alat = (int) (dlat  * 1E6);
    			along = (int) (dlong * 1E6);
    			
    			GeoPoint apoint = new GeoPoint(alat, along);
    			OverlayItem overlayitem = new OverlayItem(apoint, o.get("name"), o.get("fulladdress"));
    			
    			itemizedOverlay.addOverlay(overlayitem);
    			mapOverlays.add(itemizedOverlay);
    			
            }
        	
        	
        }
        
		
		
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    
    @SuppressWarnings("rawtypes")
	private  class HelloItemizedOverlay extends ItemizedOverlay {
    	
    	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    	Context mContext;
    	private int indice;


    		public HelloItemizedOverlay(Drawable defaultMarker) {
    			super(boundCenterBottom(defaultMarker));		
    		}

    		public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
    		    /*This passes the defaultMarker up to the default constructor 
    		     * to bound its coordinates and then initialize mContext with the given Context.*/
    		    super(boundCenterBottom(defaultMarker));
    		      mContext = context;
    		      
    		}
    		
    		public void addOverlay(OverlayItem overlay) {
    		    mOverlays.add(overlay);
    		    populate();
    		}
    		@Override
    		protected OverlayItem createItem(int i) {
    		  return mOverlays.get(i);
    		}

    		@Override
    		public int size() {
    			return mOverlays.size();
    		}	

    		
    		
    		
    		protected boolean onTap(int index) {
    			/*This uses the member android.content.Context to create 
    			 * a new AlertDialog.Builder and uses the tapped OverlayItem's title 
    			 * and snippet for the dialog's title and message text. (You'll see the 
    			 * OverlayItem title and snippet defined when you create it below.)*/

    			  OverlayItem item = mOverlays.get(index);
    			  indice = index;
    			  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
    			  dialog.setTitle(item.getTitle());
    			  dialog.setMessage(item.getSnippet());
    			  
    			  dialog.setCancelable(false);
    		      dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() 
    		      {
    		           public void onClick(DialogInterface dialog, int id) 
    		           {
    		        	   dialog.cancel();
 
    		           }
    		       });
    		       
    		       dialog.setNegativeButton("More Info", new DialogInterface.OnClickListener() {
    		           public void onClick(DialogInterface dialog, int id) {
    		               HashMap<String,String> o;
    		               GuiatelApp app = (GuiatelApp) getApplication();
    		               o = app.listItems.get(indice);
		                	Intent intent = new Intent(mContext, detail.class);
	                		intent.putExtra("Sno",o.get("Sno") );
	                		intent.putExtra("name",o.get("name") );
	                		intent.putExtra("address",o.get("address") );
	                		intent.putExtra("state",o.get("state") );
		                	intent.putExtra("country",o.get("country") );
		                	intent.putExtra("zip",o.get("zip") );
		                	intent.putExtra("phone",o.get("phone") );
		                	intent.putExtra("website",o.get("website") );
		                	intent.putExtra("latitude",o.get("latitude") );
		                	intent.putExtra("longitude",o.get("longitude") );
		                	intent.putExtra("fulladdress",o.get("fulladdress") );
	                	startActivity(intent);

    		           }
    		       });		  
    			  
    		       
    			   dialog.show();
    			  
    			  
    			   return true;
    			}
    		
    		
    		
    	}

    
    
}


