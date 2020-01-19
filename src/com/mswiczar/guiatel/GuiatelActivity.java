package com.mswiczar.guiatel;

import java.io.IOException;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.MASTAdView.MASTAdView;
import com.MASTAdView.MASTAdViewCoreWrapper.AdSize;



public class GuiatelActivity extends Activity {
    /** Called when the activity is first created. */
	ArrayList<HashMap<String,String> > listProvincias;
	ArrayList<String> listProvinciasString;
	
	ArrayAdapter<String> listProvinciasStringAdapter;
	public int provincia;

	ArrayList<HashMap<String,String> > listResults ;

	
	ProgressDialog dialog;
	private Runnable viewOrders;
	OrderAdapter m_adapter;
	Thread thread;
	PaginasBlancas theBusqueda;
	int pagina;
	
	String nombre;
	String strSavedMem1;
	
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

    @Override
    public void onDestroy() {
      super.onDestroy();
    }
    
    
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
       }
      
       private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        
        strSavedMem1  = sharedPreferences.getString("pos", "0");
       }
    
    
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menumain, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) 
        {
                
    	case R.id.menuRecents:
			Intent intentRecents = new Intent(GuiatelActivity.this, recents.class);
  			startActivity(intentRecents);
    		return true;
        
        
        	case R.id.menuFavorites:
				Intent intentFavorites = new Intent(GuiatelActivity.this, favorites.class);
      			startActivity(intentFavorites);

        		return true;
            
        	case R.id.menuAbout:
				Intent intentAbout = new Intent(GuiatelActivity.this, about.class);
      			startActivity(intentAbout);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    } 
    	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MASTAdView adserverView = new MASTAdView(this,26697,119960,AdSize.BANNER_320x50);
         LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mainLayout);
        adserverView.setInternalBrowser(false); 
         adserverView.update(); 
         linearLayout.addView(adserverView);
         
        LoadPreferences();

        
        //LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        // Add the adView to it

                
        
        GuiatelApp app = (GuiatelApp) getApplication();

    	app.tracker.start("UA-19434553-1", 10, this);

        app.tracker.trackPageView("/StartApp");

        theBusqueda = new PaginasBlancas();
        
        
        listProvincias = new ArrayList<HashMap<String,String>>();
        listProvinciasString = new ArrayList<String>();
        
        listResults = app.listItems;
        provincia=0;
        
        provincia = Integer.parseInt(strSavedMem1);
        
        
        app.myDbHelper = new DataBaseHelper(this);

        try 
        {
        	app.myDbHelper.createDataBase();
        } 
        catch (IOException ioe) 
        {
        	throw new Error("Unable to create database");
        }

        app.myDbHelper.openDataBase();
	
	    final TextView buttonCambiar = (TextView) findViewById(R.id.labelProvincia);
	    listProvinciasStringAdapter = new ArrayAdapter<String>(GuiatelActivity.this, android.R.layout.select_dialog_item, listProvinciasString);

	
	
	Cursor curProv =app.myDbHelper.createCursorProvincias();
	curProv.moveToFirst();
		while (!curProv.isAfterLast())
		{
			
			HashMap<String,String> o = new  HashMap<String,String>();
            o.put("id",curProv.getString(0));
            o.put("desc1",curProv.getString(1));
            o.put("desc2",curProv.getString(2));
            listProvincias.add(o);
            
            listProvinciasString.add(curProv.getString(1));
			curProv.moveToNext();
		}
		curProv.close();

		
		final TextView textProvincia = (TextView) findViewById(R.id.labelProvincia);
		HashMap<String,String> o = listProvincias.get(provincia);
		
		textProvincia.setText("Buscar en: "+ o.get("desc1") + " Toque  aqui para cambiar");
	
	    buttonCambiar.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(GuiatelActivity.this);
        	builder.setTitle("Buscar en:");

        	builder.setAdapter(listProvinciasStringAdapter, new DialogInterface.OnClickListener() {
        	    public void onClick(DialogInterface dialog, int item) {
        	    	provincia = item;
        	    	HashMap<String,String> o = listProvincias.get(provincia);
        			textProvincia.setText("Buscar en: "+ o.get("desc1")+ " Toque  aqui para cambiar");
        			String store = ""+provincia;
        			SavePreferences("pos",store);
        			
    	            Toast.makeText(GuiatelActivity.this, "Buscar en: " + o.get("desc1"),
    	                    Toast.LENGTH_LONG).show();
        	    }});
        	builder.show();
        }
    });


    final Button buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
    buttonBuscar.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	
        	
        	
        	final EditText atext = (EditText)findViewById(R.id.editTextNombre);
        	nombre = atext.getText().toString();
        	
            viewOrders = new Runnable(){
                
                public void run() {
                    getOrders();
                }
            };
            listResults.clear();
            pagina=1;
            thread =  new Thread(null, viewOrders, "MagentoBackground");
            thread.start();
	        dialog = ProgressDialog.show(GuiatelActivity.this, "Obteniendo datos", 
                    "Aguarde por favor...", true);


        	
        }
    });
    final ListView listavisos = (ListView) findViewById(R.id.listResults);
	m_adapter = new OrderAdapter(this, R.layout.row, listResults);
    listavisos.setAdapter(m_adapter);
    
    listavisos.setItemsCanFocus(false);
    
    listavisos.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			
			if (arg2 <(listResults.size()-1))
			{
                HashMap<String,String> o = listResults.get(arg2);
                if (o != null) 
                {
                    GuiatelApp app = (GuiatelApp) getApplication();

                    String sql = "delete from  recents  where tel = '"+o.get("tel")+"' and nombre ='" +o.get("nombre") +"'";
            	//	Log.e("Delete from recents ", sql);
            		app.myDbHelper.myDataBase.execSQL(sql);

                    
            		sql = "insert into recents  (nombre, direccion, tel,latitud,longitud) " +
            		"VALUES ('" + o.get("nombre") + "', '" + o.get("direccion") + "', '"+o.get("tel")+"', '"+""+"' ,'"+""+"')";
            	//	Log.e("Test Saving", sql);
            		app.myDbHelper.myDataBase.execSQL(sql);
                	
                	Intent intent = new Intent(GuiatelActivity.this, detail.class);
                		intent.putExtra("id"        , o.get("id")        );
                		intent.putExtra("nombre"    , o.get("nombre")    );
                		intent.putExtra("direccion" , o.get("direccion") );
                		intent.putExtra("tel"       , o.get("tel")       );
                	startActivity(intent);
                }
			}
			else
			{
				pagina++;
				
		        thread =  new Thread(null, viewOrders, "MagentoBackground");
		        thread.start();
		        dialog = ProgressDialog.show(GuiatelActivity.this, "Obteniendo datos", 
	                    "Aguarde por favor...", true);
				
			}
  			
  			
		}
    	 });
    
    
	
	}
	
    private Runnable returnRes = new Runnable() 
    {
    	
        public void run() {
        	try {
				Thread.sleep(100);
				m_adapter.notifyDataSetChanged();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
        	dialog.dismiss();
        	
        	if (listResults.isEmpty())
        	{
        		AlertDialog alertDialog;
        		alertDialog  = new AlertDialog.Builder(GuiatelActivity.this).create();
        		alertDialog.setTitle("Guia Telefonica");
        		alertDialog.setMessage("No se han encontrado resultados para esta busqueda!");
        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {

        		} }); 
        		alertDialog.show();
        	}
        	
        	
        }
    };

	
	private void getOrders()
	{
        try
        {

   			String strUrl = "http://new2.mswiczar.com";
   	    	XmlReaderToHashMap axmlreader =  new XmlReaderToHashMap();
   	   	    axmlreader.setTheURL(strUrl);
   	   	    String resultado = axmlreader.getCVSData();
   	   	
   	   	    resultado =resultado.trim();
   	   	    Log.v("Resultado",resultado);
   	   	    
	          int buscar = Integer.parseInt(resultado);

   	    	if (buscar!=1)
   	    	{
   	          runOnUiThread(returnRes);
   	          return;
   	    	}

        	
        	if (listResults.size()!=0)
	      	  {
	      		listResults.remove(listResults.size()-1);
	      	  }

	          int proviid = Integer.parseInt(listProvincias.get(provincia).get("id"));
	      	  
	      	  
  	    	  String strProvSearch =  listProvincias.get(provincia).get("desc2")+"-"+listProvincias.get(provincia).get("id");
	      //	  Log.v("Hola","Buscando: " + nombre+ " " + strProvSearch + " int prov:  "+ provincia );
	      	  theBusqueda.buscarPorNombreXML(nombre, proviid ,strProvSearch,  pagina  , listResults );
	      	  
	      	  
	      	  HashMap<String,String> ahash;
	      	  

	      	 
	
	      	  
	      	  ahash = new  HashMap<String,String>();
	      	  
	      	  
	      	  
	      	  listResults.add(ahash);

          } 
          catch (Exception e) 
          { 
        	  Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
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
	               if (items.size()==0)
	               {
	            	   return v;
	               }
	                
                   LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   if (position<(items.size()-1))
                   {
                	   v = vi.inflate(R.layout.row, null);
                   }
                   else
                   {
	                   v = vi.inflate(R.layout.rowmore, null);
                   }

	                if  (position>=(items.size()-1))
	                {
	                	return v;
	                }	
	                
	                
	                HashMap<String,String> o = items.get(position);
	                
	                if (o != null) 
	               {
	                	/*
	                	 Set st = o.keySet();
	                	    //iterate through the Set of keys
	                	    Iterator itr = st.iterator();
	                	    while(itr.hasNext())
	                	    {
	                	    	Object str =itr.next();
	                	        Log.v("key: "+str + ":"," -> "+o.get(str));
	                	    }
	                	    */
	                	TextView tt = (TextView) v.findViewById(R.id.labelName);
	                   TextView bt = (TextView) v.findViewById(R.id.labelPhone );
	                   TextView mt = (TextView) v.findViewById(R.id.labelStreet1);
	                   o.put("nombre",o.get("busqueda"));
	                   o.put("direccion",o.get("calle") +"  "+o.get("altura") +" "+ o.get("descripcion"));

	                   o.put("tel",o.get("area") +"-"+o.get("prefijo")+ "-"+o.get("sufijo"));
	                   
	                   tt.setText(o.get("busqueda"));
	                   mt.setText( o.get("calle") +" "+o.get("altura") +" "+o.get("piso")+" "+ o.get("departamento") +" "+ o.get("descripcion"));
	                   bt.setText("Tel: ("+o.get("area") +") "+o.get("prefijo")+ "-"+o.get("sufijo") );
	               }
	               return v;
	        }
	 }
	 
    
	
	
}