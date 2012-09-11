package com.ayudante.estudiantil;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dataframework.DataFramework;
import com.android.dataframework.Entity;

public class AgregarActivity extends Activity
{
	private static final int DIALOG_FECHA = 0;
	private int anioP;
    private int mesP;
    private int diaP;
    private StringBuilder fechaString;
    
	private EditText fieldTitulo, fieldTexto;
	private Button btnAgregar,btnFecha;
	private TableLayout tabla;
	private TableRow row;
	private TextView titulo,texto,fecha;
	private Context contexto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_activity);
		
		contexto = getApplicationContext();
		
		fieldTitulo = (EditText) findViewById(R.id.fieldTitulo);
		fieldTexto = (EditText) findViewById(R.id.fieldTexto);
		
		btnAgregar = (Button) findViewById(R.id.btnAgregarNueva);
		btnAgregar.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				if(!fieldTitulo.getText().toString().equals("") && !fieldTexto.getText().toString().equals("") && btnFecha.getText().toString().compareToIgnoreCase("Fecha") != 0)
				{
					Entity ent = new Entity("tarea");
					ent.setValue("TITULO", fieldTitulo.getText().toString());
					ent.setValue("TEXTO", fieldTexto.getText().toString());
					ent.setValue("FECHA", fechaString);
					ent.save();
					
					cargarDatos();
					Toast.makeText(contexto, "Tarea Agregada!!!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(contexto, "Rellena Los Campos", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnFecha = (Button) findViewById(R.id.btnFecha);
		btnFecha.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				showDialog(DIALOG_FECHA);
			}
		});
		
		Calendar cal = Calendar.getInstance();
        anioP = cal.get(Calendar.YEAR);
        mesP = cal.get(Calendar.MONTH);
        diaP = cal.get(Calendar.DAY_OF_MONTH);
        
        updateDisplay();
        
		tabla = (TableLayout) findViewById(R.id.tablelayoutVer);
		cargarDatos();
		
	}

    public void cargarDatos()
    {
    	for (int i = 1; i < tabla.getChildCount(); i++) 
    	{
    		tabla.removeViewAt(i);
		}
    	 List<Entity> list = DataFramework.getInstance().getEntityList("tarea");
         @SuppressWarnings("rawtypes")
         Iterator iter = list.iterator();
         
         // Mientras exista uno mas
         while (iter.hasNext()) 
         {
         	Entity ent = (Entity)iter.next();
         	String tituloP = ent.getString("TITULO");
         	String textoP = ent.getString("TEXTO");
         	String fechaP = ent.getString("FECHA");
         	
         	rellenarTablaConRows(tituloP, textoP, fechaP);
         }
    }
    
    public void rellenarTablaConRows(String tituloP, final String textoP, String fechaP)
    {	
    	row = new TableRow(contexto);
     	
    	titulo = new TextView(contexto);
    	titulo.setWidth(100);
        titulo.setText(tituloP);
        titulo.setTextSize(10);
        titulo.setTextColor(Color.BLACK);
        titulo.setGravity(Gravity.CENTER);

        texto = new TextView(contexto);
        texto.setWidth(100);
        texto.setText(textoP);
        texto.setTextSize(10);
        texto.setWidth(100);
        texto.setTextColor(Color.BLACK);
        texto.setGravity(Gravity.CENTER);
        
        fecha = new TextView(contexto);
        fecha.setWidth(100);
        fecha.setText(fechaP);
        fecha.setTextSize(10);
        fecha.setWidth(100);
        fecha.setTextColor(Color.BLACK);
        fecha.setGravity(Gravity.CENTER);
        
        row.addView(titulo);
        row.addView(texto);
        row.addView(fecha);
 		row.setOnClickListener(new OnClickListener() 
 		{
			public void onClick(View v) 
			{
				Toast.makeText(contexto, "La Tarea es : " + textoP, Toast.LENGTH_SHORT).show();
			}
		});
 		
        tabla.addView(row);
    }
    
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() 
    {
    	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
    	{
    		anioP = year;
    		mesP = monthOfYear;
            diaP = dayOfMonth;
            updateDisplay();
            displayToast();
    	}
	};
	
	private void updateDisplay() 
	{
		fechaString = (new StringBuilder().append(mesP + 1).append("/").append(diaP).append("/").append(anioP).append(" "));
		btnFecha.setText(fechaString);
	}
	
	private void displayToast() 
	{
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(fechaString),  Toast.LENGTH_SHORT).show();
    }
	
	protected Dialog onCreateDialog(int id) 
	{
        switch (id) {
        case DIALOG_FECHA:
            return new DatePickerDialog(this,dateSetListener,anioP, mesP, diaP);
        }
        return null;
    }	
}
