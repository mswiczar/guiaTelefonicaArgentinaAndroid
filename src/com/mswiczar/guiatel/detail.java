package com.mswiczar.guiatel;


/*
 * 
 * 
 * CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT , aname varchar(128), aadress varchar(128) , aurl varchar(128) , atel varchar(128) , x float , y float, latitude varchar(128) , longitude varchar(128));

 */

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class detail extends MapActivity {
	
	private String nombre;
	private String direccion;
	private String tel;
	private String latitud;
	private String longitud;
	
	ProgressDialog dialog;
	private Runnable viewOrders;
	Thread thread;

	String resultado;
	
	public MapController mapController;
	public MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable;
	HelloItemizedOverlay itemizedOverlay;

	
	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
			
			
			
	private void updateLocation() 
	{
		
   	    
   	    try
   	    {
   			String query = URLEncoder.encode(direccion, "utf-8");
   	    	String strUrl = "http://maps.google.com/maps/geo?q="+ query+"&output=csv";
   	    	//Log.v( "strUrl " , strUrl);
   	    	
   	    	XmlReaderToHashMap axmlreader =  new XmlReaderToHashMap();
   	   	    axmlreader.setTheURL(strUrl);
   	   	    resultado = axmlreader.getCVSData();
   	    	//Log.v( "maps " , resultado);
   	    	
   	    	
   	    	
   	    }
        catch (Exception e) 
        { 
      	// Log.e("Parse Location", e.getMessage());
        }

   	    	
   	    	
	}
	
	private void addToFavorites()
	{
		
		/*
		 * 
		 * 
		 * CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT , aname varchar(128), aadress varchar(128) , aurl varchar(128) ,
		 *  atel varchar(128) , x float , y float, latitude varchar(128) , longitude varchar(128));

		 */
        GuiatelApp app = (GuiatelApp) getApplication();

        Cursor acur = app.myDbHelper.createCursorFavoriteExists(nombre,tel);
        acur.moveToNext();
        
        Log.e("DB", acur.getString(0));
        int val = acur.getInt(0);
        
        
        
        if (val!=0)
        {
			  AlertDialog.Builder dialog = new AlertDialog.Builder(detail.this);
			  dialog.setTitle("Fovoritos");
			  dialog.setMessage(nombre + " Ya se encuentra en Favoritos");
			  
		      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		      {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	   dialog.cancel();

		           }
		       });

			   dialog.show();

        	
        	
        }
        else
        {
        	//CREATE TABLE favorites (id integer PRIMARY KEY AUTOINCREMENT ,  nombre varchar(128) , direccion varchar(255) , tel varchar(128),latitud varchar(64) , longitud varchar(64));

    		String sql = "insert into favorites  (nombre, direccion, tel,latitud,longitud) " +
    				"VALUES ('" + nombre + "', '" + direccion + "', '"+tel+"', '"+latitud+"' ,'"+longitud+"')";
    				Log.e("Test Saving", sql);
    				app.myDbHelper.myDataBase.execSQL(sql);
    				
    				
    				  AlertDialog.Builder dialog = new AlertDialog.Builder(detail.this);
    				  dialog.setTitle("Favorites");
    				  dialog.setMessage(nombre + " Agregado en Favoritos");
    				  
    			      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
    			      {
    			           public void onClick(DialogInterface dialog, int id) 
    			           {
    			        	   dialog.cancel();

    			           }
    			       });

    				   dialog.show();
        }
        
        
        

		
		
		
	}
	
	private void call() {
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+tel));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("iGPS", "Call failed", e);
	    }
	}

	private void showInMap()
	{
	
	try {
        final Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+latitud+","+longitud+"?z=14&q="+latitud+","+longitud+"("+nombre +")"));
        startActivity(myIntent);
		} 
	catch (ActivityNotFoundException e) 
		{
        	Log.e("iGPS", "Map failed", e);
		}
	
	}
	
	private void addToAddressBook()
	{
	
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

		intent.putExtra(ContactsContract.Intents.Insert.NAME, nombre);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, tel);
		intent.putExtra(ContactsContract.Intents.Insert.POSTAL, direccion);

		startActivity(intent);
	
	}
	
	

	
	    
	private Runnable returnRes = new Runnable() 
    {
    	
        public void run() 
        {
        	try 
        	{
        		
				Thread.sleep(10);
				
	   	    	StringTokenizer st = new StringTokenizer(resultado,",");
	   	    	st.nextToken();
	   	    	int i=0;
	   	    		
		   	    	while(st.hasMoreTokens())
		   	    	{
		   	    		i++;
		   	    		String n = (String)st.nextToken();
		   	    		if (i==2)
		   	    		{
		   	    			// latitud
		   	    			latitud= n;
		   	    		}
		   	    		if (i==3)
		   	    		{
		   	    		// longitud
		   	    		 longitud =n;
		   	    		}
		   	    		
		   	    	}
	   	    		
	                 double dlat = Double.valueOf(latitud).doubleValue();
	             	 double dlong = Double.valueOf(longitud).doubleValue();

		   	    	
		   	    	
		   	        int lat = (int) (dlat * 1E6);
		   			int lng = (int) (dlong* 1E6);
		   			GeoPoint point = new GeoPoint(lat, lng);
		   			
		   			mapController = mapView.getController();
		   			mapController.animateTo(point); 
		   			mapController.setZoom(17);
		   	        
		   			mapOverlays = mapView.getOverlays();
		   	    	
		   	    	
	    			OverlayItem overlayitem = new OverlayItem(point,nombre, direccion);
	    			itemizedOverlay.addOverlay(overlayitem);
	    			mapOverlays.add(itemizedOverlay);

		   			
		   			
		   			
		   	    	
		   	    	

				
			} 
        	catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
        	dialog.dismiss();
        }
    };
    

	
	
	private void getOrders()
	{
        try
        {
	      	  Thread.sleep(100);
	      	  updateLocation();
          } 
          catch (Exception e) 
          { 
        	  Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
	}     





    
    
    @Override
    protected void onStop(){
    	if (thread!=null)
    	{
    		thread.stop();
    	}
    	if (dialog!= null)
    	{
    		dialog.dismiss();
    	}
        super.onStop();
    }
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        GuiatelApp app = (GuiatelApp) getApplication();

        app.tracker.trackPageView("/Detail");

        
        mapView = (MapView) findViewById(R.id.mapview);

        mapView.setBuiltInZoomControls(true);

			drawable = this.getResources().getDrawable(R.drawable.pin);
			itemizedOverlay = new HelloItemizedOverlay(drawable,this);

        
        
        Bundle extras = getIntent().getExtras(); 
        if(extras !=null)
        {
        	
        	
        	
        	//id = extras.getString("id");
        	nombre = extras.getString("nombre");
        	direccion = extras.getString("direccion");

        	tel = extras.getString("tel");
        	latitud = extras.getString("latitud");
        	longitud = extras.getString("longitud");
        	
	        dialog = ProgressDialog.show(detail.this, "Obteniendo datos de Mapa", 
                    "Aguarde por favor...", true);

            viewOrders = new Runnable(){
                
                public void run() {
                    getOrders();
                }
            };

	        
	        thread =  new Thread(null, viewOrders, "MagentoBackground");
	        thread.start();

        	
        	
            final TextView textName = (TextView) findViewById(R.id.textViewName);
            textName.setText( nombre);


            final TextView textStreet = (TextView) findViewById(R.id.textViewStreet);
            
            
       	 if (Configuration.ORIENTATION_LANDSCAPE  == getScreenOrientation())
       	 {
         	String out =   direccion;
         	out = out.replaceAll("\n", "");
            textStreet.setText( out);
       	 }
       	 else
       	 {
             textStreet.setText( direccion);
       		 
       	 }



            

        	
            final TextView textCallTo = (TextView) findViewById(R.id.textCallTo);
            textCallTo.setText("Llamar: "+ tel);
            
            textCallTo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	call();
                }
            });        	
        	


            
            final TextView textShowInMap = (TextView) findViewById(R.id.textViewMap);
            textShowInMap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	showInMap();
                }
            });        	
        	


                        
            
            final TextView textADDC = (TextView) findViewById(R.id.textViewAddressBook);
            textADDC.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	addToAddressBook();
                }
            });        	



            final TextView textFavorites = (TextView) findViewById(R.id.textViewFavorites);
            textFavorites.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	addToFavorites();
                }
            });        	


        }        
        
    }

    
    
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    @SuppressWarnings("rawtypes")
	private  class HelloItemizedOverlay extends ItemizedOverlay {
    	
    	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    	Context mContext;


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
    		       
    			  
    		       
    			   dialog.show();
    			  
    			  
    			   return true;
    			}
    		
    		
    		
    	}
    
    


	
}
