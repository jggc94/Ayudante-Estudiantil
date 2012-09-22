package com.ayudante.estudiantil;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.android.dataframework.DataFramework;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ImageButton btnAgregar;
	private ProgressDialog dialog;
	private Context contexto;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    
        contexto = getApplicationContext();
        
        btnAgregar = (ImageButton) findViewById(R.id.btnAgregar);
        
        btnAgregar.setOnClickListener(new OnClickListener() 
        {
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(), AgregarActivity.class);
				startActivity(intent);
			}
		});
        
        try 
        {
			DataFramework.getInstance().open(contexto, getPackageName());
		} catch (XmlPullParserException e) 
		{
			Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
		} catch (IOException e) 
		{
			Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
        
//        dialog = new ProgressDialog(contexto);
//        dialog.setMessage(getString(R.string.cargando));
//		dialog.setCancelable(false);
//		new ProgressAbriendo().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) 
	{	
    	if(item.getItemId() == R.id.exit)
		{
			finish();
		}
		return true;
	}
	
    public class ProgressAbriendo extends AsyncTask<Void, Integer, Void>
	{
		int progress;

		/**
		 * Metodo para despues que pasa el Thread
		 */
		@Override
		protected void onPostExecute(Void result) 
		{
			// Cierra el dialog
			dialog.dismiss();
		}

		/**
		 * Metodo para antes de empezar el Thread
		 */
		@Override
		protected void onPreExecute() 
		{
			// Muestra el dialog
			dialog.show();
			progress = 0;
		}
		@Override
		protected void onProgressUpdate(Integer... values) 
		{
			dialog.setProgress(values[0]);
		}

		/**
		 * Metodo usado mientras se hace el Thread
		 */
		@Override
		protected Void doInBackground(Void... params) 
		{
			try 
			{
				// Abre la base de datos
				DataFramework.getInstance().open(contexto, getPackageName());
	    	} catch (Exception  e) 
			{
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), getString(R.string.errorAbriendoBD), Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		
	}
}
