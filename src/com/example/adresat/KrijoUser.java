package com.example.adresat;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;



public class KrijoUser extends FragmentActivity implements OnFocusChangeListener {
	
	EditText editUsername;
	EditText editPassword;
	EditText editPassword2;
	EditText editEmail;
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t11;
	TextView t22;
	TextView t33;
	TextView Mesazhi;
	Spinner spLista;
	ProgressBar objProgress;
	boolean valid=false;
	boolean errorNukEgziston=false;
	boolean errorIregjistruar=false;
	String Gabimi="";
	AlertDialog.Builder Alert;
	ArrayAdapter<String> myAdapter;
	List<String> listItems = new ArrayList<String>();
	List<String> listItemExtras = new ArrayList<String>();
	String Lloji="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_krijouser);
		editUsername = (EditText)findViewById(R.id.editcUsername);
		editPassword = (EditText)findViewById(R.id.editcPassword);
		editPassword2 = (EditText)findViewById(R.id.editcPassword2);
		editEmail = (EditText)findViewById(R.id.editEmail);
		t1 = (TextView)findViewById(R.id.txt1);
		t2 = (TextView)findViewById(R.id.txt2);
		t3 = (TextView)findViewById(R.id.txt3);
		t11 = (TextView)findViewById(R.id.txt11);
		t22 = (TextView)findViewById(R.id.txt22);
		t33 = (TextView)findViewById(R.id.txt33);
		Mesazhi = (TextView)findViewById(R.id.txtMesazhi);
		spLista = (Spinner)findViewById(R.id.spLloji);
		objProgress = (ProgressBar)findViewById(R.id.progressLoading);
		editUsername.setOnFocusChangeListener(this);
		
		myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_2, android.R.id.text1,
				listItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text1 = (TextView) view
						.findViewById(android.R.id.text1);
				TextView text2 = (TextView) view
						.findViewById(android.R.id.text2);

