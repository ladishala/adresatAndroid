package com.example.adresat;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Login extends FragmentActivity {
	
	ProgressBar objProgress;
	TextView Mesazhi;
	EditText editUsername;
	EditText editPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Mesazhi=(TextView)findViewById(R.id.txtErrorMessage);
		objProgress=(ProgressBar)findViewById(R.id.loginProgress);
		editUsername=(EditText)findViewById(R.id.editUsername);
		editPassword=(EditText)findViewById(R.id.editPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loginClick(View v)
	{
		objProgress.setVisibility(0);
		(new loginTask()).execute("OK",editUsername.getText().toString());
		
		
		
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


	
	
	
	private class loginTask extends
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
	objProgress.setVisibility(View.GONE);
    if(credentials!="Deshtoi" && credentials.length()>40)
    {
    	String dbHash=credentials.substring(0,40);
    	String salt=credentials.substring(40);
    	String Hash=hash(editPassword.getText().toString()+salt,"SHA-1");
    	Mesazhi.setText(Hash);
    	if(Hash.equalsIgnoreCase(dbHash))
    	{  		
    		Intent i = new Intent(Login.this, MainActivity.class);
    		startActivity(i);
    	}
    	else
    	{
    		
    		//Mesazhi.setText("Keni dhene kredenciale jo valide!");
    	}
    	
    	
    }
    else
    {
    	
    	Mesazhi.setText("Keni dhene kredenciale jo valide!");
    }
    }
}

}	
		
	
	

