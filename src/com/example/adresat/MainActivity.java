package com.example.adresat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements 
LocationSource,LocationListener,OnMapClickListener,
OnMapLongClickListener,OnMarkerDragListener,
OnMarkerClickListener, OnEditorActionListener {

	GoogleMap mapView;
	TextView Mesazhi;
	EditText editNumri;
	EditText editRruga;
	EditText editQyteti;
	ToggleButton btnUpdate;
	ImageView objImg;
	ProgressBar objProgress;
	Marker marker;
	AlertDialog.Builder Alert;
	boolean iRegjistruar=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Mesazhi=(TextView)findViewById(R.id.txtError);
		editNumri=(EditText)findViewById(R.id.editNumri);
		editRruga=(EditText)findViewById(R.id.editRruga);
		editQyteti=(EditText)findViewById(R.id.editQyteti);
		btnUpdate=(ToggleButton)findViewById(R.id.togleUpdate);
		objImg =(ImageView)findViewById(R.id.imgRegjistro);
		objProgress=(ProgressBar)findViewById(R.id.progressReading);
		
		mapView = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapview)).getMap();
		mapView.setOnMapClickListener(this);
		mapView.setOnMapLongClickListener(this);
		mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.6700877, 20.8906191), 7));
		
		editQyteti.setOnEditorActionListener(this);
		
		Intent i = getIntent();
		iRegjistruar = i.getBooleanExtra("iRegjistruar", false); 
		
		if(iRegjistruar)
		{
			new lexoAdresenTask().execute(getSharedPreferences("Settings",MODE_PRIVATE).getString("Username", ""));
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
			Intent i = new Intent(MainActivity.this, Login.class);
			i.putExtra("LogOut", true);
			startActivity(i);

		}
		else if(item.getItemId()==R.id.action_ChangePassword)
		{
			Intent i = new Intent(MainActivity.this, NdryshoPassword.class);
			startActivity(i);
		}
		return true;

	}
	private void showFinishAlert() {
		Alert = new AlertDialog.Builder(this);
		String strTitle = "Ndryshimet u ruajten!";
		String strMessage = "Adresa u ruajt me sukses!";
		String strOK = "OK";
		Alert.setTitle(strTitle);
		Alert.setMessage(strMessage);
		Alert.setNeutralButton(strOK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(MainActivity.this,Board.class);
						i.putExtra("Username",getSharedPreferences("Settings",MODE_PRIVATE).getString("Username", ""));
						startActivity(i);
					}});
		
		Alert.show();
	}
	public void ruajClick(View v)
	{
		if(marker!=null)
		{
			if(editNumri.getText().toString().length()!=0)
			{
				LatLng lokacion = marker.getPosition();
				new shtoadreseTask().execute(getSharedPreferences("Settings",MODE_PRIVATE).getString("Username", ""),Double.toString(lokacion.latitude),Double.toString(lokacion.longitude),editNumri.getText().toString());
			}
			else
			{
				Mesazhi.setText("Plotesoni Nr!");
			}
			
		}
		else
		{
			Mesazhi.setText("Zgjedhni adresen!");
		}
		
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		Location tempLokacion = new Location("");
		tempLokacion.setLatitude(point.latitude);
		tempLokacion.setLongitude(point.longitude);
		
		if(btnUpdate.isChecked())
		{
			(new GetAddressTask()).execute(tempLokacion);
			mapView.clear();
			
			marker = mapView.addMarker(new MarkerOptions()
		     .position(new LatLng(point.latitude, point.longitude))
		     .title("Lokacioni")
		     .snippet("Lokacioni i zgjedhur"));
			objImg.setVisibility(View.GONE);
			objProgress.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}
	
	public void LidhuClick(View v)
	{
	
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
     
     
     if(btnUpdate.isChecked())
     {
    	 String funksioni="ktheAdresen";
    	 String argumentet[] = new String[4];
    	 argumentet[0]="lat";
         argumentet[1]=Double.toString(lokacioni.getLatitude());
         argumentet[2]="lng";
         argumentet[3]=Double.toString(lokacioni.getLongitude());
         String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
         
         return aResponse;
         
     }
     else
     {
    	 String funksioni="ktheKordinatat";
    	 String argumentet[] = new String[2];
    	 argumentet[0]="Query";
    	 argumentet[1]=editRruga.getText().toString()+"+"+editQyteti.getText().toString();
    			 
    	 String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
         
         return aResponse;
     }
    
 }
 
 @Override
 protected void onPostExecute(String input) {
     // Display the results of the lookup.
	 objProgress.setVisibility(View.GONE);
	 objImg.setVisibility(View.VISIBLE);
	 	 
	 if(!input.equals("Error") && btnUpdate.isChecked())
     {
    	 String Rruga = input.substring(0,input.indexOf("Qyteti=>"));
    	 String Qyteti = input.substring(input.indexOf("Qyteti=>")+8,input.indexOf("Kodi=>"));
    	 String Kodi = input.substring(input.indexOf("Kodi=>")+6);
    	 editRruga.setText(Rruga);
    	 editQyteti.setText(Qyteti+" "+Kodi);
     }
	 else if(!input.equals("Error"))
	 {
		 String Latitude = input.substring(0,input.indexOf("Longitude=>"));
		 String Longitude = input.substring(input.indexOf("Longitude=>")+11);
		 LatLng Lokacion =new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitude));
		 mapView.clear();
		 marker = mapView.addMarker(new MarkerOptions()
	     .position(Lokacion)
	     .title("Lokacioni")
	     .snippet("Lokacioni i zgjedhur"));
		 mapView.animateCamera(CameraUpdateFactory.newLatLngZoom(Lokacion, 18));		 
		 
	 }
 }
 
}

private class shtoadreseTask extends
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
		
		argumentet[0]="PerdoruesiID";
		argumentet[1]=params[0];
		argumentet[2]="Lat";
		argumentet[3]=params[1];
		argumentet[4]="Lng";
		argumentet[5]=params[2];
		argumentet[6]="Numri";
		argumentet[7]=params[3];
		String funksioni="shtoAdrese";
		if(iRegjistruar)
		{
			funksioni="azhuroAdresen";
			
		}
		String aResponse = objThirrja.thirrMetoden(funksioni, argumentet); 
		
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
			Mesazhi.setText("Gabim me lidhjen me databaze"+teDhenat);
		}
	}
}

private class lexoAdresenTask extends
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
			String Latitude= teDhenat.substring(0,teDhenat.indexOf("Lng=>"));
			String Longitude = teDhenat.substring(teDhenat.indexOf("Lng=>")+5,teDhenat.indexOf("Numri=>"));
			String Numri = teDhenat.substring(teDhenat.indexOf("Numri=>")+7);
			
			editNumri.setText(Numri);
			btnUpdate.setChecked(true);
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
			objImg.setVisibility(View.GONE);
			objProgress.setVisibility(View.VISIBLE);
		}
	}
}


@Override
public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	// TODO Auto-generated method stub
	if(!btnUpdate.isChecked())
	{
		Location loc = new Location("Lokacioni");
		(new GetAddressTask()).execute(loc);
	}
	return false;
}

}
