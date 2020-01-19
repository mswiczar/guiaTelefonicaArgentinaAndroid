package com.mswiczar.guiatel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/*
 * 
 * 
 * 
  
CREATE TABLE favorites (id integer PRIMARY KEY AUTOINCREMENT ,  nombre varchar(128) , direccion varchar(255) , tel varchar(128),latitud varchar(64) , longitud varchar(64));

CREATE TABLE recents (id integer PRIMARY KEY AUTOINCREMENT ,  nombre varchar(128) , direccion varchar(255) , tel varchar(128),latitud varchar(64) , longitud varchar(64));
create table provincias (id varchar(64) , desc1 varchar(128) , desc2 varchar(128));


 */


public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.mswiczar.guiatel/databases/";
    private static String DB_NAME = "guiatel.sql";
    public SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		
    		Log.v("DB","dbExistes");
    		//do nothing - database already exist
    	}else{
 
    		Log.v("DB","NO EXISTE");

    		//By calling this method and empty database will be created into the default system path
              //of your application so we are gonna be able to overwrite that database with our database.
        	//this.getReadableDatabase();
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		
    		
    		Log.v("DB",myPath);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		Log.v("DB","Close DB ==NULL");
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);

    	
    	Log.v("DB",DB_PATH);
    	
    	
    	(new File(DB_PATH)).mkdir();

    	
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        try
        {
        	
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}catch(SQLiteException e){
		 
		//database does't exist yet.

	}
    	
    }
 
    /*
  		CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT , 
  		aname varchar(128), 
  		aadress varchar(128) , 
  		aurl varchar(128) , 
  		atel varchar(128) , 
  		x float , 
  		y float, 
  		latitude varchar(128) , 
  		longitude varchar(128));



    */
    
    public Cursor createCursorFavorites () 
    {
    	return 	myDataBase.rawQuery("select id , nombre , direccion ,tel , latitud  , longitud  from favorites order by nombre", null);
    }
    

    public Cursor createCursorFavoriteExists (String aname,String tel) 
    {
        Log.e("DB","select count(*) as cantidad from favorites where nombre = '"+aname+"' and tel ='"+tel+"'");

    	return 	myDataBase.rawQuery("select count(*) as cantidad from favorites where nombre = '"+aname+"' and tel ='"+tel+"'", null);
    }



    public Cursor createCursorRecents () 
    {
    	return 	myDataBase.rawQuery("select id , nombre , direccion ,tel , latitud  , longitud  from recents order by id desc", null);
    }
    
    
    

    public Cursor createCursorProvincias () 
    {
    	return 	myDataBase.rawQuery("select id , desc1 , desc2   from provincias where id <> 0", null);
    }

    
    
    public Cursor createCursorRecentsExists (String aname, String tel) 
    {
        Log.e("DB","select count(*) as cantidad from recents where nombre = '"+aname+"' and tel ='"+tel+"'");

    	return 	myDataBase.rawQuery("select count(*) as cantidad from recents where nombre = '"+aname+"' and tel ='"+tel+"'", null);
    }

    
    
    
    @Override
	public synchronized void close() 
    {
    	if(myDataBase != null)
    	{
    		    myDataBase.close();
    	}
    	super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}