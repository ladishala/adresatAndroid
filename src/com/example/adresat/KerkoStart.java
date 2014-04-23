package com.example.adresat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class KerkoStart extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kerkostart);
	}
	
	public void individClick(View v)
	{
		Intent i = new Intent(KerkoStart.this,Kerko.class);
		i.putExtra("Lloji", "Individ");
		startActivity(i);
	}
	public void biznesClick(View v)
	{
		Intent i = new Intent(KerkoStart.this,Kerko.class);
		i.putExtra("Lloji", "Biznes");
		startActivity(i);
	}
	public void institucionClick(View v)
	{
		Intent i = new Intent(KerkoStart.this,Kerko.class);
		i.putExtra("Lloji", "Institucion");
		startActivity(i);
	}
}
