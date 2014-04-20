package com.example.adresat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



public class Board extends FragmentActivity {
	
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t11;
	TextView t22;
	TextView t33;
	String Username;
	ImageView imgProfile;
	ProgressBar objProgress;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		t1 = (TextView)findViewById(R.id.txtb1);
		t2 = (TextView)findViewById(R.id.txtb2);
		t3 = (TextView)findViewById(R.id.txtb3);
		t11 = (TextView)findViewById(R.id.txtb11);
		t22 = (TextView)findViewById(R.id.txtb22);
		t33 = (TextView)findViewById(R.id.txtb33);
		imgProfile = (ImageView)findViewById(R.id.imgProfile);
		objProgress = (ProgressBar)findViewById(R.id.progressLoading);
		
		Intent i = getIntent();
		Username= i.getStringExtra("Username");
		String lloji="Institucion";
		if(Username.length()==10)
		{
			lloji="Individ";
		}
		else if(Username.length()==8)
		{
			lloji="Biznes";
		}
		
		(new lexoTask()).execute(lloji);
	}

	@Override
	public void onBackPressed() {
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
			Intent i = new Intent(Board.this, Login.class);
			i.putExtra("LogOut", true);
			startActivity(i);

		}
		return true;

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
	
	if(!teDhenat.substring(0,10).equals("java.net.S"))
	{
		objProgress.setVisibility(View.GONE);
		t1.setVisibility(0);
		t2.setVisibility(0);
		t3.setVisibility(0);
		t11.setVisibility(0);
		t22.setVisibility(0);
		t33.setVisibility(0);
		
		if(Username.length()==10)
		{
			
			String EmriMbiemri = teDhenat.substring(0,teDhenat.indexOf("DataLindjes=>"));
			String dataLindjes = teDhenat.substring(teDhenat.indexOf("DataLindjes=>")+13,teDhenat.indexOf("VendiLindjes=>")-11);
			String vendiLindjes = teDhenat.substring(teDhenat.indexOf("VendiLindjes=>")+14);
			t1.setText("Emri dhe Mbiemri: ");
	    	t2.setText("Datelindja: ");
	    	t3.setText("Vendlindja:");
			t11.setText(EmriMbiemri);
			t22.setText(dataLindjes);
			t33.setText(vendiLindjes);
			imgProfile.setImageResource(R.drawable.individ);
			
		}
		else if( Username.length()==8)
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Pronari=>"));
			String Pronari = teDhenat.substring(teDhenat.indexOf("Pronari=>")+9,teDhenat.indexOf("Veprimtaria=>"));
			String Veprimtaria = teDhenat.substring(teDhenat.indexOf("Veprimtaria=>")+13);
			t1.setText("Emri i kompanise: ");
	    	t2.setText("Pronari: ");
	    	t3.setText("Veprimtaria:");
			t11.setText(Emri);
			t22.setText(Pronari);
			t33.setText(Veprimtaria);
			imgProfile.setImageResource(R.drawable.biznes);
			
			
		}
		else if(Username.length()==9)
		{
			String Emri = teDhenat.substring(0,teDhenat.indexOf("Telefoni=>"));
			String Telefoni = teDhenat.substring(teDhenat.indexOf("Telefoni=>")+10,teDhenat.indexOf("Lloji=>"));
			String Lloji = teDhenat.substring(teDhenat.indexOf("Lloji=>")+7);
			t1.setText("Emri i institucionit: ");
	    	t2.setText("Telefoni: ");
	    	t3.setText("Lloji:");
			t11.setText(Emri);
			t22.setText(Telefoni);
			t33.setText(Lloji);
			imgProfile.setImageResource(R.drawable.intitution);
			
		}
	}
	
    }
}

}	
		
	
	

