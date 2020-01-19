package com.mswiczar.guiatel;


import com.MASTAdView.MASTAdView;
import com.MASTAdView.MASTAdViewCoreWrapper.AdSize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
 * 
    Site ID:26697
	Zone ID:119960
 * 
 */



public class about extends Activity {

	@Override
	public void onDestroy() {
	 super.onDestroy();
	}

	
	private void sendEmail()
	{
	    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

	    String[] recipients = new String[]{"info@mswiczar.com", "",};

	    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

	    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Me gustaria conectarme con el desarrollador de Guia Telefonica de la Republica Argentina para Android");

	    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hola Sr desarrollador de la guia telefonica de la republica Argentina para Android. Mi pregunta es la siguiente:");

	    //emailIntent.setType("text/plain");
	    emailIntent.setType("message/rfc822");
	    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	
	}
	
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);
        
        GuiatelApp app = (GuiatelApp) getApplication();

        app.tracker.trackPageView("/About");

        
        MASTAdView adserverView = new MASTAdView(this,26697,119960,AdSize.BANNER_320x50);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mainLayout);
       adserverView.setInternalBrowser(false); 
        adserverView.update(); 
        linearLayout.addView(adserverView);

        // Lookup your LinearLayout assuming it’s been given
        // the attribute android:id="@+id/mainLayout"
        
//        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);
        // Add the adView to it

       
        
		final TextView textEmail = (TextView) findViewById(R.id.textViewEmail);
		textEmail.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				sendEmail();
			}
		});
        }
        

}
