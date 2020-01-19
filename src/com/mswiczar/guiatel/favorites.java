package com.mswiczar.guiatel;

import java.util.ArrayList;
import java.util.HashMap;

import com.MASTAdView.MASTAdView;
import com.MASTAdView.MASTAdViewCoreWrapper.AdSize;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//CREATE TABLE favorites (id integer PRIMARY KEY AUTOINCREMENT ,  nombre varchar(128) , direccion varchar(255) , tel varchar(128),latitud varchar(64) , longitud varchar(64));


public class favorites extends Activity {

    ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;
	OrderAdapter m_adapter;

	   @Override
	    public void onDestroy() {
	      super.onDestroy();
	    }

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favorites);
        
        
        
        MASTAdView adserverView = new MASTAdView(this,26697,119960,AdSize.BANNER_320x50);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mainLayout);
        adserverView.setInternalBrowser(false); 
        adserverView.update(); 
        linearLayout.addView(adserverView);

        
        final ListView listRows = (ListView) findViewById(R.id.list);
        listRows.setItemsCanFocus(false);
        
        GuiatelApp app = (GuiatelApp) getApplication();

        app.tracker.trackPageView("/Favorites");

        
    	Cursor cursorFavorites = app.myDbHelper.createCursorFavorites();
    	cursorFavorites.moveToFirst();
    	HashMap<String,String> o;
    	
    	
    	
 		while (!cursorFavorites.isAfterLast())
 		{
 			o = new  HashMap<String,String>();
            o.put("id",cursorFavorites.getString(0));
            o.put("nombre",cursorFavorites.getString(1));
            o.put("direccion",cursorFavorites.getString(2));
            o.put("tel",cursorFavorites.getString(3));
            o.put("latitud",cursorFavorites.getString(4));
            o.put("longitud",cursorFavorites.getString(5));
 			listItems.add(o);
 		 	cursorFavorites.moveToNext();
 		}
 		cursorFavorites.close();
        
        listRows.setOnItemClickListener(new OnItemClickListener() 
        {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3)
    				{
    					/*
    					 * 
    					 */
    		                HashMap<String,String> o = listItems.get(arg2);
    		                if (o != null) 
    		                {
    		                	//CREATE TABLE favorites (id integer PRIMARY KEY AUTOINCREMENT ,  nombre varchar(128) , direccion varchar(255) ,
    		                	//tel varchar(128),latitud varchar(64) , longitud varchar(64));
    		                   			    						
    		                	Intent intent = new Intent(favorites.this, detail.class);
    		                		intent.putExtra("id",o.get("id") );
    		                		intent.putExtra("nombre",o.get("nombre") );
    		                		intent.putExtra("direccion",o.get("direccion") );
    		                		intent.putExtra("tel",o.get("tel") );
	    		                	intent.putExtra("latitud",o.get("latitud") );
	    		                	intent.putExtra("longitud",o.get("longitud") );
    		                	startActivity(intent);
    		                }
    		                
    				}
    	  });        
        m_adapter = new OrderAdapter(this, R.layout.row, listItems);
   		listRows.setAdapter(m_adapter);        
        
    }

	 private class OrderAdapter extends ArrayAdapter<HashMap<String,String> > 
	 {
		 private ArrayList<HashMap<String,String> > items;
	        public OrderAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String,String> > items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }
	      
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);

	                HashMap<String,String> o = items.get(position);
	                
	                if (o != null) 
	               {
	                	
	                	
	                   TextView tt = (TextView) v.findViewById(R.id.labelName);
	                   TextView mt = (TextView) v.findViewById(R.id.labelPhone);
	                   
	                   
	                   TextView bt = (TextView) v.findViewById(R.id.labelStreet1);
	                    
   
	                   tt.setText(o.get("nombre"));
	                   mt.setText("Tel: "+o.get("tel"));

	                   bt.setText( o.get("direccion"));

	                   
	               }
	               return v;
	        }
	 }
	 
    
	
}