				text1.setText(listItems.get(position));
				text2.setText(listItemExtras.get(position));
				return view;
			}
		};

		spLista.setAdapter(myAdapter);
		
		listItems.add("Llogaria");
		listItemExtras.add("Zgjedhni llojin e llogarise!");
		
		listItems.add("Individ");
		listItemExtras.add("NrPersonal = Username");
		
		listItems.add("Biznes");
		listItemExtras.add("NrBiznesit = Username");
		
		listItems.add("Institucion");
		listItemExtras.add("NrInstitucionit = Username");
		
		myAdapter.notifyDataSetChanged();
		
		spLista.setOnItemSelectedListener(new OnItemSelectedListener() {

	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1,
	                int arg2, long arg3) {

	            if(arg2==1)
	            {
	            	t1.setText("Emri dhe Mbiemri: ");
	            	t2.setText("Datelindja: ");
	            	t3.setText("Vendlindja:");
	            	Lloji="Individ";
	            }
	            else if(arg2==2)
	            {
	            	t1.setText("Emri i Biznesit: ");
	            	t2.setText("Pronari: ");
	            	t3.setText("Veprimatria :");
	            	Lloji="Biznes";
	            }
	            else if(arg2==3)
	            {
	            	t1.setText("Emri i Institucionit: ");
	            	t2.setText("Telefoni: ");
	            	t3.setText("Lloji: ");
	            	Lloji="Institucion";
	            }
	        }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    });
	}

	
	public void regjistrohuClick(View v)
	{
		Gabimi="";
		valid=false;
		if(Lloji!="")
		{
		(new validouserTask()).execute(editUsername.getText().toString(),Lloji);
		}
		else
		{
			Mesazhi.setText("Zgjedhnji llojin e llogarise!");
		}
		
	}
	private boolean validuesi()
	{
		String Password = editPassword.getText().toString();
		String Email = editEmail.getText().toString();
		String UserName = editUsername.getText().toString();
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
		
		if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches() || Email.length()==0)
		{
			Gabimi += "\nEmail eshte jovalid.";
			editEmail.setText("");
			dalja = false;	
		}
		
		int l = UserName.length();
		if(errorNukEgziston  || (l!=10 && l!=8 && l!= 9) )
		{
			Gabimi +="\nKeni dhene Username jovalid.";
			editUsername.setText("");
			dalja=false;
		}
		
		else if(errorIregjistruar)
		{
			Gabimi +="\nKy Username eshte i regjistruar.";
			editUsername.setText("");
			dalja=false;
		}
		
		if(Lloji=="")
		{
			Gabimi ="Zgjedhnji llojin e llogarise!";
			dalja=false;
			
		}
		return dalja;
		
	}
	
	
	private void showFinishAlert() {
		Alert = new AlertDialog.Builder(this);
		String strTitle = "Regjistrimi perfundoi!";
		String strMessage = "U regjistruat me sukses!";
		String strOK = "OK";
		Alert.setTitle(strTitle);
		Alert.setMessage(strMessage);
		Alert.setNeutralButton(strOK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(KrijoUser.this,Login.class);
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
    
    argumentet[1]=editUsername.getText().toString();
    String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	
	if(!teDhenat.substring(0,10).equals("java.net.S") && Lloji=="Individ")
	{
		
		String EmriMbiemri = teDhenat.substring(0,teDhenat.indexOf("DataLindjes=>"));
		String dataLindjes = teDhenat.substring(teDhenat.indexOf("DataLindjes=>")+13,teDhenat.indexOf("VendiLindjes=>")-11);
		String vendiLindjes = teDhenat.substring(teDhenat.indexOf("VendiLindjes=>")+14);
		t11.setText(EmriMbiemri);
		t22.setText(dataLindjes);
		t33.setText(vendiLindjes);
		objProgress.setVisibility(View.GONE);
	}
	else if(!teDhenat.substring(0,10).equals("java.net.S") && Lloji=="Biznes")
	{
		String Emri = teDhenat.substring(0,teDhenat.indexOf("Pronari=>"));
		String Pronari = teDhenat.substring(teDhenat.indexOf("Pronari=>")+9,teDhenat.indexOf("Veprimtaria=>"));
		String Veprimtaria = teDhenat.substring(teDhenat.indexOf("Veprimtaria=>")+13);
		t11.setText(Emri);
		t22.setText(Pronari);
		t33.setText(Veprimtaria);
		objProgress.setVisibility(View.GONE);
	}
	else if(!teDhenat.substring(0,10).equals("java.net.S") && Lloji=="Institucion")
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
	
	
	private class shtouserTask extends
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
    String argumentet[] = new String[8];
    
    argumentet[0]="Username";
    argumentet[1]=params[0];
    argumentet[2]="Hash";
    argumentet[3]=params[1];
    argumentet[4]="Salt";
    argumentet[5]=params[2];
    argumentet[6]="Email";
    argumentet[7]=params[3];
    
    String aResponse = objThirrja.thirrMetoden("shtoUser", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	if(teDhenat.equals("U insertua me sukses"))
	{
	showFinishAlert();
	}
	else
	{
		Mesazhi.setText("Gabim me lidhjen me databaze");
	}
    }
}

	private class validouserTask extends
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
    String argumentet[] = new String[4];
    
    argumentet[0]="Username";
    argumentet[1]=params[0];
    argumentet[2]="Lloji";
    argumentet[3]=params[1];
        
    String aResponse = objThirrja.thirrMetoden("validoUsername", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	if(teDhenat.length()==10 || teDhenat.length()==11 || teDhenat.length()==9)
	{
		String Iregjistruar = teDhenat.substring(0,teDhenat.indexOf(","));
		String Egziston = teDhenat.substring(teDhenat.indexOf(",")+1);
		if(Iregjistruar.equals("True"))
		{
			errorIregjistruar=true;
		}
		if(Egziston.equals("False"))
		{
			errorNukEgziston=true;
	
		}
		if(validuesi())
		{
			String Username=editUsername.getText().toString();
			String Hash1 = hash(editPassword.getText().toString());
			String Salt=gjeneroSalt();
			Hash1 = Hash1+Salt;
			String Hash = hash(Hash1);
			String Email = editEmail.getText().toString();
			(new shtouserTask()).execute(Username,Hash,Salt,Email);
	
		}
		else
		{
			Mesazhi.setText(Gabimi);
			
		}
	}
	else
	{
		Mesazhi.setText("Gabim me lidhjen me databaze");
	}
    }
}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
		if(!editUsername.isFocused() && editUsername.getText().toString()!="")
		{
		objProgress.setVisibility(0);
		(new lexoTask()).execute(Lloji);
		}
}

}	
		
	
	

