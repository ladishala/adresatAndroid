package com.example.adresat;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;



public class KrijoUser extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_krijouser);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
 
	
	public String hash(String s, String algorithm) {
	    
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			md.update(s.getBytes("Unicode"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new BigInteger(md.digest()).toString(16);
	}

	private class createTask extends
    AsyncTask<String, Void, String> {

@Override
protected String doInBackground(String... params) {
    
    /**
     * Marrim Mesazhin nga konstruktori
     */
    String Mesazhi = params[0];
    String Username = params[1];
    
    if(Mesazhi=="OK")
    {
    /**
     * /Krijojme objektin e Web Sherbimit
     */
    WebserviceCall objThirrja = new WebserviceCall(); 

    /**
     * /Thirrim Web Sherbimin dhe marrim pergjigjjen.
     */
    String argumentet[] = new String[2];
    argumentet[0]="Username";
    argumentet[1]=Username;
    String aResponse = objThirrja.thirrMetoden("merrCred", argumentet); 
    
    return aResponse;
    }
    
    else
    {
    return "Deshtoi";	
    }
  }



@Override
protected void onPostExecute(String credentials) {
    // Display the results of the lookup.
	
    }
}

}	
		
	
	

