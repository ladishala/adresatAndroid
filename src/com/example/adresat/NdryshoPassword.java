package com.example.adresat;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;



public class NdryshoPassword extends FragmentActivity {
	
	TextView txtUsername;
	EditText editOldPassword;
	EditText editPassword;
	EditText editPassword2;
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t11;
	TextView t22;
	TextView t33;
	TextView Mesazhi;
	TextView txtEmail;
	ProgressBar objProgress;
	String Username="";
	boolean valid=false;
	
	String Gabimi="";
	AlertDialog.Builder Alert;
	String Lloji="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ndryshopassword);
		
		editOldPassword = (EditText)findViewById(R.id.editOldPassword);		
		editPassword = (EditText)findViewById(R.id.editNewPassword);
		editPassword2 = (EditText)findViewById(R.id.editNewPassword2);
		t1 = (TextView)findViewById(R.id.txtn1);
		t2 = (TextView)findViewById(R.id.txtn2);
		t3 = (TextView)findViewById(R.id.txtn3);
		t11 = (TextView)findViewById(R.id.txtn11);
		t22 = (TextView)findViewById(R.id.txtn22);
		t33 = (TextView)findViewById(R.id.txtn33);
		Mesazhi = (TextView)findViewById(R.id.txtnError);
		objProgress = (ProgressBar)findViewById(R.id.progressNdryshoo);
		txtUsername=(TextView)findViewById(R.id.txtnUsername);
		txtEmail=(TextView)findViewById(R.id.txtnEmail);
		
		Username=getSharedPreferences("Settings",MODE_PRIVATE).getString("Username", "");
		
