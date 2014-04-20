package com.example.adresat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
		
		Intent in = getIntent();
		boolean logout = in.getBooleanExtra("LogOut", false);
		String cUsername =getPreferences(MODE_PRIVATE).getString("Username",""); 
		if (logout)
		{
			getPreferences(MODE_PRIVATE).edit().putString("Username", "").commit();
			
		}
		else if(!cUsername.equals(""))
		{
			Intent i = new Intent(Login.this, Board.class);
    		i.putExtra("Username", cUsername);
    		startActivity(i);
		}
	}

	
	public void loginClick(View v)
	{
		if(editUsername.getText().toString().length()!=0 && editPassword.getText().toString().length()!=0)
		{
		objProgress.setVisibility(0);
		
		(new loginTask()).execute("OK",editUsername.getText().toString());
		}
		else
		{	
			Mesazhi.setText("Mbushni Username dhe Password!");
		}	
	}
	
	public void registerClick(View v)
	{
		Intent i = new Intent(Login.this, KrijoUser.class);
		startActivity(i);
		
		
	}
	
	public String hash(String s)
	{
		
		StringBuffer sb = new StringBuffer();
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        md.update(s.getBytes());
	        byte byteData[] = md.digest();
	        for (int i = 0; i < byteData.length; i++) {
	            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
	                    .substring(1));
	        }
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return sb.toString();
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
	if (credentials.equals("anyType{}"))
    {
    	Mesazhi.setText("Keni dhene te dhena jo valide!");
    	editUsername.setText("");
		editPassword.setText("");
    }
	else if(credentials.substring(44).equals("OK"))
    {
    	String dbHash=credentials.substring(0,40);
    	String salt=credentials.substring(40,44);
    	String Hash1=hash(editPassword.getText().toString());
    	Hash1 = Hash1+salt;
    	String Hash=hash(Hash1);
    	if(Hash.equalsIgnoreCase(dbHash))
    	{  	
    		getPreferences(MODE_PRIVATE).edit().putString("Username", editUsername.getText().toString()).commit();
    		Intent i = new Intent(Login.this, Board.class);
    		i.putExtra("Username", editUsername.getText().toString());
    		startActivity(i);
    	}
    	else
    	{
    		
    		Mesazhi.setText("Keni dhene te dhena jo valide!");
    		editUsername.setText("");
    		editPassword.setText("");
    	}
    	
    	
    }
    
    else
    {
    	Mesazhi.setText("Gabim me lidhjen ne databaze!");
    	editUsername.setText("");
		editPassword.setText("");
    }
    }
}

}	
		
	
	

