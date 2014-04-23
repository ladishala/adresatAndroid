package com.example.adresat;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Kerko extends FragmentActivity  {
	
	EditText edit1;
	EditText edit2;
	EditText edit3;
	TextView txtPerdoruesi1;
	TextView txtPerdoruesi2;
	TextView txtPerdoruesi3;
	TextView txtAdresa1;
	TextView txtAdresa2;
	TextView txtAdresa3;
	TextView Mesazhi;
	ProgressBar objProgress;
	String Lloji="";
	String Username;
	GoogleMap mapView;
	Marker marker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kerko);
		
		txtPerdoruesi1 = (TextView)findViewById(R.id.txtPerdoruesi1);
		txtPerdoruesi2 = (TextView)findViewById(R.id.txtPerdoruesi2);
		txtPerdoruesi3 = (TextView)findViewById(R.id.txtPerdoruesi3);
		txtAdresa1 = (TextView)findViewById(R.id.txtAdresa1);
		txtAdresa2 = (TextView)findViewById(R.id.txtAdresa2);
		txtAdresa3 = (TextView)findViewById(R.id.txtAdresa3);
		objProgress = (ProgressBar)findViewById(R.id.progressKerko);
		Mesazhi = (TextView)findViewById(R.id.txtKerkoMesazhi);
		edit1 = (EditText)findViewById(R.id.editPrimary);
		edit2 = (EditText)findViewById(R.id.editSecond);
		edit3 = (EditText)findViewById(R.id.editThird); 
			
		mapView = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapview)).getMap();
		mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.6700877, 20.8906191), 7));
		
		Intent i = getIntent();
		Lloji = i.getStringExtra("Lloji");
		
		if(Lloji.equals("Individ"))
		{
			edit1.setHint("NrPersonal/Emri dhe Mbiemri");
			edit2.setHint("Datelindja (yyyy-mm-dd)");
			edit3.setHint("Vendlindja");
			
		}
		else if(Lloji.equals("Biznes"))
		{
			edit1.setHint("NrBiznesit/Emri");
			edit2.setHint("NrPersonal i pronart");
			edit3.setHint("Veprimtaria");
		}
		else if(Lloji.equals("Institucion"))
		{
			edit1.setHint("NrInstitucionit/Emri");
			edit2.setHint("Telefoni");
			edit3.setHint("Lloji");
		}
		
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.action_logout) {
			Intent i = new Intent(Kerko.this, Login.class);
			i.putExtra("LogOut", true);
			startActivity(i);

		}
		else if(item.getItemId()==R.id.action_ChangePassword)
		{
			Intent i = new Intent(Kerko.this, NdryshoPassword.class);
			startActivity(i);
		}
		return true;

	}
	
	
	public void searchClick(View v)
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		
		int length = edit1.getText().toString().length();
		if(isInteger(edit1.getText().toString()) && (length==10 || length == 9 || length ==8))
		{
			objProgress.setVisibility(0);
			Username=edit1.getText().toString();
			new validouserTask().execute(edit1.getText().toString());
			
		}
		else if(edit1.getText().toString().length()!=0 || edit2.getText().toString().length()!=0 || edit3.getText().toString().length()!=0)
		{
			objProgress.setVisibility(0);
			new kerkoqueryTask().execute("");
			
		}
		else
		{
			Mesazhi.setText("Mbushni kriteret e kerkimit");
		}
		
		
		
	}
	public boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {}
	    return false;
	}
	
	
	private class lexoTask extends
    AsyncTask<String, Void, String> {
		
@Override
protected String doInBackground(String... params) {
    
	String funksioni="";

    /**
     * /Krijojme objektin e Web Sherbimit
     */
    WebserviceCall objThirrja = new WebserviceCall(); 

    /**
     * /Thirrim Web Sherbimin dhe marrim pergjigjjen.
     */
    String argumentet[] = new String[2];
    if(Lloji.equals("Individ"))
    {
    argumentet[0]="NrPersonal";
    funksioni="lexoRegjistrinCivil";
    }
    else if(Lloji.equals("Biznes"))
    {
    	argumentet[0]="NrBiznesit";	
    	funksioni="lexoArbk";
    }
    else if(Lloji.equals("Institucion"))
    {
    	argumentet[0]="NrInstitucionit";
    	funksioni="lexoInstitucionet";
    	
    }
    
    argumentet[1]=params[0];
    String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 

    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	if(!teDhenat.equals("anyType{}") && !teDhenat.substring(0,10).equals("java.net.S"))
	{
		if(Lloji.equals("Individ"))
		{
			
			String EmriMbiemri = teDhenat.substring(0,teDhenat.indexOf("DataLindjes=>"));
			String dataLindjes = teDhenat.substring(teDhenat.indexOf("DataLindjes=>")+13,teDhenat.indexOf("VendiLindjes=>")-11);
			String vendiLindjes = teDhenat.substring(teDhenat.indexOf("VendiLindjes=>")+14);
			txtPerdoruesi1.setText(EmriMbiemri);
			txtPerdoruesi2.setText(dataLindjes);
			txtPerdoruesi3.setText(vendiLindjes);
		}
		else if(Lloji.equals("Biznes"))
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Pronari=>"));
			String Pronari = teDhenat.substring(teDhenat.indexOf("Pronari=>")+9,teDhenat.indexOf("Veprimtaria=>"));
			String Veprimtaria = teDhenat.substring(teDhenat.indexOf("Veprimtaria=>")+13);
			txtPerdoruesi1.setText(Emri);
			txtPerdoruesi2.setText(Pronari);
			txtPerdoruesi3.setText(Veprimtaria);
		}
		else if(Lloji.equals("Institucion"))
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Telefoni=>"));
			String Telefoni = teDhenat.substring(teDhenat.indexOf("Telefoni=>")+10,teDhenat.indexOf("Lloji=>"));
			String Lloji = teDhenat.substring(teDhenat.indexOf("Lloji=>")+7);
			txtPerdoruesi1.setText(Emri);
			txtPerdoruesi2.setText(Telefoni);
			txtPerdoruesi3.setText(Lloji);
		}
	}
    }
}

	private class lexoKordinatatTask extends
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
			
			argumentet[0]="PerdoruesiID";
			argumentet[1]=params[0];
			
			
			String aResponse = objThirrja.thirrMetoden("lexoAdresen", argumentet); 
			
			return aResponse;
		
	}



		@Override
		protected void onPostExecute(String teDhenat) {
			// Display the results of the lookup.
			if(!teDhenat.equals("Error"))
			{
				
				String Latitude= teDhenat.substring(0,teDhenat.indexOf("Lng=>")	);
				String Longitude = teDhenat.substring(teDhenat.indexOf("Lng=>")+5,teDhenat.indexOf("Numri=>"));
				String Numri = teDhenat.substring(teDhenat.indexOf("Numri=>")+7);
				
				txtAdresa1.setText(Numri);
				Location tempLokacion = new Location("");
				tempLokacion.setLatitude(Double.parseDouble(Latitude));
				tempLokacion.setLongitude(Double.parseDouble(Longitude));
				(new GetAddressTask()).execute(tempLokacion);
				mapView.clear();
				mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tempLokacion.getLatitude(), tempLokacion.getLongitude()), 16));
				marker = mapView.addMarker(new MarkerOptions()
			     .position(new LatLng(tempLokacion.getLatitude(), tempLokacion.getLongitude()))
			     .title("Lokacioni")
			     .snippet("Lokacioni i zgjedhur"));
				Mesazhi.setText("");
				
			}
		}
	}   


	
	private class GetAddressTask extends
    AsyncTask<Location, Void, String> {

@Override
protected String doInBackground(Location... params) {
    
    /**
     * Marrim Lokacionin
     */
    Location lokacioni = params[0];
    
    /**
     * /Krijojme objektin e Web Sherbimit
     */
    WebserviceCall objThirrja = new WebserviceCall(); 

    /**
     * /Thirrim Web Sherbimin dhe marrim pergjigjjen.
     */
    
     String funksioni="ktheAdresen";
   	 String argumentet[] = new String[4];
   	 argumentet[0]="lat";
        argumentet[1]=Double.toString(lokacioni.getLatitude());
        argumentet[2]="lng";
        argumentet[3]=Double.toString(lokacioni.getLongitude());
        String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
        
        return aResponse;
   
}

@Override
protected void onPostExecute(String input) {
    // Display the results of the lookup.
	 
	 	 
	if(!input.equals("Error"))
    {
   	 String Rruga = input.substring(0,input.indexOf("Qyteti=>"));
   	 String Qyteti = input.substring(input.indexOf("Qyteti=>")+8,input.indexOf("Kodi=>"));
   	 String Kodi = input.substring(input.indexOf("Kodi=>")+6);
   	 txtAdresa2.setText(Rruga);
   	 txtAdresa3.setText(Qyteti+" "+Kodi);
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
    argumentet[3]="Tjeter";
        
    String aResponse = objThirrja.thirrMetoden("validoUsername", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	objProgress.setVisibility(View.GONE);
	if(teDhenat.length()==5 || teDhenat.length()==4)
	{
		if(teDhenat.equals("True"))
		{
			new lexoTask().execute(Username);
			new lexoKordinatatTask().execute(Username);
		}
		else
		{
			if(Lloji.equals("Individ"))
			{
				Mesazhi.setText("Per kete NrPersonal nuk ka adrese te regjistruar ne databaze");
			}
			else if(Lloji.equals("Biznes"))
			{
				Mesazhi.setText("Per kete NrBiznesi nuk ka adrese te regjistruar ne databaze");
			}
			else if(Lloji.equals("Institucion"))
			{
				Mesazhi.setText("Per kete NrInstitucioni nuk ka adrese te regjistruar ne databaze");
			}
		}
	}
	else
	{
		Mesazhi.setText("Gabim me lidhjen me databaze");
	}
    }
}


	private class kerkoqueryTask extends
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
    
    argumentet[0]="Lloji";
    argumentet[1]=Lloji;
    argumentet[2]="P1";
    argumentet[3]=edit1.getText().toString();
    argumentet[4]="P2";
    argumentet[5]=edit2.getText().toString();
    argumentet[6]="P3";
    argumentet[7]=edit3.getText().toString();
        
    String aResponse = objThirrja.thirrMetoden("kerkoAdrese", argumentet); 
    
    return aResponse;
   
  }



@Override
protected void onPostExecute(String teDhenat) {
    // Display the results of the lookup.
	objProgress.setVisibility(View.GONE);
	if(!teDhenat.equals(""))
	{
		String Key = teDhenat.substring(0,1);
		
		if(Key.equals("0"))
		{
			Mesazhi.setText("Asnje adrese nuk u gjet me kushtet tuaja");
		}
		else if(Key.equals("1"))
		{
			Username = teDhenat.substring(1);
			new validouserTask().execute(Username);
		}
		else if(Key.equals("6"))
		{
			Mesazhi.setText("Me shume se 5 rekorde jane gjetur me kushtet tuaja, ju lutem vendosni kushte me specifike");
		}
	}
	else
	{
		Mesazhi.setText("Gabim me lidhjen me databaze");
	}
    }
}	
	
	
}