		if(Username.length()==10)
	    {
	        	t1.setText("Emri dhe Mbiemri: ");
	           	t2.setText("Datelindja: ");
	           	t3.setText("Vendlindja:");
	           	Lloji="Individ";
	    }
	    else if(Username.length()==8)
	    {
	        	t1.setText("Emri i Biznesit: ");
	           	t2.setText("Pronari: ");
	           	t3.setText("Veprimatria :");
	           	Lloji="Biznes";
	    }
	    else if(Username.length()==9)
	    {
	           	t1.setText("Emri i Institucionit: ");
	           	t2.setText("Telefoni: ");
	           	t3.setText("Lloji: ");
	           	Lloji="Institucion";
	    }
		(new lexoTask()).execute(Lloji);
		(new lexoemailTask()).execute("");
	}

	
	public void regjistrohuClick(View v)
	{
		Gabimi="";
		valid=false;
		if(validuesi())
		{
			
			(new loginTask()).execute("");
		}
		else
		{
			Mesazhi.setText(Gabimi);
		}
		
	}
	private boolean validuesi()
	{
		String Password = editPassword.getText().toString();

		boolean dalja = true;
		
		if(Password.length()==0)
		{
			Gabimi ="Ju lutem zgjedhni nje password";
			editPassword2.setText("");
			dalja=false;			
		}
		else if(!(editPassword.getText().toString().equals(editPassword2.getText().toString())))
		{
			Gabimi ="Passwordet nuk perputhen";
			editPassword.setText("");
			editPassword2.setText("");
			dalja=false;
		}
		
		else if(!regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])[0-9A-Za-z]{8,}$",Password))
		{
			Gabimi ="Keni zgjedhur password jo te sigurt.";
			editPassword.setText("");
			editPassword2.setText("");
			dalja=false;
			
		}
		
		return dalja;
		
	}
	
	
	private void showFinishAlert() {
		Alert = new AlertDialog.Builder(this);
		String strTitle = "Ndryshimi perfundoi!";
		String strMessage = "Ndryshuat passwordin me sukses!";
		String strOK = "OK";
		Alert.setTitle(strTitle);
		Alert.setMessage(strMessage);
		Alert.setNeutralButton(strOK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(NdryshoPassword.this,Login.class);
						i.putExtra("LogOut", true);
						startActivity(i);
					}});
		
		Alert.show();
	}
	private boolean regex(String expression,String Text)
	{
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(Text);
		
		return matcher.matches();
		
		
	}
	public String gjeneroSalt()
	{
		Random generator = new Random();
	    StringBuilder randomStringBuilder = new StringBuilder();

	    char tempChar;
	    for (int i = 0; i < 4; i++){
	        tempChar = (char) (generator.nextInt(96) + 32);
	        randomStringBuilder.append(tempChar);
	    }
	    return randomStringBuilder.toString();
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

	private class lexoTask extends
    AsyncTask<String, Void, String> {
		
@Override
protected String doInBackground(String... params) {
    
	String lloji=params[0];
	String funksioni="";

    /**
     * /Krijojme objektin e Web Sherbimit
     */
    WebserviceCall objThirrja = new WebserviceCall(); 

    /**
     * /Thirrim Web Sherbimin dhe marrim pergjigjjen.
     */
    String argumentet[] = new String[2];
    if(lloji=="Individ")
    {
    argumentet[0]="NrPersonal";
    funksioni="lexoRegjistrinCivil";
    }
    else if(lloji=="Biznes")
    {
    	argumentet[0]="NrBiznesit";	
    	funksioni="lexoArbk";
    }
    else if(lloji=="Institucion")
    {
    	argumentet[0]="NrInstitucionit";
    	funksioni="lexoInstitucionet";
    	
    }
    
    argumentet[1]=Username;
    String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.

	objProgress.setVisibility(View.GONE);
	if(!teDhenat.equals("anyType{}") && !teDhenat.substring(0,10).equals("java.net.S"))
	{
		txtUsername.setVisibility(0);
		t11.setVisibility(0);
		t22.setVisibility(0);
		t33.setVisibility(0);		
		txtEmail.setVisibility(0);
		if(Lloji=="Individ")
		{
			
			String EmriMbiemri = teDhenat.substring(0,teDhenat.indexOf("DataLindjes=>"));
			String dataLindjes = teDhenat.substring(teDhenat.indexOf("DataLindjes=>")+13,teDhenat.indexOf("VendiLindjes=>")-11);
			String vendiLindjes = teDhenat.substring(teDhenat.indexOf("VendiLindjes=>")+14);
			t11.setText(EmriMbiemri);
			t22.setText(dataLindjes);
			t33.setText(vendiLindjes);
			objProgress.setVisibility(View.GONE);
		}
		else if(Lloji=="Biznes")
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Pronari=>"));
			String Pronari = teDhenat.substring(teDhenat.indexOf("Pronari=>")+9,teDhenat.indexOf("Veprimtaria=>"));
			String Veprimtaria = teDhenat.substring(teDhenat.indexOf("Veprimtaria=>")+13);
			t11.setText(Emri);
			t22.setText(Pronari);
			t33.setText(Veprimtaria);
			objProgress.setVisibility(View.GONE);
		}
		else if(Lloji=="Institucion")
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Telefoni=>"));
			String Telefoni = teDhenat.substring(teDhenat.indexOf("Telefoni=>")+10,teDhenat.indexOf("Lloji=>"));
			String Lloji = teDhenat.substring(teDhenat.indexOf("Lloji=>")+7);
			t11.setText(Emri);
			t22.setText(Telefoni);
			t33.setText(Lloji);
			objProgress.setVisibility(View.GONE);
		}
	}
    }
}
		
	private class ndryshopasswordTask extends
    AsyncTask<String, Void, String> {
		
@Override
protected String doInBackground(String... params) {
    
    /**
     * /Krijojme objektin e Web Sherbimit
     */
    WebserviceCall objThirrja = new WebserviceCall(); 

    /**
     * /Thirrim Web Sherbimin dhe marrim pergjigjjen.
     */
    String argumentet[] = new String[6];
    
    argumentet[0]="Username";
    argumentet[1]=Username;
    argumentet[2]="Hash";
    argumentet[3]=params[0];
    argumentet[4]="Salt";
    argumentet[5]=params[1];
        
    String aResponse = objThirrja.thirrMetoden("ndryshoPassword", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	if(teDhenat.equals("U ndryshua me sukses"))
	{
		showFinishAlert();
	}
	else
	{
		Mesazhi.setText("Gabim me lidhjen me databaze");
	}
    }
}

	private class lexoemailTask extends
    AsyncTask<String, Void, String> {
		
@Override
protected String doInBackground(String... params) {
    
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
    
        
    String aResponse = objThirrja.thirrMetoden("lexoEmail", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	objProgress.setVisibility(View.GONE);
	if(!teDhenat.equals("Error"))
	{
		
		txtUsername.setVisibility(0);
		t11.setVisibility(0);
		t22.setVisibility(0);
		t33.setVisibility(0);
		txtEmail.setVisibility(0);
		txtEmail.setText(teDhenat);
		txtUsername.setText(Username);
	}

    }
}

	private class loginTask extends
    AsyncTask<String, Void, String> {

@Override
protected String doInBackground(String... params) {
    
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



@Override
protected void onPostExecute(String credentials) {
    // Display the results of the lookup.
	objProgress.setVisibility(View.GONE);
	if (credentials.equals("anyType{}"))
    {
    	Mesazhi.setText("Keni dhene te dhena jo valide!");
    
    }
	else if(credentials.substring(44).equals("OK"))
    {
    	String dbHash=credentials.substring(0,40);
    	String salt=credentials.substring(40,44);
    	String Hash1=hash(editOldPassword.getText().toString());
    	Hash1 = Hash1+salt;
    	String Hash=hash(Hash1);
    	if(Hash.equalsIgnoreCase(dbHash))
    	{  	
    		String Salt = gjeneroSalt();
    		String newHash1=hash(editPassword.getText().toString());
    		newHash1 = newHash1+Salt;
    		String newHash=hash(newHash1);
    		(new ndryshopasswordTask()).execute(newHash,Salt);
    	}
    	else
    	{
    		
    		Mesazhi.setText("Passwordi aktual eshte gabim!");

    	}
    	
    	
    }
    
    else
    {
    	Mesazhi.setText("Gabim me lidhjen ne databaze!");

    }
    }
}
	
}	
		
	
	

