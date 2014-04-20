package com.example.adresat;


import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements 
LocationSource,LocationListener,OnMapClickListener,
OnMapLongClickListener,OnMarkerDragListener,
OnMarkerClickListener {

	GoogleMap mapView;
	TextView t1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		t1=(TextView)findViewById(R.id.txtErrorMessage);
		mapView = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapview)).getMap();
		mapView.setOnMapClickListener(this);
		mapView.setOnMapLongClickListener(this);
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
		return true;

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
		(new GetAddressTask()).execute(tempLokacion);
		
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
     String argumentet[] = new String[4];
     argumentet[0]="lat";
     argumentet[1]=Double.toString(lokacioni.getLatitude());
     argumentet[2]="lng";
     argumentet[3]=Double.toString(lokacioni.getLongitude());
     String aResponse = objThirrja.thirrMetoden("ktheAdresen", argumentet); 
     
     return aResponse;
    
 }
 
 @Override
 protected void onPostExecute(String address) {
     // Display the results of the lookup.
     t1.setText(address);
 }
 
}

}
